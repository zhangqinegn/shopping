package com.qf.admin.service;

import com.qf.commons.TableData;
import com.qf.fronted.domain.Goods;

import java.math.BigDecimal;

/**
 * 通过bootstrap-table显示的分页数据：
 * {
 *     "rows": [],
 *     "total": 23
 * }
 */
public interface GoodsService {

    /**
     * 返回分页的数据
     * @param limit      每页的数据量
     * @param offset     每页开始的索引
     * @return
     */
    TableData<Goods> getPageData(Integer limit, Integer offset);

    /**
     * 根据id删除对应的商品
     * @param goodsId
     */
    void deleteGoods(Integer goodsId);

    /**
     * 添加商品
     * @param title
     * @param imagePath
     * @param stockNum
     * @param price
     */
    void addGoods(String title, String imagePath, Integer stockNum, BigDecimal price, Integer itemId);
}
