define(function(require) {
	require('jquery');
	require('jqueryEasyUi');
	require('layer');
	require('layui');
	require('element');
	require('JsExcelXml');
	require('ztree_all');
	require('ztree_exhide');
	require('fuzzysearch');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	
	var url = $("#url").val();
	var msgUtil = new MsgUtil();
	var httpUtil = new HttpUtil();
	var objectUtil = new ObjectUtil();
	var zTreeUtil = new ZTreeUtil();
	var dataIndex = "";//表格索引
	var editIndex = undefined;
	var operationFlg = "Template";//当前功能的标识
	
	// 补录表数据配置初始值
	var dataConfigsEmpty = {
			baseTInfo:{TEMPLATE_NAME:"",TABLE_NAME:"",BR_NO:"",RESPONSIBLER:"",REMARK:"",AUTHORITY:"",AUTHORITY_JGMC:""},
			fieldTInfo:[],
			templateManager:[]
	};
	// 补录表数据配置默认值
	var fieldInfoDefalut = {fieldIdEditTemplate:"",fieldNameEditTemplate:"",fieldTypeEditTemplate:"",fieldLengthEditTemplate:"0",fieldDecimalPlaceEditTemplate:"0",fieldAllowNullEditTemplate:0};
	
	$(function() {
		//表格ID
		var grid = $("#dg"+operationFlg);
		
		//查询
		$("#doSearch"+operationFlg).click(function(){
			doSearch(grid);
		});
		
		// 保存模板数据
		$("#saveData"+operationFlg).click(function(){
			saveData();
		});
		// 上一步
		$("#preStep"+operationFlg).click(function(){
			preStep();
		});
		// 下一步
		$("#nextStep"+operationFlg).click(function(){
			nextStep();
		});
		
		// 初始查询
		doSearch(grid);
	});
	
/** ----------------- 模板定制弹框-内部按钮 START ----------------- */
	// 弹窗表格-根据数据配置信息初始化表格显示
	function initTableInfoBTable(tableId,tableDataName){
		// 表头ID
		var hTableId = tableId.indexOf("HTable") > 0 ? tableId : tableId.replaceAll("BTable","HTable");
		// 表数据ID
		var bTableId = tableId.indexOf("BTable") > 0 ? tableId : tableId.replaceAll("HTable","BTable");
		var tableElems = "";
		var tableElem = "";
		var tableElemTemplate = $("#"+hTableId).find("thead").eq(0).html().replaceAll("<th","<td").replaceAll("</th","</td");
		// 数据配置信息
		var dataConfigs = $("#dataConfigs"+operationFlg).val().length > 0 ? JSON.parse($("#dataConfigs"+operationFlg).val()) : objectUtil.deepCopy(dataConfigsEmpty);
		// 表格数据
		var tableData = eval("dataConfigs."+tableDataName);
		var count = 1;
		if (tableData.length > 0) {
			for (var index=0; index < tableData.length; index++) {
				tableElem = tableElemTemplate;
				$.each($("#"+hTableId).find("tr").eq(0).children("th"),function(){
					var replaceValue = ">"+(eval("tableData[index]."+$(this).attr("field")) != undefined ? eval("tableData[index]."+$(this).attr("field")) : "");
					// 处理字段之外的值
					if ($(this).attr("field") == "index") {
						replaceValue = ">"+count++;
					} else if ($(this).attr("field") == "caozuo") {
						replaceValue = " class='last-one-model'><a href='javascript:void(0)' class='caozuo-delete'>删除</a>";
					} else if ($(this).attr("field") == "fieldAllowNullEditTemplate") {
						// 可否为空
						replaceValue = ">"+(eval("tableData[index]."+$(this).attr("field")) == 1 ? "是" : "否");
					}
					tableElem = tableElem.replaceAll(eval('/ field=\"'+$(this).attr("field")+'\">[^<]*/'),replaceValue);
				});
				tableElems+=tableElem;
			}
		} else {
			$("#"+bTableId).find("tbody").eq(0).html("");
		}
		$("#"+bTableId).find("tbody").eq(0).html(tableElems);
		
		// 根据数据数调整宽度
		var $table = $("#"+tableId);
		$table.children(":eq(0)").find("tr").size() > 5 
			? $table.find("td.last-one-model").addClass("last-one-small").removeClass("last-one-model") 
			: $table.find("td.last-one-small").addClass("last-one-model").removeClass("last-one-small");
			
		// 为表格中操作按钮绑定事件
		$table.find(".caozuo-delete").click(function(){
			if (bTableId.replaceAll("BTable"+operationFlg,"") == "fieldTInfo") {
				fieldDelete($(this).parent().parent().children().eq(1).html());
			}
		});
	}
	
	/** --------------------------- 基本信息页面 START --------------------------- */
	// 初始化基本信息页面
	function initBaseTInfoPage(){
		// 初始化物理表名(仅)
		httpUtil.ajax("post","cusReportAddController/getTableInfo",{},function(result){
			var optionTemplate = "<option value='&1'>&2(&1)</option>";
			var options = "";
			for (var resultIndex in result) {
				if (result[resultIndex].TABLE_NO != null && result[resultIndex].TABLE_NO != "" 
					&& result[resultIndex].TABLE_NAME != null && result[resultIndex].TABLE_NAME != "") {
					options += optionTemplate.replaceAll('&1',result[resultIndex].TABLE_NO).replaceAll('&2',result[resultIndex].TABLE_NAME);
				}
			}
			if (options != "") {
				// 当用户选择的有值时，选中对应值
				if ($("#TABLE_NAMEEditTemplate").children("option").size() > 0 
						&& $("#TABLE_NAMEEditTemplate").children("option").eq(0).attr("value") != $("#TABLE_NAMEEditTemplate").val()) {
					var tempTABLE_NAME = $("#TABLE_NAMEEditTemplate").val();
					$("#TABLE_NAMEEditTemplate").html(options);
					$("#TABLE_NAMEEditTemplate").val(tempTABLE_NAME);
				} else {
					$("#TABLE_NAMEEditTemplate").html(options);
				}			
			}
		});
		// 若定义权限为机构，则显示机构选择框
		$("#AUTHORITY").unbind("change");
		$("#AUTHORITY").change(function(){
			if ($(this).val() == "1") {
				$("#authorityJGEditTemplate").parent().prev().show();
				$("#authorityJGEditTemplate").parent().show();
			} else {
				$("#authorityJGEditTemplate").parent().prev().hide();
				$("#authorityJGEditTemplate").parent().hide();
				$("#authorityJGEditTemplate").val("");
				$("#AUTHORITY_JGMC").val("");
			}
		});
		// 初始化定义权限-机构控件的单击事件
		var treeCheckedData = [$("#authorityJGEditTemplate").val()];
		zTreeUtil.jrjgMcDialogInit(treeCheckedData,"authorityJGEditTemplate","AUTHORITY_JGMC");
		// 初始化定义权限-机构显隐
		$("#AUTHORITY").change();
	}
	/** --------------------------- 基本信息页面 END --------------------------- */
	
	/** --------------------------- 字段信息页面 START --------------------------- */
	// 初始化字段信息页面
	function initFieldTInfoPage(){
		// 初始化查询条件
		// 补录数据源
		$("#datasourceEdit"+operationFlg).unbind("change");
		$("#datasourceEdit"+operationFlg).change(function(){
			httpUtil.ajax("post","cusReportAddController/getTableInfo",{"datasource":$("#datasourceEdit"+operationFlg).val()},function(result){
				var optionTemplate = "<option value='&1'>&1</option>";
				var options = "";
				for (var resultIndex in result) {
					if (result[resultIndex].TABLE_NO != null && result[resultIndex].TABLE_NO != "") {
						options += optionTemplate.replaceAll('&1',result[resultIndex].TABLE_NO);
					}
				}
				if (options != "") {
					$("#tablesEdit"+operationFlg).html('<option value="">-- 请选择 --</option>'+options);
				}
			});
		});
		// 补录表
		$("#tablesEdit"+operationFlg).unbind("change");
		$("#tablesEdit"+operationFlg).change(function(){fieldInfoBTableTemplate
			// 展示、查询从补录表中
			fieldSearch(true,true);
		});
		
		// 展示、根据数据配置显示
		fieldSearch(true);
	}
	
	/**
	 * 字段搜索
	 * @param isFlushTable 是否刷新前台表格(默认不刷新)
	 * @param isSynchronizedWithTable 是否同步补录表信息中的字段信息（若不同步则根据当前数据配置-字段信息初始化表格显示；默认不同步）
	 */
	function fieldSearch(isFlushTable,isSynchronizedWithTable){
		isFlushTable = !(isFlushTable == undefined || isFlushTable === false);
		isSynchronizedWithTable = !(isSynchronizedWithTable == undefined || isSynchronizedWithTable === false);
		
		var dataConfigs = $("#dataConfigs"+operationFlg).val().length > 0 ? JSON.parse($("#dataConfigs"+operationFlg).val()) : objectUtil.deepCopy(dataConfigsEmpty);
		// 同步所选的补录表信息中的字段信息
		if (isSynchronizedWithTable) {
			// 清空字段信息数据
			eval("dataConfigs."+$("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-this").attr("lay-id")+" = []");
			// 字段信息
			httpUtil.ajax("post","cusReportAddController/getTableInfo",{"datasource":$("#datasourceEditTemplate").val(),"tableNo":$("#tablesEditTemplate").val()},function(result){
				var fieldInfoOne = null;
				var field = null;
				for (var fieldIndex = 0; fieldIndex < JSON.parse(result[0].DATA_CONFIG).fieldInfo.length; fieldIndex++) {
					fieldInfoOne = objectUtil.deepCopy(fieldInfoDefalut);
					field = JSON.parse(result[0].DATA_CONFIG).fieldInfo[fieldIndex];
					fieldInfoOne.fieldIdEditTemplate = field.fieldIdEditTable;
					fieldInfoOne.fieldNameEditTemplate = field.fieldNameEditTable;
					fieldInfoOne.fieldTypeEditTemplate = field.fieldTypeEditTable;
					fieldInfoOne.fieldLengthEditTemplate = field.fieldLengthEditTable;
					fieldInfoOne.fieldDecimalPlaceEditTemplate = field.fieldDecimalPlaceEditTable;
					fieldInfoOne.fieldAllowNullEditTemplate = field.fieldAllowNullEditTable;
					eval("dataConfigs."+$("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-this").attr("lay-id")+".push(fieldInfoOne)");
				}
			},false);
		}
		$("#dataConfigs"+operationFlg).val(JSON.stringify(dataConfigs));
		
		// 刷新前台表格
		if (isFlushTable) initTableInfoBTable("fieldInfoBTableTemplate","fieldTInfo");
	}
	/**
	 * 删除弹框内表格数据
	 * @param infoId 要删除记录的记录ID
	 */
	function fieldDelete(recordId){
		var dataConfigs = JSON.parse($("#dataConfigs"+operationFlg).val());
		for (var index = 0; index < dataConfigs.fieldTInfo.length; index++) {
			if (dataConfigs.fieldTInfo[index].fieldIdEditTemplate == recordId) {
				dataConfigs.fieldTInfo.remove(dataConfigs.fieldTInfo[index]);
				break;
			}
		}
		$("#dataConfigs"+operationFlg).val(JSON.stringify(dataConfigs));
		initTableInfoBTable("fieldInfoBTableTemplate","fieldTInfo")
	}
	/** --------------------------- 字段信息页面 END --------------------------- */
/** ----------------- 模板定制弹框-内部按钮 END ----------------- */
	
/** ----------------- 补录表弹框-基础操作 START ----------------- */
	// 根据指定的标签页layId，显示、隐藏按钮
	function initDialogButtons(layId){
		if (layId == "baseTInfo") {
			// 基本信息
			$("#saveData"+operationFlg).hide();
			$("#preStep"+operationFlg).hide();
			$("#nextStep"+operationFlg).show();

			initBaseTInfoPage();
		} else if (layId == "fieldTInfo") {
			// 字段信息
			$("#saveData"+operationFlg).hide();
			$("#preStep"+operationFlg).show();
			$("#nextStep"+operationFlg).show();
			
			initFieldTInfoPage();
		} else if (layId == "templateManager") {
			// 补录模板
			$("#saveData"+operationFlg).show();
			$("#preStep"+operationFlg).show();
			$("#nextStep"+operationFlg).hide();
		}
	}
	
	// 临时保存数据配置到页面隐藏域
	function saveTempDataConfigs(){
		var dataConfigs = $("#dataConfigs"+operationFlg).val().length > 0 ? JSON.parse($("#dataConfigs"+operationFlg).val()) : objectUtil.deepCopy(dataConfigsEmpty);
		$.each($("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-tab-item.layui-show").find(".content").children(".input-tag"),function(){
			var value = JSON.stringify($(this).val());
			// checkbox的取值方法与一般input不同
			if ($(this).context.tagName == "INPUT" && $(this).context.type == "checkbox") {
				value = $("#"+$(this).attr("id")+":checked").size();
			}
			
			// 特殊字段处理
			// 定义权限
			if ($(this).attr("name") == "AUTHORITY") {
				value = JSON.stringify($(this).val() == "1" ? $(this).val()+"_"+$("#authorityJGEditTemplate").val() : $(this).val());
			}
			eval("dataConfigs."+$("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-this").attr("lay-id")+"."+$(this).attr("name")+"="+value);
		});
		$("#dataConfigs"+operationFlg).val(JSON.stringify(dataConfigs));
	}
	
	// 弹框上一步
	function preStep(){
		// 临时保存数据配置到页面隐藏域
		saveTempDataConfigs();

		// 上一步
		var $layuiThis = $("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-this");
		if ($layuiThis.prev().size() > 0) {
			$layuiThis.prev().addClass("layui-this");
			$layuiThis.removeClass("layui-this");
			// 变更显示的标签页
			var $layuiShow = $("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-tab-item.layui-show");
			$layuiShow.prev().addClass("layui-show");
			$layuiShow.removeClass("layui-show");
			// 根据显示的标签页，显示、隐藏按钮
			$layuiThis = $layuiThis.prev();
		}
		
		initDialogButtons($layuiThis.attr("lay-id"));
	}
	
	// 弹框下一步
	function nextStep(){
		// 临时保存数据配置到页面隐藏域
		saveTempDataConfigs();

		// 下一步
		var $layuiThis = $("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-this");
		if ($layuiThis.next().size() > 0) {
			$layuiThis.next().addClass("layui-this");
			$layuiThis.removeClass("layui-this");
			// 变更显示的标签页
			var $layuiShow = $("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-tab-item.layui-show");
			$layuiShow.next().addClass("layui-show");
			$layuiShow.removeClass("layui-show");
			// 根据显示的标签页，显示、隐藏按钮
			$layuiThis = $layuiThis.next();
		}
		
		initDialogButtons($layuiThis.attr("lay-id"));
	}
	
	// 保存数据配置
	function saveData(){
		var dataConfigs = JSON.parse($("#dataConfigs"+operationFlg).val());
//		var params = {
//			"table_no":dataConfigs.baseInfo.TABLE_NO,
//			"table_name":dataConfigs.baseInfo.TABLE_NAME,
//			"datasource":dataConfigs.baseInfo.DATASOURCE,
//			"data_config":JSON.stringify(dataConfigs),
//			"createTableSql":dataConfigs.generateTable.createTableSqlEditTable
//		};
//		// 保存补录表信息
//		httpUtil.ajax("post","cusReportAddController/saveTableInfo",params,function(result){
//			if (result == null || result.data == null || result.data <= 0) {
//				$.messager.alert('消息','保存补录表信息失败！','error');
//			} else {
//				// 保存补录表信息成功，创建补录表
//				params.data_config = null;//该项未在下面的接口使用，减少数据传输量
//				httpUtil.ajax("post","cusReportAddController/createTable",params,function(result){
//					if (result == null || result.data == null || result.data.flg == null || result.data.flg == false) {
//						$.messager.alert('消息','补录表信息已保存，但创建补录表失败：\n'+result.data.msg,'error');
//					} else {
//						$.messager.alert('消息','创建补录表成功！');
//						doSearch($("#dg"+operationFlg));
//						$('#dlg'+operationFlg).dialog('close');
//					}
//				});
//			}
//		});
	}
/** ----------------- 补录表弹框-基础操作 END ----------------- */
	
	//查询Template
	function doSearch(grid){
		//遮罩层开始
		var index = layer.load(1, {
			shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
		});
		var data = {
			templateName_like: $('#templateName_like').val(),
			tableName_like: $('#tableName_like').val()
		}
		
		var IsCheckFlag = true;
		$("#myTable"+operationFlg).css("display", "block");
		
		grid.datagrid({
			url:"/currencySys/cusReportAddController/getTemplateInfo",
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
				if(ddata.rows.length <= 0){
					$("#dg"+operationFlg).prev().find('tr[datagrid-row-index="0"]').find('td[field="ck"]').hide();
					$("#dg"+operationFlg).prev().find('tr[datagrid-row-index="0"]').find('td[field="cuozuo"]').hide();
					$("#dg"+operationFlg).prev().find('tr[class ="datagrid-header-row"]').find('td[field="ck"]').hide();
					$("#dg"+operationFlg).prev().find('tr[class ="datagrid-header-row"]').find('td[field="cuozuo"]').hide();
				}
				layer.close(index);
			},
			onSelect: function (rowIndex, rowData) {
		        if (!IsCheckFlag) {
		            IsCheckFlag = true;
		            grid.datagrid("unselectRow", rowIndex);
		        }
		    },                    
		    onUnselect: function (rowIndex, rowData) {
		        if (!IsCheckFlag) {
		            IsCheckFlag = true;
		            grid.datagrid("selectRow", rowIndex);
		        }
		    },
			onClickCell:function(index,field,value){
				editIndex = index;
				if(field == 'cuozuo'){
					dataIndex = index;
					grid.datagrid('selectRow', index);
					var row = grid.datagrid('getSelected');
					//hidden
					$('#last_row').val(JSON.stringify([row]));
					if (row){
						$('#insertData'+operationFlg).attr("style","display:none;");
						$('#saveData'+operationFlg).attr("style","display:block;");
						$('#saveData'+operationFlg).attr("style","width:90px;");
						$('#dlg'+operationFlg).dialog('open').dialog('setTitle','编辑 '+$("li[lay-id='"+operationFlg+"']").html());
						$('[name="fm'+operationFlg+'"').form('load',[]);
						$('[name="fm'+operationFlg+'"').form("reset");
						// 初始化定义权限、机构的显示：若值形如 1_nbjgh1,nbjgh2,nbjgh3，则定义权限选为机构，机构内容根据"_"后值显示
						var rowCopy = objectUtil.deepCopy(row);
						if (row.AUTHORITY != null && row.AUTHORITY != "" && row.AUTHORITY.substring(0,1) == "1") {
							rowCopy.AUTHORITY = row.AUTHORITY.substring(0,1);
							rowCopy.authorityJGEditTemplate = row.AUTHORITY.substring(2,row.AUTHORITY.length);
						}
						$('[name="fm'+operationFlg+'"').form('load',rowCopy);
						// 编辑弹窗数据初始化
						$("#dataConfigs"+operationFlg).val(row.DATA_CONFIG);
						// 重置选项卡
						for (var index = 0; index < 4; index++) {
							preStep();
						}
					}
				}else{
					IsCheckFlag = false;
				}
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
			},
			buttons:[{
		    	text:'删除',
				handler:function(){
					$.messager.confirm('删除', '您确定要删除吗？', function(r){
						if (r){
							//获取选中的数据
							var delData = grid.datagrid('getSelections');
							if (delData.length <= 0){
								$.messager.alert('删除','请先选择数据!','error');
								return;
							}else{
								//删除数据
								var d_da = {
									deleted:JSON.stringify(delData)
								}
								$.ajax({
									type:"post",
									url:"/currencySys/cusReportAddController/deleteTemplateInfo",
									data:d_da,
									success:function(data){
										if(data == "" && data.length <= 0){
											return;
										}
										data = JSON.parse(data);
										if(data.data > 0){
											var num = 0;
											$("#dg"+operationFlg).prev().find('table[class="datagrid-btable"]').find("tr").each(function(index,obj){
												var ckInput = $(obj).find('td[field="ck"]').eq(0).find('input').prop('checked');
												if(ckInput){
													grid.datagrid('deleteRow', (index - num));
													//解决删除过之后 索引改变
													num++;
												}
							                });
											editIndex = undefined;
											doSearch(grid)
											$.messager.alert('消息','删除成功！'); 
										}else{
											$.messager.alert('消息','删除失败！'); 
										}
									}
								});
							}
						}
					});
				}
			},{
		    	text:'新增',
				handler:function(){
					$('#saveData'+operationFlg).attr("style","display:none;");
					$('#insertData'+operationFlg).attr("style","display:block;");
					$('#insertData'+operationFlg).attr("style","width:90px;");
					$('#dlg'+operationFlg).dialog('open').dialog('setTitle','新增 '+$("li[lay-id='"+operationFlg+"']").html());
					$('[name="fm'+operationFlg+'"').form('load',[]);
					$('[name="fm'+operationFlg+'"').form("reset");
					// 弹框内表格初始化
					$("#dataConfigs"+operationFlg).val("");
					// 重置选项卡
					for (var index = 0; index < 4; index++) {
						preStep();
					}
				}
			}]
		});
	}
});