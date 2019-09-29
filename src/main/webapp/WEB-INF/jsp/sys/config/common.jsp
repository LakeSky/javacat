<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="row">
    <div class="col-md-8 col-xs-12">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
    </div>
    <div class="col-xs-12 col-sm-8 widget-container-col ui-sortable">
        <div class="widget-box ui-sortable-handle widget-color-blue">
            <div class="widget-header">
                <h5 class="widget-title">参数配置</h5>
                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="ace-icon fa fa-chevron-up"></i>
                    </a>
                </div>
            </div>
            <div class="widget-body">
                <div class="widget-main no-padding">
                    <table class="table table-striped table-bordered table-hover">
                        <thead class="thin-border-bottom">
                        <tr>
                            <th>参数名</th>
                            <th>参数值</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="">域服务器IP</td>
                            <td>
                                <span id="domain_server_ip" class="parameter">${config.domain_server_ip.configValue}</span>
                            </td>
                            <td>
                                <button type="button" class="btn btn-xs btn-primary" onclick="setParam('domain_server_ip')">设置</button>
                            </td>
                        </tr>
                        <tr>
                            <td class="">域后缀</td>
                            <td>
                                <span id="domain_postfix" class="parameter">${config.domain_postfix.configValue}</span>
                            </td>
                            <td>
                                <button type="button" class="btn btn-xs btn-primary" onclick="setParam('domain_postfix')">设置</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="modal-parameter" class="modal fade" tabindex="-1" data-backdrop="static">
    <div class="modal-dialog">
        <form id="parameterForm" method="post" class="form-horizontal">
            <input type="hidden" id="configKey">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">参数修改</h4>
                </div>
                <div class="modal-body" style="max-height: 500px;overflow-y: scroll;">
                    <div id="modal-tip" class="red clearfix"></div>
                    <div class="form-group">
                        <label id="parameterName" class="blue col-sm-2 control-label no-padding-right"> 值 </label>
                        <div class="col-sm-9">
                            <textarea id="parameterValue" name="parameterValue" class="col-xs-12 col-sm-12"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer no-margin-top">
                    <div class="text-center">
                        <button id="submitBtn" type="button" class="btn btn-info btn-xs">
                            保存
                        </button>
                        <button class="btn btn-grey btn-xs" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<script>
    $(document).ready(function () {
        $("#submitBtn").on("click", function (e) {
            var configKey = $("#configKey").val();
            $("#submitBtn").attr("disabled", "disabled");
            $.ajax({
                type: "POST",
                dataType: 'json',
                url: 'sys/config/saveParam',
                data: {configKey: configKey, configValue: $("#parameterValue").val()},
                success: function (result) {
                    if (result.success) {
                        $("#modal-parameter").modal("hide");
                        $("#" + configKey).html($("#parameterValue").val());
                    }
                    layer.msg(result.msg);
                    $("#submitBtn").removeAttr("disabled");
                },
                error: function (data, status, e) {
                    $("#submitBtn").removeAttr("disabled");
                }
            });
        });
    });

    function setParam(id) {
        $("#configKey").val(id);
        $("#parameterForm")[0].reset();
        $("#parameterName").html($("#" + id + "Label").html());
        $("#parameterValue").val($("#" + id).html());
        $("#modal-parameter").modal("show");
    }
</script>
