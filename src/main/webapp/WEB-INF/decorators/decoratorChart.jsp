<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <base href="${basePath}">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="description" content="overview &amp; stats">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <sitemesh:write property='head'/>
    <link rel='shortcut icon' href='favicon.ico' type='image/x-icon'/>
    <%@include file="/WEB-INF/common/AdminLTE/head-chart.jsp" %>
</head>
<body class="no-skin">
<div class="main-container ace-save-state" id="main-container">
    <sitemesh:write property='body'/>
</div>
<%@include file="/WEB-INF/common/AdminLTE/foot-script.jsp" %>
</body>
</html>