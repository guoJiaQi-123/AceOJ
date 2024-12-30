package com.tyut.judge.codesendbox;


import com.tyut.model.codesendbox.ExecuteCodeRequest;
import com.tyut.model.codesendbox.ExecuteCodeResponse;

/**
 * @version v1.0
 * @apiNote 代码沙箱抽象类
 * @author OldGj 2024/12/23
 */
public interface CodeSendBox {

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
