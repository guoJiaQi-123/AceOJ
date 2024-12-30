package com.tyut.judge;


import cn.hutool.json.JSONUtil;
import com.tyut.common.common.ErrorCode;
import com.tyut.common.exception.BusinessException;
import com.tyut.judge.codesendbox.CodeSendBox;
import com.tyut.judge.codesendbox.CodeSendBoxFactory;
import com.tyut.judge.strategy.JudgeContext;
import com.tyut.judge.strategy.JudgeManger;
import com.tyut.model.codesendbox.ExecuteCodeRequest;
import com.tyut.model.codesendbox.ExecuteCodeResponse;
import com.tyut.model.codesendbox.JudgeInfo;
import com.tyut.model.dto.question.JudgeCase;
import com.tyut.model.entity.Question;
import com.tyut.model.entity.QuestionSubmit;
import com.tyut.model.enums.QuestionSubmitStatusEnum;
import com.tyut.serviceclient.service.QuestionService;
import com.tyut.serviceclient.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 判题服务
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;
    @Resource
    private JudgeManger judgeManger;
    // 使用配置文件中配置的代码沙箱类型，配合静态工厂模式，创建不同类型的代码沙箱
    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 从数据库中查询提交
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        // 从数据库中查询题目
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息不存在");
        }
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "正在判题中");
        }
        // 更新提交状态为正在判题中
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 构造代码沙箱和参数
        CodeSendBox codeSandbox = CodeSendBoxFactory.newInstance(type);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        // 创建题目时候指定的输入用例
        List<String> createInput = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        // 创建题目时候指定的输出用例
        List<String> output = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .inputList(createInput)
                .language(language)
                .code(code)
                .build();
        // 调用代码沙箱，执行代码，获取到执行结果
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 封装上下文信息给策略类使用
        JudgeContext judgeContext = new JudgeContext();
        // 判题信息
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        // 输出用例
        judgeContext.setInputList(createInput);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        // 策略模式：根据不同的语言执行不同的判题策略
        JudgeInfo judgeInfo = judgeManger.doJudge(judgeContext);
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        return questionSubmitService.getById(questionId);
    }
}
