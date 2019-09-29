<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String contextPath = request.getContextPath();
    request.setAttribute("contextPath", contextPath);
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>javacat | 登录</title>

    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.7 -->
    <link rel="stylesheet" href="static/AdminLTE-2.4.12/bower_components/bootstrap/dist/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="static/AdminLTE-2.4.12/bower_components/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="static/AdminLTE-2.4.12/bower_components/Ionicons/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="static/AdminLTE-2.4.12/dist/css/AdminLTE.min.css">
    <!-- iCheck -->
    <link rel="stylesheet" href="static/AdminLTE-2.4.12/plugins/iCheck/square/blue.css">
</head>
<body class="hold-transition login-page">
<div class="login-box">
    <div class="login-logo">
        <img src="favicon.ico" style="margin-top: -6px;height: 40px;"><b>javacat</b>
    </div>
    <div class="login-box-body">
        <p class="login-box-msg">
            请输入登录信息
        </p>

        <form id="loginForm" method="post">
            <div class="form-group has-feedback">
                <input type="text" name="j_username" id="j_username" class="form-control" placeholder="用户名">
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" name="j_password" id="j_password" class="form-control" placeholder="密码">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <%--<div class="checkbox icheck">
                        <label>
                            <input type="checkbox"> Remember Me
                        </label>
                    </div>--%>
                </div>
                <div class="col-xs-4">
                    <button type="button" onclick="login()" class="btn btn-primary btn-block btn-flat">登录</button>
                </div>
            </div>
        </form>

        <%--        <a href="#">I forgot my password</a><br>--%>
        <%--        <a href="register.html" class="text-center">Register a new membership</a>--%>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <h4>联系作者QQ 2644328654(月牙儿)</h4>
            <table class="table table-bordered">
                <tr>
                    <th>分类</th>
                    <th>测试用户点击进入测试</th>
                </tr>
                <tr>
                    <td>测试账号</td>
                    <td>
                        <button type="button" onclick="loginTest('admin','123456')" class="btn btn-sm btn-primary">admin超级管理员</button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>

<script src="static/AdminLTE-2.4.12/bower_components/jquery/dist/jquery.min.js"></script>
<script src="static/AdminLTE-2.4.12/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="static/AdminLTE-2.4.12/plugins/iCheck/icheck.min.js"></script>
<script src="static/jquery.form.min.js"></script>
<script src="static/layer-v3.0.2/layer/layer.js"></script>
<script src="js/generate/common.js"></script>
<script>
    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' /* optional */
        });
    });

    function login() {
        var form = $("#loginForm");
        form.attr("action", "login/go");
        form.ajaxSubmit({
            success: function (result) {
                if (result && result.success) {
                    location.href = "${contextPath}" + result.data;
                } else {
                    layer.msg(result.msg);
                }
            }, error: function () {
                layer.msg("登陆失败!", "网络异常，请稍候再试！");
            }
        });
    }

    $("#loginForm").keydown(function (e) {
        if (e.keyCode === 13) {
            login();
        }
    });

    function loginTest(username, password) {
        $("#j_username").val(username);
        $("#j_password").val(password);
        var form = $("#loginForm");
        form.attr("action", "login/go");
        form.ajaxSubmit({
            success: function (result) {
                if (result && result.success) {
                    location.href = "${contextPath}" + result.data;
                } else {
                    layer.msg(result.msg);
                }
            }, error: function () {
                layer.msg("登陆失败!", "网络异常，请稍候再试！");
            }
        });
    }
</script>
</body>
</html>
