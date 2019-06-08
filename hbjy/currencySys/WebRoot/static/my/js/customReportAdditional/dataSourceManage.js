define(function(require) {
	require('jquery');
	require('jqueryEasyUi');
	require('layer');
	require('layui');
	require('element');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	
	var url = $("#url").val();
	var msgUtil = new MsgUtil();
	var httpUtil = new HttpUtil();
	var operationFlg = "Datasource";//当前功能的标识
	
	$(function() {
		//表格ID
		var grid = $("#dg"+operationFlg);
		
		// layer-tab（自定义报表补录全局）
		var element = layui.element;
		element.on('tab(docDemoTabBrief)', function(data){
			// 表格宽度最小阈值
			var minWidth = 110;
			if (parseInt($("div.layui-tab-content").css("width").substring(0,4)) > 500) {
				minWidth = parseInt($("div.layui-tab-content").css("width").substring(0,4)) - 500;
			}
			if (parseInt($("#dg"+$("li.layui-this").attr("lay-id")).parent().parent().css("width").substring(0,4)) < minWidth) {
				$("#doSearch"+$("li.layui-this").attr("lay-id")).click();
			}
		});
		
		//查询
		$("#doSearch"+operationFlg).click(function(){
			doSearch(grid);
		});
		
		// 初始查询
		doSearch(grid);
	});
	
	//查询
	function doSearch(grid){
		//遮罩层开始
		var index = layer.load(1, {
			shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
		});
		var data = {
			dname_like: $('#dname_like').val()
		}
		
		var title = $('title').html();
		var IsCheckFlag = true;
		$("#myTable"+operationFlg).css("display", "block");
		grid.datagrid({
			url:"/currencySys/cusReportAddController/getDatasourceInfo",
			queryParams:data,
			fitColumns: false,
			nowrap:true,
			pagination:true,
			emptyMsg:'暂无数据<br>',
			showRefresh: false,//定义是否显示刷新按钮
		    pageSize: 10,//每页显示的记录条数，默认为20 
		    pageList: [10,20,50,100],//可以设置每页记录条数的列表 
		    loadMsg:'数据正在努力加载，请稍后...', 
			onLoadSuccess:function(ddata){
				$("#dg"+operationFlg).prev().find("table.datagrid-htable").css("width","100%");$("#dg"+operationFlg).prev().find("table.datagrid-btable").css("width","100%");
				layer.close(index);
			}
		});
		//分页
		var pager = grid.datagrid("getPager");  
		pager.pagination({  
		    showRefresh: false,//定义是否显示刷新按钮
		    beforePageText: '第',//页数文本框前显示的汉字 
		    afterPageText: '页    共 {pages} 页', 
		    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录&nbsp;', 
		    onChangePageSize:function(){
		    	$("#dg"+operationFlg).prev().find("table.datagrid-htable").css("width","100%");$("#dg"+operationFlg).prev().find("table.datagrid-btable").css("width","100%");
			},
		});
	}
});

//------------- formatter START ----------
//驱动类型
function driverFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "01":
				value = "ORACLE";
				break;
			case "02":
				value = "MYSQL";
				break;
			case "03":
				value = "HIVE";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//------------- formatter END ----------