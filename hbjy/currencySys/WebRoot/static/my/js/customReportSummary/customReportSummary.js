define(function(require){
	require('jquery');
	require('jqueryEasyUi');
	require('layer');
	require('layui');
	require('element');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	
	var url = $("#url").val();
	var objectUtil = new ObjectUtil();
	var cookieUtil = new CookieUtil();
	var positiveIntRegex = /^[1-9]\d*$/;// 正整数
	
	$(function(){
		// 初始化【汇总方式】下拉框
		$("#summaryType").combobox({
			multiple:false,
			onUnselect: function(record){
				// 隐藏取消选中的方式对应的选项
				$("#summaryType").parent().find(".summary-type-option."+record.value).hide();
			},
			onSelect: function(record){
				// 显示选中的方式对应的选项
				$("#summaryType").parent().find(".summary-type-option."+record.value).show();
			}
		});
		
		// 初始化页面
		$("#importExcel").click(function(){
			if ($("input[name='templateUpload'").val() == undefined || $("input[name='templateUpload'").val() == "") {
				$.messager.alert('消息','请选择模板文件！','error');
				return;
			} else if ($("input[name='summaryUpload'").val() == undefined || $("input[name='summaryUpload'").val() == "") {
				$.messager.alert('消息','请选择要汇总的文件！','error');
				return;
			} else if ($("#summaryType").combobox("getValue") == undefined || $("#summaryType").combobox("getValue") == "") {
				$.messager.alert('消息','请选择汇总方式！','error');
				return;
			} else if (($("#dataRowNumStart").css("display") != "none" && $("#dataRowNumEnd").css("display") != "none") 
					&& ($("#dataRowNumStart").val() == undefined || $("#dataRowNumStart").val() == "" || $("#dataRowNumEnd").val() == undefined || $("#dataRowNumEnd").val() == "")) {
				// 显示则必填
				$.messager.alert('消息','请填写数据行范围！','error');
				return;
			} else if (($("#dataRowNumStart").val() != undefined && $("#dataRowNumStart").val() != "" && $("#dataRowNumEnd").val() != undefined && $("#dataRowNumEnd").val() != "") 
					&& (!positiveIntRegex.test($("#dataRowNumStart").val()) || !positiveIntRegex.test($("#dataRowNumEnd").val()))) {
				// 数据行范围为整数
				$.messager.alert('消息','数据行范围应填写正整数！','error');
				return;
			} else if (($("#dataRowNumStart").val() != undefined && $("#dataRowNumStart").val() != "" && $("#dataRowNumEnd").val() != undefined && $("#dataRowNumEnd").val() != "") 
					&& (parseInt($("#dataRowNumStart").val()) > parseInt($("#dataRowNumEnd").val()))) {
				// 数据行范围为整数
				$.messager.alert('消息','数据行范围的开始行号应不比结束行号大！','error');
				return;
			}
			
			//遮罩层开始
			var shadeIndex = layer.load(1, {
				shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
			});
			$("#uploadExcelForm").form('submit', {
	            url: url+'cusReportSummaryController/uploadSummaryFiles',
	            success: function (result) {
	            	var resultObj = null;
	            	try {
	            		resultObj = JSON.parse(result);
	            		if (resultObj.code == "2000") {
	            			$.messager.alert('消息',resultObj.message,'error');
	            			layer.close(shadeIndex);
		            		return;
	            		} else if (resultObj.data == undefined) {
	            			$.messager.alert('消息','上传失败，请检查汇总方式是否选择正确！','error');
	            			layer.close(shadeIndex);
		            		return;
	            		}
	            	} catch (e) {
	            		$.messager.alert('消息','上传失败，请检查汇总方式是否选择正确！','error');
	            		layer.close(shadeIndex);
	            		return;
	            	}
	            	
	            	// 上传成功
	            	// 初始化【纵向汇总的列】下拉框
	        		var templateHeadKeys = Object.keys(resultObj.data.templateHeadersWithParent);
	        		var dataHeaderList = [];
	        		for (var index = 0; index < templateHeadKeys.length; index++) {
	        			var dataHeaderElem = {"id":templateHeadKeys[index],"name":resultObj.data.templateHeadersWithParent[templateHeadKeys[index]]};
	        			dataHeaderList.push(dataHeaderElem);
	        		}
	        		if (dataHeaderList != null && dataHeaderList.length > 0) {
	        			// 纵向汇总的列
	        			try {
	        				$("#verticalSummaryList").combobox('clear');// 先清除数据
	        			} catch (error) {
	        				// 加载多选下拉框
	    	        		$('#verticalSummaryList').combobox({
	    	        			textField:'name',
	    	        		    valueField:'id',
	    	        			multiple:true
	    	        		});
	    	        		// 修正宽度
        					initComboboxWidth($('#verticalSummaryList'));
	        			}
	        			$("#verticalSummaryList").combobox('loadData',dataHeaderList);
	        			// 横向汇总公式
	        			$.each(($("select[name='horizontalSummaryResult']").size() > 0 
	        					? $("select[name='horizontalSummaryResult']") : $("select[comboname='horizontalSummaryResult']")).parent().children(".horizontal-summary"),function(){
	        				try {
	        					$(this).combobox('clear');// 先清除数据
	        				} catch (error) {
	        	        		// 单选下拉框
	        					$(this).combobox({
	        						textField:'name',
		    	        		    valueField:'id',
	        	        			multiple:false
	        	        		});
	        					// 修正宽度
	        					initComboboxWidth($(this));
		        			}
	        				var dataHeaderListTemp = objectUtil.deepCopy(dataHeaderList);
	        				dataHeaderListTemp.unshift({"id":"","name":"-- 请选择 --"});
	        				$(this).combobox('loadData',dataHeaderListTemp);
	        			});
	        		}
					
					// 若用户更换了模板则清空纵向汇总公式
					if (resultObj.data.templatePath != $("#lastTemplatePath").val()) {
						$("[name='verticalFormulaResult']").parent().parent().find("input.vertical-formula").val("");
					}
	            	
	        		// 切换页面
	            	$("#templatePath").val(resultObj.data.templatePath);
	            	$("#summaryPathsJson").val(JSON.stringify(resultObj.data.summaryPaths));
	            	$("#lastTemplatePath").val(resultObj.data.templatePath);
	            	$(".upload-show").hide();
	            	$(".download-show").show();
	            	layer.close(shadeIndex);
	            }
	        });
		});
		
		$("#downExcel").click(function(){
			//遮罩层开始
			var index = layer.load(1, {
				shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
			});
			
			// 取得文件路径
			var templatePath = $("#templatePath").val();
			// 汇总文件路径
			var summaryPaths = JSON.parse($("#summaryPathsJson").val());
			// 需要纵向汇总的列
			var verticalSummaryList = []; 
			var verticalSummaryTempList = $("#verticalSummaryList").combobox('getValues').length > 0 ? 
					$("#verticalSummaryList").combobox('getValues').join().replace(new RegExp("\\d+", "g"),"").split(",") : [];
			// 去除为空的项
			for (var vsIx = 0; vsIx < verticalSummaryTempList.length; vsIx++) {
				if (verticalSummaryTempList[vsIx] != null && verticalSummaryTempList[vsIx] != "") {
					verticalSummaryList.push(verticalSummaryTempList[vsIx]);
				}
			}
			// 纵向加总的公式
			var verticalFormulaList = [];
			for (var formulaIx = 1; formulaIx <= $("input[name='verticalFormulaResult']").size(); formulaIx++) {
				// 只添加不为空的
				if (($("#verticalFormulaResult_"+formulaIx).val() == null || $("#verticalFormulaResult_"+formulaIx).val().trim().length <= 0)
						|| ($("#verticalFormulaEle_"+formulaIx+"_1").val() == null || $("#verticalFormulaEle_"+formulaIx+"_1").val().trim().length <= 0)
						|| ($("#verticalFormulaResult_"+formulaIx).hasClass("elem-range") 
								&& ($("#verticalFormulaEle_"+formulaIx+"_2").val() == null || $("#verticalFormulaEle_"+formulaIx+"_2").val().trim().length <= 0))) {
					continue;
				}
				if ($("#verticalFormulaResult_"+formulaIx).hasClass("elem-range") && parseInt($("#verticalFormulaEle_"+formulaIx+"_2").val()) >= parseInt($("#verticalFormulaEle_"+formulaIx+"_1").val())) {
					verticalFormulaList.push(
							$("#verticalFormulaResult_"+formulaIx).val()+"=SUM("+
							$("#verticalFormulaEle_"+formulaIx+"_1").val()+":"+
							$("#verticalFormulaEle_"+formulaIx+"_2").val()+")");
				} else if ($("#verticalFormulaResult_"+formulaIx).hasClass("elem-collection")) {
					verticalFormulaList.push(
							$("#verticalFormulaResult_"+formulaIx).val()+"=SUM("+
							$("#verticalFormulaEle_"+formulaIx+"_1").val().replaceAll("、",",")+")");
				}
			}
			// 横向加总的公式
			var horizontalSummaryList = [];
			var regExp = new RegExp("\\d+", "g");
			$.each($("select[comboname='horizontalSummaryResult']"), function(index,resultElem){
				if ($(resultElem).combobox("getValue") != null && $(resultElem).combobox("getValue").length > 0) {
					// 当前选项行号
					var currentRowNum = parseInt($(resultElem).prop("id").split("_")[1]);
					var horizontalSummaryFormula = "";
					$.each($("[comboname='horizontalSummaryEle_"+currentRowNum+"']"), function(index,elem){
						if ($(elem).combobox("getValue").replace(regExp,"") != null && $(elem).combobox("getValue").replace(regExp,"").length > 0) {
							horizontalSummaryFormula += $(elem).combobox("getValue").replace(regExp,"");
							horizontalSummaryFormula += "+";
						}
					});
					if (horizontalSummaryFormula.length > 0) {
						horizontalSummaryFormula = horizontalSummaryFormula.substring(0,horizontalSummaryFormula.length-1);
						horizontalSummaryList.push($(resultElem).combobox("getValue").replace(regExp,"")+"="+horizontalSummaryFormula);
					}
				}
			});
			// 另一种写法：
//			$.each($("select[comboname='horizontalSummaryResult']"), function(index,resultElem){
//				var horizontalSummaryFormula = $(resultElem).combobox("getValue").replace(/\d+/,"")+"="+$.map($.makeArray($(resultElem).parent().children("[comboname='horizontalSummaryEle_"+$(resultElem).prop("id").split("_")[1]+"']")),function(elem,index){
//					if ($(elem).combobox("getValue").length > 0) {return $(elem).combobox("getValue").replaceAll(/\d+/,"");}
//				}).join("+");
//				if (horizontalSummaryFormula.split("=")[0].length > 0 && horizontalSummaryFormula.split("=")[1].length > 0) {
//					horizontalSummaryList.push(horizontalSummaryFormula);
//				}
//			});
			// 汇总方式
			var summaryType = $("#summaryType").val();
			// 汇总方式选项
			var simpleSummaryOption = {
				"dataStartRowNum": $("#dataRowNumStart").val(),
				"dataEndRowNum": $("#dataRowNumEnd").val()
			};
			var param = {
				"templatePath": templatePath,
				"filePathList": summaryPaths,
				"verticalSummaryList": verticalSummaryList,
				"verticalFormulaList": verticalFormulaList,
				"horizontalSummaryList": horizontalSummaryList,
				"summaryType": summaryType,
				"simpleSummaryOption": simpleSummaryOption
			};
			// 特殊字符处理
			// "+"号替换成"add"
			param = JSON.stringify(param).replaceAll("\\+","add");
			// js监听下载文件是否准备好
			try {
				var downloadToken = +new Date();
				// 文件下载url
				window.location.href = url+"cusReportSummaryController/customSummaryExcel?downloadToken="+downloadToken+"&params="+encodeURI(param);
				function checkToken() {
	                var token = cookieUtil.getValue("downloadToken");
	                if (token && token == downloadToken) {
	                    clearTimeout(downloadTimer);
						layer.close(index);
	                }
	            }
				var downloadTimer = setInterval(checkToken, 1000);
			} catch (e) {
				console.error(e);
			}
			
//			// TODO 切换回上传页面（暂不考虑临时文件不删的情况）
//			$("#cancel").click();
		});
		
		$("#cancel").click(function(){
			$("#templatePath").val("");
        	$("#summaryPathsJson").val("");
			$(".upload-show").show();
        	$(".download-show").hide();
		});
	});
});

