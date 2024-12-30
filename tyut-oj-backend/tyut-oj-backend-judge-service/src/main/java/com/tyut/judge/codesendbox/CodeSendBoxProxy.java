package com.tyut.judge.codesendbox;


import com.tyut.model.codesendbox.ExecuteCodeRequest;
import com.tyut.model.codesendbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 代理类，用于增强代码沙箱中执行代码方法，在其前后增加记录日志操作
 */
@Slf4j
public class CodeSendBoxProxy implements CodeSendBox {


    private final CodeSendBox codeSendBox;

    public CodeSendBoxProxy(CodeSendBox codeSendBox) {
        this.codeSendBox = codeSendBox;
    }

    /**
     * 代理方法
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：{}", executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSendBox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：{}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
