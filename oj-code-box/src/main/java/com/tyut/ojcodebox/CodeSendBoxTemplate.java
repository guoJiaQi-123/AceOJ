package com.tyut.ojcodebox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.tyut.ojcodebox.model.ExecuteCodeRequest;
import com.tyut.ojcodebox.model.ExecuteCodeResponse;
import com.tyut.ojcodebox.model.ExecuteMessage;
import com.tyut.ojcodebox.model.JudgeInfo;
import com.tyut.ojcodebox.utils.ProcessUtils;
import org.springframework.util.StopWatch;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 代码沙箱模板类
 */
public abstract class CodeSendBoxTemplate implements CodeSendBox {
    protected static final String GLOBAL_CODE_DIR_NAME = "codetemp";
    protected static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";
    // 超时时间 3 秒
    protected static final long TIME_OUT = 3 * 1000;

    // 第一次执行拉取docker镜像
    protected static final boolean FIRST_INIT = false;

    /**
     * 在沙箱中执行代码
     *
     * @param executeCodeRequest 代码沙箱接收的输入
     * @return 执行响应
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();
        // 1. 将用户代码保存到本地
        File userCodeFile = saveCodeToFile(code);
        // 2.编译代码，得到class文件
        ExecuteMessage compileCodeFileMessage = compileCodeFile(userCodeFile);
        // 3.在docker容器中执行代码，得到结果
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);
        // 4.封装沙箱响应结果
        ExecuteCodeResponse executeCodeResponse = getOutputResponse(executeMessageList);
        // 5.文件清理
        if (!clearFile(userCodeFile)) {
            throw new RuntimeException("文件清理失败");
        }
        return executeCodeResponse;
    }


    /**
     *  1. 将用户代码保存到本地
     * @param code 用户代码
     * @return 保存到本地的文件
     */
    public File saveCodeToFile(String code) {
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
        return FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
    }

    /**
     * 2.编译代码，得到class文件
     * @param userCodeFile 用户源代码
     * @return 编译结果
     */
    public ExecuteMessage compileCodeFile(File userCodeFile) {
        String javacCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());
        ExecuteMessage compliedProcessMessage;
        try {
            // 执行javac命令编译代码
            Process compliedProcess = Runtime.getRuntime().exec(javacCmd);
            // 执行进程，并获取信息
            compliedProcessMessage = ProcessUtils.runProcessAndGetMessage(compliedProcess, "编译");
            if (compliedProcessMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误");
            }
        } catch (IOException e) {
            throw new RuntimeException("编译代码错误", e);
        }
        return compliedProcessMessage;
    }


    /**
     * 3.在docker容器中执行代码，得到结果
     * @param userCodeFile 要执行的代码路径
     * @param inputList 输入用例
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        // 3.1 拉去带有JDK环境的docker镜像，只需要拉取一次
        String image = "openjdk:8-alpine";
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        if (FIRST_INIT) {
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    System.out.println("下载镜像" + item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("镜像拉取成功");
        }
        // 3.2 创建容器 在创建容器时，指定容器的cpu资源，内存资源，映射编译后的代码目录到容器内部
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100 * 1024 * 1024L); // 指定容器内存
        hostConfig.withMemorySwap(0L);
        hostConfig.withCpuCount(1L); // 指定容器cpu核心
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/app")));
        /*
            .withAttachStdin(true)：将标准输入（stdin）附加到容器上，这意味着可以通过标准输入向容器发送数据。
            .withAttachStderr(true)：将标准错误输出（stderr）附加到容器上，这样可以捕获容器运行时的错误信息。
            .withAttachStdout(true)：将标准输出（stdout）附加到容器上，以便可以捕获容器正常运行时的输出信息。
            .withTty(true)：将容器设置为使用终端（TTY）模式，这在需要与容器进行交互时比较有用，
                比如运行一个 shell 进程的容器时，可以使用终端模式来方便地进行输入输出操作。
         */
        CreateContainerResponse containerResponse = containerCmd.withHostConfig(hostConfig).withNetworkDisabled(true).withReadonlyRootfs(true).withAttachStdin(true).withAttachStderr(true).withAttachStdout(true).withTty(true).exec();
        // 获取容器ID
        String containerId = containerResponse.getId();
        // 3.3 启动容器
        dockerClient.startContainerCmd(containerId).exec();
        // 3.4 在容器中执行java命令，指定输入用例，运行程序并获取输出用例
        ArrayList<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inputList) {
            StopWatch stopWatch = new StopWatch();
            String[] inputArgsArray = input.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArgsArray);
            // 创建要执行的命令
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId).withCmd(cmdArray).withAttachStderr(true).withAttachStdin(true).withAttachStdout(true).exec();
            System.out.println("创建执行命令：" + execCreateCmdResponse);


            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};
            long time = 0L;
            final boolean[] timeout = {true};
            String execId = execCreateCmdResponse.getId();
            // 执行命令后的回调
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    timeout[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("输出错误结果：" + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果：" + message[0]);
                    }
                    super.onNext(frame);
                }
            };

            final long[] maxMemory = {0L};
            //获取占用内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {
                @Override
                public void onNext(Statistics statistics) {
                    System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
                    maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void close() throws IOException {

                }
            });
            statsCmd.exec(statisticsResultCallback);

            try {
                stopWatch.start();
                dockerClient.execStartCmd(execId).exec(execStartResultCallback).awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);
                stopWatch.stop();
                time = stopWatch.getLastTaskTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setTime(time);
            executeMessage.setMemory(maxMemory[0]);
            executeMessageList.add(executeMessage);
        }
        return executeMessageList;
    }

    /**
     * 4.封装沙箱响应结果
     * @param executeMessageList 代码执行结构
     * @return 沙箱返回结果
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        //取最大值判断是否超时
        long maxTime = 0;
        long maxMemory = 0;
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
            Long memory = executeMessage.getMemory();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
            if (memory != null) {
                maxMemory = Math.max(maxMemory, memory);
            }
        }
        //正常运行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(maxMemory);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 5.文件清理
     * @param userCodeFile 用户代码
     * @return 是否删除成功
     */
    public boolean clearFile(File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        return FileUtil.del(userCodeParentPath);
    }


}