/**
 * 删除汇总选项行
 * @param type 汇总选项类型(horizontalSummary：横向汇总公式；verticalFormula：纵向汇总公式；)
 */
function removeOptionRow(type){
	var $currentRow = $("#"+type+"RowOperat").parent();
	$("#"+type+"RowOperat").parent().prev().children("br").before($("#"+type+"RowOperat"));
	$currentRow.remove();
	// 若当前只有一行，则不显示删除行按钮
	if ($("#"+type+"RowOperat").parent().parent().children("li").size() == 1) {
		$("#"+type+"RemoveRowBtn").hide();
	}
}

/**
 * 新增汇总选项行
 * @param type 汇总选项类型(horizontalSummary：横向汇总公式；verticalFormula：纵向汇总公式；)
 * @param childType 汇总选项子类型(range：纵向汇总公式-行范围；collection：纵向汇总公式-行集合；)
 */
function addOptionRow(type,childType){
	// 参数处理
	// 汇总选项子类型首字母转大写
	childType = childType != null && childType.length > 1 ? (childType.charAt(0).toUpperCase() + childType.substring(1,childType.length)) : "";
	
	// 汇总选项的ul
	var $optionUlTag = $("#"+type+"RowOperat").parent().parent();
	// 新增的li
	var $newOptionLi;
	// 新增的li的当前行号
	var newOptionLiNum = $("#"+type+"RowOperat").parent().parent().children("li").size()+1;
	// html模板
	var templateObj = {};
	// 横向汇总公式
	templateObj.horizontalSummaryRowTemplate = '<li>'
		+'<span style="color:white;">_</span>'
		+'<select id="horizontalSummaryResult_&1" name="horizontalSummaryResult" class="input-tag-table horizontal-summary" style="width: 8%;height: 30px;">'
		+'</select>'
		+'<span class="horizontal-summary-operator">=</span>'
		+'<select id="horizontalSummaryEle_&1_1" name="horizontalSummaryEle_&1" class="input-tag-table horizontal-summary" style="width: 8%;height: 30px;">'
		+'</select>'
		+'<span class="horizontal-summary-operator">+</span>'
		+'<select id="horizontalSummaryEle_&1_2" name="horizontalSummaryEle_&1" class="input-tag-table horizontal-summary" style="width: 8%;height: 30px;">'
		+'</select>'
		+'<div id="horizontalSummaryElemOperat_&1">'
		+'<a href="javascript:void(0)" onclick="javascript:addOptionElem(\'horizontalSummary\',this)" id="horizontalSummaryAddBtn_&1" class="easyui-linkbutton add-option-row-btn" data-options="iconCls:\'icon-add\'"></a>'
		+'<a href="javascript:void(0)" onclick="javascript:removeOptionElem(\'horizontalSummary\',this)" id="horizontalSummaryRemoveBtn_&1" class="easyui-linkbutton add-option-row-btn" data-options="iconCls:\'icon-remove\'"></a>'
		+'</div>'
		+'<br/>'
		+'</li>';
	// 纵向汇总公式-行范围
	templateObj.verticalFormulaRangeRowTemplate = '<li>'
		+'<span style="color:white;">_</span>'
		+'<input id="verticalFormulaResult_&1" name="verticalFormulaResult" class="vertical-formula elem-range small" placeholder="结果行号">'
		+'<span class="vertical-formula-operator">=</span>'
		+'<span class="vertical-formula-operator sum-left">SUM(</span>'
		+'<input id="verticalFormulaEle_&1_1" name="verticalFormulaEle_&1" class="vertical-formula elem-range small" placeholder="开始行号">'
		+'<span class="vertical-formula-operator">~</span>'
		+'<input id="verticalFormulaEle_&1_2" name="verticalFormulaEle_&1" class="vertical-formula elem-range small" placeholder="结束行号">'
		+'<span class="vertical-formula-operator sum-right">)</span>'
		+'<br/>'
		+'</li>';
	// 纵向汇总公式-行集合
	templateObj.verticalFormulaCollectionRowTemplate = '<li>'
		+'<span style="color:white;">_</span>'
		+'<input id="verticalFormulaResult_&1" name="verticalFormulaResult" class="vertical-formula elem-collection small" placeholder="结果行号">'
		+'<span class="vertical-formula-operator">=</span>'
		+'<span class="vertical-formula-operator sum-left">SUM(</span>'
		+'<input id="verticalFormulaEle_&1_1" name="verticalFormulaEle_&1" class="vertical-formula elem-collection big" placeholder="加总行号（以顿号分隔）">'
		+'<span class="vertical-formula-operator sum-right">)</span>'
		+'<br/>'
		+'</li>';
	// 取得模板
	var template = templateObj[type+childType+"RowTemplate"];
	if (template == null || template == "") return;
	
	// 替换&1为当前行号
	template = template.replaceAll("&1",newOptionLiNum);
	// 在汇总选项的ul最后追加template
	$optionUlTag.append(template);
	$newOptionLi = $optionUlTag.children("li:last");
	
	// 行内easyui控件初始化
	if (type == "horizontalSummary") {
		// 下拉框
		$newOptionLi.children("select.input-tag-table").combobox({
			textField:'name',
		    valueField:'id',
		    data:$optionUlTag.children("li:first").children("select.input-tag-table:eq(0)").combobox("getData"),// 数据取横向汇总公式第一行的第一个下拉框的数据
			multiple:false
		});
		// 公式项按钮
		$newOptionLi.children("#horizontalSummaryElemOperat_"+newOptionLiNum).children("a").linkbutton();
	}
	
	// 将行操作按钮移到新增的行后
	$newOptionLi.children("br").before($("#"+type+"RowOperat"));
	
	// 若当前有多行，则显示删除行按钮
	if ($("#"+type+"RowOperat").parent().parent().children("li").size() > 1) {
		$("#"+type+"RemoveRowBtn").show();
	}
}

