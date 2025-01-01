package com.tyut.serviceclient.service;


import com.tyut.common.common.ErrorCode;
import com.tyut.common.exception.BusinessException;
import com.tyut.model.entity.User;
import com.tyut.model.enums.UserRoleEnum;
import com.tyut.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.tyut.common.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author 
 * @from 
 */
@FeignClient(name = "oj-backend-user-service", path = "/api/user/inner")
public interface FeignUserClient {


    /**
     * 根据用户ID查询用户
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);

    /**
     * 批量获取用户ID
     * @param userIds 主键ID列表
     * @return
     */
    @GetMapping("/get/list")
    List<User> listByIds(@RequestParam("userIds") Collection<Long> userIds);


    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    /**
     * 是否为管理员（默认实现）
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }


}
