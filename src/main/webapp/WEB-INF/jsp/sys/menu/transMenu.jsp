<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="modal fade in" id="treeModal" tabindex="-1" role="dialog" aria-hidden="false">
    <form id="treeForm" class="form-horizontal" role="form" method="post">
        <input type="hidden" name="id" id="id">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                    <h4 class="modal-title">选择目标节点</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="tree-container" style="overflow: auto;max-height: 300px;width: 100%;">
                            <ul id="myTree" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="selectedMenu()" class="btn btn-primary">确定</button>
                    <button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    var zTreeObjModal;
    var setting = {};
    function showtreeModal() {
        var nodes = zTreeObj.getSelectedNodes();
        if (G.isEmpty(nodes) || nodes.length > 1) {
            layer.msg("请选择一个树节点操作");
            return;
        }
        if (G.isEmpty(nodes[0].pId)) {
            return;
        }
        $.ajax({
            dataType: "json",
            url: "${contextPath}/sys/menu/menuTree",
            type: "post",
            data: {},
            success: function (result) {
                zTreeObjModal = $.fn.zTree.init($("#myTree"), setting, result);
            }
        });
        $("#myTree").empty();
        $("#treeModal").modal("show");
    }

    function selectedMenu() {
        var nodes = zTreeObjModal.getSelectedNodes();
        if (G.isEmpty(nodes) || nodes.length > 1) {
            layer.msg("请选择一个树节点操作");
            return;
        }
        $.ajax({
            dataType: "json",
            url: "${contextPath}/sys/menu/move",
            type: "post",
            data: {sourceId: zTreeObj.getSelectedNodes()[0].id, targetId: nodes[0].id},
            success: function (result) {
                if (result.success) {
                    var targetNode = zTreeObj.getNodeByParam("id", nodes[0].id, null);
                    console.log("targetNode" + targetNode);
                    zTreeObj.addNodes(targetNode, zTreeObj.getSelectedNodes()[0]);
                    zTreeObj.removeNode(zTreeObj.getSelectedNodes()[0]);
                }
                else {
                    layer.msg(result.msg);
                }
            }
        });
        $("#treeModal").modal("hide");
    }
</script>
