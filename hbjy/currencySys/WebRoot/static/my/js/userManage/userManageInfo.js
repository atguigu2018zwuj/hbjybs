var defaults = null;
var url = $("#url").val();
var AllOrgCode;
var dataDwCode=null;
var cookieUtil = new CookieUtil();
var userInfo = cookieUtil.getUserInfoFromCookie();
define(function(require) {
	require('jquery');
	require('jqueryEasyUi');
	require('datetimepicker');
	require('layer');
	require('JsExcelXml');
	require('daterangepicker');
	require('validate');
	require('ztree_all');
	require('ztree_exhide');
	require('fuzzysearch');
	require('select2');
	
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	// 初始化方法
	$(function() {
		// 定义表格变量
		var $grid = $("#gridConsult");
		// 初始化用户信息列表
		var data =[]; 
		/*= 
		{
		    YHNAME :$("#YHNAME").val()"admin",
		    NAME :$("#NAME").val(),
		    TELEPH :$("#TELEPH").val(),
		    SSBM :$("#SSBM").val(),
		    SSJG :$("#SSJG").val(),
		    DWBM :$("#DWBM").val()		
		};*/
		initTable($grid, data);
		// 添加按鈕
		var $add = $("#addId");
		var addTitleValue = '新增用户';
		addOrUpdateData($add, addTitleValue);
		// 修改按钮
		var $Update = $("#updateId");
		var updateTitleValue = '修改用户';
		addOrUpdateData($Update, updateTitleValue);
		
		// 删除按钮
		$("#deleteId").click(function() {
			deleteBtn();
		});
		
		// 重置按钮
		$("#rechargeId").click(function() {
			rechargeBtn();
		});
		$("#checkSearch").click(function(){
			checkSearch();
		});
	});
});

// 页面加载时，初始化表格
function initTable($grid, data) {
	var IsCheckFlag = true;
	$.ajax({
		type : "POST",
		url : url + "userManage/userManageSearch",
		data : data,
		dataType : "json",
		success : function(result) {// 回调函数
			if(result == "" && result.length <= 0){
				return;
			}
			var data = result.data;
			var dataList = data.dataList;
			dataDwCode=data.dwCodeList;
			// 显示myTable这个div
			$("#myTable").css("display", "block");
				dataList = addXh(dataList);
				$grid.datagrid({
					data : dataList.slice(0,10),
					nowrap : true,
					fitColumns: false,
					emptyMsg:'暂无数据<br>',
					pagination:true,
					showRefresh: false,//定义是否显示刷新按钮
				    loadMsg:'数据正在努力加载，请稍后...', 
					onCheck : function(rowIndex, rowData) {
						singleUpdate($grid);// 禁用或开启修改按钮
					},
					onUncheck : function(rowIndex, rowData) {
						singleUpdate($grid);// 禁用或开启修改按钮
					},
					onCheckAll : function(rows) {
						singleUpdate($grid);// 禁用或开启修改按钮
					},
					onUncheckAll : function(rows) {
						singleUpdate($grid);// 禁用或开启修改按钮
					},
					onSelect : function(rowIndex, rowData) {
						singleUpdate($grid);// 禁用或开启修改按钮
						
						if (!IsCheckFlag) {//只有点击复选框 才可以选中
				            IsCheckFlag = true;
				            $grid.datagrid("unselectRow", rowIndex);
				        }
					},
					onUnselect : function(rowIndex, rowData) {
						singleUpdate($grid);// 禁用或开启修改按钮
						
						if (!IsCheckFlag) {//只有点击复选框 才可以选中
				            IsCheckFlag = true;
				            $grid.datagrid("selectRow", rowIndex);
				        }
					},
					onClickCell:function(index,field,value){
						IsCheckFlag = false;
					},
					onLoadSuccess : function(data) {
						if(data.rows.length <= 0){
							$('tr[datagrid-row-index="0"]').find('td[field="ck"]').hide();
							$('tr[class ="datagrid-header-row"]').find('td[field="ck"]').hide();
							//$('div[class="datagrid-view"]').attr("style","height: 19px; width: 1200px;");
							$('div[class="datagrid-view"]').attr("style","height: 19px; ");
						}
						// 防止右侧出现垂直滚动条
						$(".datagrid-body").css("height",parseInt($(".datagrid-body").css("height").replace("px",""))+3+"px");
					}
				});
				pagefun(result,$grid);
		}// 回调函数
	});
}
// datagird的事件触发方法，主要是禁用或启用修改按钮
function singleUpdate($grid) {
	var rowsList = $grid.datagrid('getSelections');
	$("#updateId").removeAttr("disabled");
	// 当只选中一行时，开启修改按钮，否则禁止修改按钮
	if (rowsList.length <= 1) {
		$("#updateId").removeAttr("disabled");
	} else {
		$("#updateId").attr({
			"disabled" : "disabled"
		});
	}
}

