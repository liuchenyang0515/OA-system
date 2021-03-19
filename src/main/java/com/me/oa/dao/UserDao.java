package com.me.oa.dao;

import com.me.oa.entity.User;
import com.me.oa.utils.MybatisUtils;

/**
 * 用户表
 */

public class UserDao {
    /**
     * 按用户名查询用户表
     * @param username 用户名
     * @return  User对象包含对应的用户信息，null则代表对象不存在
     */
    public User selectByUsername(String username) {
        User user = (User) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("usermapper.selectByUsername", username));
        return user;
    }
}
