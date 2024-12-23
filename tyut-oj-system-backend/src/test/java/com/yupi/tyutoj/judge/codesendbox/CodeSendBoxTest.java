package com.yupi.tyutoj.judge.codesendbox;

import com.yupi.tyutoj.judge.codesendbox.impl.ExampleCodeSendBox;
import com.yupi.tyutoj.judge.codesendbox.impl.RemoteCodeSendBox;
import com.yupi.tyutoj.judge.codesendbox.model.ExecuteCodeRequest;
import com.yupi.tyutoj.judge.codesendbox.model.ExecuteCodeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSendBoxTest {
    // 使用配置文件中配置的代码沙箱类型，配合静态工厂模式，创建不同类型的代码沙箱
    @Value("${codesandbox.type:}")
    private String type;

    @Test
    void executeCode() {
        CodeSendBox codeSendBox = new RemoteCodeSendBox();
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code("xxx")
                .language("java")
                .inputList(new ArrayList<>())
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSendBox.executeCode(executeCodeRequest);
    }


    @Test
    void executeCode2() {
        CodeSendBox codeSendBox = CodeSendBoxFactory.newInstance(type);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code("xxx")
                .language("java")
                .inputList(new ArrayList<>())
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSendBox.executeCode(executeCodeRequest);
    }
}