package com.tyut.ojcodebox.controller;

import com.tyut.ojcodebox.JavaNativeCodeSandbox;
import com.tyut.ojcodebox.model.ExecuteCodeRequest;
import com.tyut.ojcodebox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version v1.0
 * @author OldGj 2024/12/25
 * @apiNote 验证项目的控制器
 */
@RestController
public class OkController {

    private static final String AUTH_REQUEST_HEADER="auth";
    private static final String AUTH_REQUEST_SECRET="secretKey";

    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    /**
     * 开放判题服务API
     * @param executeCodeRequest
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/judgment")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        if (!AUTH_REQUEST_SECRET.equals(request.getHeader(AUTH_REQUEST_HEADER))) {
            response.setStatus(403);
            return null;
        }
        return javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }

    @GetMapping("/health")
    public String ok() {
        return "ok";
    }


}
