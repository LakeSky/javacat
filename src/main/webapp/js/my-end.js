$(":password").keydown(function (e) { //设定enter键默认提交查询
    e = e || window.event;
    if (e.keyCode == 32) {
        return false;
    }
});