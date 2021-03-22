package com.me.oa.dao;

import com.me.oa.entity.LeaveForm;
import com.me.oa.utils.MybatisUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeaveFormDaoTest {

    @Test
    public void insert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            LeaveFormDao dao = sqlSession.getMapper(LeaveFormDao.class);
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(4l); // 员工编号
            form.setFormType(1); // 事假
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = null; // 起始时间
            Date endTime = null; // 结束时间
            try {
                startTime = sdf.parse("2021-03-22 08:00:00");
                endTime = sdf.parse("2021-04-01 18:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            form.setStartTime(startTime);
            form.setEndTime(endTime);
            form.setReason("回家探亲"); // 请假事由
            form.setCreateTime(new Date()); // 创建时间
            form.setState("processing"); // 当前状态
            dao.insert(form);
            return null;
        });
    }
}