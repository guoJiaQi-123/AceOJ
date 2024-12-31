package com.tyut.user.controller.inner;

import com.tyut.model.entity.User;
import com.tyut.serviceclient.service.FeignUserClient;
import com.tyut.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/12/31
 * @apiNote TODO(一句话给出该类描述)
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements FeignUserClient {


    @Resource
    private UserService userService;

    /**
     * 根据用户ID查询用户
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    @Override
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    /**
     * 批量获取用户ID
     * @param userIds 主键ID列表
     * @return
     */
    @GetMapping("/get/list")
    @Override
    public List<User> listByIds(@RequestParam("userIds") Collection userIds) {
        return userService.listByIds(userIds);
    }
}
