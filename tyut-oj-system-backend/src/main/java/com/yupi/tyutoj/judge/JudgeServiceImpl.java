package com.yupi.tyutoj.judge;

import cn.hutool.json.JSONUtil;
import com.yupi.tyutoj.common.ErrorCode;
import com.yupi.tyutoj.exception.BusinessException;
import com.yupi.tyutoj.judge.codesendbox.CodeSendBox;
import com.yupi.tyutoj.judge.codesendbox.CodeSendBoxFactory;
import com.yupi.tyutoj.judge.codesendbox.model.ExecuteCodeRequest;
import com.yupi.tyutoj.judge.codesendbox.model.ExecuteCodeResponse;
import com.yupi.tyutoj.judge.strategy.JudgeContext;
import com.yupi.tyutoj.judge.strategy.JudgeManger;
import com.yupi.tyutoj.model.dto.question.JudgeCase;
import com.yupi.tyutoj.model.dto.question.JudgeInfo;
import com.yupi.tyutoj.model.entity.Question;
import com.yupi.tyutoj.model.entity.QuestionSubmit;
import com.yupi.tyutoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.tyutoj.service.QuestionService;
import com.yupi.tyutoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
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
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息不存在");
        }
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "正在判题中");
        }
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 调用代码沙箱，执行代码
        CodeSendBox codeSandbox = CodeSendBoxFactory.newInstance(type);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> input = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        List<String> output = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .inputList(input)
                .language(language)
                .code(code)
                .build();
        // 获取到执行结果
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 封装上下文信息给策略类使用
        JudgeContext judgeContext = new JudgeContext();
        // 判题信息
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        // 输出用力
        judgeContext.setInputList(input);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
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
