package com.yupi.tyutoj.model.dto.question;

import lombok.Data;

/**
 * 题目相关信息类
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制
     */
    private Long timeLimit;
    /**
     * 内存限制
     */
    private Long memoryLimit;
    /**
     * 堆栈限制
     */
    private Long stackLimit;
}