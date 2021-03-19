package com.me.oa.service;

import com.me.oa.dao.EmployeeDao;
import com.me.oa.entity.Employee;
import com.me.oa.utils.MybatisUtils;

/**
 * 员工服务
 */
public class EmployeeService {
    /**
     * 按编号查找员工
     * @param employeeId 员工编号
     * @return 员工对象，不存在时返回null
     */
    public Employee selectById(Long employeeId) {
        return (Employee) MybatisUtils.executeQuery(sqlSession -> {
            // 传入接口自动生成接口实现类
            EmployeeDao employeeDao = sqlSession.getMapper(EmployeeDao.class);
            return employeeDao.selectById(employeeId); // 可以直接调用对应sql语句，sql语句id就是方法名，一定要满足xml注释写出的4个条件
        });
    }
}
