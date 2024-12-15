package com.yupi.tyutoj.controller;


import com.yupi.tyutoj.common.BaseResponse;
import com.yupi.tyutoj.common.ErrorCode;
import com.yupi.tyutoj.common.ResultUtils;
import com.yupi.tyutoj.exception.BusinessException;
import com.yupi.tyutoj.model.dto.questionsubmit.QuestionSumitAddRequest;
import com.yupi.tyutoj.model.entity.User;
import com.yupi.tyutoj.service.QuestionSubmitService;
import com.yupi.tyutoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_sumit")
@Slf4j
public class QuestionSumitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSumitAddRequest
     * @param request
     * @return 提交记录id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSumit(@RequestBody QuestionSumitAddRequest questionSumitAddRequest,
                                              HttpServletRequest request) {
        if (questionSumitAddRequest == null || questionSumitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionId = questionSumitAddRequest.getQuestionId();
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSumitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

}
