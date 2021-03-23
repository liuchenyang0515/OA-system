package com.me.oa.dao;

import com.me.oa.entity.LeaveForm;
import com.me.oa.utils.MybatisUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    /**
     * 实现四表关联查询，给定查询条件(complete、process、ready)和经办人id(operator_id)，查询经办人该审批状态的假单，并查出提交假单的员工姓名和部门
     * <p>
     * [main] 16:49:33.951 DEBUG c.m.o.d.LeaveFormDao.selectByParams - ==>  Preparing: SELECT f.*, e.name, d.department_name FROM adm_leave_form f,
     * adm_process_flow pf, adm_employee e, adm_department d WHERE f.form_id = pf.form_id AND f.employee_id = e.employee_id
     * AND e.department_id = d.department_id AND pf.state = ? AND pf.operator_id = ?
     * [main] 16:49:34.096 DEBUG c.m.o.d.LeaveFormDao.selectByParams - ==> Parameters: process(String), 2(Long)
     * [main] 16:49:34.133 DEBUG c.m.o.d.LeaveFormDao.selectByParams - <==      Total: 1
     * [{start_time=2021-03-01 08:00:00.0, reason=测试, create_time=2021-03-23 15:20:29.0, employee_id=4, department_name=研发部, form_id=43,
     * end_time=2021-03-19 18:00:00.0, name=宋彩妮, form_type=4, state=processing}]
     */
    @Test
    public void testSelectByParams() {
        MybatisUtils.executeUpdate(sqlSession -> {
            LeaveFormDao dao = sqlSession.getMapper(LeaveFormDao.class);
            List<Map> list = dao.selectByParams("process", 2l);
            System.out.println(list);
            return list;
        });
    }
}