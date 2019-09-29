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
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <%@include file="/WEB-INF/common/AdminLTE/main-header-plus.jsp" %>
    <%@include file="/WEB-INF/common/AdminLTE/main-sidebar.jsp" %>
    <div class="content-wrapper">
        <section class="content">
            <sitemesh:write property='body'/>
        </section>
    </div>
    <%@include file="/WEB-INF/common/AdminLTE/main-footer.jsp" %>
    <%@include file="/WEB-INF/common/AdminLTE/foot-script.jsp" %>
</div>
</body>
</html>