package com.yupi.tyutoj.judge.strategy;

import com.yupi.tyutoj.model.dto.question.JudgeInfo;

/**
 * @version v1.0
 * @apiNote 判题策略
 * @author OldGj 2024/12/23
 */
public interface JudgeStrategy {


    /**
     * 判题接口
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);

}
