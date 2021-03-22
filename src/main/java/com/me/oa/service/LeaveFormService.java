package com.me.oa.service;

import com.me.oa.dao.EmployeeDao;
import com.me.oa.dao.LeaveFormDao;
import com.me.oa.dao.ProcessFlowDao;
import com.me.oa.entity.Employee;
import com.me.oa.entity.LeaveForm;
import com.me.oa.entity.ProcessFlow;
import com.me.oa.utils.MybatisUtils;

import java.util.Date;

/**
 * 请假单流程服务
 */
public class LeaveFormService {
    /**
     * 创建请假单
     * @param form 前端输入的请假单数据
     * @return 持久化后的请假单对象
     */
    public LeaveForm createLeaveForm(LeaveForm form) {
        MybatisUtils.executeUpdate(sqlSession -> {
            //1.持久化form表单数据,8级以下员工表单状态为processing,8级(总经理)状态为approved
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());
            if (employee.getLevel() == 8) {
                form.setState("approved");
            } else {
                form.setState("processing");
            }
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            leaveFormDao.insert(form);
            //2.增加第一条流程数据,说明表单已提交,状态为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow flow = new ProcessFlow();
            flow.setFormId(form.getFormId());
            flow.setOperatorId(employee.getEmployeeId());
            flow.setAction("apply");
            flow.setCreateTime(new Date());
            flow.setOrderNo(1);
            flow.setState("complete");
            flow.setIsLast(0);
            processFlowDao.insert(flow);
            //3.分情况创建其余流程数据
            //3.1 7级以下员工,生成部门经理审批任务,请假时间大于72小时,还需生成总经理审批任务
            if (employee.getLevel() < 7) {

            }

            //3.2 7级员工,生成总经理审批任务
            //3.3 8级员工,生成总经理审批任务,系统自动通过
            return null;
        });
        return null;
    }
}
