package com.tyut.ojcodebox;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.tyut.ojcodebox.model.ExecuteCodeRequest;
import com.tyut.ojcodebox.model.ExecuteCodeResponse;
import com.tyut.ojcodebox.model.ExecuteMessage;
import com.tyut.ojcodebox.utils.ProcessUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @version v1.0
 * @author OldGj 2024/12/25
 * @apiNote Java 原生代码沙箱
 */
public class JavaNativeCodeSandbox implements CodeSendBox {
    private static final String GLOBAL_CODE_DIR_NAME = "codetemp";
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";
    // 超时时间 3 秒
    private static final long TIME_OUT = 3 * 1000;

    // 字典树
    private static final WordTree WORD_TREE = new WordTree();

    private static final List<String> BLACKLIST = Arrays.asList("Files", "exec");

    static {
        WORD_TREE.addWords(BLACKLIST);
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
        String code = ResourceUtil.readStr("errorfiles/ReadFileError.java", StandardCharsets.UTF_8);
        List<String> input = new ArrayList<>();
        input.add("xxx");
        ExecuteCodeRequest codeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(input)
                .language("java")
                .build();
        codeSandbox.executeCode(codeRequest);
    }


    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();
        // 校验用户提交上来的代码中是否有黑名单中禁止的违法操作字符串
        List<FoundWord> foundWords = WORD_TREE.matchAllWords(code);
        if (foundWords != null) {
            System.out.println("上传的代码操作违法");
            System.out.println(foundWords);
            return null;
        }
        // 获取用户当前目录
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        //判断全局代码文件目录是否存在
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        //把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        // 将用户代码写到磁盘
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        //2.编译代码，得到class文件
        String javacCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());
        try {
            // 执行javac命令编译代码
            Process compliedProcess = Runtime.getRuntime().exec(javacCmd);
            // 执行进程，并获取信息
            ExecuteMessage compliedProcessMessage = ProcessUtils.runProcessAndGetMessage(compliedProcess, "编译");
            System.out.println(compliedProcessMessage);
        } catch (IOException e) {
            return getErrorResponse(e);
        }
        // 3.执行代码，得到结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String s : inputList) {
            String javaCmd = String.format("java -Xmx=256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputList);
            try {
                Process javaRunProcess = Runtime.getRuntime().exec(javaCmd);
                // 使用守护线程，如果主线程运行超过超时时间 ，则使用守护线程销毁主线程的运行
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(TIME_OUT);
                            System.out.println("超时了，中断");
                            javaRunProcess.destroy();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();
                ExecuteMessage runJavaExecuteMessage = ProcessUtils.runProcessAndGetMessage(javaRunProcess, "运行");
                executeMessageList.add(runJavaExecuteMessage);
                System.out.println(runJavaExecuteMessage);
            } catch (IOException e) {
                return getErrorResponse(e);
            }
        }
        //4.收集整理输出的结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        //取最大值判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                //用户提交代码执行中存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }
        //正常运行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        //要借助第三方库来获取内存，过于麻烦，不做展示
//        judgeInfo.setMemory();
        executeCodeResponse.setJudgeInfo(judgeInfo);
        //5.文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }

    /**
     * 获取错误响应
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        //表示代码沙箱错误（可能是编译错误）
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
