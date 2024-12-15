package com.yupi.tyutoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.tyutoj.model.dto.questionsubmit.QuestionSumitAddRequest;
import com.yupi.tyutoj.model.entity.QuestionSubmit;
import com.yupi.tyutoj.model.entity.User;

/**
* @author HX
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-12-14 17:06:34
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    long doQuestionSubmit(QuestionSumitAddRequest questionSumitAddRequest, User loginUser);
}
