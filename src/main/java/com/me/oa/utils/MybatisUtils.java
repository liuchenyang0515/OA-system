package com.me.oa.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class MybatisUtils {
    // 利用static(静态)属于类不属于对象，且全局唯一
    private static SqlSessionFactory sqlSessionFactory = null;
    // 利用静态块在初始化类时实例化sqlSessionFactory
    static {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
            // 初始化错误时，通过抛出异常ExceptionInitializerError通知调用者
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Object executeQuery(Function<SqlSession, Object> func) {
        return null;
    }
}
