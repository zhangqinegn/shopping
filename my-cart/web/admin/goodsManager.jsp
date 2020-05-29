<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/27
  Time: 11:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="commons/header.jsp"%>
<style>
    .pagination {
        margin-top: 0;
        margin-bottom: 0;
    }
</style>
<div style="background-color: #ffffff; border-radius: 3px; padding: 12px;">

    <table id="goodsDataTable" class="table table-bordered table-striped"></table>

    <div id="goodsTableToolbar">
        <button class="btn btn-success" onclick="showAddGoodsModal()">添加</button>
        <button class="btn btn-primary">导入</button>
        <button class="btn btn-danger">导出</button>
    </div>

    <div class="modal" id="addGoodsModal" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button onclick="destoryAddGoodsModal()" class="close">
                        <span>&times;</span>
                    </button>
                    <h3 class="modal-title">添加商品</h3>
                </div>
                <div class="modal-body">
                    <form>
                        <input type="hidden" id="imageServerPath">
                        <div class="form-group">
                            <label for="title">标题</label>
                            <input class="form-control" autocomplete="off" id="title">
                        </div>
                        <div class="form-group">
                            <label for="items">种类</label>
                            <select class="form-control" id="items">
                            </select>
                        </div>
                        <div class="form-group">
                            <label>展示图<span style="color: red;">(在选择展示图之前必须选种类)</span></label>
                            <div style="margin-bottom: 6px;">
                                <img src="../images/default.png" width="158px" id="preDisplayImage" height="180px">
                            </div>
                            <div class="file-upload-inner file-upload-inner-right ts-forms">
                                <div class="input append-small-btn">
                                    <div class="file-button">
                                        Browse
                                        <input id="displayImage" autocomplete="off" onchange="uploadDispalyImage(this)" type="file">
                                    </div>
                                    <input type="text" autocomplete="off" id="append-small-btn" placeholder="no file selected">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="price">价格</label>
                            <input class="form-control" autocomplete="off" id="price">
                        </div>
                        <div class="form-group">
                            <label for="stock">库存</label>
                            <input class="form-control" autocomplete="off" id="stock">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button onclick="destoryAddGoodsModal()" class="btn btn-danger">取消</button>
                    <button class="btn btn-success" onclick="addGoods()">确认</button>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="commons/footer.jsp"%>
