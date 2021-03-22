package com.me.oa.dao;

import com.me.oa.entity.ProcessFlow;
import com.me.oa.utils.MybatisUtils;
import org.junit.Test;

import java.util.Date;

public class ProcessFlowDaoTest {

    @Test
    public void insert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowDao dao = sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow flow = new ProcessFlow();
            flow.setFormId(3l);
            flow.setOperatorId(2l);
            flow.setAction("audit");
            flow.setResult("approved");
            flow.setReason("同意");
            flow.setCreateTime(new Date());
            flow.setAuditTime(new Date());
            flow.setOrderNo(1);
            flow.setState("ready");
            flow.setIsLast(1);
            dao.insert(flow);
            return null;
        });
    }
}