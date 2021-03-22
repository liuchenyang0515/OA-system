package com.me.oa.dao;

import com.me.oa.entity.Notice;
import com.me.oa.utils.MybatisUtils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class NoticeDaoTest {

    @Test
    public void insert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            NoticeDao dao = sqlSession.getMapper(NoticeDao.class);
            Notice notice = new Notice();
            notice.setReceiverId(2l);
            notice.setContent("测试消息");
            notice.setCreateTime(new Date());
            dao.insert(notice);
            return null;
        });
    }
}