<script>
    /**
     * {
     *           "total": 23,
     *           "rows": [{type: '', }, {}, {}]
     *       }
     */
    $('#goodsDataTable').bootstrapTable({
        url: '${pageContext.servletContext.contextPath}/admin-goods',
        method: 'GET',
        columns: [
            // field是返回rows中每个对象的属性名
            {title: '类型', field: 'type', align: 'center'},
            {title: '商品编号', field: 'goodId', align: 'center'},
            {title: '描述信息', field: 'title', align: 'center', formatter: formatTitle },
            {title: '价格', field: 'price', align: 'center'},
            {title: '状态', field: 'status', align: 'center', formatter: formatStatus},
            {title: '库存', field: 'stock', align: 'center'},
            {title: '创建日期', field: 'createTime', align: 'center'},
            {title: '更新日期', field: 'updateTime', align: 'center'},
            {title: '操作', align: 'center', formatter: addProcessBtn}
        ],
        /**
         * 决定了在哪边分页：client server, 两种不同的配置决定了服务器端返回的数据格式。
         *  client分页, 服务器端返回的数据  [{}, {}, {}]
         *  server端分页，服务器端返回的数据：{rows:[{}, {}], total: 23}
         */
        sidePagination: 'server',
        pagination: true, //表示使用分页
        toolbar: '#goodsTableToolbar'  //设置工具条
    })

    /**
     * 当内容长度大于30的时候，多余的不显示
     * @param value
     * @returns {string|*}
     */
    function formatTitle(value) {
        var length = value.length; //获取文本长度
        if(length > 30) {
            return value.substring(0, 31) + '...';
        }
        return value;
    }

    // 格式化商品的状态
    function formatStatus(value) {
        if(value == 1) {
            return '<span class="label label-success">可用</span>'
        }
        return '<span class="label label-warning">不可用</span>'
    }

    // 添加操作按钮
    function addProcessBtn(value, row) {
        var goodsId = row.id;
        return '<button onclick="deleteGoods(' + goodsId + ')" class="btn btn-danger btn-xs">删除</button>'
    }


    // 删除对应的商品，实际就是改状态
    function deleteGoods(goodsId) {
        $.ajax({
            url: '${pageContext.servletContext.contextPath}/admin-goods',
            data: {goodsId: goodsId, method: 'deleteGoods'},
            dataType: 'json',
            type: 'post',
            success: function(_data) {
                // 表示删除成功，更新表格
                if(_data.code > 0) {
                    $('#goodsDataTable').bootstrapTable('refresh');
                }else { // 删除失败，给用户一个提示
                    showErrorBox('删除失败, 请联系管理员.')
                }
            }
        })
    }

    /**
     * 展示添加的模态框
     *   1. 首先将所有种类查出来，渲染在模态框中的种类下拉列表中
     *   2. 显示模态框
     */
    function showAddGoodsModal() {
        var items = $('#items');
        items.empty(); //清空种类的下拉列表
        /**
         * 第一个参数url地址
         * 第二个参数是请求的参数
         * 第三个参数是回调方法。
         */
        $.getJSON('${pageContext.servletContext.contextPath}/admin-item', {}, function(_data){
            for(var i = 0; i < _data.length; i++){
                var item = _data[i];
                // <option value="3#shoes">鞋子</option>
                items.append('<option value="' + (item.id + '#' + item.alias)+ '">' + item.name + '</option>')
            }
        });

        $('#addGoodsModal').modal('show');
    }

    // 隐藏模态框
    function destoryAddGoodsModal() {
        resetModal();
        $('#addGoodsModal').modal('hide');
    }

    /**
     * 上传展示图：
     *    1.在选择图片旁边的那个输入框中显示文件的名字；
     *    2.上传图片。
     *    3.回显图片。
     */
    function uploadDispalyImage(fileTag) {
        var item = $('#items').val(); //取种类的值，目的是上传的文件放在哪个目录下

        var itemAlias = item.split('#')[1];

        var displayImage = fileTag.files[0];
        // console.log(displayImage.name)  //文件的名字，在很多网站中要去判断文件的格式是否正确，要通过后缀来判断
        // console.log(displayImage.size); // 文件的大小，单位是字节，如果要判断多少 M, size / (1024 * 1024) 结果单位就位M
        var name = displayImage.name;

        $('#append-small-btn').val(name);

        var formData = new FormData();
        formData.append('displayImage', displayImage);
        formData.append('itemAlias', itemAlias);
        formData.append('method', 'uploadDisplayImage')

        $.ajax({
            url: '${pageContext.servletContext.contextPath}/admin-goods',
            type: 'post',
            dataType: 'json',
            data: formData,
            /**
             * 内容类型。默认值：application/x-www-form-urlencoded;
             * false表示不设置，将FormData原封不动的直接提价服务器端
             */
            contentType: false,
            /**
             * processData 处理数据， 例如get请求，会将data中的数据，name=张三&password=125,
             * 对于FormData包含文件，此处要为false, 禁止转换
             */
            processData: false,
            success: function(_data) {
                // {"filePath":"mobile/147617383170300EfssbV3XDA8PFrTsSj.jpg","imageUrl":"http://localhost/mobile/147617383170300EfssbV3XDA8PFrTsSj.jpg"}
                $('#preDisplayImage').prop('src', _data.imageUrl);  //回显图片
                $('#imageServerPath').val(_data.filePath);
            }
        })
    }

    // 关闭模态框的时候，重置所有的信息
    function resetModal() {
        $('#imageServerPath').val('');
        $('#items').empty();
        $('#append-small-btn').val('');
        $('#preDisplayImage').prop('src', '../images/default.png');

        $('#title').val('');
        $('#price').val('');
        $('#stock').val('');
        $('#displayImage').val('');
    }

    // 添加商品
    function addGoods() {
        var imageServerPath = $('#imageServerPath').val();  //商品的路径
        var item = $('#items').val(); // 1#books 种类
        var itemId = item.split('#')[0]; //获取种类的id
        var price = $('#price').val();  //价格
        var stock = $('#stock').val();
        var title = $('#title').val();

        // 构建请求的参数
        var requestParams = {
            imageServerPath: imageServerPath,
            itemId: itemId,
            price: price,
            stock: stock,
            title: title,
            method: 'addGoods'
        };

        /**
         * 第一个是地址
         * 第二个是数据
         * 第三个是回调函数
         * 第四个是服务器返回的数据类型
         */
        $.post('${pageContext.servletContext.contextPath}/admin-goods', requestParams, function(_data) {
            if(_data.code > 0) {
                destoryAddGoodsModal(); //销毁添加商品模态框
                $('#goodsDataTable').bootstrapTable('refresh');
            }else {
                showErrorBox('添加商品失败，请联系管理员.')
            }
        }, 'json');
    }

</script>
