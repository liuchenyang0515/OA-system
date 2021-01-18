package com.me.oa.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

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


    /**
     * 执行SELECT查询SQL
     *
     * @param func 要执行查询语句的代码块
     * @return 查询结果
     */
    public static Object executeQuery(Function<SqlSession, Object> func) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            Object obj = func.apply(sqlSession);
            return obj;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 执行INSERT/UPDATE/DELETE写操作SQL
     *
     * @param func 要执行的写操作代码块
     * @return 写操作后返回的结果
     */
    @Test
    public static Object executeUpdate(Function<SqlSession, Object> func) {
        // 写操作和查询操作区别就是对事物的控制。这里在上一步查询基础上增加了事务的控制
        SqlSession sqlSession = sqlSessionFactory.openSession(false); // 不自动提交
        try {
            Object obj = func.apply(sqlSession);
            sqlSession.commit();
            return obj;
        } catch (RuntimeException e) {
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }
    }
}
