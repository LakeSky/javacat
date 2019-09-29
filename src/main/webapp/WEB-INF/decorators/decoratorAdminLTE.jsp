<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="description" content="overview &amp; stats">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <sitemesh:write property='head'/>
    <title>javacat</title>
    <link rel='shortcut icon' href='favicon.ico' type='image/x-icon'/>
    <%@include file="/WEB-INF/common/AdminLTE/head-script.jsp" %>
</head>
<body class="hold-transition skin-green sidebar-mini layout-top-nav">
<div class="wrapper">
<%--    <%@include file="/WEB-INF/common/AdminLTE/top-nav.jsp" %>--%>
    <%@include file="/WEB-INF/common/AdminLTE/main-header.jsp" %>
<%--    <%@include file="/WEB-INF/common/AdminLTE/main-sidebar.jsp" %>--%>
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <%--<section class="content-header">
            <h1>
                Dashboard
                <small>Control panel</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Dashboard</li>
            </ol>
        </section>--%>

        <!-- Main content -->
        <section class="content" style="padding-top: 0;padding-bottom: 0;">
            <sitemesh:write property='body'/>
        </section>
        <!-- /.content -->
    </div>
    <%@include file="/WEB-INF/common/AdminLTE/main-footer.jsp" %>
    <%@include file="/WEB-INF/common/AdminLTE/foot-script.jsp" %>
</div>
</body>
</html>