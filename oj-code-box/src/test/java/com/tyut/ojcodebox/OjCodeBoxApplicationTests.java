package com.tyut.ojcodebox;

import cn.hutool.core.io.resource.ResourceUtil;
import com.tyut.ojcodebox.model.ExecuteCodeRequest;
import com.tyut.ojcodebox.model.ExecuteCodeResponse;
import com.tyut.ojcodebox.old.JavaNativeCodeSandboxOld;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SpringBootTest
class OjCodeBoxApplicationTests {

	@Test
	void contextLoads() {
		JavaNativeCodeSandboxOld javaNativeCodeSandboxOld = new JavaNativeCodeSandboxOld();
		ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
		executeCodeRequest.setInputList(Arrays.asList("1 2", "3 4"));
		String code = ResourceUtil.readStr("simplecodetest/Main.java", StandardCharsets.UTF_8);
		executeCodeRequest.setCode(code);
		executeCodeRequest.setLanguage("java");
		ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandboxOld.executeCode(executeCodeRequest);
		System.out.println(executeCodeResponse);
	}

}
