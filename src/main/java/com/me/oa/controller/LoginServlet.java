package com.me.oa.controller;

import com.alibaba.fastjson.JSON;
import com.me.oa.entity.User;
import com.me.oa.service.UserService;
import com.me.oa.service.exception.BussinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = "/check_login")
public class LoginServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Map<String, Object> result = new HashMap<>();
        try {
            User user = userService.checkLogin(username, password);
            result.put("code", "0");
            result.put("message", "success");
        } catch (BussinessException e) {
            logger.error(e.getMessage(), e);
            result.put("code", e.getCode());
            result.put("message", e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("code", e.getClass().getSimpleName());
            result.put("message", e.getMessage());
        }
        // 返回对应结果
        String json = JSON.toJSONString(result);
        response.getWriter().println(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
