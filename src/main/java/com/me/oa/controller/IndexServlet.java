package com.me.oa.controller;

import com.me.oa.entity.Node;
import com.me.oa.entity.User;
import com.me.oa.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IndexServlet", urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // 因为不是同一个请求，window.location.href是心情求，request.getAttribute也是获取不到用户信息的，所以使用范围更大的session
        // 得到当前登录用户对象
        User user = (User) session.getAttribute("login_user");
        // 获取登录用户可用功能模块列表
        List<Node> nodeList = userService.selectNodeByUserId(user.getUserId());
        // 放入请求属性
        request.setAttribute("node_list", nodeList);
        // 请求派发至ftl进行展现
        request.getRequestDispatcher("/index.ftl").forward(request, response);
    }
}
