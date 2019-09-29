var q_dataTable = $("#q_dataTable");
var q_searchForm = $("#q_searchForm");
var q_hiddenForm = $("#q_hiddenForm");

function q_tableSelectIds() {
    var ids = [];
    var rows = q_dataTable.bootstrapTable('getSelections');
    $.each(rows, function (n, row) {
        if (G.isNotEmpty(row)) {
            ids.push(row.id);
        }
    });
    return ids;
}

function q_exportExcel() {
    var form = q_hiddenForm;
    form.attr("action", "/auto/export?fileName=导出结果");
    $("#params").val(JSON.stringify(q_searchForm.serializeObject()));
    form.submit();
}

function q_refreshDataTable() {
    q_dataTable.bootstrapTable('refresh', {silent: true});
}

function q_clearAndRefresh() {
    q_searchForm[0].reset();
    q_refreshDataTable();
}

var q_imageRender = function (value, row, index) {
    var img = "";
    if (G.isNotEmpty(value)) {
        img = "<a href=\"" + value + "\" title=\"\" data-rel=\"colorbox\" class=\"colorbox\">\n" +
            "<img width=\"120\" src=\"" + value + "\">\n" +
            "</a>";
    }

    return img;
};

var q_iconRender = function (value, row, index) {
    var img = "";
    if (G.isNotEmpty(value)) {
        img = "<img src=\"" + value + "\">";
    }

    return img;
};

var q_initColorbox = function () {
    var $overflow = '';
    var colorbox_params = {
        rel: 'colorbox',
        reposition: true,
        scalePhotos: true,
        photo: true,
        scrolling: false,
        previous: '<i class="ace-icon fa fa-arrow-left"></i>',
        next: '<i class="ace-icon fa fa-arrow-right"></i>',
        close: '&times;',
        current: '{current} of {total}',
        maxWidth: '80%',
        maxHeight: '80%',
        onOpen: function () {
            $overflow = document.body.style.overflow;
            document.body.style.overflow = 'hidden';
        },
        onClosed: function () {
            document.body.style.overflow = $overflow;
        },
        onComplete: function () {
            $.colorbox.resize();
        }
    };
    $('a[data-rel="colorbox"]').colorbox(colorbox_params);
};

function q_del(url, id) {
    var ids = "";
    if (G.isNotEmpty(id)) {
        ids = id;
    } else {//批量删除
        ids = q_tableSelectIds();
        if (G.isEmpty(ids)) {
            layer.msg("请选择条目");
            return false;
        }
    }
    layer.confirm('确定删除？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $.ajax({
            type: "POST",
            url: url,
            dataType: "json",
            traditional: true, //默认false
            data: {ids: ids},
            success: function (result) {
                if (result.success) {
                    q_refreshDataTable();
                    layer.closeAll('dialog');
                } else {
                    layer.msg(result.msg);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.msg("对不起，系统异常！");
            }
        });
    }, function () {

    });
}

q_searchForm.keydown(function (e) { //设定enter键默认提交查询
    e = e || window.event;
    if (e.keyCode === 13) {
        e.preventDefault();
        if (G.isNotEmpty(q_dataTable)) {
            q_dataTable.bootstrapTable('refresh', {silent: true});
        }
    }
});

$("#q_searchForm select").change(function () {
    q_dataTable.bootstrapTable('refresh', {silent: true});
});
