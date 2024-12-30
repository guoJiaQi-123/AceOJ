package com.tyut.judge.codesendbox.impl;


import com.tyut.judge.codesendbox.CodeSendBox;
import com.tyut.model.codesendbox.ExecuteCodeRequest;
import com.tyut.model.codesendbox.ExecuteCodeResponse;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 第三方代码沙箱
 */
public class ThirdPartyCodeSendBox implements CodeSendBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
