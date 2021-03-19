package com.me.oa.dao;

import com.me.oa.entity.Node;
import com.me.oa.utils.MybatisUtils;

import java.util.List;

/**
 *
 */
public class RbacDao {
    public List<Node> selectNodeByUserId(Long userId) {
        return (List) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectList("rbacmapper.selectNodeByUserId", userId));
    }
}
