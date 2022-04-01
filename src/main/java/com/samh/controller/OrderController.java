package com.samh.controller;

import com.samh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/add")
    public Object createOrder() {
        orderService.createOrder();
        return "OK";
    }
}
