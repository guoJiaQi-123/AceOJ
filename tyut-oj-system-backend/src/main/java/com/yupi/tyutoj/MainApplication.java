package com.yupi.tyutoj;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication
@MapperScan("com.yupi.tyutoj.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@Slf4j
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        log.info("\n ________       ___  ___      ________      ________      _______       _______       ________     \n" +
                "|\\   ____\\     |\\  \\|\\  \\    |\\   ____\\    |\\   ____\\    |\\  ___ \\     |\\  ___ \\     |\\   ___ \\    \n" +
                "\\ \\  \\___|_    \\ \\  \\\\\\  \\   \\ \\  \\___|    \\ \\  \\___|    \\ \\   __/|    \\ \\   __/|    \\ \\  \\_|\\ \\   \n" +
                " \\ \\_____  \\    \\ \\  \\\\\\  \\   \\ \\  \\        \\ \\  \\        \\ \\  \\_|/__   \\ \\  \\_|/__   \\ \\  \\ \\\\ \\  \n" +
                "  \\|____|\\  \\    \\ \\  \\\\\\  \\   \\ \\  \\____    \\ \\  \\____    \\ \\  \\_|\\ \\   \\ \\  \\_|\\ \\   \\ \\  \\_\\\\ \\ \n" +
                "    ____\\_\\  \\    \\ \\_______\\   \\ \\_______\\   \\ \\_______\\   \\ \\_______\\   \\ \\_______\\   \\ \\_______\\\n" +
                "   |\\_________\\    \\|_______|    \\|_______|    \\|_______|    \\|_______|    \\|_______|    \\|_______|\n" +
                "   \\|_________|                                                                                    \n" +
                "                                                                                       ");
    }

}
