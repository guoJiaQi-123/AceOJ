package com.tyut.ojcodebox.model;

import lombok.Data;

/**
 * 程序执行信息
 */
@Data
public class ExecuteMessage {
    // 返回码
    private Integer exitValue;
    // 正常信息
    private String message;
    // 异常信息
    private String errorMessage;
    // 运行时间
    private Long time;
    // 运行内存
    private Long memory;
}