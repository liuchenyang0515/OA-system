package com.me.oa.dao;

import com.me.oa.entity.Employee;
import org.apache.ibatis.annotations.Param;

public interface EmployeeDao {
    public Employee selectById(Long employeeId);

    /**
     * 根据传入员工对象获取上级主管对象
     * 写@Param是方便在xml中操作实体类对象的属性，进行条件筛选
     * @param employee
     * @return
     */
    public Employee selectLeader(@Param("emp") Employee employee);
}
