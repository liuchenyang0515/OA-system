package com.me.oa.service;

import com.me.oa.dao.RbacDao;
import com.me.oa.dao.UserDao;
import com.me.oa.entity.Node;
import com.me.oa.entity.User;
import com.me.oa.service.exception.BussinessException;
import com.me.oa.utils.MD5Utils;

import java.util.List;

public class UserService {
    private UserDao userDao = new UserDao();
    private RbacDao rbacDao = new RbacDao();

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
        // 加盐后MD5摘要，进行比对
        // 对前台输入的密码加盐混淆后生成MD5，与保存在数据库中的MD5密码进行比对
        String md5 = MD5Utils.md5Digest(password, user.getSalt());
        if (!md5.equals(user.getPassword())) {
            throw new BussinessException("L002", "密码错误");
        }
        return user;
    }

    public List<Node> selectNodeByUserId(Long userId) {
        List<Node> nodeList = rbacDao.selectNodeByUserId(userId);
        return nodeList;
    }
}
