<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta charset="utf-8">
    <title>权限异常</title>
    <meta name="description" content="overview &amp; stats">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
</head>
<body class="no-skin" screen_capture_injected="true">
<div class="main-content">
    <div class="breadcrumbs" id="breadcrumbs">
        <script type="text/javascript">
            try {
                ace.settings.check('breadcrumbs', 'fixed')
            } catch (e) {
            }
        </script>
        <div class="row">
            <div class="col-md-8 col-xs-12">
                <ul class="breadcrumb ">
                    <li>
                        <i class="ace-icon fa fa-home home-icon"></i>
                        操作无权限
                    </li>
                </ul>
            </div>
            <div class="col-md-4 text-right">
            </div>
        </div>
    </div>
    <div class="page-content">
        <div class="page-content-area">
            <div class="row">
                <div class="col-xs-12">
                    <div class="error-container">
                        <div class="well">
                            <h1 class="red lighter smaller">
                                <i class="ace-icon fa fa-times bigger-125"></i>
                                操作无权限
                            </h1>
                            <hr>
                            <h3 class="lighter smaller red">您好，您的操作权限不足，请联系管理员!</h3>
                            <div>
                                <div class="space-12"></div>
                                <h4 class="smaller ">您可以尝试如下操作:</h4>
                                <ul class="list-unstyled spaced inline bigger-110 margin-15">
                                    <li>
                                        <i class="ace-icon fa fa-hand-o-right blue"></i>
                                        联系您的主管，开通相关权限
                                    </li>
                                    <li>
                                        <i class="ace-icon fa fa-hand-o-right blue"></i>
                                        如已开通权限无法操作，请重试或联系技术人员
                                    </li>
                                </ul>
                            </div>
                            <hr>
                            <div class="space"></div>
                            <div class="center">
                                <a href="javascript:history.back()" class="btn btn-grey">
                                    <i class="ace-icon fa fa-arrow-left"></i>
                                    返回
                                </a>
                                <a href="${contextPath}/busi/home" class="btn btn-primary">
                                    <i class="ace-icon fa fa-tachometer"></i>
                                    首页
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>