// 初始化添加对话框
function addOrUpdateData($addOrUpdate, titleValue) {
	$addOrUpdate.click(function() {
		console.log("123");
		var html="";
		html+='</select>';
		// 对话框中控件
		var addlist = '<form id="formId" style="margin-left:24px;">'
			+ '<div class="row">'
			+ '<div class="col-md-6">柜员号：<input id="yhNameId" type="text" name="yhName" style="width:162px;height:30px" /></div>'
			+ '<div class="col-md-6">用户密码：<input id="yhPWDId" type="text" name="yhPWD" style="width:162px;height:30px" /></div>'
			+ '</div>'
			+ '<div class="row">'
			+ ' <div class="col-md-6"><span style="margin-left:18px">姓名：</span><input id="nameId" type="text" name="namev" style="width:162px;height:30px" /></div>'
			+ ' <div class="col-md-6">手机号码：<input id="telephId" type="text" name="teleph" style="width:162px;height:30px" /></div>'
			+ '</div>'
			+ '<div class="row">'
			+'<div class="col-md-6"><span style="margin-left:-18px">所在单位：</span><input id="szdwMcid" type="hidden" name="szdwMc" style="width:0px;height:30px" /> <input id="szdwMc" type="text" style="width:162px;height:30px" /></div>'
			+ '<div class="col-md-6">所属部门：<input id="ssbmId" type="text" name="ssbm" style="width:162px;height:30px" /></div>'
			+ '</div>' 
			+ '<div class="row">'
			+ ' <div class="col-md-6"><span style="margin-left:18px">权限：</span><input id="authorityId" type="text" readonly name="authority" style="width:162px;height:30px" /></div>'
			+ ' <div class="col-md-6">短信通知：<select class="organizationSelect rightSelect" id="smsNotice" name="smsNotice" style="width:162px;height:30px"> ' 
			+ ' <option selected value="0">否</option>' 
			+ ' <option value="1">是</option>' 
			+ ' </select></div>'
			+ '</div>' 
			+ '</form>';
		// 如果是修改按钮，判断是否选中一行记录
		if (titleValue.indexOf("修改") >= 0) {
		var rowValue = $('#gridConsult').datagrid('getSelected');
		if (rowValue == null) {
				diaLog("请选择要修改的一行");
				return;
			}
		}		
		// jquery的validate校验
		var validate = {
			textvalidate : function() {
				return $("#formId").validate({
					rules : {
						yhName : {
							required : true,
							minlength : 1,
							maxlength : 100
						},
						yhPWD : {
							required : true,
							minlength : 6,
							maxlength : 100
						},
						namev : {
							required : true,
							minlength : 1,
							maxlength : 100
						},
						teleph : {
							required : true,
							digits : true,
							rangelength : [ 11, 11 ]
						},
						ssbm : {
							required : true,
							rangelength : [ 1, 200 ]
						},
						authority : {
							required : true
						}
					},
					messages : {
						yhName : {
							required : "必填项",
							minlength : "最小长度1个字符",
							maxlength : "最大长度100个字符"
						},
						yhPWD : {
							required : "必填项",
							minlength : "最小长度6个字符",
							maxlength : "最大长度100个字符"
						},
						namev : {
							required : "必填项",
							minlength : "最小长度1个字符",
							maxlength : "最大长度100个字符"
						},
						teleph : {
							required : "必填项",
							digits : "必须为数字",
							rangelength : "手机号为11为数字"
						},
						ssbm : {
							required : "必填项",
							rangelength : "所属部门1-200字符"
						},
						authority : {
							required : "必填项"
						}
					}
				});
			}
		}
		
		// 对话框
		$dialog({
			title : titleValue,
			width : 600,
			content : '<div style="height: ;overflow: hidden;">'+ addlist + '</div>',
			onshow : function() {
				$('.artui-dialog-content').css({ "max-height" : "150px", "overflow" : "auto" });
				if (titleValue.indexOf("新增") >=0) {
					if (userInfo.YH_NAME =='admin') {
		            	$("#authorityId").val("finf,xjwb,khdk,rcyw,gzhm,xjsz,kcqk,xjls,prsn,jqsb,jcdb,ymkc,crkb,xqmy,jrkc,jrjb,qfnl,kcxb,ybzx,pbsl,szqk,xjqf,xjkc,zxth,wssl,jryw,jryt,jrds,jrtj,rbsf,kcxj,xqqk,szlx,bzjd,wdsl,sydw,xjfw,sszt,xjcl,jjsb,bsgl,conversion,qxgl,finfwh,gggl,fgwj,customReport,customReportAdditional,customReportSummary");
					}else if (userInfo.ALEVEL=='1' &&userInfo.YH_NAME !='admin') {
						$("#authorityId").val("finf,xjwb,khdk,rcyw,gzhm,xjsz,kcqk,xjls,prsn,jqsb,jcdb,ymkc,crkb,xqmy,jrkc,jrjb,qfnl,kcxb,ybzx,pbsl,szqk,xjqf,xjkc,zxth,wssl,jryw,jryt,jrds,jrtj,rbsf,kcxj,xqqk,szlx,bzjd,wdsl,sydw,xjfw,sszt,xjcl,jjsb,bsgl,qxgl,finfwh");

					} else {
		            	$("#authorityId").val("finf,xjwb,khdk,rcyw,gzhm,xjsz,kcqk,xjls,prsn,jqsb,jcdb,ymkc,crkb,xqmy,jrkc,jrjb,qfnl,kcxb,ybzx,pbsl,szqk,xjqf,xjkc,zxth,wssl,jryw,jryt,jrds,jrtj,rbsf,kcxj,xqqk,szlx,bzjd,wdsl,sydw,xjfw,sszt,xjcl,jjsb,bsgl,qxgl,finfwh");

					}
					/*if (userInfo.ALEVEL=='1' &&userInfo.YH_NAME !='admin') {
						$("#authorityId").val("finf,xjwb,khdk,rcyw,gzhm,xjsz,kcqk,xjls,prsn,jqsb,jcdb,ymkc,crkb,xqmy,jrkc,jrjb,qfnl,kcxb,ybzx,pbsl,szqk,xjqf,xjkc,zxth,wssl,jryw,jryt,jrds,jrtj,rbsf,kcxj,xqqk,szlx,bzjd,wdsl,sydw,xjfw,sszt,xjcl,jjsb,bsgl,qxgl,finfwh");
		            }
		            if (userInfo.ALEVEL=='2' || userInfo.ALEVEL=='3') {
		            	$("#authorityId").val("finf,xjwb,khdk,rcyw,gzhm,xjsz,kcqk,xjls,prsn,jqsb,jcdb,ymkc,crkb,xqmy,jrkc,jrjb,qfnl,kcxb,ybzx,pbsl,szqk,xjqf,xjkc,zxth,wssl,jryw,jryt,jrds,jrtj,rbsf,kcxj,xqqk,szlx,bzjd,wdsl,sydw,xjfw,sszt,xjcl,jjsb,bsgl,qxgl,finfwh");
					}
		            if (userInfo.ALEVEL=='4' || userInfo.ALEVEL=='5') {
		            	$("#authorityId").val("finf,xjwb,khdk,rcyw,gzhm,xjsz,kcqk,xjls,prsn,jqsb,jcdb,ymkc,crkb,xqmy,jrkc,jrjb,qfnl,kcxb,ybzx,pbsl,szqk,xjqf,xjkc,zxth,wssl,jryw,jryt,jrds,jrtj,rbsf,kcxj,xqqk,szlx,bzjd,wdsl,sydw,xjfw,sszt,xjcl,jjsb,finfwh");
					}*/
				}
				// 如果是修改按钮，回显数据
				if (titleValue.indexOf("修改") >= 0) {
					var rowValue = $('#gridConsult').datagrid('getSelected');
					$("#yhNameId").val(rowValue.YH_NAME);
					$("#yhPWDId").val(rowValue.YH_PWD);
					$("#nameId").val(rowValue.NAME);
					$("#telephId").val(rowValue.TELEPH);
					$("#ssbmId").val(rowValue.SSBM);
					$("#authorityId").val(rowValue.AUTHORITY);
					$("#szdwMc").val(rowValue.DWMC);
					$("#szdwMcid").val(rowValue.DWBM);
					if(rowValue.SMSNOTICE == '是'){
						$("#smsNotice").val('1');
					}else{
						$("#smsNotice").val('0');
					}
					
					// 存储一个记录的id，用于update的where条件
					$("#updateId").data("rowId", rowValue.ID);
					// 存储修改之前的密码，用于判断更新时，是否修改了密码，如果修改了密码就加密，否则就不加密
					$("#updateId").data("yh_pwd", rowValue.YH_PWD);
					// 如果是admin账户，禁止修改用户名
					if (rowValue.YH_NAME == "admin") {
						$("#yhNameId").attr("readonly","readonly");// 设为只读
					} else {
						$("#yhNameId").removeAttr("readonly");// 取消只读的设置
					}
				} else {
					$("#yhNameId").removeAttr("readonly");// 取消只读的设置
				}
				// 初始化权限控件的单击事件
				$("#authorityId").click(function() {
					if (titleValue.indexOf("新增") >= 0) {
						authorityDialog($("#authorityId").val());
					} else if (rowValue.YH_NAME != "admin") {
						authorityDialog($("#authorityId").val());
					}
				});
				// 初始化单位控件的单击事件
				$("#szdwMc").click(function() {
					szdwMcDialog($("#szdwMc").val());
				});
				
			},
			okValue : '确定',
			ok : function() {
				// 校验失败则返回
				if (!validate.textvalidate().form()) {
					$("label.error").css({ "color" : "red", "margin-left" : "81px" });
					return false;
				}
				// 获取控件数据
				var yhName = $("#yhNameId").val();
				var yhPWD = $("#yhPWDId").val();
				var name = $("#nameId").val();
				var teleph = $("#telephId").val();
				var ssbm = $("#ssbmId").val();
				var authority = $("#authorityId").val();
				var szdwMc = $("#szdwMcid").val();
				var ssjg = $("#szdwMc").val();
				var smsNotice = $("#smsNotice").val();
				// 用户名称重名则返回
				var id = '';
				if (titleValue.indexOf("修改") >= 0) {
					var rowValueq = $('#gridConsult').datagrid('getSelected');
					id = rowValueq.ID;
				} else {
					id = '';
				}
				/*var isAlreadyTag = isYHNameAlready(yhName, id);
				if (isAlreadyTag == "1") {
					// 先清空错误信息
					$("#yhNameId").siblings().remove("label");
					// 添加错误提示信息
					$("#yhNameId").after('<label for="yhNameId" generated="true" class="error" style="color: red; margin-left: 81px;">用户名称不能重复</label>');
					return false;
				} else {
					// 先清空错误信息
					$("#yhNameId").siblings().remove("label");
				}*/
				// 拼接成json对象
				var data = {
					yhName : yhName,
					yhPWD : yhPWD,
					name : name,
					teleph : teleph,
					ssbm : ssbm,
					authority : authority,
					smsNotice : smsNotice,
					szdwMc:szdwMc,
					ssjg:ssjg
				};
				// 根据添加或修改，调用对应方法
				if (titleValue.indexOf("新增") >= 0) {// 新增
					insertTableData(data);
				} else {// 修改
					updateTableData(data);
				}
			},
			cancelValue : '取消',
			cancel : function() {
			}
		}).showModal();
	});
}

