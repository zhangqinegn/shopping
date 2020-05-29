package com.qf.admin.dao.impl;

import com.qf.admin.dao.GoodsDao;
import com.qf.commons.TableData;
import com.qf.fronted.domain.Goods;
import com.qf.utils.DataUtils;
import com.qf.utils.DbUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GoodsDaoImpl implements GoodsDao {

    @Override
    public TableData<Goods> getPageData(Integer limit, Integer offset) {
        TableData<Goods> tableData = new TableData<>();
        List<Goods> goodsList = null;

        // 查询数据的sql
        StringBuffer dataSql = new StringBuffer("SELECT g.id, i.name type, g.price, g.small_pic imageSrc, g.status, g.stock_num stock, g.create_time createTime, ")
                .append(" g.update_time updateTime, g.title, g.goods_id goodId ")
                .append(" from goods g JOIN item i on g.item_id = i.id order by g.create_time desc")
                .append(" limit ?, ?");

        // 查询总数的sql
        String countSql = "select count(1) from goods g JOIN item i on g.item_id = i.id";

        Connection connection = DbUtils.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(dataSql.toString());
            ps.setObject(1, offset);
            ps.setObject(2, limit);

            ResultSet rs = ps.executeQuery();

            goodsList = DataUtils.getAll(Goods.class, rs);

            ps = connection.prepareStatement(countSql);  //执行查询总数的sql
            rs = ps.executeQuery();  // 返回的结果就一行一列

            rs.next();  //
            Integer total = rs.getInt(1); //获取总数

            tableData.setRows(goodsList); //设置分页要显示的数据
            tableData.setTotal(total);

            DbUtils.colse(rs, ps, connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableData;
    }

    /**
     * 在实际的工作中，重要的数据前端点击 “删除” 按钮的时候，并不会实际从数据库中删除，
     * 往往是通过改变状态来删除，也就是常说的 “软删除”
     * @param goodsId
     */
    @Override
    public void deleteGoods(Integer goodsId) {
        String sql = "update goods set status = 0 where id = ?";
        Connection connection = DbUtils.getConnection();

        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setObject(1,goodsId);
            ps.executeUpdate();

            DbUtils.colse(null, ps, connection);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public synchronized void  addGoods(String title, String imagePath, Integer stockNum, BigDecimal price, Integer itemId) {
        String insertSql = "insert into goods(item_id, goods_id, title, small_pic, price, status, create_time, update_time, stock_num) values(?,?,?,?,?,?,?,?,?)";
        Connection connection = DbUtils.getConnection();

        try{
            String querySql = "select max(goods_id) from goods";

            PreparedStatement ps = connection.prepareStatement(querySql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int goodsId = rs.getInt(1);  //查询goods_id

            Date date = new Date();
            ps = connection.prepareStatement(insertSql);
            ps.setObject(1, itemId);
            ps.setObject(2, goodsId + 1);
            ps.setObject(3, title);
            ps.setObject(4, imagePath);
            ps.setObject(5, price);
            ps.setObject(6, 1);
            ps.setObject(7, date);
            ps.setObject(8, date);
            ps.setObject(9, stockNum);

            ps.executeUpdate();

            DbUtils.colse(rs, ps, connection);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
