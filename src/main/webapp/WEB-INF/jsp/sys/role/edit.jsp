<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="main-content">
    <div class="main-content-inner">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
        <div class="page-content">
            <form id="addForm" action="sys/role/save" class="form-horizontal" role="form">
                <input type="hidden" name="id" value="${obj.id}">
                <div class="form-group">
                    <label class="col-xs-3 control-label no-padding-right">
                        角色名称
                        <span style="color: red;">*</span>
                    </label>
                    <div class="col-md-4 col-xs-6">
                        <input type="text" name="roleName" value="${obj.roleName}" class="form-control col-xs-10 col-sm-5">
                    </div>
                </div>
                <%--                <input type="hidden" name="dataLevel" value="ALL">--%>
                <div class="form-group">
                    <label class="col-xs-3 control-label no-padding-right">
                        可控数据范围<span style="color: red;">*</span>
                    </label>
                    <div class="col-md-4 col-xs-6">
                        <select id="dataLevel" name="dataLevel" class="form-control col-xs-10 col-sm-5">
                            <option value="">请选择</option>
                            <c:forEach items="${dataLevels}" var="item" varStatus="status">
                                <option value="${item}" <c:if test="${item eq obj.dataLevel}">selected</c:if>>${item.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label no-padding-right">
                        角色权限 <span style="color: red;">*</span>
                    </label>
                    <div class="col-xs-9">
                        <div class="col-md-3" style="padding-left: 0;">
                            <div class="box box-solid" style="height: 350px;overflow: scroll">
                                <div class="box-header with-border">
                                    <h3 class="box-title">菜单</h3>
                                </div>
                                <div class="box-body">
                                    <input type="hidden" name="selMenuIds" id="selMenuIds">
                                    <ul id="menuTree" class="ztree"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="clearfix form-actions">
                    <div class="col-md-offset-3 col-md-9">
                        <c:if test='${page ne "view"}'>
                            <button class="btn btn-info" type="button" onclick="saveForm()">
                                <i class="ace-icon fa fa-check bigger-110"></i>
                                保存
                            </button>
                        </c:if>
                        <button type="button" class="btn btn-grey" onclick="history.back()">
                            <i class="ace-icon fa fa-arrow-left bigger-110"></i>
                            返回
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    <c:if test='${page eq "view"}'>
    $('input').attr("readonly", "readonly");
    $('input').attr("disabled", "disabled");
    </c:if>

    var dataTree;
    var menuTree;
    var resourceTree;
    var setting = {
        check: {
            enable: true
        }
    };
    $(document).ready(function () {
        $.ajax({
            dataType: "json",
            url: "sys/role/menuTree",
            type: "post",
            data: {roleId: '${obj.id}'},
            success: function (result) {
                menuTree = $.fn.zTree.init($("#menuTree"), setting, result);
            }
        });
    });

    function saveForm() {
        var menuNodes = menuTree.getCheckedNodes(true);
        var selMenuIds = "";
        for (var i = 0; i < menuNodes.length; i++) {
            selMenuIds += menuNodes[i].id + ",";
        }
        $("#selMenuIds").val(selMenuIds);

        var form = $("#addForm");
        form.attr("action", "sys/role/save");
        form.ajaxSubmit({
            success: function (result) {
                if (result && result.success) {
                    location.href = "${contextPath}/sys/role/home";
                } else {
                    layer.msg(result.msg);
                }
            }, error: function () {
                layer.msg("网络异常，请稍候再试！");
            }
        });
    }
</script>