var setting = {
	check : {
		enable : true,
		chkStyle : "checkbox",
		chkboxType : {
			"Y" : "ps",
			"N" : "ps"
		}
	},
	view : {
		dblClickExpand : false,// 屏蔽掉双击事件
		selectedMulti : true // 可以多选
	},
	data : {
		simpleData : {
			enable : true,
			idKey : "ID",
			pIdKey : "PID",
			rootPId : 0
		},
		key : {
			name : "VALUE"// 上面data里的数据包含一个codeTypeDes属性，也就是说从数据库中对应的表必须有这个字段
		}
	}
};

function getCheckedLength(treeObj) {
	var nodes = treeObj.getCheckedNodes(true);
	var newNodes = new Array();
	if (nodes) {
		for (var i = 0; i < nodes.length; i++) {
			if (!nodes[i].isParent) {// isParent判断是否为父级，也就是是否有下级
				newNodes.push(nodes[i].KEY);// 存放所选节点的名称
			}
		}
	}
	return newNodes;
}

var setting1 = {
		check:{
			enable: true,
            chkStyle: "radio",  //单选框
            radioType: "all"   //对所有节点设置单选
		},
		data:{
			simpleData:{
				enable:true
			}
		}
};

function getCheckedLength1(treeObj){
		var nodes = treeObj.getCheckedNodes(true);
		var newNodes = new Array();
		if(nodes){
			for(var i=0; i<nodes.length; i++){
				if(!nodes[i].isParent){
					newNodes.push(nodes[i]);
				}
			}
		}
		return newNodes;
}


