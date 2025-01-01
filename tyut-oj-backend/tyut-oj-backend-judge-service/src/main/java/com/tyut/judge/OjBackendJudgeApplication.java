package com.tyut.judge;

import com.tyut.judge.rabbitmq.RabbitMQInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.tyut")
@EnableFeignClients(basePackages = "com.tyut.serviceclient.service")
@EnableDiscoveryClient
public class OjBackendJudgeApplication {

    public static void main(String[] args) {
        // 项目启动，初始化消息队列
        RabbitMQInit.doInitRabbitMQ();
        SpringApplication.run(OjBackendJudgeApplication.class, args);
    }

}
