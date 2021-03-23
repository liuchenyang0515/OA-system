package com.me.oa.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ForwardServlet", urlPatterns = "/forward/*")
public class ForwardServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        /**
         * /forward/form
         * /forward/a/b/c/form   有多级目录
         *
         */
        String subUri = uri.substring(1);
        String page = subUri.substring(subUri.indexOf("/"));
        // page就是ftl文件的完整路径,包含/
        request.getRequestDispatcher(page + ".ftl").forward(request, response);
    }
}
