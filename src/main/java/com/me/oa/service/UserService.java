package com.me.oa.service;

import com.me.oa.dao.UserDao;
import com.me.oa.entity.User;
import com.me.oa.service.exception.BussinessException;

public class UserService {
    private UserDao userDao = new UserDao();

    /**
     * 根据前台输入进行登录校验
     *
     * @param username 前台输入的用户名
     * @param password 前台输入的密码
     * @return 校验通过后，包含对应用户数据的User实体类
     * @throws BussinessException L001-用户名不存在，L002-密码错误
     */
    public User checkLogin(String username, String password) {
        User user = userDao.selectByUsername(username);
        if (user == null) {
            // 抛出用户不存在异常
            throw new BussinessException("L001", "用户名不存在");
        }
        if (!password.equals(user.getPassword())) {
            throw new BussinessException("L002", "密码错误");
        }
        return user;
    }
}
