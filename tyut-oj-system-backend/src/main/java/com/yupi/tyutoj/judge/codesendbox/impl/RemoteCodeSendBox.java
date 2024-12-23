package com.yupi.tyutoj.judge.codesendbox.impl;

import com.yupi.tyutoj.judge.codesendbox.CodeSendBox;
import com.yupi.tyutoj.judge.codesendbox.model.ExecuteCodeRequest;
import com.yupi.tyutoj.judge.codesendbox.model.ExecuteCodeResponse;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 远程代码沙箱
 */
public class RemoteCodeSendBox implements CodeSendBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
