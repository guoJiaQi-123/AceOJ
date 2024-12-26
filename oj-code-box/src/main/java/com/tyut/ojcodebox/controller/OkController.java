package com.tyut.ojcodebox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version v1.0
 * @author OldGj 2024/12/25
 * @apiNote TODO(一句话给出该类描述)
 */
@RestController("/")
public class OkController {
    @GetMapping("health")
    public String ok() {
        return "ok";
    }
}
