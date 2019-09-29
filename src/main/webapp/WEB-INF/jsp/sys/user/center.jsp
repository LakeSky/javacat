<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>
<div class="main-content">
    <div class="main-content-inner">
        <%@include file="/WEB-INF/common/AdminLTE/breadcrumb.jsp" %>
        <div class="page-content">
            <div class="page-header">
                <h1>
                    用户中心
                    <%--<small>
                        <i class="ace-icon fa fa-angle-double-right"></i>
                        3 styles with inline editable feature
                    </small>--%>
                </h1>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <%--<div class="hr dotted"></div>--%>

                    <div>
                        <div class="user-profile row" id="user-profile-1">
                            <%--<div class="col-xs-12 col-sm-3 center">
                                <div>
                                    <span class="profile-picture">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.avatar}">
                                                <img class="editable img-responsive editable-click editable-empty" id="avatar"
                                                     data-pk="${user.id}" style="display: block;" alt="头像" src="${contextPath}/${user.avatar}">
                                            </c:when>
                                            <c:otherwise>
                                                <img class="editable img-responsive editable-click editable-empty" id="avatar"
                                                     data-pk="${user.id}" style="display: block;" alt="头像" src="${contextPath}/static/ace/assets/images/avatars/avatar2.png">
                                            </c:otherwise>
                                        </c:choose>
										
									</span>
                                    <div>头像(点击更换)</div>
                                    <div class="space-4"></div>

                                    &lt;%&ndash;<div class="width-80 label label-info label-xlg arrowed-in arrowed-in-right">
                                        <div class="inline position-relative">
                                            <a class="user-title-label dropdown-toggle" href="#" data-toggle="dropdown">
                                                <i class="ace-icon fa fa-circle light-green"></i>
                                                &nbsp;
                                                <span class="white">Alex M. Doe</span>
                                            </a>

                                            <ul class="align-left dropdown-menu dropdown-caret dropdown-lighter">
                                                <li class="dropdown-header"> Change Status</li>

                                                <li>
                                                    <a href="#">
                                                        <i class="ace-icon fa fa-circle green"></i>
                                                        &nbsp;
                                                        <span class="green">Available</span>
                                                    </a>
                                                </li>

                                                <li>
                                                    <a href="#">
                                                        <i class="ace-icon fa fa-circle red"></i>
                                                        &nbsp;
                                                        <span class="red">Busy</span>
                                                    </a>
                                                </li>

                                                <li>
                                                    <a href="#">
                                                        <i class="ace-icon fa fa-circle grey"></i>
                                                        &nbsp;
                                                        <span class="grey">Invisible</span>
                                                    </a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>&ndash;%&gt;
                                </div>

                                <div class="space-6"></div>

                                <div class="hr hr12 dotted"></div>
                            </div>--%>
                            <div class="col-xs-12 col-sm-9">
                                <%--<h4 class="blue">
                                    <span class="middle">Alex M. Doe</span>

                                    <span class="label label-purple arrowed-in-right">
                                        <i class="ace-icon fa fa-circle smaller-80 align-middle"></i>
                                        online
                                    </span>
                                </h4>--%>

                                <div class="profile-user-info">
                                    <div class="profile-info-row">
                                        <div class="profile-info-name"> 用户名:</div>

                                        <div class="profile-info-value">
                                            <span>${user.username}</span>
                                        </div>
                                    </div>

                                    <div class="profile-info-row">
                                        <div class="profile-info-name"> 姓名:</div>

                                        <div class="profile-info-value">
                                            <%--<i class="fa fa-map-marker light-orange bigger-110"></i>
                                            <span>Netherlands</span>
                                            <span>Amsterdam</span>--%>
                                            <span>${user.name}</span>
                                        </div>
                                    </div>

                                    <%--<div class="profile-info-row">
                                        <div class="profile-info-name"> Joined</div>

                                        <div class="profile-info-value">
                                            <span>2010/06/20</span>
                                        </div>
                                    </div>

                                    <div class="profile-info-row">
                                        <div class="profile-info-name"> Last Online</div>

                                        <div class="profile-info-value">
                                            <span>3 hours ago</span>
                                        </div>
                                    </div>--%>
                                </div>

                                <div class="hr hr-8 dotted"></div>

                                <%--<div class="profile-user-info">
                                    <div class="profile-info-row">
                                        <div class="profile-info-name"> Website</div>

                                        <div class="profile-info-value">
                                            <a href="#" target="_blank">www.alexdoe.com</a>
                                        </div>
                                    </div>

                                    <div class="profile-info-row">
                                        <div class="profile-info-name">
                                            <i class="middle ace-icon fa fa-facebook-square bigger-150 blue"></i>
                                        </div>

                                        <div class="profile-info-value">
                                            <a href="#">Find me on Facebook</a>
                                        </div>
                                    </div>

                                    <div class="profile-info-row">
                                        <div class="profile-info-name">
                                            <i class="middle ace-icon fa fa-twitter-square bigger-150 light-blue"></i>
                                        </div>

                                        <div class="profile-info-value">
                                            <a href="#">Follow me on Twitter</a>
                                        </div>
                                    </div>
                                </div>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    jQuery(function ($) {
        $.fn.editable.defaults.mode = 'inline';
        $.fn.editableform.loading = "<div class='editableform-loading'><i class='ace-icon fa fa-spinner fa-spin fa-2x light-blue'></i></div>";
        $.fn.editableform.buttons = '<button type="submit" class="btn btn-info editable-submit"><i class="ace-icon fa fa-check"></i></button>' +
            '<button type="button" class="btn editable-cancel"><i class="ace-icon fa fa-times"></i></button>';

        var last_gritter;
        $('#avatar').editable({
            type: 'image',
            name: '头像',
            value: null,
            image: {
                btn_choose: '更换头像',
                droppable: true,
                maxSize: 110000,//~100Kb
                //and a few extra ones here
                name: 'avatar',//put the field name here as well, will be used inside the custom plugin
                on_error: function (error_type) {//on_error function will be called when the selected file has a problem
                    if (last_gritter) $.gritter.remove(last_gritter);
                    if (error_type == 1) {//file format error
                        last_gritter = $.gritter.add({
                            title: 'File is not an image!',
                            text: 'Please choose a jpg|gif|png image!',
                            class_name: 'gritter-error gritter-center'
                        });
                    } else if (error_type == 2) {//file size rror
                        last_gritter = $.gritter.add({
                            title: 'File too big!',
                            text: 'Image size should not exceed 100Kb!',
                            class_name: 'gritter-error gritter-center'
                        });
                    }
                    else {//other error
                    }
                },
                on_success: function () {
                    $.gritter.removeAll();
                }
            },
            url: function (params) {
                console.log(params);
                //this is similar to the file-upload.html example
//replace the code inside profile page where it says ***UPDATE AVATAR HERE*** with the code below

// ***UPDATE AVATAR HERE*** //

                var submit_url = '${contextPath}/sys/user/uploadAvatar';//please modify submit_url accordingly
                var deferred = null;
                var avatar = '#avatar';

//if value is empty (""), it means no valid files were selected
//but it may still be submitted by x-editable plugin
//because "" (empty string) is different from previous non-empty value whatever it was
//so we return just here to prevent problems
                var value = $(avatar).next().find('input[type=hidden]:eq(0)').val();
                if (!value || value.length == 0) {

                    deferred = new $.Deferred
                    deferred.resolve();
                    return deferred.promise();

                }

                var $form = $(avatar).next().find('.editableform:eq(0)')
                var file_input = $form.find('input[type=file]:eq(0)');
                var pk = $(avatar).attr('data-pk');//primary key to be sent to server

                var ie_timeout = null


                if ("FormData" in window) {

                    var formData_object = new FormData();//create empty FormData object

                    //serialize our form (which excludes file inputs)
                    $.each($form.serializeArray(), function (i, item) {

                        //add them one by one to our FormData 
                        formData_object.append(item.name, item.value);

                    });
                    //and then add files
                    $form.find('input[type=file]').each(function () {

                        var field_name = $(this).attr('name');
                        var files = $(this).data('ace_input_files');
                        if (files && files.length > 0) {

                            formData_object.append(field_name, files[0]);

                        }

                    });

                    //append primary key to our formData
                    formData_object.append('pk', pk);
                    formData_object.append('objId', pk);

                    deferred = $.ajax({

                        url: submit_url,
                        type: 'POST',
                        processData: false,//important
                        contentType: false,//important
                        dataType: 'json',//server response type
                        data: formData_object

                    })

                }
                else {

                    deferred = new $.Deferred

                    var temporary_iframe_id = 'temporary-iframe-' + (new Date()).getTime() + '-' + (parseInt(Math.random() * 1000));
                    var temp_iframe =
                        $('<iframe id="' + temporary_iframe_id + '" name="' + temporary_iframe_id + '" \
			frameborder="0" width="0" height="0" src="about:blank"\
			style="position:absolute; z-index:-1; visibility: hidden;"></iframe>')
                            .insertAfter($form);

                    $form.append('<input type="hidden" name="temporary-iframe-id" value="' + temporary_iframe_id + '" />');

                    //append primary key (pk) to our form
                    $('<input type="hidden" name="pk" />').val(pk).appendTo($form);
                    $('<input type="hidden" name="objId" />').val(pk).appendTo($form);

                    temp_iframe.data('deferrer', deferred);
                    //we save the deferred object to the iframe and in our server side response
                    //we use "temporary-iframe-id" to access iframe and its deferred object

                    $form.attr({

                        action: submit_url,
                        method: 'POST',
                        enctype: 'multipart/form-data',
                        target: temporary_iframe_id //important

                    });

                    $form.get(0).submit();

                    //if we don't receive any response after 30 seconds, declare it as failed!
                    ie_timeout = setTimeout(function () {

                        ie_timeout = null;
                        temp_iframe.attr('src', 'about:blank').remove();
                        deferred.reject({
                            'status': 'fail', 'message': 'Timeout!'
                        });

                    }, 30000);

                }


//deferred callbacks, triggered by both ajax and iframe solution
                deferred
                    .done(function (result) {
                        console.log(result);
//success
                        //var res = result[0];//the `result` is formatted by your server side response and is arbitrary
                        if (result.success) {
                            var res = result.data;//the `result` is formatted by your server side response and is arbitrary
                            $(avatar).get(0).src = res.downloadUrl;
                        }
                        else alert(res.message);

                    })
                    .fail(function (result) {
//failure
                        alert("There was an error");

                    })
                    .always(function () {
//called on both success and failure
                        if (ie_timeout) clearTimeout(ie_timeout)
                        ie_timeout = null;

                    });

                return deferred.promise();
// ***END OF UPDATE AVATAR HERE*** //			
// ***END OF UPDATE AVATAR HERE*** //
            },

            success: function (response, newValue) {
            }
        });
    });
    function modifyPassword() {
        $.ajax({
            type: "POST",
            url: "${contextPath}/sys/user/modifyPassword",
            data: $('#modifyPasswordForm').serializeArray(),
            async: false,
            success: function (result) {
                if (result.success) {
                    layer.msg(result.msg);
                    window.location.href = "${contextPath}/sys/user/home";
                }
                else {
                    layer.msg(result.msg);
                }
            }
        });
    }
</script>