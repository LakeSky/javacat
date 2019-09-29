<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>找不到页面</title>
    <meta name="keywords" content="404,找不到页面,Page Not Found"/>
    <meta name="description" content="404页面,找不到页面,Page Not Found，404页面设计范例">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
</head>
<body>
<div class="main-content">
    <div class="page-content">
        <div class="row">
            <div class="col-xs-12">
                <div class="error-container">
                    <div class="well">
                        <h1 class="grey lighter smaller">
					<span class="blue bigger-125">
						<i class="ace-icon fa fa-sitemap"></i>
						404
					</span>
                            Page Not Found
                        </h1>
                        <hr/>
                        <div>
                            <div class="space"></div>
                            <h4 class="smaller">试一下以下方法:</h4>
                            <ul class="list-unstyled spaced inline bigger-110 margin-15">
                                <li>
                                    <i class="ace-icon fa fa-hand-o-right blue"></i>
                                    核对URL
                                </li>
                            </ul>
                        </div>
                        <hr/>
                        <div class="space"></div>
                        <div class="center">
                            <a href="javascript:history.back()" class="btn btn-grey">
                                <i class="ace-icon fa fa-arrow-left"></i>
                                返回
                            </a>
                            <a href="${contextPath}/busi/home" class="btn btn-primary">
                                <i class="ace-icon fa fa-tachometer"></i>
                                主页
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>