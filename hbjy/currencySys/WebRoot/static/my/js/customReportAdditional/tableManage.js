define(function(require) {
	require('jquery');
	require('jqueryEasyUi');
	require('layer');
	require('layui');
	require('element');
	require('JsExcelXml');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	var url = $("#url").val();
	var msgUtil = new MsgUtil();
	var httpUtil = new HttpUtil();
	var objectUtil = new ObjectUtil();
	var dataIndex = "";//表格索引
	var editIndex = undefined;
	var operationFlg = "Table";//当前功能的标识
	
	// 补录表数据配置初始值
	var dataConfigsEmpty = {
			baseInfo:{DATASOURCE:"",TABLE_NO:"",TABLE_NAME:""},
			fieldInfo:[],
			keyIndexInfo:[],
			generateTable:{createTableSqlEditTable:""}
	};
	// 补录表数据配置默认值
	var fieldInfoDefalut = {fieldIdEditTable:"",fieldNameEditTable:"",fieldTypeEditTable:"",fieldLengthEditTable:0,fieldDecimalPlaceEditTable:0,fieldDefaultEditTable:"",fieldAllowNullEditTable:0};
	var keyIndexInfoDefalut = {keyIndexTypeEditTable:"",keyIndexFieldEditTable:""};
	var generateTableInfoDefalut = "create table &dbuser.&tableNo\n(\n DATAINPUT_ID VARCHAR2(32) not null  ,\n&fields SYS_DATA_DATE VARCHAR2(20) not null  ,\n SYS_DATA_CASE VARCHAR2(32) not null  ,\n SYS_OPERATOR VARCHAR2(256) ,\n SYS_OPER_ORG VARCHAR2(32) not null  ,\n SYS_OPER_DATE VARCHAR2(20) ,\n SYS_DATA_STATE VARCHAR2(10) ,\n SYS_ORDER_NO NUMBER(19,0) ,\n ERROR_MARK VARCHAR2(2) ,\n FLOW_NODE_ID VARCHAR2(32) , \n COMMENTS VARCHAR2(500) \n);\n&tableComment\n&fieldCommentscomment on column &dbuser.&tableNo.SYS_DATA_DATE is '数据日期';\ncomment on column &dbuser.&tableNo.SYS_DATA_CASE is '任务实例ID';\ncomment on column &dbuser.&tableNo.SYS_OPERATOR is '补录人员';\ncomment on column &dbuser.&tableNo.SYS_OPER_ORG is '补录机构';\ncomment on column &dbuser.&tableNo.SYS_OPER_DATE is '操作日期';\ncomment on column &dbuser.&tableNo.SYS_DATA_STATE is '数据状态:save(已保存),submit(已提交),reject(驳回),sucess(审核通过),refuse(已回退)';\ncomment on column &dbuser.&tableNo.SYS_ORDER_NO is '序号';\n&uniques&indexs&primaryKey";
	var fieldsGenerateTableDefalut = " &fieldId &fieldType &defalut &allowNull  ,\n";//not null
	var fieldCommentsGenerateTableDefalut = "comment on column &dbuser.&tableNo.&fieldId is '&fieldName';\n";
	var uniquesGenerateTableDefalut = "alter table &dbuser.&tableNo add constraint UNQ_&unqcount&tableNo unique (&unqFields,SYS_DATA_DATE,SYS_DATA_CASE,SYS_OPER_ORG,SYS_ORDER_NO) using index;\n";
	var indexsGenerateTableDefalut = "create index &dbuser.&tableNo on &tableNo (&indexFields,SYS_DATA_DATE,SYS_DATA_CASE,SYS_OPER_ORG,SYS_ORDER_NO);\n";
	
	$(function() {
		//表格ID
		var grid = $("#dg"+operationFlg);
		
		// 初始化查询条件
		// 数据源
		httpUtil.ajax("post","cusReportAddController/getDatasourceInfo",{},function(result){
			var optionTemplate = "<option value='&1'>&1</option>";
			var options = "";
			for (var resultIndex in result) {
				if (result[resultIndex].DNAME != null && result[resultIndex].DNAME != "") {
					options += optionTemplate.replaceAll('&1',result[resultIndex].DNAME);
				}
			}
			if (options != "") {
				$("#datasource").append(options);
				$("#DATASOURCE").append(options);
				$("#datasourceEditTemplate").append(options);
			}
		});
		
		//查询
		$("#doSearch"+operationFlg).click(function(){
			doSearch(grid);
		});
		
		// 补录表弹框-基础操作------
//		// 编辑保存
//		$("#saveData"+operationFlg).click(function(){
//			saveData();
//		});
//		//insert数据 到数据库中
//		$("#insertData"+operationFlg).click(function(){
//			insertData();
//		});
		// 创建补录表
		$("#createTable"+operationFlg).click(function(){
			createTable();
		});
		// 上一步
		$("#preStep"+operationFlg).click(function(){
			preStep();
		});
		// 下一步
		$("#nextStep"+operationFlg).click(function(){
			nextStep();
		});
		
		// 补录表弹框-内部按钮------
		// 字段信息-新增
		$("#fieldAdd"+operationFlg).click(function(){
			fieldAdd();
		});
		// 字段信息-修改
		$("#fieldEdit"+operationFlg).click(function(){
			fieldEdit();
		});
		// 字段信息-从表中导入
		$("#fieldAddFromTable"+operationFlg).click(function(){
			fieldAddFromTable();
		});
		// 主键索引-新增
		$("#keyIndexAdd"+operationFlg).click(function(){
			keyIndexAdd();
		});
		// 主键索引-修改
		$("#keyIndexEdit"+operationFlg).click(function(){
			keyIndexEdit();
		});
		
		
		// 初始查询
		doSearch(grid);
	});
	
/** ----------------- 补录表弹框-内部按钮 START ----------------- */
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
					} else if ($(this).attr("field") == "fieldAllowNullEditTable") {
						// 可否为空
						replaceValue = ">"+(eval("tableData[index]."+$(this).attr("field")) == 1 ? "是" : "否");
					} else if ($(this).attr("field") == "keyIndexTypeEditTable") {
						// 可否为空
						replaceValue = ">"+(eval("tableData[index]."+$(this).attr("field")) == "unique" ? "唯一性(unique)" : (eval("tableData[index]."+$(this).attr("field")) == "index" ? "索引(index)" : ""));
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
			if (bTableId.replaceAll("BTableTable","") == "fieldInfo") {
				fieldDelete($(this).parent().parent().children().eq(1).html());
			} else if (bTableId.replaceAll("BTableTable","") == "keyIndexInfo") {
				keyIndexDelete($(this).parent().parent().children().eq(1).html(),$(this).parent().parent().children().eq(2).html());
			}
		});
	}
	
	/** --------------------------- 字段信息页面 START --------------------------- */
	// 字段新增
	function fieldAdd(){
		// TODO 做校验
		// 字段信息
		var fieldInfoOne = objectUtil.deepCopy(fieldInfoDefalut);
		var dataConfigs = $("#dataConfigs"+operationFlg).val().length > 0 ? JSON.parse($("#dataConfigs"+operationFlg).val()) : objectUtil.deepCopy(dataConfigsEmpty);
		$.each($("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-tab-item.layui-show").find(".content").children(".input-tag-table"),function(){
			var value = JSON.stringify($(this).val());
			if ($(this).context.tagName == "INPUT" && $(this).context.type == "checkbox") {
				value = $("#"+$(this).attr("id")+":checked").size();
			}
			eval("fieldInfoOne."+$(this).attr("name")+"="+value);
		});
		eval("dataConfigs."+$("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-this").attr("lay-id")+".push(fieldInfoOne)");
		$("#dataConfigs"+operationFlg).val(JSON.stringify(dataConfigs));
		initTableInfoBTable("fieldInfoBTableTable","fieldInfo");
	}
	// 字段编辑
	function fieldEdit(){
	}
	// 字段从表中导入
	function fieldAddFromTable(){
	}
	/**
	 * 删除弹框内表格数据
	 * @param infoId 要删除记录的记录ID
	 */
	function fieldDelete(recordId){
		var dataConfigs = JSON.parse($("#dataConfigs"+operationFlg).val());
		for (var index = 0; index < dataConfigs.fieldInfo.length; index++) {
			if (dataConfigs.fieldInfo[index].fieldIdEditTable == recordId) {
				dataConfigs.fieldInfo.remove(dataConfigs.fieldInfo[index]);
				break;
			}
		}
		$("#dataConfigs"+operationFlg).val(JSON.stringify(dataConfigs));
		initTableInfoBTable("fieldInfoBTableTable","fieldInfo");
	}
	/** --------------------------- 字段信息页面 END --------------------------- */
	
	/** --------------------------- 主键索引页面 START --------------------------- */
	// 初始化主键索引页面
	function initKeyIndexPage(){
		var dataConfigs = $("#dataConfigs"+operationFlg).val().length > 0 ? JSON.parse($("#dataConfigs"+operationFlg).val()) : objectUtil.deepCopy(dataConfigsEmpty);
		// 初始化约束字段下拉框
		var optionTemplate = "<option value='&1'>&2(&3)</option>";
		var options = "";
		for (var index = 0; index < dataConfigs.fieldInfo.length; index++) {
			options += optionTemplate.replaceAll('&1',dataConfigs.fieldInfo[index].fieldIdEditTable)
				.replaceAll('&2',dataConfigs.fieldInfo[index].fieldNameEditTable).replaceAll('&3',dataConfigs.fieldInfo[index].fieldIdEditTable);
		}
		if (options != "") {
//			"<option value=''>-- 请选择 --</option>"+
			$("#keyIndexFieldEditTable").html(options);
		}
		// 加载多选下拉框
		$('#keyIndexFieldEditTable').combobox({
			multiple:true,
		});
		
		// 初始化表格数据
		initTableInfoBTable("keyIndexInfoBTableTable","keyIndexInfo");
	}
	// 主键索引新增
	function keyIndexAdd(){
		// TODO 做校验
		// 字段信息
		var keyIndexInfoOne = objectUtil.deepCopy(keyIndexInfoDefalut);
		var dataConfigs = $("#dataConfigs"+operationFlg).val().length > 0 ? JSON.parse($("#dataConfigs"+operationFlg).val()) : objectUtil.deepCopy(dataConfigsEmpty);
		$.each($("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-tab-item.layui-show").find(".content").children(".input-tag-table"),function(){
			var value = JSON.stringify($(this).val());
			if ($(this).context.tagName == "INPUT" && $(this).context.type == "checkbox") {
				value = $("#"+$(this).attr("id")+":checked").size();
			} else if ($(this).context.tagName == "SELECT" && $(this).hasClass("combobox-f")) {
				// easyui combobox
				value = JSON.stringify($(this).combobox('getValues').join());
			}
			eval("keyIndexInfoOne."+($(this).attr("name") != null ? $(this).attr("name") : $(this).attr("id"))+"="+value);
		});
		eval("dataConfigs."+$("div[lay-filter='edit"+operationFlg+"TabBrief']").find(".layui-this").attr("lay-id")+".push(keyIndexInfoOne)");
		$("#dataConfigs"+operationFlg).val(JSON.stringify(dataConfigs));
		initTableInfoBTable("keyIndexInfoBTableTable","keyIndexInfo");
	}
	// 主键索引编辑
	function keyIndexEdit(){
	}
	/**
	 * 主键索引删除
	 * @param type 约束类型
	 * @param field 约束字段
	 */ 
	function keyIndexDelete(type,field){
		var dataConfigs = JSON.parse($("#dataConfigs"+operationFlg).val());
		for (var index = 0; index < dataConfigs.keyIndexInfo.length; index++) {
			if (type.indexOf(dataConfigs.keyIndexInfo[index].keyIndexTypeEditTable) > 0 && dataConfigs.keyIndexInfo[index].keyIndexFieldEditTable == field) {
				dataConfigs.keyIndexInfo.remove(dataConfigs.keyIndexInfo[index]);
				break;
			}
		}
		$("#dataConfigs"+operationFlg).val(JSON.stringify(dataConfigs));
		initTableInfoBTable("keyIndexInfoBTableTable","keyIndexInfo");
	}
	/** --------------------------- 主键索引页面 END --------------------------- */
	
	/** --------------------------- 生成表页面 START --------------------------- */
	// 初始化生成表页面
	function initGenerateTablePage(){
		var dataConfigs = JSON.parse($("#dataConfigs"+operationFlg).val());
		// 生成表sql
		var generateTableInfoDefalutTemp = "";
		var fieldCommentsGenerateTableDefalutTemp = "";
		var uniquesGenerateTableDefalutTemp = "";
		var indexsGenerateTableDefalutTemp = "";
		// 数据库user
		var dbuser = dataConfigs.baseInfo.DATASOURCE;
		// 表名
		var tableNo = dataConfigs.baseInfo.TABLE_NO;
		// 表注释
		var tableName = dataConfigs.baseInfo.TABLE_NAME;
		// 表注释sql
		var tableComment = "comment on table &dbuser.&tableNo is '&tableName';";
		// 主键sql
		var primaryKey = "alter table &dbuser.&tableNo add constraint pk_&tableNo primary key (DATAINPUT_ID) using index;";
		// 字段sql
		var fields = "";
		// 字段注释sql
		var fieldComments = "";
		// 唯一性约束sql
		var uniques = "";
		// 索引sql
		var indexs = "";
		
		// 拼接sql
		tableComment = tableComment.replaceAll("&dbuser",dbuser).replaceAll("&tableNo",tableNo).replaceAll("&tableName",tableName);
		primaryKey = primaryKey.replaceAll("&dbuser",dbuser).replaceAll("&tableNo",tableNo).replaceAll("&tableName",tableName);
		generateTableInfoDefalutTemp = generateTableInfoDefalut.replaceAll("&dbuser",dbuser).replaceAll("&tableNo",tableNo);
		fieldCommentsGenerateTableDefalutTemp = fieldCommentsGenerateTableDefalut.replaceAll("&dbuser",dbuser).replaceAll("&tableNo",tableNo);
		uniquesGenerateTableDefalutTemp = uniquesGenerateTableDefalut.replaceAll("&dbuser",dbuser).replaceAll("&tableNo",tableNo);
		indexsGenerateTableDefalutTemp = indexsGenerateTableDefalut.replaceAll("&dbuser",dbuser).replaceAll("&tableNo",tableNo);
		// 字段相关sql拼接
		var fieldInfo = null;
		for (var index = 0; index < dataConfigs.fieldInfo.length; index++) {
			fieldInfo = dataConfigs.fieldInfo[index];
			var fieldType = fieldInfo.fieldTypeEditTable;
			// 默认值：DATE/TIMESTAMP：default sysdate；INTEGER、NUMBER：12；CHAR、VARCHAR2：'a1'；
			var fieldDefalut = "";
			if (fieldInfo.fieldDefaultEditTable != null && fieldInfo.fieldDefaultEditTable != "") {
				fieldDefalut = "default ";
				if (fieldType == "DATE" || fieldType == "TIMESTAMP") {
					fieldDefalut += "sysdate";
				} else if (fieldType == "NUMBER" || fieldType == "INTEGER") {
					fieldDefalut += fieldInfo.fieldDefaultEditTable;
				} else {
					fieldDefalut += "'"+fieldInfo.fieldDefaultEditTable+"'";
				}
			}
			// 字段类型：DATE、INTEGER、NUMBER(5,1)、CHAR(1)、VARCHAR2(1)、TIMESTAMP(1)
			if (fieldType == "DATE" || fieldType == "INTEGER") {
				fieldType += "";
			} else if (fieldType == "NUMBER") {
				fieldType += "("+parseInt(fieldInfo.fieldLengthEditTable == "" || fieldInfo.fieldLengthEditTable == null ? "0" : fieldInfo.fieldLengthEditTable)+","
					+parseInt(fieldInfo.fieldDecimalPlaceEditTable == "" || fieldInfo.fieldDecimalPlaceEditTable == null ? "0" : fieldInfo.fieldDecimalPlaceEditTable)+")";
			} else {
				fieldType += "("+parseInt(fieldInfo.fieldLengthEditTable == "" || fieldInfo.fieldLengthEditTable == null ? "0" : fieldInfo.fieldLengthEditTable)+")";
			}
			// 拼接字段sql
			fields += fieldsGenerateTableDefalut.replaceAll("&fieldId",fieldInfo.fieldIdEditTable)
				.replaceAll("&fieldType",fieldType).replaceAll("&defalut",fieldDefalut).replaceAll("&allowNull",fieldInfo.fieldAllowNullEditTable == 0 ? "not null" : "");
			// 拼接字段注释sql
			fieldComments += fieldCommentsGenerateTableDefalutTemp.replaceAll("&fieldId",fieldInfo.fieldIdEditTable).replaceAll("&fieldName",fieldInfo.fieldNameEditTable);
		}
		// 字段相关sql拼接
		var keyIndexInfo = null;
		for (var index = 0; index < dataConfigs.keyIndexInfo.length; index++) {
			keyIndexInfo = dataConfigs.keyIndexInfo[index];
			if (keyIndexInfo.keyIndexTypeEditTable == "unique") {
				// 拼接唯一性约束sql
				uniques += uniquesGenerateTableDefalutTemp.replaceAll("&unqcount",index).replaceAll("&unqFields",keyIndexInfo.keyIndexFieldEditTable);
			} else if (keyIndexInfo.keyIndexTypeEditTable == "index") {
				// 拼接索引sql
				indexs = indexsGenerateTableDefalutTemp.replaceAll("&indexFields",keyIndexInfo.keyIndexFieldEditTable);
			}
		}
		dataConfigs.generateTable.createTableSqlEditTable = generateTableInfoDefalutTemp.replaceAll("&dbuser",dbuser).replaceAll("&tableNo",tableNo).replaceAll("&tableComment",tableComment).replaceAll("&fields",fields)
			.replaceAll("&fieldComments",fieldComments).replaceAll("&uniques",uniques).replaceAll("&indexs",indexs).replaceAll("&primaryKey",primaryKey);
		$("#dataConfigs"+operationFlg).val(JSON.stringify(dataConfigs));
		$("#createTableSqlEditTable").val(dataConfigs.generateTable.createTableSqlEditTable);
	}
	/** --------------------------- 生成表页面 END --------------------------- */
