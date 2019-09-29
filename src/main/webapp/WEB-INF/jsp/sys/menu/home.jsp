<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="row">
    <div class="col-md-8 col-xs-6">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
    </div>
    <div class="col-md-12">
        <world:hasPermission permissions="/sys/menu/add">
            <button class="btn btn-info btn-xs" type="button" onclick="showAddMenu()">
                添加
            </button>
        </world:hasPermission>
        <world:hasPermission permissions="/sys/menu/edit">
            <button class="btn btn-info btn-xs" type="button" onclick="showEditMenu()">
                编辑
            </button>
        </world:hasPermission>

        <world:hasPermission permissions="/sys/menu/del">
            <button class="btn btn-info btn-xs" type="button" onclick="delMenu()">
                删除
            </button>
        </world:hasPermission>

        <world:hasPermission permissions="/sys/menu/move">
            <button class="btn btn-info btn-xs" type="button" onclick="showtreeModal()">
                转移
            </button>
        </world:hasPermission>
        <ul id="menuTree" class="ztree"></ul>
    </div>
</div>

<div class="modal fade in" id="addMenuModal" aria-hidden="false">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="modifyTitle">添加子菜单</h4>
            </div>
            <div class="modal-body">
                <form method="post" id="addMenuForm" class="form-horizontal" action="sys/menu/save">
                    <input type="hidden" id="parentId" name="parentId">
                    <input type="hidden" id="id" name="id">
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><b class="red">*</b> 名称 </label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="name" name="name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"> url </label>
                        <div class="col-sm-8">
                            <%--<input type="text" class="form-control" name="url">--%>
                            <div class="input-group">
                                <input type="text" id="url" name="url" class="form-control">
                                <div class="input-group-btn">
                                    <button type="button" class="btn btn-info btn-white" onclick="showmenuResourceModal()">
                                        选择
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"><b class="red">*</b> 序号 </label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="seq">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label no-padding-right"> 图标 </label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" name="icon" value="fa fa-user">
                            <c:if test="${isAdmin}">
                                <a href="static/AdminLTE-2.4.12/pages/UI/icons.html" target="_blank">图标参考</a>
                            </c:if>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" onclick="saveMenu()" class="btn btn-primary">确定</button>
                <button type="button" data-dismiss="modal" class="btn btn-default">
                    <i class="ace-icon fa fa-times bigger-110"></i> 关闭
                </button>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/jsp/sys/menu/transMenu.jsp" %>
<%@include file="/WEB-INF/jsp/sys/menu/menuResourceModal.jsp" %>
<script>
    var zTreeObj;
    var setting = {
        callback: {
            onDblClick: zTreeOnDblClick
        }
    };

    function zTreeOnDblClick(event, treeId, treeNode) {
        if (!treeNode.isParent) {
            showEditMenu();
        }
    }

    $.ajax({
        dataType: "json",
        url: "sys/menu/menuTree",
        type: "post",
        data: {},
        success: function (result) {
            zTreeObj = $.fn.zTree.init($("#menuTree"), setting, result);
        }
    });

    function delMenu() {
        var nodes = zTreeObj.getSelectedNodes();
        if (G.isEmpty(nodes) || nodes.length > 1) {
            layer.msg("请选择一个树节点操作");
            return;
        }
        layer.confirm('确定删除？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            var node = nodes[0];
            $("#id").val("");
            $("#parentId").val(node.id);
            $.ajax({
                dataType: "json",
                url: "sys/menu/del",
                type: "post",
                data: {ids: node.id},
                success: function (result) {
                    if (result.success) {
                        zTreeObj.removeNode(nodes[0]);
                        layer.closeAll('dialog');
                    } else {
                        layer.msg(result.msg);
                    }
                }
            });
        }, function () {

        });
    }


    function showAddMenu() {
        $('#addMenuForm')[0].reset();
        var nodes = zTreeObj.getSelectedNodes();
        if (G.isEmpty(nodes) || nodes.length > 1) {
            layer.msg("请选择一个树节点操作");
            return;
        }
        var node = nodes[0];
        $("#id").val("");
        $("#parentId").val(node.id);
        $("#modifyTitle").html("添加子菜单");
        $("#addMenuModal").modal("show");
    }

    function showEditMenu() {
        $('#addMenuForm')[0].reset();
        var nodes = zTreeObj.getSelectedNodes();
        if (G.isEmpty(nodes) || nodes.length > 1) {
            layer.msg("请选择一个树节点操作");
            return;
        }
        var node = nodes[0];
        if (G.isEmpty(node.pId)) {
            return;
        }
        $.ajax({
            dataType: "json",
            url: "sys/menu/info",
            type: "post",
            data: {id: node.id},
            success: function (result) {
                var menu = result.data;
                $("input[name='id']").val(menu.id);
                $("input[name='parentId']").val(menu.parentId);
                $("input[name='name']").val(menu.name);
                $("input[name='url']").val(menu.url);
                $("input[name='seq']").val(menu.seq);
                $("input[name='icon']").val(menu.icon);
                $("#modifyTitle").html("编辑子菜单");
                $("#addMenuModal").modal("show");
            }
        });
    }

    function saveMenu() {
        var ajax_option;
        if ("添加子菜单" == $("#modifyTitle").html()) {
            ajax_option = {
                url: "sys/menu/save",//默认是form 
                success: function (result) {
                    if (result.success) {
                        $("#addMenuModal").modal("hide");
                        var newNode = result.data;
                        zTreeObj.addNodes(zTreeObj.getSelectedNodes()[0], newNode);
                    } else {
                        layer.msg(result.msg);
                    }
                }
            };
        } else {
            ajax_option = {
                url: "sys/menu/save",
                success: function (result) {
                    if (result.success) {
                        $("#addMenuModal").modal("hide");
                        var nodes = zTreeObj.getSelectedNodes();
                        var newNode = result.data;
                        nodes[0].name = newNode.name;
                        nodes[0].url = newNode.url;
                        zTreeObj.updateNode(nodes[0]);
                    } else {
                        layer.msg(result.msg);
                    }
                }
            };
        }
        $('#addMenuForm').ajaxSubmit(ajax_option);
    }

</script>