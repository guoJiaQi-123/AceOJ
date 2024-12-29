package com.tyut.ojcodebox;


import cn.hutool.core.io.resource.ResourceUtil;
import com.tyut.ojcodebox.model.ExecuteCodeRequest;
import com.tyut.ojcodebox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author OldGj 2024/12/25
 * @version v1.0
 * @apiNote Docker 代码沙箱实现
 */
@Component
public class JavaDockerCodeSandbox extends CodeSendBoxTemplate {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }

    public static void main(String[] args) {
        JavaDockerCodeSandbox codeSandbox = new JavaDockerCodeSandbox();
//        String code = ResourceUtil.readStr("errorfiles/SleepError.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("errorfiles/MemoryError.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("errorfiles/ReadFileError.java", StandardCharsets.UTF_8);
        String code = ResourceUtil.readStr("simplecodetest/Main.java", StandardCharsets.UTF_8);
        List<String> input = new ArrayList<>();
        input.add("1 2");
        input.add("3 4");
        ExecuteCodeRequest codeRequest = ExecuteCodeRequest.builder().code(code).inputList(input).language("java").build();
        codeSandbox.executeCode(codeRequest);
    }
}
