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
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * 创建请假单
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
}
