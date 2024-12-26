package com.tyut.ojcodebox;


import com.tyut.ojcodebox.model.ExecuteCodeRequest;
import com.tyut.ojcodebox.model.ExecuteCodeResponse;

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
