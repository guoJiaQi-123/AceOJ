package com.yupi.tyutoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.tyutoj.common.ErrorCode;
import com.yupi.tyutoj.exception.BusinessException;
import com.yupi.tyutoj.mapper.QuestionSubmitMapper;
import com.yupi.tyutoj.model.dto.questionsubmit.QuestionSumitAddRequest;
import com.yupi.tyutoj.model.entity.Question;
import com.yupi.tyutoj.model.entity.QuestionSubmit;
import com.yupi.tyutoj.model.entity.User;
import com.yupi.tyutoj.model.enums.QuestionSubmitLanguageEnum;
import com.yupi.tyutoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.tyutoj.service.QuestionService;
import com.yupi.tyutoj.service.QuestionSubmitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 27506
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-10-20 15:33:09
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    /**
     * 提交题目
     *
     * @param questionSumitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSumitAddRequest questionSumitAddRequest, User loginUser) {
        String language=questionSumitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(languageEnum==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        long questionId=questionSumitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        // 锁必须要包裹住事务方法
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSumitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WATING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据输入失败");
        }
        return questionSubmit.getId();
    }

}