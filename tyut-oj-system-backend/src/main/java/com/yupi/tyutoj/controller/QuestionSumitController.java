package com.yupi.tyutoj.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.tyutoj.common.BaseResponse;
import com.yupi.tyutoj.common.ErrorCode;
import com.yupi.tyutoj.common.ResultUtils;
import com.yupi.tyutoj.exception.BusinessException;
import com.yupi.tyutoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.tyutoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.tyutoj.model.entity.QuestionSubmit;
import com.yupi.tyutoj.model.entity.User;
import com.yupi.tyutoj.model.vo.QuestionSubmitVO;
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
//@RequestMapping("/question_sumit")
@Slf4j
public class QuestionSumitController {

    @Resource
    private UserService userService;


}
