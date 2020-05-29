package com.qf.admin.service.impl;

import com.qf.admin.dao.ItemDao;
import com.qf.admin.dao.impl.ItemDaoImpl;
import com.qf.admin.domain.Item;
import com.qf.admin.service.ItemService;

import java.util.List;

public class ItemServiceImpl implements ItemService {

    private ItemDao itemDao = new ItemDaoImpl();

    @Override
    public List<Item> getAll() {
        return itemDao.getAll();
    }
}
