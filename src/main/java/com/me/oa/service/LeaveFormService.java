package com.me.oa.service;

import com.me.oa.dao.EmployeeDao;
import com.me.oa.dao.LeaveFormDao;
import com.me.oa.dao.NoticeDao;
import com.me.oa.dao.ProcessFlowDao;
import com.me.oa.entity.Employee;
import com.me.oa.entity.LeaveForm;
import com.me.oa.entity.Notice;
import com.me.oa.entity.ProcessFlow;
import com.me.oa.service.exception.BussinessException;
import com.me.oa.utils.MybatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
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
                // 请假单已提交消息
                String noticeContent = String.format("您的请假申请[%s-%s]已提交，请等待上级审批.",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
                // 通知部门经理审批消息
                noticeContent = String.format("%s-%s提起请假申请[%s-%s], 请尽快审批",
                        employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(dmanager.getEmployeeId(), noticeContent));
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

                // 请假单已提交消息
                String noticeContent = String.format("您的请假申请[%s-%s]已提交，请等待上级审批.",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
                // 通知总经理审批消息
                noticeContent = String.format("%s-%s提起请假申请[%s-%s], 请尽快审批",
                        employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(manager.getEmployeeId(), noticeContent));

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

                String noticeContent = String.format("您的请假申请[%s-%s]系统已自动审批通过",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(), noticeContent));
            }
            return form;
        });
        return savedForm;
    }

    /**
     * 获取指定任务状态及指定经办人对应的请假单列表
     *
     * @param pfState    ProcessFlow任务状态
     * @param operatorId 经办人帐号
     * @return 请假单及相关数据列表
     */
    public List<Map> getLeaveFormList(String pfState, Long operatorId) {
        return (List<Map>) MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormDao dao = sqlSession.getMapper(LeaveFormDao.class);
            List<Map> formList = dao.selectByParams(pfState, operatorId);
            return formList;
        });
    }

    /**
     * 审核请假单
     * 测试用例的日志如下，中途审批拒绝，后续取消
     * ==========下面就是根据formId查询假单流程对象结点processflow，执行了processFlowDao.selectByFormId(formId);========
     * [main] 15:20:53.970 DEBUG c.m.o.d.P.selectByFormId - ==>  Preparing: select * from adm_process_flow where form_id = ? order by order_no
     * [main] 15:20:54.078 DEBUG c.m.o.d.P.selectByFormId - ==> Parameters: 32(Long)
     * [main] 15:20:54.129 DEBUG c.m.o.d.P.selectByFormId - <==      Total: 3
     * ==========下面就是将当前processflow对象的状态设置为complete状态，然后更新到adm_process_flow表中，执行了processFlowDao.update(process);==============
     * [main] 15:20:54.132 DEBUG com.me.oa.dao.ProcessFlowDao.update - ==>  Preparing: UPDATE adm_process_flow SET form_id = ?, operator_id = ?,
     * action = ?, result = ?, reason = ?, create_time = ?, audit_time = ?, order_no = ?, state = ?, is_last = ? WHERE process_id = ?;
     * [main] 15:20:54.135 DEBUG com.me.oa.dao.ProcessFlowDao.update - ==> Parameters: 32(Long), 2(Long), audit(String), refused(String), null,
     * 2020-03-28 11:50:35.0(Timestamp), 2021-03-24 15:20:54.131(Timestamp), 2(Integer), complete(String), 0(Integer), 78(Long)
     * [main] 15:20:54.137 DEBUG com.me.oa.dao.ProcessFlowDao.update - <==    Updates: 1
     * ==========下面是根据formId查询表单，执行了leaveFormDao.selectById(formId);==================
     * [main] 15:20:54.140 DEBUG c.me.oa.dao.LeaveFormDao.selectById - ==>  Preparing: select * from adm_leave_form where form_id = ?
     * [main] 15:20:54.140 DEBUG c.me.oa.dao.LeaveFormDao.selectById - ==> Parameters: 32(Long)
     * [main] 15:20:54.144 DEBUG c.me.oa.dao.LeaveFormDao.selectById - <==      Total: 1
     * ==========下面是将后面假单流程对象结点状态变为cancel，然后更新节点，执行了processFlowDao.update(p);=================
     * [main] 15:20:54.145 DEBUG com.me.oa.dao.ProcessFlowDao.update - ==>  Preparing: UPDATE adm_process_flow SET form_id = ?, operator_id = ?,
     * action = ?, result = ?, reason = ?, create_time = ?, audit_time = ?, order_no = ?, state = ?, is_last = ? WHERE process_id = ?;
     * [main] 15:20:54.146 DEBUG com.me.oa.dao.ProcessFlowDao.update - ==> Parameters: 32(Long), 1(Long), audit(String), null, null,
     * 2020-03-28 11:50:36.0(Timestamp), null, 3(Integer), cancel(String), 1(Integer), 79(Long)
     * [main] 15:20:54.146 DEBUG com.me.oa.dao.ProcessFlowDao.update - <==    Updates: 1
     * ==========下面是将假单状态变为refused，然后根据formId更新假单，执行了leaveFormDao.update(form);===================
     * [main] 15:20:54.147 DEBUG com.me.oa.dao.LeaveFormDao.update - ==>  Preparing: UPDATE adm_leave_form SET employee_id = ?, form_type = ?,
     * start_time = ?, end_time = ?, reason = ?, create_time = ?, state = ? WHERE form_id = ?;
     * [main] 15:20:54.148 DEBUG com.me.oa.dao.LeaveFormDao.update - ==> Parameters: 3(Long), 1(Integer), 2020-03-29 00:00:00.0(Timestamp),
     * 2020-04-04 00:00:00.0(Timestamp), 没啥原因,单纯想休息几天(String), 2020-03-28 11:50:35.0(Timestamp), refused(String), 32(Long)
     * [main] 15:20:54.149 DEBUG com.me.oa.dao.LeaveFormDao.update - <==    Updates: 1
     *
     * @param formId     表单编号
     * @param operatorId 经办人(当前登录员工)
     * @param result     审批结果
     * @param reason     审批意见
     */
    public void audit(Long formId, Long operatorId, String result, String reason) {
        MybatisUtils.executeUpdate(sqlSession -> {
            // 1.无论同意/驳回,当前任务状态变更为complete
            ProcessFlowDao processFlowDao = sqlSession.getMapper(ProcessFlowDao.class);
            // 根据表单id能得到多个审批流程的记录,有各种状态ready-准备 process-正在处理 complete-处理完成 cancel-取消
            List<ProcessFlow> flowList = processFlowDao.selectByFormId(formId);
            if (flowList.size() == 0) {
                throw new BussinessException("PF001", "无效的审批流程");
            }
            // lambda表达式进行数据筛选
            // 获取当前任务ProcessFlow对象
            List<ProcessFlow> processList = flowList.stream().filter(p -> p.getOperatorId() == operatorId && p.getState().equals("process")).collect(Collectors.toList());
            ProcessFlow process = null;
            if (processList.size() == 0) {
                throw new BussinessException("PF002", "未找到待处理任务");
            } else {
                process = processList.get(0);
                process.setState("complete");
                process.setResult(result);
                process.setReason(reason);
                process.setAuditTime(new Date());
                processFlowDao.update(process);
            }

            // 2.如果当前任务是最后一个节点,代表流程结束,更新请假单状态为对应的approved/refused
            LeaveFormDao leaveFormDao = sqlSession.getMapper(LeaveFormDao.class);
            LeaveForm form = leaveFormDao.selectById(formId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            Employee employee = employeeDao.selectById(form.getEmployeeId());// 表单提交人信息
            Employee operator = employeeDao.selectById(operatorId);// 任务经办人信息
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            if (process.getIsLast() == 1) {
                form.setState(result); // approved|refused
                leaveFormDao.update(form);

                String strResult = null;
                if (result.equals("approved")) {
                    strResult = "批准";
                } else if (result.equals("refused")) {
                    strResult = "驳回";
                }
                String noticeContent = String.format("您的请假申请[%s-%s]%s%s已%s，审批意见：%s， 审批流程已结束",
                        sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                        operator.getTitle(), operator.getName(), strResult, reason); // 发给表单提交人的通知
                noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent));

                noticeContent = String.format("%s-%s提起请假申请[%s-%s]您已%s，审批意见：%s， 审批流程已结束",
                        employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                        strResult, reason); // 发给审批人的通知
                noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent));
            } else {
                List<ProcessFlow> readyList = flowList.stream().filter(p -> p.getState().equals("ready")).collect(Collectors.toList());
                // 3.如果当前任务不是最后一个节点且审批通过,那下一个节点的状态从ready变为process
                if (result.equals("approved")) {
                    ProcessFlow readyProcess = readyList.get(0);
                    readyProcess.setState("process");
                    processFlowDao.update(readyProcess);
                    //消息1: 通知表单提交人,部门经理已经审批通过,交由上级继续审批
                    String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已批准,审批意见:%s ,请继续等待上级审批",
                            sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                            operator.getTitle(), operator.getName(), reason);
                    noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent1));

                    //消息2: 通知总经理有新的审批任务
                    String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s],请尽快审批",
                            employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()));
                    noticeDao.insert(new Notice(readyProcess.getOperatorId(), noticeContent2));

                    //消息3: 通知部门经理(当前经办人),员工的申请单你已批准,交由上级继续审批
                    String noticeContent3 = String.format("%s-%s提起请假申请[%s-%s]您已批准,审批意见:%s,申请转至上级领导继续审批",
                            employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()), reason);
                    noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent3));
                } else if (result.equals("refused")) {
                    // 4.如果当前任务不是最后一个节点且审批驳回,则后续所有任务状态变为cancel,请假单状态变为refused
                    for (ProcessFlow p : readyList) {
                        p.setState("cancel");
                        processFlowDao.update(p);
                    }
                    form.setState("refused");
                    leaveFormDao.update(form);

                    //消息1: 通知申请人表单已被驳回
                    String noticeContent1 = String.format("您的请假申请[%s-%s]%s%s已驳回,审批意见:%s,审批流程已结束",
                            sdf.format(form.getStartTime()), sdf.format(form.getEndTime()),
                            operator.getTitle(), operator.getName(), reason);
                    noticeDao.insert(new Notice(form.getEmployeeId(), noticeContent1));

                    //消息2: 通知经办人表单"您已驳回"
                    String noticeContent2 = String.format("%s-%s提起请假申请[%s-%s]您已驳回,审批意见:%s,审批流程已结束",
                            employee.getTitle(), employee.getName(), sdf.format(form.getStartTime()), sdf.format(form.getEndTime()), reason);
                    noticeDao.insert(new Notice(operator.getEmployeeId(), noticeContent2));
                }
            }
            return null;
        });
    }
}
