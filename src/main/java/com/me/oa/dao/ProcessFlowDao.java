package com.me.oa.dao;

import com.me.oa.entity.ProcessFlow;

import java.util.List;

public interface ProcessFlowDao {
    public void insert(ProcessFlow flow);
    public void update(ProcessFlow processFlow);
    public List<ProcessFlow> selectByFormId(Long formId);
}
