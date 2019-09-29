<%@ page language="java" pageEncoding="UTF-8" %>
<header class="main-header">
    <!-- Logo -->
    <a href="javascript:void(0)" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"><b>J</b>C</span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><b>javacat</b></span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
            <span class="sr-only">Toggle navigation</span>
        </a>

        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <!-- User Account: style can be found in dropdown.less -->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <img src="static/AdminLTE-2.4.12/dist/img/avatar0.png" class="user-image" alt="User Image">
                        <span class="hidden-xs">${sessionScope.user.username}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <li class="user-header">
                            <img src="static/AdminLTE-2.4.12/dist/img/avatar0.png" class="img-circle" alt="User Image">

                            <p>
                                ${sessionScope.user.username} - ${sessionScope.user.role.roleName}
                                <%--                                <small>${sessionScope.user.name}</small>--%>
                            </p>
                        </li>
                        <!-- Menu Body -->
                        <%--
                        <li class="user-body">
                            <div class="row">
                                <div class="col-xs-4 text-center">
                                    <a href="#">Followers</a>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="#">Sales</a>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="#">Friends</a>
                                </div>
                            </div>
                            <!-- /.row -->
                        </li>
                        --%>
                        <!-- Menu Footer-->
                        <li class="user-footer" style="background-color: #0b93d5">
                            <div class="pull-left">
                                <a href="sys/user/set" class="btn btn-default btn-flat">设置</a>
                            </div>
                            <div class="pull-right">
                                <a href="logout" class="btn btn-default btn-flat">注销</a>
                            </div>
                        </li>
                    </ul>
                </li>
                <!-- Control Sidebar Toggle Button -->
                <%--<li>
                    <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
                </li>--%>
            </ul>
        </div>
    </nav>
</header>