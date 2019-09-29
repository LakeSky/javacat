<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="modal fade in" id="menuResourceModal" tabindex="-1" role="dialog" aria-hidden="false">
    <form id="menuResourceForm" class="form-horizontal" role="form" method="post">
        <input type="hidden" name="id" id="id">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title">选择</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div style="overflow: auto;height: 300px;">
                            <ul id="menuResourceTree" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="selectMenu()" class="btn btn-primary">确定</button>
                    <button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
                </div>
            </div>
        </div>
    </form>
</div>
<script>
    var menuResourceTree;
    // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
    var setting = {};

    function showmenuResourceModal() {
        //提取所有controller里面的页面
        $.ajax({
            dataType: "json",
            url: "${contextPath}/sys/menu/resources",
            type: "post",
            data: {roleId: '${role.id}'},
            success: function (result) {
                menuResourceTree = $.fn.zTree.init($("#menuResourceTree"), setting, result);
            }
        });
        $("#menuResourceTree").empty();
        $("#menuResourceModal").modal("show");
    }

    function selectMenu() {
        var nodes = menuResourceTree.getSelectedNodes();
        if (G.isEmpty(nodes) || nodes.length > 1) {
            layer.msg("请选择一个树节点操作");
            return;
        }
        
        var node = nodes[0];
        console.log(node);
        $("#url").val(node.id);
        $("#menuResourceModal").modal("hide");
    }
</script>
