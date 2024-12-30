package com.tyut.serviceclient.service;


import com.tyut.model.entity.QuestionSubmit;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 判题服务
 */
public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);
}
