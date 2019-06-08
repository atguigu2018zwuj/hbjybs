var url = $("#url").val();
var msgUtil = new MsgUtil();
var httpUtil = new HttpUtil();
define(function(require) {
	require('jquery');
	require('JsExcelXml');
	require('jqueryEasyUi');
	require('layer');
	require('timeFormat');
	require('My97DatePicker');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	$(function() {
		var grid = $("#test");
		wdatePickerYMD("beginDate");
		//easyui treegrid自适应
		$(window).resize(function(){
			grid.datagrid('resize', {
				width:function(){return document.body.clientWidth;},
				height:function(){return document.body.clientHeight;},
			});
		});
		$('#beginDate').bind("change",function(){
			doSearch(grid);
		});
		$('#bm').bind("change",function(){
			doSearch(grid);
		});
		$('#ispass').bind("change",function(){
			doSearch(grid);
		});
		
		//键盘松开是 触发
		$('#ywm').bind("change",function(){
			doSearch(grid);
		});
		//值改变时触发
		
		var data = {
			beginSjrq: $('#beginDate').val()
		};
		
		//生成报文  不做标识判断
		$('#scbwId').bind("click",function(){
			var kv = grid.treegrid("getCheckedNodes");
			if(kv.length <= 0){
				msgUtil.diaLog('请先选择报文');
				return false;
			}
			var index = layer.load(1, {
				shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
			});
			var t1 = setInterval(aa,1000*60*29);//定时刷新防止session超时 29分钟
			//去除父类 只要子类
			var zlData =[];
			for(var i = 0;i < kv.length; i++){
				if(kv[i].id > 110100){
					zlData.push(kv[i]);
				}
			}
			var data = {
				beginSjrq: $('#beginDate').val(),
				id:'scbw',
				ywm: $('#ywm').val(),
				bm: $('#bm').val()
			};
			data.treeData = JSON.stringify(zlData);
			downFile(data,index,grid,"generateMessage",t1);
		});
		
		//下载报文  标识判断  只要标识!=null || '' 都可以下载 
		$('#downFile').bind("click",function(){
			//获取已经勾选的
			var kv = grid.treegrid("getCheckedNodes");
			if(kv.length <= 0){
				msgUtil.diaLog('请先选择报文');
				return false;
			}
			//去除父类 只要子类
			var zlData =[];
			for(var i = 0;i < kv.length; i++){
				if(kv[i].id > 110100){
					if(kv[i].scbwxx == null || kv[i].scbwxx == '' || kv[i].scbwxx == '生成报文失败'){
						msgUtil.diaLog('请先生成报文');
						return false;
					}
					zlData.push(kv[i].key);
				}
			}
			data.id = '';
			window.location.href = url + 'generateMessageController/downloadMessage?beginSjrq='+$('#beginDate').val()+'&params='+encodeURIComponent(JSON.stringify(zlData));
		});
		
		//校验程序   标识判断  只要标识!=null || !='' || != '生成报文失败'  都可以校验
		$('#checkData').bind("click",function(){
			//获取已经勾选的
			var kv = grid.treegrid("getCheckedNodes");
			if(kv.length <= 0){
				msgUtil.diaLog('请先选择报文');
				return false;
			}
			//去除父类 只要子类
			var zlData =[];
			for(var i = 0;i < kv.length; i++){
				if(kv[i].id > 110100){
					if(kv[i].scbwxx == null || kv[i].scbwxx == '' || kv[i].scbwxx == '生成报文失败'){
						msgUtil.diaLog('请先生成报文');
						return false;
					}
					zlData.push(kv[i]);
				}
			}
			var index = layer.load(1, {
				shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
			});
			var t1 = setInterval(aa,1000*60*29);
			var data = {
				beginSjrq: $('#beginDate').val(),
				id:'cxjy',
				treeData:JSON.stringify(zlData),
				ywm: $('#ywm').val(),
				bm: $('#bm').val()
			};
			downFile(data,index,grid,"generateMessage",t1);
		});
		
		//报文上报  标识判断 只有校验成功 、上报失败或者上报成功 才可以
		$('#bwsbId').bind("click",function(){
			var fla = false;
			//获取已经勾选的
			var kv = grid.treegrid("getCheckedNodes");
			if(kv.length <= 0){
				msgUtil.diaLog('请先选择报文');
				return false;
			}
			//去除父类 只要子类
			var zlData =[];
			for(var i = 0;i < kv.length; i++){
				if(kv[i].id > 110100){
					if(kv[i].scbwxx == '校验失败'){
						fla = true;
					}
					if(kv[i].scbwxx != '校验失败' && kv[i].scbwxx != '校验成功' && kv[i].scbwxx != '上报成功' && kv[i].scbwxx != '上报失败'){
						msgUtil.diaLog('请先通过校验！');
						return false;
					}
					zlData.push(kv[i]);
				}
			}
			var index = layer.load(1, {
				shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
			});
			var t1 = setInterval(aa,1000*60*29);
			var data = {
				beginSjrq: $('#beginDate').val(),
				id:'bwsb',
				treeData:JSON.stringify(zlData),
				ywm: $('#ywm').val(),
				bm: $('#bm').val()
			};
			if(fla){
				$.ajax({
					type:"post",
					url:url + "generateMessageController/readFile",
					data:data,
					success:function(result){
						if(result == "" && result.length <= 0){
							return;
						}
						result = JSON.parse(result); 
						if(result.data != null && result.data != ''){
							msgUtil.diaLog('有强制行校验错误！');
							downFile(data,index,grid,"getCodeData",t1);
						}else{
							downFile(data,index,grid,"generateMessage",t1); 
						}
					}
				});
			}else{
				downFile(data,index,grid,"generateMessage",t1); 	
			}
		});
		
		function aa(){
			 $.ajax({
				  type : "post",
				  async : false, //同步请求
//				  url : url + 'code/getData?sqlId=getCodeData&table_name=code_jglb',
				  url : url + 'static/my/images/logo.png?time='+formatDate(new Date()),
			});
		 }
	});
});

