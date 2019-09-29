var qx_options = {
    method: 'get',
    dataType: "json",
    striped: true,//设置为 true 会有隔行变色效果  
    undefinedText: "",//当数据为 undefined 时显示的字符  
    pagination: true, //分页  
    showToggle: false,//是否显示 切换试图（table/card）按钮  
    showColumns: false,//是否显示 内容列下拉框  
    pageNumber: 1,//如果设置了分页，首页页码  
    pageSize: 10,//如果设置了分页，页面数据条数  
    pageList: [10, 20, 50],  //如果设置了分页，设置可供选择的页面数据条数。设置为All 则显示所有记录。  
    search: false, //显示搜索框  
    data_local: "zh-US",//表格汉化 
    locale: 'zh-CN',//中文支持,
    sidePagination: "server", //服务端处理分页
    dataField: "content",
    queryParamsType: 'notLimit',
    queryParams: function (params) {//自定义参数，这里的参数是传给后台的，我这是分页用的
        var searchParam = q_searchForm.serializeObject();
        searchParam.pageIndex = params.pageNumber;//从数据库第几条记录开始
        searchParam.pageSize = params.pageSize;//找多少条
        return searchParam;
    },
    idField: "id",//指定主键列 
    responseHandler: function (res) {
        //在ajax获取到数据，渲染表格之前，修改数据源
        console.log(res);
        res.total = res.totalElements;
        return res;
    }
};