package com.tyut.serviceclient.service;


import com.tyut.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 判题服务
 */
@FeignClient(name = "oj-backend-judge-service", path = "/api/judge/inner")
public interface FeignJudgeClient {

    @GetMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
