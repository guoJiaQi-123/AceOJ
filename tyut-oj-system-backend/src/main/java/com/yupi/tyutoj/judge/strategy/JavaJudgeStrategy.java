package com.yupi.tyutoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.yupi.tyutoj.model.dto.question.JudgeCase;
import com.yupi.tyutoj.model.dto.question.JudgeConfig;
import com.yupi.tyutoj.judge.codesendbox.model.JudgeInfo;
import com.yupi.tyutoj.model.entity.Question;
import com.yupi.tyutoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 默认判题策略
 */
public class JavaJudgeStrategy implements JudgeStrategy {

    /**
     * 默认判题逻辑
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        // 真实消耗内存
        Long RealConsumedMemory = judgeInfo.getMemory();
        // 真实消耗时间
        Long RealConsumedTime = judgeInfo.getTime();
        // 程序运行输出用例
        List<String> runningOutputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();
        // 正确输出用例
        List<String> createOutputList = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        //根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(RealConsumedMemory);
        judgeInfoResponse.setTime(RealConsumedTime);
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPT;
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (runningOutputList.size() != createOutputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;// 答案错误
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(runningOutputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER; // 答案错误
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        //判断题目限制
        // 获取内存限制和时间限制
        String createJudgeConfigStr = question.getJudgeConfig();
        JudgeConfig createJudgeConfig = JSONUtil.toBean(createJudgeConfigStr, JudgeConfig.class);
        // 内存限制
        Long needMemoryLimit = createJudgeConfig.getMemoryLimit();
        // 时间限制
        Long needTimeLimit = createJudgeConfig.getTimeLimit();
        if (RealConsumedMemory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED; // 内存溢出
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // Java程序本身运行需要花费的时间
        long JAVA_PROGRAM_TIME_COST = 1000L;
        if (RealConsumedTime - JAVA_PROGRAM_TIME_COST > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED; // 运行超时
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // AC！
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