/**
 * 增加公式项(暂时仅用于horizontalSummary)
 * @param type 汇总选项类型(horizontalSummary：横向汇总公式；verticalFormula：纵向汇总公式；)
 * @param elem 被点击的增加公式项按钮的对象
 */
function addOptionElem(type,elem){
	// 当前选项行
	var $currentOptionLi = $(elem).parent().parent();
	// 当前行号
	var currentRowNum = parseInt($(elem).prop("id").split("_")[1]);
	// 新增的项号
	var newElemNum = $currentOptionLi.children("select.input-tag-table").size()-1+1;
	// 公式项最多为6个
	if (newElemNum > 6) {
		$.messager.alert('消息','公式项最多为6个！','error');
		return;
	}
	// html模板
	var templateObj = {};
	// 横向汇总公式
	templateObj.horizontalSummaryElemTemplate = '<span class="horizontal-summary-operator">+</span>'
		+'<select id="horizontalSummaryEle_&1_&2" name="horizontalSummaryEle_&1" class="input-tag-table horizontal-summary" style="width: 8%;height: 30px;">'
		+'</select>';
	// 取得模板
	var template = templateObj[type+"ElemTemplate"];
	if (template == null || template == "") return;
	// 将&1替换为当前行号，将&2替换为当前项号
	template = template.replaceAll("&1",currentRowNum).replaceAll("&2",newElemNum);
	
	if (type == "horizontalSummary") {
		// 横向汇总公式
		// 在当前行的最后一个select之后(由于使用了easyui控件，因此需要在select之后的span之后)拼上新的公式项
		$currentOptionLi.children("select.input-tag-table:last").next().after(template);
		// 下拉框easyui控件初始化
		$currentOptionLi.children("select.input-tag-table:last").combobox({
			textField:'name',
		    valueField:'id',
		    data:$currentOptionLi.parent().children("li:first").children("select.input-tag-table:eq(0)").combobox("getData"),// 数据取横向汇总公式第一行的第一个下拉框的数据
			multiple:false
		});
	}
	
	// 若加到了6个则调整校验提示的宽度为107px
	if ($currentOptionLi.children("select.input-tag-table").size()-1 > 5) {
		$currentOptionLi.children("span.validate-msg").css("width","107px");
	}
}

