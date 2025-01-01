package com.tyut.question.controller.inner;

import com.tyut.model.entity.Question;
import com.tyut.model.entity.QuestionSubmit;
import com.tyut.question.service.QuestionService;
import com.tyut.question.service.QuestionSubmitService;
import com.tyut.serviceclient.service.FeignQuestionClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @version v1.0
 * @author OldGj 2024/12/31
 * @apiNote TODO(一句话给出该类描述)
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements FeignQuestionClient {

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    @GetMapping("/get/id")
    @Override
    public Question getById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }


    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/question_submit/update/id")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate) {
        return questionSubmitService.updateById(questionSubmitUpdate);
    }

}
