package com.tyut.model.dto.question;

import lombok.Data;

/**
 * 题目用例类
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;
    /**
     * 输出用例
     */
    private String output;
}