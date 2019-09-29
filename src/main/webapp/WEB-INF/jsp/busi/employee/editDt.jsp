<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="main-content">
    <div class="main-content-inner">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
        <div class="page-content">
            <form id="addForm" class="form-horizontal" role="form" method="post">
                <input type="hidden" name="id" value="${obj.id}">
                <%@include file="/WEB-INF/common/AdminLTE/fragment/edit-fragement.jsp" %>

                <div class="space-4"></div>

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
    function saveForm() {
        var form = $("#addForm");
        form.attr("action", "busi/employee/save");
        form.ajaxSubmit({
            success: function (result) {
                if (result && result.success) {
                    location.href = "busi/employee/home";
                } else {
                    layer.msg(result.msg);
                }
            }, error: function () {
                layer.msg("网络异常，请稍候再试！");
            }
        });
    }
</script>
