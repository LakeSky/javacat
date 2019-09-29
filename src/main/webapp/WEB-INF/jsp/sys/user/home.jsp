<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="row">
    <div class="col-md-8 col-xs-6">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
    </div>
    <div class="col-md-4 text-right">
        <world:hasPermission permissions="/sys/user/add">
            <a class="btn btn-primary btn-sm no-border" href="sys/user/add">
                <i class="ace-icon fa fa-plus"></i>添加
            </a>
        </world:hasPermission>
        <world:hasPermission permissions="/sys/user/export">
            <a class="btn btn-primary btn-sm no-border" onclick="q_exportExcel();" href="javascript:void(0);">
                <i class="ace-icon fa fa-download"></i>导出
            </a>
            <form id="q_hiddenForm" method="post">
                <input type="hidden" id="params" name="params">
                <input type="hidden" name="o" value="User">
            </form>
        </world:hasPermission>
    </div>
    <div class="col-md-12">
        <form class="form-inline" id="q_searchForm" method="post">
            <input name="pageIndex" type="hidden">
            <div class="form-group">
                <input type="text" name="username" value="" class="form-control" placeholder="用户名">
            </div>
            <div class="form-group">
                <input type="text" name="name" value="" class="form-control" placeholder="姓名">
            </div>
            <div class="form-group">
                <select name="role.id" class="form-control">
                    <option value="">请选择角色</option>
                    <c:forEach items="${roles}" var="item" varStatus="status">
                        <option value="${item.id}">${item.roleName}</option>
                    </c:forEach>
                </select>
            </div>
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
        <world:hasPermission permissions="/sys/user/del">
            <button type="button" class="btn btn-xs btn-primary" onclick="q_del('sys/user/del')">批量删除</button>
        </world:hasPermission>
        <table id="q_dataTable"></table>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        qx_options.url = 'sys/user/page';
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
            {title: '用户名', field: 'username', align: 'center'},
            {title: '姓名', field: 'name', align: 'center'},
            {title: '角色', field: 'role.roleName', align: 'center'},
            // {title: '数据权限', field: 'dataLevel', align: 'center'},
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
        var btn = "";
        <world:hasPermission permissions="/sys/use/edit">
        btn += "<a href='sys/user/edit?id=" + row.id + "'>编辑</a> ";
        </world:hasPermission>
        <world:hasPermission permissions="/sys/use/del">
        btn += '<a href="javascript:void(0)" onclick="q_del(\'sys/user/del\',\'' + row.id + '\')">删除</a> ';
        </world:hasPermission>
        <world:hasPermission permissions="/sys/use/role/save">
        btn += '<a href="javascript:void(0)" onclick="showRoleModal(\'' + row.id + '\')">选择角色</a>';
        </world:hasPermission>
        return btn;
    };

    var userId;
    var app = new Vue({
        el: '#app',
        data: {
            roles: []
        }
    });

    function showRoleModal(id) {
        layer.msg("开发中。。。");
    }

    function saveUserRole() {
        var roleId = $("input[name='itemChk']:checked").val();

        $.ajax({
            type: "POST",
            url: "sys/user/role/save",
            dataType: "json",
            data: {
                userId: userId,
                roleId: roleId
            },
            success: function (result) {
                if (result.success) {
                    q_refreshDataTable();
                } else {
                    layer.msg(result.msg);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.msg("对不起，系统异常！");
            }
        });
        $("#selRoles").modal('hide');
    }
</script>