package com.me.oa.utils;


import org.junit.Test;

public class MybatisUtilsTestor {
    @Test
    public void testcase1() {
        String result = (String) MybatisUtils.executeQuery(sqlSession -> {
            String out = (String) sqlSession.selectOne("test.sample"); // 命名空间.sql的id
            return out;
        });
        System.out.println(result);
    }
    // 这两种写法一样
    @Test
    public void testcase2() {
        String result = (String) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("test.sample"));
        System.out.println(result);
    }
}