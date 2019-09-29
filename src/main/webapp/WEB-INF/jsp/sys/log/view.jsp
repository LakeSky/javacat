<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="main-content">
    <div class="main-content-inner">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
        <div class="page-content">
            <form id="addForm" class="form-horizontal" role="form" method="post">
                <%@include file="/WEB-INF/common/AdminLTE/fragment/view-fragement.jsp" %>
                <div class="space-4"></div>
                <div class="clearfix form-actions">
                    <div class="col-md-offset-3 col-md-9">
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

</script>