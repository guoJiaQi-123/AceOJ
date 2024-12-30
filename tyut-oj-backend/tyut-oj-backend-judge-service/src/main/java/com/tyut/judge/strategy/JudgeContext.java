package com.tyut.judge.strategy;


import com.tyut.model.codesendbox.JudgeInfo;
import com.tyut.model.dto.question.JudgeCase;
import com.tyut.model.entity.Question;
import com.tyut.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 判题信息上下文
 */
@Data
public class JudgeContext {

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
    /**
     * 输入
     */
    private List<String> inputList;
    /**
     * 输出
     */
    private List<String> outputList;
    /**
     * 创建题目时指定的正确的输入输出用例
     */
    private List<JudgeCase> judgeCaseList;
    /**
     * 题目
     */
    private Question question;
    /**
     * 题目提交
     */
    private QuestionSubmit questionSubmit;

}
