package com.tyut.ojcodebox;


import cn.hutool.core.io.resource.ResourceUtil;
import com.tyut.ojcodebox.model.ExecuteCodeRequest;
import com.tyut.ojcodebox.model.ExecuteCodeResponse;
import com.tyut.ojcodebox.model.ExecuteMessage;
import com.tyut.ojcodebox.utils.ProcessUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/12/25
 * @apiNote Java 原生代码沙箱
 */
@Component
public class JavaNativeCodeSandbox extends CodeSendBoxTemplate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = super.executeCode(executeCodeRequest);
        System.out.println("沙箱执行结果：" + executeCodeResponse);
        return executeCodeResponse;
    }

    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        // 3.执行代码，得到结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inputList) {
            String javaCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, input);
            try {
                Process javaRunProcess = Runtime.getRuntime().exec(javaCmd);
                // 使用守护线程，如果主线程运行超过超时时间 ，则使用守护线程销毁主线程的运行
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        if (javaRunProcess.isAlive()) {
                            javaRunProcess.destroy();
                            System.out.println("超时了，中断");
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage runJavaExecuteMessage = ProcessUtils.runProcessAndGetMessage(javaRunProcess, "运行");
                executeMessageList.add(runJavaExecuteMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return executeMessageList;
    }


    /*
        java原生代码沙箱实现需要解决的问题：
            1.时间限制问题  解决方案：使用守护线程，如果主线程运行超过超时时间 ，则使用守护线程销毁主线程的运行
            2.内存限制问题  解决方案：为用户限制资源分配。实现就使用JVM 的 -Xmx 参数限制JVM运行的堆最大内存空间
            3.读服务器文件  解决方案：黑白名单 可以使用字典树使用更少的空间和时间来判定
            4.向服务器写文件  解决方案：黑白名单 可以使用字典树使用更少的空间和时间来判定
            5.执行服务器上的危险文件  解决方案：黑白名单 可以使用字典树使用更少的空间和时间来判定
            6.执行服务器上的危险命令  解决方案：黑白名单 可以使用字典树使用更少的空间和时间来判定
     */

    public static void main(String[] args) {
        JavaNativeCodeSandbox codeSandbox = new JavaNativeCodeSandbox();
//        String code = ResourceUtil.readStr("errorfiles/SleepError.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("errorfiles/MemoryError.java", StandardCharsets.UTF_8);
        String code = ResourceUtil.readStr("simplecodetest/Main.java", StandardCharsets.UTF_8);
        List<String> input = new ArrayList<>();
        input.add("1 2");
        input.add("3 4");
        ExecuteCodeRequest codeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(input)
                .language("java")
                .build();
        codeSandbox.executeCode(codeRequest);
    }

}