function szdwMcDialog(){
	//所属单位树形结构
	var treeObj;
	var nodes;
	var areaIds;
	$dialog({
		title:'请选择所属单位',
	    title_html:true,
		width:600,
		height:200,
		content:'<div style="width: 600px">'
			+'<input id="keyword" type="search" placeHolder="搜索关键字"/>'
		    +'</div>'
		    +'<div style="height: ;overflow: auto;"><ul id="treeDiv" class="ztree"></ul></div>',
		onshow:function(){
			$('.artui-dialog-content').css({"max-height":"150px","overflow":"auto"});
			$.ajax({
		    	type:"POST",
				url: url+"userManage/queryDwCode",
				data:'',
				dataType:"json",
				success: function(result) {
					if(result == "" && result.length <= 0){
						return;
					}
					if(result.code==1000){
						var treeCheckedData = $('#szdwMcid').val();
						if(treeCheckedData != undefined){
							for(var j=0; j < treeCheckedData.length; j++){
								$.each(result.data,function(i,obj){
		    				    	if(treeCheckedData == obj.id){ 
		    				    		result.data[i].checked = true;
		    				    	}
		 	    				});
					        }
						}
						$.fn.zTree.init($("#treeDiv"),setting1,result.data);
						treeObj = $.fn.zTree.getZTreeObj("treeDiv");
			 			nodes = getCheckedLength(treeObj);
			 			fuzzySearch('treeDiv','#keyword',null,true); //初始化模糊搜索方法
					}else{
						$dialog.alert('拉取地区列表失败！','warning');
					}
				}
		    });
		},
		okValue: '保存',
		ok:function(){
			var _this = $.fn.zTree.getZTreeObj("treeDiv").getCheckedNodes();
			$("#szdwMcid").val(_this[0].id);
			$("#szdwMc").val(_this[0].name);
			$("#szdwMc").data("nodes",nodes);
		},
		cancelValue:'取消',
		cancel:function(){}
		}).showModal();
}

