<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<style>
    table {
        width: 100px;
        table-layout: fixed; /* 只有定义了表格的布局算法为fixed，下面td的定义才能起作用。 */
    }

    td {
        width: 100%;
        word-break: keep-all; /* 不换行 */
        white-space: nowrap; /* 不换行 */
        overflow: hidden; /* 内容超出宽度时隐藏超出部分的内容 */
        text-overflow: ellipsis; /* 当对象内文本溢出时显示省略标记(...) ；需与overflow:hidden;一起使用*/
        -o-text-overflow: ellipsis;
        -icab-text-overflow: ellipsis;
        -khtml-text-overflow: ellipsis;
        -moz-text-overflow: ellipsis;
        -webkit-text-overflow: ellipsis;
    }
</style>
<div class="row">
    <div class="col-md-8 col-xs-6">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
    </div>
    <div class="col-md-4 text-right">
        <world:hasPermission permissions="/sys/log/add">
            <a class="btn btn-primary btn-sm no-border" href="sys/log/add">
                <i class="ace-icon fa fa-plus"></i>添加
            </a>
        </world:hasPermission>
        <world:hasPermission permissions="/sys/log/export">
            <a class="btn btn-primary btn-sm no-border" onclick="q_exportExcel();" href="javascript:void(0);">
                <i class="ace-icon fa fa-download"></i>导出
            </a>
            <form id="q_hiddenForm" method="post">
                <input type="hidden" id="params" name="params">
            </form>
        </world:hasPermission>
    </div>
    <div class="col-md-12">
        <form class="form-inline" id="q_searchForm" method="post">
            <input name="pageIndex" type="hidden">
            <select name="category" class="form-control">
                <option value="">请选择类别</option>
                <c:forEach items="${categories}" var="item" varStatus="status">
                    <option value="${item}">${item}</option>
                </c:forEach>
            </select>
            <select name="action" class="form-control">
                <option value="">请选择动作</option>
                <c:forEach items="${actions}" var="item" varStatus="status">
                    <option value="${item}">${item}</option>
                </c:forEach>
            </select>
            <%@include file="/WEB-INF/common/AdminLTE/fragment/query-fragement.jsp" %>
            <button type="button" onclick="q_refreshDataTable()" class="btn btn-purple btn-sm no-border">
                查询
            </button>
            <button type="button" onclick="q_clearAndRefresh()" class="btn btn-info btn-sm no-border">
                清除
            </button>
            <div class="space-4"></div>
        </form>
    </div>
    <div class="col-xs-12">
        <world:hasPermission permissions="/sys/log/del">
            <button type="button" class="btn btn-xs btn-primary" onclick="q_del('sys/log/del')">批量删除</button>
        </world:hasPermission>
        <table id="q_dataTable"></table>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        qx_options.url = 'sys/log/page';
        qx_options.columns = [
            {checkbox: true},
            {
                title: '序号',
                field: '',
                align: 'center',
                formatter: function (value, row, index) {
                    var pageSize = $('#q_dataTable').bootstrapTable('getOptions').pageSize;     //通过table的#id 得到每页多少条
                    var pageNumber = $('#q_dataTable').bootstrapTable('getOptions').pageNumber; //通过table的#id 得到当前第几页
                    return pageSize * (pageNumber - 1) + index + 1;    // 返回每条的序号： 每页条数 *（当前页 - 1 ）+ 序号
                }
            },
            {title: '类别', field: 'category', align: 'center'},
            {title: '操作人', field: 'creator', align: 'center'},
            {title: '操作时间', field: 'createTimeStr', align: 'center'},
            {title: '动作', field: 'action', align: 'center'},
            {title: '处理内容', field: 'content', align: 'center'},
            {title: '详细', field: 'detail', align: 'center', formatter: detailRender},
            {title: '操作', field: 'id', align: 'center', formatter: handleRender}
        ];
        var t = $("#q_dataTable").bootstrapTable(qx_options);
        t.on('load-success.bs.table', function (e, data) {//table加载成功后的监听函数  
        });
    });


    var handleRender = function (value, row, index) {
        var btn = "";
        <world:hasPermission permissions="/sys/log/edit">
        btn += "<a href='sys/log/edit?id=" + row.id + "'>编辑</a> ";
        </world:hasPermission>
        <world:hasPermission permissions="/sys/log/del">
        btn += '<a href="javascript:void(0)" onclick="q_del(\'sys/log/del\',\'' + row.id + '\')">删除</a> ';
        </world:hasPermission>
        return btn;
    };

    var detailRender = function (value, row, index) {
        var btn = "";
        if (G.isNotEmpty(value) && value.length > 100) {
            return '<a href="javascript:void(0)" onclick="detail(\'' + row.id + '\')">详细</a> ';
        }
        return value;
    };

    function detail(id) {
        layer.open({
            type: 2,
            title: '详细信息',
            shadeClose: true,
            shade: 0.8,
            area: ['600px', '70%'],
            content: 'sys/log/viewDetail?id=' + id
        });
    }

    function onOff(id, flag) {
        var url = "";
        if ("on" == flag) {
            url = "sys/log/setOn";
        } else {
            url = "sys/log/setOff";
        }
        layer.confirm('确定启用？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajax({
                type: "POST",
                url: url,
                dataType: "json",
                data: {
                    id: id
                },
                success: function (result) {
                    if (result.success) {
                        q_refreshDataTable();
                        layer.closeAll('dialog');
                    } else {
                        layer.msg(result.msg);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.msg("对不起，系统异常！");
                }
            });
        }, function () {

        });
    }
</script>