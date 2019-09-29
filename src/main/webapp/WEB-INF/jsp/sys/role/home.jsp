<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="row">
    <div class="col-md-8 col-xs-6">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
    </div>
    <div class="col-md-4 text-right">
        <world:hasPermission permissions="/sys/role/add">
            <a class="btn btn-primary btn-sm no-border" href="sys/role/add">
                <i class="ace-icon fa fa-plus"></i>添加
            </a>
        </world:hasPermission>
    </div>
    <div class="col-md-12">
        <form class="form-inline" id="q_searchForm" method="post">
            <input name="pageIndex" type="hidden">
            <%@include file="/WEB-INF/common/AdminLTE/fragment/query-fragement.jsp" %>
            <button type="button" onclick="q_refreshDataTable()" class="btn btn-purple btn-sm no-border">
                查询
            </button>
            <button type="button" onclick="q_clearAndRefresh()" class="btn btn-info btn-sm no-border">
                清除
            </button>
            <div class="space-4"></div>
        </form>
        <table id="q_dataTable"></table>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        qx_options.url = 'sys/role/page';
        qx_options.columns = [
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
            {title: '角色名称', field: 'roleName', align: 'center'},
            {title: '数据权限', field: 'dataLevelStr', align: 'center'},
            {title: '创建人', field: 'creator', align: 'center'},
            {title: '创建时间', field: 'createTimeStr', align: 'center'},
            {
                title: '操作',
                field: 'id',
                align: 'center',
                formatter: handleRender
            }
        ];
        var t = $("#q_dataTable").bootstrapTable(qx_options);
        t.on('load-success.bs.table', function (e, data) {//table加载成功后的监听函数  
        });
    });


    var handleRender = function (value, row, index) {
        var btn = "&nbsp";
        <world:hasPermission permissions="/sys/role/edit">
        btn += "<a href='sys/role/edit?id=" + row.id + "'>编辑</a> ";
        </world:hasPermission>
        <world:hasPermission permissions="/sys/role/del">
        btn += '<a href="javascript:void(0)" onclick="q_del(\'sys/role/del\',\'' + row.id + '\')">删除</a> ';
        </world:hasPermission>
        <world:hasPermission permissions="/sys/role/menu">
        btn += "<a href='javascript:void(0)' onclick=configMenu(\'" + row.id + "')>配置菜单</a> ";
        </world:hasPermission>
        <world:hasPermission permissions="/sys/role/resource">
        btn += "<a href='javascript:void(0)' onclick=configResource(\'" + row.id + "')>配置权限</a> ";
        </world:hasPermission>
        return btn;
    };

    function configMenu(id) {
        layer.msg("开源版本不提供");
    }

    function configResource(id) {
        layer.msg("开源版本不提供");
    }
</script>