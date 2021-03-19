package com.me.oa.dao;

import com.me.oa.entity.Employee;

public interface EmployeeDao {
    public Employee selectById(Long employeeId);
}
