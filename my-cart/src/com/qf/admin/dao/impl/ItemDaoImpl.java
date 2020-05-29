package com.qf.admin.dao.impl;

import com.qf.admin.dao.ItemDao;
import com.qf.admin.domain.Item;
import com.qf.fronted.domain.Goods;
import com.qf.utils.DataUtils;
import com.qf.utils.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ItemDaoImpl implements ItemDao {

    @Override
    public List<Item> getAll() {
        List<Item> items = null;
        String sql = "select * from item";

        Connection connection = DbUtils.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            items = DataUtils.getAll(Item.class, rs);

            DbUtils.colse(rs, ps, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
