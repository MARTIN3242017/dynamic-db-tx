package com.samh.service.impl;

import com.samh.common.utils.AnalyseUtil;
import com.samh.mapper.OrderMapper;
import com.samh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AnalyseUtil analyseUtil;


    @Transactional
    @Override
    public void createOrder() {
        analyseUtil.setBusinessType(1);
        orderMapper.addAge();
        int k = 1 / 0;
        analyseUtil.setBusinessType(2);
        orderMapper.addAge();
    }
}
