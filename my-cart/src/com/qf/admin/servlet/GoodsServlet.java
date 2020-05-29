package com.qf.admin.servlet;

import com.qf.admin.service.GoodsService;
import com.qf.admin.service.impl.GoodsServiceImpl;
import com.qf.base.BaseServlet;
import com.qf.commons.Data;
import com.qf.commons.TableData;
import com.qf.fronted.domain.Goods;
import com.qf.fronted.servlet.SyncCartDataServlet;
import org.apache.commons.text.RandomStringGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@WebServlet(value = "/admin-goods", name = "AdminGoodsServlet")
@MultipartConfig
public class GoodsServlet extends BaseServlet {

    private final String imageServerAddress = "http://localhost/";

    private GoodsService goodsService = new GoodsServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("method");
        if(null == methodName) {
            methodName = "showList";
        }
        try {
            Method method = GoodsServlet.class.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 展示分页数据
    private void showList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 每页显示的数据,  limit 后的第一个参数 limit 0, 10
        String limit = req.getParameter("limit");
        // 偏移量 limit 后的第一个参数 limit 0, 10
        String offset = req.getParameter("offset");

        TableData<Goods> tableData = goodsService.getPageData(Integer.parseInt(limit), Integer.parseInt(offset));

        sendJson(resp, tableData);
    }

    // 删除对应的商品
    private void deleteGoods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String goodsId = req.getParameter("goodsId");

        Data data = null;
        try{
            goodsService.deleteGoods(Integer.parseInt(goodsId));
            data = new Data(1, "删除成功");
        }catch (Exception ex) {
            ex.printStackTrace();
            data = new Data(-1, "删除失败");
        }

        sendJson(resp, data);
    }

    /**
     * 处理文件上传
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void uploadDisplayImage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取商品的别名，目的存储图片对应的位置
        String alias = req.getParameter("itemAlias");

        // part中封装了文件的所以的信息
        Part part = req.getPart("displayImage");

        // contentDisposition的数据内容是： form-data; name="avatar"; filename="huawei.jpg"
        String contentDisposition = part.getHeader("content-disposition");

        String fileNamePrefix = "filename=\"";

        // prefix(前缀)  suffix(后缀)
        // 获取 filename=" 所在的索引位置
        int fileNamePrefixIndex = contentDisposition.indexOf(fileNamePrefix);

        // 获取带引号的文件名 huawei.jpg"
        String fileNameWithQuotation = contentDisposition.substring(fileNamePrefixIndex + fileNamePrefix.length());

        // 文件名
        String fileName = fileNameWithQuotation.substring(0, fileNameWithQuotation.length() - 1);

        // 获取文件的后缀名  .jgp
        String fileSuffixName = fileName.substring(fileName.lastIndexOf("."));

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange(new char[]{'a', 'z'}, new char[]{'A','Z'}, new char[]{'0', '9'})
                .build();

        // 生成新的文件名, System.nanoTime() 获取从1970年开始到现在的纳秒时间
        String newFileName = System.nanoTime() + generator.generate(18) + fileSuffixName;
        String filePath = alias + "/" + newFileName;

        InputStream is = part.getInputStream();

        byte[] bs = new byte[4096];

        OutputStream os = new FileOutputStream("D:/nginx-server/images/" + filePath);
        BufferedOutputStream bos = new BufferedOutputStream(os);

        int length = 0;
        while(-1 != (length = is.read(bs))) {
            bos.write(bs, 0, length);
        }

        bos.flush();
        os.flush();
        bos.close();
        os.close();
        is.close();

        Map<String, String> map = new HashMap<>();
        map.put("filePath", filePath); //要插入到数据库
        map.put("imageUrl", imageServerAddress + filePath); // 前端要展示用

        sendJson(resp, map);
    }

    /**
     * 添加商品
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addGoods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**
         * imageServerPath: imageServerPath,
         *             itemId: itemId,
         *             price: price,
         *             stock: stock,
         *             title: title,
         */
        String imageServerPath = req.getParameter("imageServerPath");
        String itemId = req.getParameter("itemId");
        String price = req.getParameter("price");
        String stock = req.getParameter("stock");
        String title = req.getParameter("title");

        Data data = null;
        try{
            // imagePath, Integer stockNum, BigDecimal price, Integer itemId
            goodsService.addGoods(title, imageServerPath, Integer.parseInt(stock), BigDecimal.valueOf(Double.parseDouble(price)), Integer.parseInt(itemId));
            data = new Data(1, "success");
        }catch (Exception ex) {
            ex.printStackTrace();
            data = new Data(-1, "fail");
        }
        sendJson(resp, data);
    }
}
