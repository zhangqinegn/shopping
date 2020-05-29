package com.qf.admin.servlet;

import com.qf.admin.domain.Item;
import com.qf.admin.service.ItemService;
import com.qf.admin.service.impl.ItemServiceImpl;
import com.qf.base.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/admin-item", name = "AdminItemServlet")
public class ItemServlet extends BaseServlet {

    private ItemService itemService = new ItemServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Item> itemList = itemService.getAll();
        sendJson(resp, itemList);
    }
}
