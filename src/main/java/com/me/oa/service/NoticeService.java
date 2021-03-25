package com.me.oa.service;

import com.me.oa.dao.NoticeDao;
import com.me.oa.entity.Notice;
import com.me.oa.utils.MybatisUtils;

import java.util.List;

/**
 * 消息服务
 */
public class NoticeService {
    /**
     * 查询指定员工的系统消息
     * @param receiverId 接收者的员工id
     * @return 最近100条消息
     */
    public List<Notice> getNoticeList(Long receiverId) {
        return (List) MybatisUtils.executeQuery(sqlSession -> {
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            return noticeDao.selectByReceiverId(receiverId);
        });
    }
}