// 权限列树形结构
function authorityDialog(author) {
	var treeObj;
	var nodes;
	var bzIds;
	$dialog({
		title : '请选择权限',
		width : 600,
		content:'<div style="width: 600px">'
			+'<input id="keyword" type="search" placeHolder="搜索关键字"/>'
		    +'</div>'
		    +'<div style="height: ;overflow: auto;"><ul id="treeDiv" class="ztree"></ul></div>',
		onshow : function() {
			$('.artui-dialog-content').css({ "max-height" : "250px", "overflow" : "auto"});
			$.ajax({
				type : "POST",
				url : url + "userManage/getUserAuthority?sqlId=getUserAuthority",
				data : '',
				dataType : "json",
				success : function(result) {
					if(result == "" && result.length <= 0){
						return;
					}
					if (result.code == 1000) {
						var treeCheckedData = $.trim(author).split(",");
						if (treeCheckedData != undefined) {
							for (var j = 0; j < treeCheckedData.length; j++) {
								$.each(result.data,function(i, obj) {
									var rootId = null;
									if (treeCheckedData[j] == obj.KEY) {
										result.data[i].checked = true;
										rootId = obj.PID;
									}
									if (rootId != null) {
										if (result.data[i].KEY == rootId) {
											result.data[i].checked = true;
										}
									}
								});
							}
						}
						$.fn.zTree.init($("#treeDiv"), setting, result.data);
						treeObj = $.fn.zTree.getZTreeObj("treeDiv");
						nodes = getCheckedLength(treeObj);
						fuzzySearch('treeDiv','#keyword',null,true); //初始化模糊搜索方法
					} else {
						$dialog.alert('拉取列表失败！', 'warning');
					}
				}
			});
		},
		okValue : '保存',
		ok : function() {
			// 获取树节点的id,以json的格式存入array数组
			var str = '';
			var treeData = $.fn.zTree.getZTreeObj("treeDiv").getCheckedNodes();
			if (treeData.length != 0) {
				for (var i = 0; i < treeData.length; i++) {
					if (!treeData[i].isParent) {
						str += treeData[i].KEY + ",";
					}
				}
				$("#authorityId").val(str.substr(0, str.length - 1));
			}
		},
		cancelValue : '取消',
		cancel : function() {
		}
	}).showModal();
}

