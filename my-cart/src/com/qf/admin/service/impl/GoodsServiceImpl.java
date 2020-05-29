package com.qf.admin.service.impl;


import com.qf.admin.dao.GoodsDao;
import com.qf.admin.dao.impl.GoodsDaoImpl;
import com.qf.admin.service.GoodsService;
import com.qf.commons.TableData;
import com.qf.fronted.domain.Goods;

import java.math.BigDecimal;

public class GoodsServiceImpl implements GoodsService {

    private GoodsDao goodsDao = new GoodsDaoImpl();

    @Override
    public TableData<Goods> getPageData(Integer limit, Integer offset) {
        return goodsDao.getPageData(limit, offset);
    }

    @Override
    public void deleteGoods(Integer goodsId) {
        goodsDao.deleteGoods(goodsId);
    }

    @Override
    public void addGoods(String title, String imagePath, Integer stockNum, BigDecimal price, Integer itemId) {
        goodsDao.addGoods(title, imagePath, stockNum, price, itemId);
    }
}
