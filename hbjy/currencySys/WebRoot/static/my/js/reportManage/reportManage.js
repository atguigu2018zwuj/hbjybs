var defaults = null;
var url = $("#url").val();
var table;
var hzData;
var dlog;
var wjbhList=[];
define(function(require) {
	require('jquery');
	require('jqueryEasyUi');
	require('My97DatePicker');
	require('timeFormat');
	require('st-grid');
	require('select2');
	require('ztree');
	require('JsExcelXml');
	require('layer');
	require('daterangepicker');
	var $dialog = require('dialog');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	var cookieUtil = new CookieUtil();	
	var msgUtil = new MsgUtil();
	$(function() {
		//获取当前时间的前一天时间
		var dataday = moment().subtract('day', 1).format('YYYY-MM-DD');
		wdatePickerYMD("bsrq",dataday);	
		
		$("#sjlx").change(function(){
			dataSearch();
		});
		
		$("#bsrq").change(function(){
			mkdirTableName();
		});
		
		$("#bbmc").change(function(){
			dataSearch();
		});
		
		$('#sjzt').change(function(){
			if($('#sjzt').val() == 1){
				$('#titleName').html("已报送金融机构列表");
			}else{
				$('#titleName').html("未报送金融机构列表");
			}
			dataSearch();
		});
		
		//汇总之前 先查询是否有未上报的数据
		$('#hzsb').click(function(){
			if($('#bsrq').val() == null || $('#bsrq').val() == ''){
				diaLog('报送日期不能为空');
				return false;
			}
			if($('#sjzt').val() == null || $('#sjzt').val() == ''){
				diaLog('报送状态不能为空');
				return false;
			}
			if($('#bbmc').val() == null || $('#bbmc').val() == ''){
				diaLog('报表名称不能为空');
				return false;
			}
			//遮罩层开始
			var index = layer.load(1, {
				shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
			});
			var data = {
				sjlx:$('#sjlx').val(),
				bbbm:$('#bbmc').val(),
				bsrq:$('#bsrq').val(),
				sjzt:'2'
			}
			$.ajax({
				type:"post",
				url:url + "reportManage/reportManageData?userszdwbm="+$("#userszdwbm").val(),
				data:data,
				success:function(data){
					if(data != "" && data.length > 0){
						data = JSON.parse(data);
						if(data.length <= 0 || (data[0].children.length <= 0 && data[0].id == cookieUtil.getUserInfoFromCookie().DWBM)){
							report();
						}else{
							msgUtil.diaLog("请确保没有下级机构未上报后，再进行汇总！");
						}
						//遮罩层结束
						layer.close(index);
					}
				}
			});
		});
		
		// 初始化操作
		mkdirTableName();
	});
});

function dataSearch(){
	if($('#bsrq').val() == null || $('#bsrq').val() == ''){
		diaLog('报送日期不能为空');
		treegridData("[ ]");
		return false;
	}
	if($('#sjzt').val() == null || $('#sjzt').val() == ''){
		diaLog('报送状态不能为空');
		treegridData("[ ]");
		return false;
	}
	if($('#bbmc').val() == null || $('#bbmc').val() == ''){
//		diaLog('报表名称不能为空');
		treegridData("[ ]");
		return false;
	}
	//遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	var data = {
		sjlx:$('#sjlx').val(),
		bbbm:$('#bbmc').val(),
		bsrq:$('#bsrq').val(),
		sjzt:$('#sjzt').val()
	}
	$.ajax({
		type:"post",
		url:url + "reportManage/reportManageData?userszdwbm="+$("#userszdwbm").val(),
		data:data,
		success:function(data){
			if(data != "" && data.length > 0){
				var fdata = [];
				//添加一个父类
//			fdata.push({children:JSON.parse(data),pid:4560,text:"全部",id:0,key:null/*,state:"closed"*/});
				//展示树形表格
				treegridData(data,index);
			}
		}
		
	});
}

