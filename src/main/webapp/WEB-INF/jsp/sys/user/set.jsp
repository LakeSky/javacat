<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="main-content">
    <div class="main-content-inner">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
        <div class="page-content">
            <form id="modifyPasswordForm" class="form-horizontal" role="form">
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right">
                        原密码
                        <span style="color: red;">*</span>
                    </label>
                    <div class="col-sm-9">
                        <input type="password" name="oldPassword" class="col-xs-10 col-sm-5">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right">
                        新密码
                        <span style="color: red;">*</span>
                    </label>
                    <div class="col-sm-9">
                        <input type="password" name="newPassword" class="col-xs-10 col-sm-5">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label no-padding-right">
                        重复新密码
                        <span style="color: red;">*</span>
                    </label>
                    <div class="col-sm-9">
                        <input type="password" name="rePassword" class="col-xs-10 col-sm-5">
                    </div>
                </div>
                <div class="clearfix form-actions">
                    <div class="col-md-offset-3 col-md-9">
                        <button class="btn btn-info" type="button" onclick="modifyPassword()">
                            <i class="ace-icon fa fa-check bigger-110"></i>
                            保存
                        </button>
                        <button class="btn" type="reset">
                            <i class="ace-icon fa fa-undo bigger-110"></i>
                            重置
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    function modifyPassword() {
        $.ajax({
            type: "POST",
            url: "${contextPath}/sys/user/modifyPassword",
            data: $('#modifyPasswordForm').serializeArray(),
            async: false,
            success: function (result) {
                if (result.success) {
                    layer.msg(result.msg);
                    window.location.href = "${contextPath}/sys/user/home";
                }
                else {
                    layer.msg(result.msg);
                }
            }
        });
    }
</script>