package com.tyut.judge.controller.inner;

import com.tyut.judge.JudgeService;
import com.tyut.model.entity.QuestionSubmit;
import com.tyut.serviceclient.service.FeignJudgeClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version v1.0
 * @author OldGj 2024/12/31
 * @apiNote 内部调用
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements FeignJudgeClient {

    @Resource
    private JudgeService judgeService;

    @Override
    @GetMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
