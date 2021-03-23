package com.me.oa.service;

import com.me.oa.dao.EmployeeDao;
import com.me.oa.dao.LeaveFormDao;
import com.me.oa.dao.ProcessFlowDao;
import com.me.oa.entity.Employee;
import com.me.oa.entity.LeaveForm;
import com.me.oa.entity.ProcessFlow;
import com.me.oa.utils.MybatisUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 请假单流程服务
 */
public class LeaveFormService {
    /**
     * 创建请假单，并且会同步更新请假流水线数据
     *
     * @param form 前端输入的请假单数据
     * @return 持久化后的请假单对象
     */
    public LeaveForm createLeaveForm(LeaveForm form) {
        LeaveForm savedForm = (LeaveForm) MybatisUtils.executeUpdate(sqlSession -> {
            //1.持久化form表单数据,8级以下员工表单状态为processing,8级(总经理)状态为approved
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());
            if (employee.getLevel() == 8) { // 总经理自动审批通过
                form.setState("approved");
            } else {
                form.setState("processing");
            }
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            // 假单表adm_leave_form插入一条假单数据，后面逻辑就是添加请假流水线的数据
            leaveFormDao.insert(form);
            // ===============================================
            //2.增加第一条流程数据,说明表单已提交,状态为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow flow1 = new ProcessFlow();
            flow1.setFormId(form.getFormId());
            flow1.setOperatorId(employee.getEmployeeId());
            flow1.setAction("apply");
            flow1.setCreateTime(new Date());
            flow1.setOrderNo(1);
            flow1.setState("complete");
            flow1.setIsLast(0);
            processFlowDao.insert(flow1);
            //3.分情况创建其余流程数据
            //3.1 7级以下员工,生成部门经理审批任务,请假时间大于72小时,还需生成总经理审批任务
            if (employee.getLevel() < 7) {
                Employee dmanager = employeeDao.selectLeader(employee);
                ProcessFlow flow2 = new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(dmanager.getEmployeeId());
                flow2.setAction("audit");
                flow2.setCreateTime(new Date());
                flow2.setOrderNo(2);
                flow2.setState("process");
                long diff = form.getEndTime().getTime() - form.getStartTime().getTime();
                double hours = diff * 1.0 / (1000 * 3600);
                if (hours >= BussinessConstants.MANAGER_AUDIT_HOURS) {
                    flow2.setIsLast(0);
                    processFlowDao.insert(flow2);
                    Employee manager = employeeDao.selectLeader(dmanager);
                    ProcessFlow flow3 = new ProcessFlow();
                    flow3.setFormId(form.getFormId());
                    flow3.setOperatorId(manager.getEmployeeId());
                    flow3.setAction("audit");
                    flow3.setCreateTime(new Date());
                    flow3.setOrderNo(3);
                    flow3.setState("ready");
                    flow3.setIsLast(1);
                    processFlowDao.insert(flow3);
                } else {
                    // 不需要总经理审批的情况
                    flow2.setIsLast(1);
                    processFlowDao.insert(flow2);
                }
            } else if (employee.getLevel() == 7) { // 部门经理
                //3.2 7级员工,生成总经理审批任务
                Employee manager = employeeDao.selectLeader(employee);
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(manager.getEmployeeId());
                flow.setAction("audit");
                flow.setCreateTime(new Date());
                flow.setState("process");
                flow.setOrderNo(2);
                flow.setIsLast(1);
                processFlowDao.insert(flow);
            } else if (employee.getLevel() == 8) {
                //3.3 8级员工,生成总经理审批任务,系统自动通过
                ProcessFlow flow = new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(employee.getEmployeeId());
                flow.setAction("audit");

                flow.setResult("approved");
                flow.setReason("自动通过");

                flow.setCreateTime(new Date());
                flow.setAuditTime(new Date());

                flow.setState("complete");
                flow.setOrderNo(2);
                flow.setIsLast(1);
                processFlowDao.insert(flow);
            }
            return form;
        });
        return savedForm;
    }


    public List<Map> getLeaveFormList(String pfState, Long operatorId) {
        return (List<Map>) MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormDao dao = sqlSession.getMapper(LeaveFormDao.class);
            List<Map> formList = dao.selectByParams(pfState, operatorId);
            return formList;
        });
    }
}