// 删除按钮方法
function deleteBtn() {
	// 先判断是否选中一行
	var rowValueList = $('#gridConsult').datagrid('getSelections');
	if (rowValueList.length == 0) {
		diaLog("请选择要删除的行");
		return;
	}
	// 判断选中的行是否是admin账户，如果是，则不允许删除
	var idList = [];
	var isamdin = "0";// 记录中没有admin
	$.each(rowValueList, function(index, item) {
		if (item.YH_NAME == "admin") {
			isamdin = "1";
			return;
		} else {
			idList.push(item.ID);
		}
	});
	if (isamdin == "1") {
		diaLog("禁止删除admin账户");
		return;
	}
	// 弹出对话框，提示是否删除
	$dialog({
		title : '提示',
		width : 200,
		fixed : false,
		content : '确认删除？',
		onshow : function() {
			$('.artui-dialog-content').css({ "max-height" : "200px", "overflow" : "auto" });
		},
		okValue : '确定',
		ok : function() {
			// 获取要删除的rowid
			var data = {};
			data.id = idList;
			// ajax删除一行记录，并刷新表格
			$.ajax({
				type : "POST",
				url : url + "userManage/userManageDelete",
				data : data,
				dataType : "json",
				success : function(result) {// 回调函数
					if(result == "" && result.length <= 0){
						return;
					}
					if (result.data.deleteTag >= 1) {
						// 提示新增数据成功
						diaLog("删除数据成功");
						// 刷新表格
						var $grid = $("#gridConsult");
						var data = [];
						initTable($grid, data);
					}
				}// 回调函数
			});
		},
		cancelValue : '取消',
		cancel : function() {
		}
	}).showModal();
}

// 提示
function diaLog(message) {
	$dialog({
		title : "提示",
		content : "<div style='font-size:30px;color:red;text-align:center'>" + message + "</div>",
		icon : 'succeed',
		width : '300px',
		height : '80px',
		follow : document.getElementById('btn2')
	}).showModal();
}
// 给每一行添加一个序号
function addXh(dataList) {
	$.each(dataList, function(index, item) {
		item.xh = index + 1;
	});
	// 返回集合
	return dataList;
}
// 新增数据
function insertTableData(data) {
	$.ajax({
		type : "POST",
		url : url + "userManage/userManageInsert",
		data : data,
		dataType : "json",
		success : function(result) {// 回调函数
			if(result == "" && result.length <= 0){
				return;
			}
			if (result.data.insertTag == 1) {
				// 提示新增数据成功
				diaLog("新增数据成功");
				// 刷新表格
				var $grid = $("#gridConsult");
				var data = [];
				initTable($grid, data);
			}
		}// 回调函数
	});
}

