package com.qf.admin.dao;

import com.qf.commons.TableData;
import com.qf.fronted.domain.Goods;

import java.math.BigDecimal;

public interface GoodsDao {

    TableData<Goods> getPageData(Integer limit, Integer offset);

    void deleteGoods(Integer goodsId);

    void addGoods(String title, String imagePath, Integer stockNum, BigDecimal price, Integer itemId);
}
