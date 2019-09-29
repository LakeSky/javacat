<%@ page language="java" pageEncoding="UTF-8" %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>AdminLTE 2 | Dashboard</title>
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
<!-- AdminLTE Skins. Choose a skin from the css/skins
folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="static/AdminLTE-2.4.12/dist/css/skins/_all-skins.min.css">
<!-- Morris chart -->
<link rel="stylesheet" href="static/AdminLTE-2.4.12/bower_components/morris.js/morris.css">
<!-- jvectormap -->
<link rel="stylesheet" href="static/AdminLTE-2.4.12/bower_components/jvectormap/jquery-jvectormap.css">
<!-- Date Picker -->
<link rel="stylesheet" href="static/AdminLTE-2.4.12/bower_components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css">
<!-- Daterange picker -->
<link rel="stylesheet" href="static/AdminLTE-2.4.12/bower_components/bootstrap-daterangepicker/daterangepicker.css">
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet" href="static/AdminLTE-2.4.12/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">

<!-- jQuery 3 -->
<script src="static/AdminLTE-2.4.12/bower_components/jquery/dist/jquery.min.js"></script>
<!-- jQuery UI 1.11.4 -->
<script src="static/AdminLTE-2.4.12/bower_components/jquery-ui/jquery-ui.min.js"></script>
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->
<script>
    $.widget.bridge('uibutton', $.ui.button);
</script>
<!-- Bootstrap 3.3.7 -->
<script src="static/AdminLTE-2.4.12/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- Morris.js charts -->
<script src="static/AdminLTE-2.4.12/bower_components/raphael/raphael.min.js"></script>
<script src="static/AdminLTE-2.4.12/bower_components/morris.js/morris.min.js"></script>
<!-- Sparkline -->
<script src="static/AdminLTE-2.4.12/bower_components/jquery-sparkline/dist/jquery.sparkline.min.js"></script>
<!-- jvectormap -->
<script src="static/AdminLTE-2.4.12/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="static/AdminLTE-2.4.12/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<!-- jQuery Knob Chart -->
<script src="static/AdminLTE-2.4.12/bower_components/jquery-knob/dist/jquery.knob.min.js"></script>
<!-- daterangepicker -->
<script src="static/AdminLTE-2.4.12/bower_components/moment/min/moment.min.js"></script>
<script src="static/AdminLTE-2.4.12/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<!-- datepicker -->
<script src="static/AdminLTE-2.4.12/bower_components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<!-- Bootstrap WYSIHTML5 -->
<script src="static/AdminLTE-2.4.12/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
<!-- Slimscroll -->
<script src="static/AdminLTE-2.4.12/bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="static/AdminLTE-2.4.12/bower_components/fastclick/lib/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="static/AdminLTE-2.4.12/dist/js/adminlte.min.js"></script>

<script src="static/base.js"></script>
<script src="js/my.js?v=1"></script>
<script src="js/generate/common.js"></script>
<script src="static/jquery.form.min.js"></script>

<script src="<%=basePath%>static/My97DatePicker/WdatePicker.js"></script>
<script src="static/layer-v3.0.2/layer/layer.js"></script>

<link rel="stylesheet" href="static/zTree_v3-master/css/metroStyle/metroStyle.css" type="text/css"/>
<script src="static/zTree_v3-master/js/jquery.ztree.core.min.js"></script>
<script src="static/zTree_v3-master/js/jquery.ztree.excheck.min.js"></script>
<script src="static/bootstrap-table/dist/bootstrap-table.min.js"></script>
<script src="static/bootstrap-table/dist/locale/bootstrap-table-zh-CN.min.js"></script>
<script src="static/vue/vue.min.js"></script>
<link rel="stylesheet" href="static/webuploader-0.1.5/webuploader.css"/>
<script src="static/webuploader-0.1.5/webuploader.min.js"></script>

<!-- jqxGrid start-->
<link rel="stylesheet" href="static/jqwidgets-ver8.1.2/jqwidgets/styles/jqx.base.css" type="text/css" />
<link rel="stylesheet" href="static/jqwidgets-ver8.1.2/jqwidgets/styles/jqx.light.css" type="text/css" />
<%--<script type="text/javascript" src="static/jqwidgets-ver8.1.2/scripts/jquery-1.11.1.min.js"></script>--%>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxcore.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxdata.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxbuttons.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxscrollbar.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxlistbox.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxdropdownlist.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxmenu.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxgrid.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxgrid.filter.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxgrid.sort.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxgrid.selection.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxpanel.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxcheckbox.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxgrid.columnsresize.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxdata.export.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidgets/jqxgrid.export.js"></script>
<%--<script type="text/javascript" src="static/jqwidgets-ver8.1.2/scripts/demos.js"></script>--%>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/generatedata.js"></script>
<script type="text/javascript" src="static/jqwidgets-ver8.1.2/jqwidget.locale-cn.js"></script>
<!-- jqxGrid end-->
<script type="text/javascript" src="static/echarts.min.js"></script>