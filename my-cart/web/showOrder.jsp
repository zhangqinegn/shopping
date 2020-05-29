<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/26
  Time: 11:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>订单展示页面</title>
    <link rel="stylesheet" href="admin/css/bootstrap.min.css" />
    <link rel="stylesheet" href="admin/css/font-awesome.min.css" />
    <link rel="stylesheet" href="admin/css/notifications/Lobibox.min.css">
    <script src="admin/js/vue.js"></script>
    <script src="admin/js/jquery.min.js"></script>
    <script src="admin/js/bootstrap.min.js"></script>
    <script src="admin/js/notifications/Lobibox.js"></script>
    <script src="admin/js/jquery.cookie.min.js"></script>
    <script src="admin/js/login-status.js"></script>
    <style>
        .navbar-text a  {
            color: #ffffff;
        }
        .login-text {
            margin-right: 50px;
        }
        .table tr:first-child {
            background-color: #e2e2e2;
            font-weight: 600;
        }
        .tab-content {
            padding-top: 24px;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-default navbar-inverse navbar-static-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="index.jsp" class="navbar-brand" style="color: #ffffff;"><i class="fa fa-sign-language"></i> 寻欢易购</a>
        </div>
    </div>
</nav>
<div class="container">
    <div class="row">
        <div class="col-xs-8 col-xs-push-2">
            <ul class="nav nav-tabs" id="orderTabs">
                <li class="active"><a id="0" href="#unpayOrders">未支付订单</a></li>
                <li><a id="1" href="#payedOrders">已支付订单</a></li>
            </ul>
            <div class="tab-content" id="app">
                <div id="unpayOrders" class="tab-pane active">
                    <table class="table" v-for="order in unpayedOrders">
                        <tr>
                            <td colspan="3">订单日期: {{order.createTime}} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;订单编号: {{order.orderNo}}</td>
                        </tr>
                        <!--                            <tr v-for="g in goods">-->
                        <tr v-for="o in order.orders">
                            <td style="max-width: 200px;">
                                <div class="media">
                                    <div class="media-left">
                                        <a href="javascript: void(0);">
                                            <img class="media-object" height="80px" width="62px" :src="o.goodsImg">
                                        </a>
                                    </div>
                                    <div class="media-body">
                                        <h4 class="media-heading">{{o.goodsTitle}}</h4>
                                    </div>
                                </div>
                            </td>
                            <td>￥{{o.goodPrice}}</td>
                            <td>{{o.goodNum}}</td>
                        </tr>
                    </table>
                </div>
                <div id="payedOrders" class="tab-pane">
                    <table class="table" v-for="order in payedOrders">
                        <tr>
                            <td colspan="3">订单日期: {{order.createTime}} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;订单编号: {{order.orderNo}}</td>
                        </tr>
                        <!--                            <tr v-for="g in goods">-->
                        <tr v-for="o in order.orders">
                            <td style="max-width: 200px;">
                                <div class="media">
                                    <div class="media-left">
                                        <a href="javascript: void(0);">
                                            <img class="media-object" height="80px" width="62px" :src="o.goodsImg">
                                        </a>
                                    </div>
                                    <div class="media-body">
                                        <h4 class="media-heading">{{o.goodsTitle}}</h4>
                                    </div>
                                </div>
                            </td>
                            <td>￥{{o.goodPrice}}</td>
                            <td>{{o.goodNum}}</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>

    var vm = new Vue({
        el: '#app',
        data: {
            unpayedOrders:[], //未支付订单
            payedOrders: [] //已支付订单
        }
    })

    function getOrders(status) {
        $.ajax({
            url: '${pageContext.servletContext.contextPath}/order',
            type: 'get',
            dataType: 'json',
            data: {status: status, method: 'getOrderOfStatus'},
            success: function(_data) {
                // console.log(this)
                if ('1' == status) {  //表示已支付订单
                    vm.payedOrders = [];
                    for(var i = 0;i < _data.length; i++) {
                        vm.payedOrders.push(_data[i]);
                    }
                }else { //未支付订单
                    vm.unpayedOrders = [];
                    for(var i = 0;i < _data.length; i++) {
                        vm.unpayedOrders.push(_data[i]);
                    }
                }
            }
        })
    }

    getOrders(0);  //默认获取未支付订单

    // 当用户点击的时候触发
    $('#orderTabs a').click(function() {
        // $(this).parent('.active'); //当前标签包含了 active 这个class的父标签
        if($(this).parent('.active').length == 0) { //如果找到的话，就表示当前标签处于未激活状态
            var status = $(this).prop('id');  //取到当前的tab的id, 与订单的状态是一致的
            getOrders(status);
            $(this).tab('show');   // 让对应的tab展示
        }
    })
</script>
</html>
