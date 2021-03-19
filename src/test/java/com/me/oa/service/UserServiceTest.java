package com.me.oa.service;

import com.me.oa.entity.User;
import org.junit.Test;

public class UserServiceTest {
    private UserService userService = new UserService();

    @Test
    public void checkLogin1() {
        // 用户名密码都不对
        userService.checkLogin("uu", "1234");
    }

    @Test
    public void checkLogin2() {
        // 用户名存在但密码不对
        userService.checkLogin("m8", "1234");
    }

    @Test
    public void checkLogin3() {
        // 用户名和密码都正确
        User user = userService.checkLogin("m8", "test");
        System.out.println(user);
    }
}