/**
 * 删除公式项(暂时仅用于horizontalSummary)
 * @param type 汇总选项类型(horizontalSummary：横向汇总公式；verticalFormula：纵向汇总公式；)
 * @param elem 被点击的增加公式项按钮的对象
 */
function removeOptionElem(type,elem){
	// 当前选项行
	var $currentOptionLi = $(elem).parent().parent();
	// 当前的项号
	var currentElemNum = $currentOptionLi.children("select.input-tag-table").size()-1;
	// 公式项最少为1个
	if (currentElemNum <= 1) {
		$.messager.alert('消息','公式项最少为1个！','error');
		return;
	}
	
	if (type == "horizontalSummary") {
		// 横向汇总公式
		// easyui生成的控件
		$currentOptionLi.children("select.input-tag-table:last").next("span.combo").remove();
		// 运算符
		$currentOptionLi.children("select.input-tag-table:last").prev("span.horizontal-summary-operator").remove();
		$currentOptionLi.children("select.input-tag-table:last").remove();
	}
	
	// 若减少到不足6个则删除元素的宽度css属性
	if ($currentOptionLi.children("select.input-tag-table").size()-1 < 6) {
		$currentOptionLi.children("span.validate-msg").css("width","");
	}
}

/**
 * 初始化Combobox的宽度
 * @param id 使用Combobox控件的标签ID
 */
function initComboboxWidth($elem) {
	$elem.next().css("width",$elem.css("width"));
	$elem.next().children(":eq(1)").css("width","calc(100% - 18px)");
}