// ------------- formatter START ----------
//是否通过
function ispassFormatter(value, row, index) {
	if (value != undefined && value != "") {
		value = value == "true" ? "是" : "否";
	} else {
		value = "";
	}
	return value;
}

//操作
function operationFormatter(value, row, index) {
	if (row.ispass == "false") {
		return "<a href='javascript:void(0)' class='btn-gen-msg-opera' onclick='repulseMessage("+JSON.stringify(row)+")'>打回</a>";
	}
}

//是否发送短信
function sendBtnFormatter(value, row, index) {
	if (row.ispass == "false") {
		return "<a href='javascript:void(0)' class='btn-gen-msg-send' onclick='sendMobileMsg("+JSON.stringify(row)+")'>发送短信</a>";
	}
}
//------------- formatter END ----------

// 打回报文
function repulseMessage(row){
	// 遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	var param = {
		sjrq:$('#beginDate').val(),//数据日期
		ywm:row.text,//业务名
		ywbm:row.ywbm,//业务编码
		bm:row.key,//表名
		reportFlag:row.scbwxx_code,//报文标识
		reportFlagName:row.scbwxx//报文标识信息
	};
	httpUtil.ajax("post","generateMessageController/repulseErrorReport",param,function(result){
		if(result.data == undefined){
			msgUtil.diaLog("上报报文打回失败！",false);
		} else if (!result.data.result) {
			msgUtil.diaLog(result.data.msg,false);
		} else {
//			msgUtil.diaLog("【"+param.sjrq+"】的业务【"+param.ywm+"】的报文因【"+param.reportFlagName+"】已被打回！");
			msgUtil.diaLog("上报报文已打回！");
		}
		// 遮罩层结束
		layer.close(index);
	});
}

