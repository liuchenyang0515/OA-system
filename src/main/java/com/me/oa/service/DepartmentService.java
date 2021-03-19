package com.me.oa.service;

import com.me.oa.dao.DepartmentDao;
import com.me.oa.entity.Department;
import com.me.oa.utils.MybatisUtils;

/**
 * 部门服务
 */
public class DepartmentService {
    public Department selectById(Long departmentId) {
        return (Department) MybatisUtils.executeQuery(sqlSession -> sqlSession.getMapper(DepartmentDao.class).selectById(departmentId));
    }
}
