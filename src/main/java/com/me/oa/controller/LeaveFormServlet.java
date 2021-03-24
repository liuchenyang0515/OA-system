package com.me.oa.controller;

import com.alibaba.fastjson.JSON;
import com.me.oa.entity.LeaveForm;
import com.me.oa.entity.User;
import com.me.oa.service.LeaveFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LeaveFormServlet", urlPatterns = "/leave/*")
public class LeaveFormServlet extends HttpServlet {
    private LeaveFormService leaveFormService = new LeaveFormService();
    private Logger logger = LoggerFactory.getLogger(LeaveFormServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        // http://localhost/leave/create
        String requestURI = request.getRequestURI();
        String methodName = requestURI.substring(requestURI.lastIndexOf("/") + 1);
        if (methodName.equals("create")) {
            this.create(request, response);
        } else if (methodName.equals("list")) {
            this.getLeaveFormList(request, response);
        } else if (methodName.equals("audit")) {
            this.audit(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * 创建请假单
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.接收各项请假单数据
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login_user");
        String formType = request.getParameter("formType");
        String strStartTime = request.getParameter("startTime");
        String strEndTime = request.getParameter("endTime");
        String reason = request.getParameter("reason");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");

        Map<String, String> result = new HashMap<>();
        try {
            LeaveForm form = new LeaveForm();
            form.setEmployeeId(user.getEmployeeId());
            form.setStartTime(sdf.parse(strStartTime));
            form.setEndTime(sdf.parse(strEndTime));
            form.setFormType(Integer.parseInt(formType));
            form.setReason(reason);
            form.setCreateTime(new Date());
            // 2.调用业务逻辑方法
            leaveFormService.createLeaveForm(form);
            result.put("code", "0");
            result.put("message", "success");
        } catch (Exception e) {
            logger.error("请假申请异常", e);
            result.put("code", e.getClass().getSimpleName());
            result.put("message", e.getMessage());
        }
        // 3.组织响应数据
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    /**
     * 查询需要审核的请假单列表
     * http://localhost/leave/list
     * <p>
     * {"msg":"","code":"0","data":"[{start_time=2020-03-26 08:00:00.0, reason=研发部部门经理请假单, create_time=2021-03-23 10:51:57.0,
     * employee_id=2, department_name=研发部, form_id=40, end_time=2020-04-01 18:00:00.0, name=齐紫陌, form_type=1, state=processing}]","count":"1"}
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getLeaveFormList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("login_user");
        List<Map> formList = leaveFormService.getLeaveFormList("process", user.getEmployeeId());
        Map<String, Object> result = new HashMap<>(); // 为了传给前端，value类型才是Object，踩的坑就是formList明明是数组，却加上了""变成了字符串导致无法渲染
        result.put("code", "0");
        result.put("msg", "");
        result.put("count", formList.size());
        result.put("data", formList);
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    /**
     * 处理审批操作
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void audit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String formId = request.getParameter("formId");
        String result = request.getParameter("result");
        String reason = request.getParameter("reason");
        User user = (User) request.getSession().getAttribute("login_user");
        Map<String, String> mapResult = new HashMap<>();
        try {
            leaveFormService.audit(Long.parseLong(formId), user.getEmployeeId(), result, reason);
            mapResult.put("code", "0");
            mapResult.put("message", "success");
        } catch (Exception e) {
            logger.error("请假单审核失败", e);
            mapResult.put("code", e.getClass().getSimpleName());
            mapResult.put("message", e.getMessage());
        }
        String json = JSON.toJSONString(mapResult);
        response.getWriter().println(json);
    }
}