//生成报表名称
function mkdirTableName(){
	$.ajax({
		type : "post",
		url : url + 'reportManage/getCodeData',
		async : true,
		data : {
			sqlId: 'getBbmc',
			sjlx : $("#sjlx").val(),
			bsrq:$('#bsrq').val()
		},
		success : function(data) {
			if(data != "" && data.length > 0){
				data = JSON.parse(data);
				$("#bbmc").empty(); 
				for (var i = 0; i < data.data.length; i++) {
					$("#bbmc").append(
							"<option value='" + data.data[i].code_key
							+ "' name='" + data.data[i].code_value
							+ "'>" + data.data[i].code_value
							+ "</option>")
				}
				dataSearch();
			}
		}
	});
}

//汇总
function report(ishz){
	//遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	var data = {
		sjlx:$('#sjlx').val(),
		sqlId:$('#bbmc').val(),
		bsrq:$('#bsrq').val(),
		sjzt:$('#sjzt').val()
	}
	if(ishz == 'yes'){
		data.ishz = 'yes';
	}
	$.ajax({
		url : url + 'dataSummaryController/updateData',
		type : 'post',
		data :data,
		async : true,
		success : function(data) {
			if(data != "" && data.length > 0){
				data = JSON.parse(data);
				if(data.data == null){
					diaLog('异常');
				}else if(data.data == 0){
					diaLog('暂无上报数据');
				}else if(data.data > 0){
					diaLog('汇总成功');
				}else if(data.data  == 'OK'){
					$dialog.confirm({
						title:'提示',
						width:350,
						height:50, 
						content:"已经汇总过，是否要更新汇总？",
						cancelValue: '否',
						cancel: true,
						okValue: '是',
						ok: function(){
							report("yes");
						},
						onclose: function(){}
					}).showModal();
				}
				//遮罩层结束
				layer.close(index);
			}
		},
		error : function(data) {
			//遮罩层结束
			layer.close(index);
		}
	});
}

//生成表格树
function treegridData(data,index){
	$("#test").treegrid({
		data:JSON.parse(data),
		fitColumns: false,
		nowrap:true,
		onLoadSuccess:function(ddata){
			//遮罩层结束
			layer.close(index);
		},
		onClickCell:function(field,index,value){
		},
		onCollapse:function(node){//当节点折叠时触发。
        } 
	});
	
	// 默认只展开根节点
	if ($("#test").treegrid("getData").length > 0) {
		$("#test").treegrid("collapseAll");
		$("#test").treegrid("expand",$("#test").treegrid("getData")[0].id);
	}
}

// 提示框
function diaLog(message, color) {
	$dialog({
		title : "提示",
		content : "<div style='font-size:18px;color:" + color + ";text-align:center'>" + message + "</div>",
		icon : 'succeed',
		width : '200px',
		height : '50px',
		follow : document.getElementById('btn2')
	}).showModal();
}

//根据上级参数调用预设好的请求数据将返回参数回填至元素中
function getData(key, params, status) {
	var element = defaults[key];
	element.dom.empty();
//	appendOptionTo(element.dom, "--请选择--", "--请选择--", "--请选择--");
	$.getJSON(element.url, params, function(data) {
		AllOrgCode = data.data;
		if (typeof Array.prototype.forEach != 'function') {
		    //不支持,此时我们需要自己定义一个类似forEach功能的函数。
			Array.prototype.forEach = function(callback){
				for (var i = 0; i < this.length; i++){
			        callback.apply(this, [this[i], i, this]);
			    }
			};
		}
		data.data.forEach(function(item, index) {
			appendOptionTo(element.dom, item.code_value, item.code_key, '');
		});
		element.dom.change();
	});
}

//将查询回的数据生成option标签并回填至页面
function appendOptionTo($o, k, v, d) {
	var $opt = $("<option>").text(k).val(v);
	if (v == d) {
		$opt.attr("selected", "selected")
	}
	$opt.appendTo($o);
}

