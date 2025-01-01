package com.tyut.serviceclient.service;


import com.tyut.model.entity.Question;
import com.tyut.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author HX
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2024-12-14 17:06:34
 */
@FeignClient(name = "oj-backend-question-service", path = "/api/question/inner")
public interface FeignQuestionClient {


    @GetMapping("/get/id")
    Question getById(@RequestParam("questionId") long questionId);


    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    @PostMapping("/question_submit/update/id")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmitUpdate);

}
