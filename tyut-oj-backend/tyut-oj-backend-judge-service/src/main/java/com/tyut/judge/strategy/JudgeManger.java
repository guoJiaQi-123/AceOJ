package com.tyut.judge.strategy;


import com.tyut.model.codesendbox.JudgeInfo;
import com.tyut.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 判题策略选择
 */
@Service
public class JudgeManger {


    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        if (language.equals("java")) {
            return new JavaJudgeStrategy().doJudge(judgeContext);
        } else {
            return new DefaultJudgeStrategy().doJudge(judgeContext);
        }
    }
}
