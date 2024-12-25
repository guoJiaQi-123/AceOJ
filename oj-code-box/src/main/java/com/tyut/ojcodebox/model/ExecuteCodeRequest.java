package com.tyut.ojcodebox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/12/25
 * @apiNote TODO(一句话给出该类描述)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteCodeRequest {
    // 输入用例
    private List<String> inputList;
    // 编程语言
    private String language;
    // 代码
    private String code;
}