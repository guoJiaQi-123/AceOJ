package com.yupi.tyutoj.judge.strategy;

import com.yupi.tyutoj.model.dto.question.JudgeCase;
import com.yupi.tyutoj.model.dto.question.JudgeInfo;
import com.yupi.tyutoj.model.entity.Question;
import com.yupi.tyutoj.model.entity.QuestionSubmit;
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
