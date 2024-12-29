package com.yupi.tyutoj.judge.codesendbox.impl;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.tyutoj.common.ErrorCode;
import com.yupi.tyutoj.exception.BusinessException;
import com.yupi.tyutoj.judge.codesendbox.CodeSendBox;
import com.yupi.tyutoj.judge.codesendbox.model.ExecuteCodeRequest;
import com.yupi.tyutoj.judge.codesendbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 远程代码沙箱
 */
public class RemoteCodeSendBox implements CodeSendBox {
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8090/judgment";
        String body = JSONUtil.toJsonStr(executeCodeRequest);
        String executeCodeResponse = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(body)
                .execute()
                .body();
        if (StringUtils.isBlank(executeCodeResponse)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "远程沙箱API获取结果失败");
        }
        return JSONUtil.toBean(executeCodeResponse, ExecuteCodeResponse.class);
    }
}