// 发送短信
function sendMobileMsg(row){
	// 遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	var param = {
			sjrq:$('#beginDate').val(),//数据日期
			ywm:row.text,//业务名
			ywbm:row.ywbm,//业务编码
			bm:row.key,//表名
			reportFlag:row.scbwxx_code,//报文标识
			reportFlagName:row.scbwxx//报文标识信息
	};
	httpUtil.ajax("post","generateMessageController/sendReportErrorMsg",param,function(result){
		if(result.data == undefined){
			msgUtil.diaLog("发送短信失败！",false);
		} else if (!result.data.result) {
			msgUtil.diaLog(result.data.msg,false);
		}  else {
//			msgUtil.diaLog("短信：【"+param.sjrq+"】的业务【"+param.ywm+"】的报文因【"+param.reportFlagName+"】失败已发送给报文上传者！");
			msgUtil.diaLog("短信发送成功！");
		}
		// 遮罩层结束
		layer.close(index);
	});
}

//查询
function doSearch(grid){
	var beginSjrq = $('#beginDate').val();
	if(beginSjrq == null || beginSjrq == ''){
		msgUtil.diaLog('请选择数据日期');
		return false;
	}
	//遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	var data = 
	{
		beginSjrq: $('#beginDate').val(),
		ywm: $('#ywm').val(),
		bm: $('#bm').val()
		//TODO 查询条件【是否通过】（正在确认）
		,ispass: $('#ispass').children('option:selected').val()
	 }
	$('div[id="myTable"] div[class="datagrid-wrap panel-body panel-body-noheader"]').attr("style","width:"+($('div[class="consult-history"]').width())+"px");
	$("#myTable").css("display", "block");
	downFile(data,index,grid,"getCodeData",null);
}

//
function downFile(treegrid,index,grid,methodName,t1){
	$.ajax({
		type:"post",
		url:url + "generateMessageController/"+methodName,
		data:treegrid,
		success:function(data){
			if(data == "" && data.length <= 0){
				return;
			}
			$('tr[node-id="1"]').find('span[class="tree-checkbox tree-checkbox0"]').click();
			$('tr[node-id="1"]').find('span[class="tree-checkbox tree-checkbox2"]').click();
			$('tr[node-id="1"]').find('span[class="tree-checkbox tree-checkbox1"]').click();
			var fdata = [];
			//展示树形表格
			grid.treegrid({
				data:JSON.parse(data),
				fitColumns: true,
				nowrap:true,
				onLoadSuccess:function(ddata){
					$('div[id="myTable"] div[class="datagrid-wrap panel-body panel-body-noheader"]').attr("style","width:"+($('div[class="consult-history"]').width())+"px");
					if(treegrid.treeData != undefined && treegrid.treeData != null && treegrid.treeData != ""){
						var treeData = JSON.parse(treegrid.treeData);
						for(var i=0;i < treeData.length;i++){
							var kv = grid.treegrid("checkNode",treeData[i].id);  //根据ID勾选节点 
						}
					}
					if(t1 != null){
						clearInterval(t1);//当执行完毕后 关闭定时刷新
					}
					// 当是网点登录时，隐藏【操作】和【是否发短信】列
					var cookieUtil = new CookieUtil();
					var userInfo = cookieUtil.getUserInfoFromCookie();
					if (userInfo.DW_JB == "4") {
						grid.datagrid("hideColumn", "cz"); // 隐藏【操作】列
						grid.datagrid("hideColumn", "issend"); // 隐藏【是否发短信】列
					}
					layer.close(index);
				},
				onClickCell:function(field,index,value){
					if(field == 'scbwxx'){
						if(index.scbwxx == '校验失败' || index.scbwxx == '强制性校验错误'){
							//下载失败的文件
							window.location.href = url + 'generateMessageController/downFlie?beginSjrq='+$('#beginDate').val()+'&fileName='+index.key;
						}
					}
				},
				onCollapse:function(node){//当节点折叠时触发。
		            	$('div[id="myTable"] div[class="datagrid-wrap panel-body panel-body-noheader"]').attr("style","width:"+($('div[class="consult-history"]').width())+"px");
		        } 
			});
		}
	});
}