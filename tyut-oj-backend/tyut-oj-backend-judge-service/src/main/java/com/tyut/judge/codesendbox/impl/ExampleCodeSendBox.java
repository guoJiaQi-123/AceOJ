package com.tyut.judge.codesendbox.impl;


import com.tyut.judge.codesendbox.CodeSendBox;
import com.tyut.model.codesendbox.ExecuteCodeRequest;
import com.tyut.model.codesendbox.ExecuteCodeResponse;
import com.tyut.model.codesendbox.JudgeInfo;
import com.tyut.model.enums.JudgeInfoMessageEnum;
import com.tyut.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 示例代码沙箱
 */
public class ExampleCodeSendBox implements CodeSendBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();


        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPT.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