/** ----------------- 补录表弹框-内部按钮 END ----------------- */
	
/** ----------------- 补录表弹框-基础操作 START ----------------- */
	// 根据指定的标签页layId，显示、隐藏按钮
	function initDialogButtons(layId){
		if (layId == "baseInfo") {
			// 基本信息表
			$("#createTable"+operationFlg).hide();
			$("#preStep"+operationFlg).hide();
			$("#nextStep"+operationFlg).show();
		} else if (layId == "fieldInfo") {
			// 字段信息
			$("#createTable"+operationFlg).hide();
			$("#preStep"+operationFlg).show();
			$("#nextStep"+operationFlg).show();
			
			initTableInfoBTable("fieldInfoBTableTable","fieldInfo");
		} else if (layId == "keyIndexInfo") {
			// 主键索引
			$("#createTable"+operationFlg).hide();
			$("#preStep"+operationFlg).show();
			$("#nextStep"+operationFlg).show();
			
			initKeyIndexPage();
		} else if (layId == "generateTable") {
			// 生成表
			$("#createTable"+operationFlg).show();
			$("#preStep"+operationFlg).show();
			$("#nextStep"+operationFlg).hide();
			
			initGenerateTablePage();
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
		var $layuiThis = $("div[lay-filter='editTableTabBrief']").find(".layui-this");
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
	
	// 创建补录表
	function createTable(){
		var dataConfigs = JSON.parse($("#dataConfigs"+operationFlg).val());
		var params = {
			"table_no":dataConfigs.baseInfo.TABLE_NO,
			"table_name":dataConfigs.baseInfo.TABLE_NAME,
			"datasource":dataConfigs.baseInfo.DATASOURCE,
			"data_config":JSON.stringify(dataConfigs),
			"createTableSql":dataConfigs.generateTable.createTableSqlEditTable
		};
		// 保存补录表信息
		httpUtil.ajax("post","cusReportAddController/saveTableInfo",params,function(result){
			if (result == null || result.data == null || result.data <= 0) {
				$.messager.alert('消息','保存补录表信息失败！','error');
			} else {
				// 保存补录表信息成功，创建补录表
				params.data_config = null;//该项未在下面的接口使用，减少数据传输量
				httpUtil.ajax("post","cusReportAddController/createTable",params,function(result){
					if (result == null || result.data == null || result.data.flg == null || result.data.flg == false) {
						$.messager.alert('消息','补录表信息已保存，但创建补录表失败：\n'+result.data.msg,'error');
					} else {
						$.messager.alert('消息','创建补录表成功！');
						doSearch($("#dg"+operationFlg));
						$('#dlg'+operationFlg).dialog('close');
					}
				});
			}
		});
	}
	
	// 新增保存数据
	function insertData(){
		$.messager.alert('消息','新增保存数据'+operationFlg); 
		$('#dlg'+operationFlg).dialog('close');
	}
	
	// 编辑保存数据
	function saveData(){
		$.messager.alert('消息','编辑保存数据'+operationFlg);
		$('#dlg'+operationFlg).dialog('close');
//		$('#fm'+operationFlg).form('submit', {  
//	        url:'/currencySys/manaProController/updateData',  
//	        onSubmit: function(){  
//	                if($('#fm'+operationFlg).form('validate')) {
//	                    return true  
//	                }else {	
//	                    return false;  
//	                } 
//	            },  
//	        success:function(data){  
//	            var obj = jQuery.parseJSON(data); 
//	            if(obj.code == 1000){  
//	            	if(obj.data > 0){
//	            		$.messager.alert('消息','保存成功！'); 
//	            	}else{
//	            		$.messager.alert('消息','保存失败！'); 
//	            	}
//	                $('#dlg'+operationFlg).dialog('close');
//	                $('#dg'+operationFlg).datagrid('reload'); 
//	                $('tr[datagrid-row-index="'+dataIndex+'"]').find('td').each(function(ind,o){
//	                	var fmValue = $('#fm'+operationFlg).serialize().split('&');//获取fm表单数据
//	                	if(ind > 2){
//	                		if(ind <= fmValue.length){
//	                			$(o).find('div').html(fmValue[ind - 2].substring((fmValue[ind - 2].indexOf("=")+1),fmValue[ind - 2].length));
//	                		}
//	                	}
//	                });
//	                $('#fm'+operationFlg).form('reset');  
//	            }else{  
//	                $.messager.alert('消息','保存失败！');  
//	            }  
//	        }  
//	    });
	}
/** ----------------- 补录表弹框-基础操作 END ----------------- */

/** ----------------- 补录表主页面 START ----------------- */
	//查询Table
	function doSearch(grid){
		//遮罩层开始
		var index = layer.load(1, {
			shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
		});
		var data = {
			tableNo_like: $('#tableNo_like').val(),
			tableName_like: $('#tableName_like').val(),
			datasource: $('#datasource').val()
		}
		
		var IsCheckFlag = true;
		$("#myTable"+operationFlg).css("display", "block");
		
		grid.datagrid({
			url:"/currencySys/cusReportAddController/getTableInfo",
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
//				$("table.datagrid-htable").css("width","100%");$("table.datagrid-btable").css("width","100%");
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
						$('[name="fm'+operationFlg+'"').form('load',row);
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
//		    	$("table.datagrid-htable").css("width","100%");$("table.datagrid-btable").css("width","100%");
			},
			buttons:[{
		    	text:'删除',
				handler:function(){
					//获取选中的数据
					var delData = grid.datagrid('getSelections');
					$.messager.confirm('删除', delData.length > 1 ? '您确定同时删除这些记录中的物理表？' : '您确定同时删除该条记录中的物理表？', function(r){
						if (r){
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
									url:"/currencySys/cusReportAddController/deleteTableInfo",
									data:d_da,
									success:function(data){
										if(data == "" && data.length <= 0){
											return;
										}
										data = JSON.parse(data);
										if(data.data > 0 || data.data == -1){
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
											if (data.data == -1) {
												$.messager.alert('消息','表数据删除成功，但对应物理表删除失败！'); 
											} else {
												$.messager.alert('消息','删除成功！');
											}
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
					initTableInfoBTable("fieldInfoBTableTable","fieldInfo");
					// 重置选项卡
					for (var index = 0; index < 4; index++) {
						preStep();
					}
				}
			}]
		});
	}
/** ----------------- 补录表主页面 END ----------------- */
});

