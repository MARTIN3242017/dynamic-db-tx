package com.samh.service.impl;

import com.samh.common.utils.AnalyseUtil;
import com.samh.mapper.ABMapper;
import com.samh.mapper.ShenceMapper;
import com.samh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ShenceMapper shenceMapper;
    @Autowired
    ABMapper abMapper;
    @Autowired
    AnalyseUtil analyseUtil;


    @Transactional
    @Override
    public void createOrder() {
        try {
            analyseUtil.setBusinessType(1);
            shenceMapper.addAge();
            int k = 1 / 0;
            analyseUtil.setBusinessType(2);
            abMapper.addAge();
        } catch (Exception e) {
            System.out.println("执行出错");
        }
    }
}
