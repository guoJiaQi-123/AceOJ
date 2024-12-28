package com.tyut.ojcodebox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version v1.0
 * @author OldGj 2024/12/25
 * @apiNote 验证项目的控制器
 */
@RestController("/")
public class OkController {
    @GetMapping("/health")
    public String ok() {
        return "ok";
    }
}