// 修改数据
function updateTableData(data) {
	// 判断密码是否被修改
	var pwdIsOld = "0";// 表示没有修改
	var oldPwd = $("#updateId").data("yh_pwd");
	var newPWD = data.yhPWD;
	if (oldPwd == newPWD) {
	} else {
		pwdIsOld = "1";// 表示有修改
	}
	data.pwdIsOld = pwdIsOld;
	$("#updateId").removeData("yh_pwd");// 移除yh_pwd
	// 添加记录的行号id
	data.id = $("#updateId").data("rowId");
	$("#updateId").removeData("rowId");// 移除rowId
	$.ajax({
		type : "POST",
		url : url + "userManage/userManageUpdate",
		data : data,
		dataType : "json",
		success : function(result) {// 回调函数
			if(result == "" && result.length <= 0){
				return;
			}
			if (result.data.updateTag == 1) {
				// 提示新增数据成功
				diaLog("修改数据成功");
				// 刷新表格
				var $grid = $("#gridConsult");
				var data = [];
				initTable($grid, data);
			}
		}// 回调函数
	});
}

//密码重置
function rechargeBtn(){
	// 先判断是否选中一行
	var rowValueList = $('#gridConsult').datagrid('getSelections');
	if (rowValueList.length == 0) {
		diaLog("请选择要重置的行");
		return;
	}
	// 弹出对话框，提示是否删除
	$dialog({
		title : '提示',
		width : 200,
		fixed : false,
		content : '确认重置密码？',
		onshow : function() {
			$('.artui-dialog-content').css({
				"max-height" : "200px",
				"overflow" : "auto"
			});
		},
		okValue : '确定',
		ok : function() {
			$.ajax({
				type : "POST",
				url : url + "userManage/userManageRecharge",
				data : {"rechargeData":JSON.stringify(rowValueList)},
				dataType : "json",
				success : function(result) {// 回调函数
					if(result == "" && result.length <= 0){
						return;
					}
					if (result.data.deleteTag >= 1) {
						// 提示新增数据成功
						diaLog("重置密码成功！");
						// 刷新表格
						var $grid = $("#gridConsult");
						var data = [];
						initTable($grid, data);
					}
				}// 回调函数
			});

		},
		cancelValue : '取消',
		cancel : function() {
		}
	}).showModal();
}

// 用户名称重名则返回
function isYHNameAlready(yhName, id) {
	var isAlreadyTag = "0";// 表示不重名
	// 根据yhName查询数据，看是否有重名记录存在
	var data = {
		yhName : yhName,
		id : id
	};
	$.ajax({
		type : "POST",
		async : false,
		url : url + "userManage/userManageSearch",
		data : data,
		dataType : "json",
		success : function(result) {// 回调函数
			if(result == "" && result.length <= 0){
				return;
			}
			if (result.data.dataList.length > 0) {// 有重复记录
				isAlreadyTag = "1";
				return isAlreadyTag;
			}
		}// 回调函数
	});
	// 返回是否重名标识
	return isAlreadyTag;
}

//分页功能
function pagefun(data,grid){
	var pager = grid.datagrid("getPager");  
	pager.pagination({  
	    total:data.data.dataList.length, 
	    disabled:false,
	    pageSize:10,//每页显示的记录条数，默认为10 
	    pageList: [10,20,50,100],//可以设置每页记录条数的列表 
	    beforePageText: '第',//页数文本框前显示的汉字 
	    afterPageText: '页    共 {pages} 页', 
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
	    onSelectPage:function (pageNo, pageSize) {  
	        var start = (pageNo - 1) * pageSize;  
	        var end = start + pageSize;  
	        grid.datagrid("loadData", data.data.dataList.slice(start, end));  
	        pager.pagination('refresh', {  
	            total:data.data.dataList.length,  
	            pageNumber:pageNo  
	        });  
	    }  
	});
}
function checkSearch(){
    var $grid = $("#gridConsult");
	var dataTwo=
	   {
		    YHNAME : $("#YHNAME").val(),
		    NAME :$("#NAME").val(),
		    TELEPH :$("#TELEPH").val(),
		    SSBM :$("#SSBM").val(),
		    NBJGH_LIKE :$("#NBJGH_LIKE").val(),
		    JRJGMC :$("#JRJGMC").val(),
		    JGJB :$("#JGJB").val()
		};
	initTable($grid,dataTwo);
}


