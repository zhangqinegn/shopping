package com.qf.fronted.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// [{"createTime":, orderNo: '', }, {}]
public class WebOrder {

    /**
     * FastJSON在转json的时候，将日期装换为何种格式
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;   //订单的创建日期

    private String orderNo;   //订单编号
    private List<Order> orders = new ArrayList<>();  // 具体的订单

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
