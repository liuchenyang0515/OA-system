package com.me.oa.dao;

import com.me.oa.entity.Department;
import com.me.oa.entity.Employee;

public interface DepartmentDao {
    public Department selectById(Long departmentId);
}
