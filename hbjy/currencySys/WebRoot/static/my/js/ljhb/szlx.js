var defaults = null;
var url = $("#url").val();
var ddata = null;
var dataIndex = "";//表格索引
var editIndex = undefined;
define(function(require) {
	require('My97DatePicker');
	require('JsExcelXml');
	require('insertData'); 
	require('ImportUtils');
	require('timeFormat');
	require('layer');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	$(function() {
		//表格ID
		var grid = $("#dg");
		// 左侧中心支库多选
		//开始时间
		wdatePickerYMD("beginSjrq");
		
		//数据日期
		wdatePickerYMD("XG_SJRQ");
		
		initdoSearch(grid);
		//查询
		$("#doSearch").click(function(){
			doSearch(grid);
		});
		
		$('#saveData').click(function(){
			saveData();
		});
		//显示更多
		$('#more').click(function(){
			$('#more').attr("style","display:none;");
			$('#hide').attr("style","display:block;");
			$('ul:eq(0)').attr("style","height:223px;");
		});
		
		$('#hide').click(function(){
			$('#more').attr("style","display:block;");
			$('#hide').attr("style","display:none;");
			$('ul:eq(0)').attr("style","height:88px;");
		});
	});
});

//查询
function doSearch(grid){
	//记录日期开始
	var beginSjrq = $('#beginSjrq').val();
	if(beginSjrq == null || beginSjrq == ''){
		diaLog('请选择数据日期');
		return false;
	}
	
	//遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	var data = 
	{
		beginSjrq: beginSjrq,
		JRJGBM : $('#JRJGBM').val(),
		NBJGH : $('#NBJGH').val(),
		KCJB : $('#KCJB').val(),
		QB : $('#QB').val(),
		KMXJSR_JG : $('#KMXJSR_JG').val(),
		KMXJSR_SY : $('#KMXJSR_SY').val(),
		KMXJSR_QY_D : $('#KMXJSR_QY_D').val(),
		KMXJSR_QY_Z : $('#KMXJSR_QY_Z').val(),
		KMXJSR_QY_X : $('KMXJSR_QY_X').val(),
		KMXJSR_GTGSH : $('#KMXJSR_GTGSH').val(),
		KMXJSR_JM : $('#KMXJSR_JM').val(),
		KMXJZC_JG : $('#KMXJZC_JG').val(),
		KMXJZC_SY : $('#KMXJZC_SY').val(),
		KMXJZC_QY_D : $('#KMXJZC_QY_D').val(),
		KMXJZC_QY_Z : $('#KMXJZC_QY_Z').val(),
		KMXJZC_QY_X : $('KMXJZC_QY_X').val(),
		KMXJZC_GTGSH : $('#KMXJZC_GTGSH').val(),
		KMXJZC_JM : $('#KMXJZC_JM').val(),
		SBLX : $('#SBLX').val(),
		selectSqlId:$('#excelSqlid').val()
	 }
	//查询数据
	chaxun(index,grid,data);
}

function initdoSearch(grid){
	var beginSjrq = $('#beginSjrq').val();
	//遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	var data = 
	{   beginSjrq: beginSjrq,
		selectSqlId:$('#excelSqlid').val()
	 }
	//查询数据
	chaxun(index,grid,data);
}


//查询
function chaxun(index,grid,data){
	var title = $('title').html();
	//是否选中复选框
	var IsCheckFlag = true;
	$("#myTable").css("display", "block");
	grid.datagrid({
		url:url+"ljhbController/getCodeData",
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
			$('div[class="datagrid-wrap panel-body panel-body-noheader"]').attr("style","width:"+($('div[class="consult-history"]').width())+"px");
			// 防止右侧出现垂直滚动条
			$(".datagrid-body").css("height",parseInt($(".datagrid-body").css("height").replace("px",""))+3+"px");
			if(ddata.rows.length <= 0){
				// 冻结ck、cuozuo列
//				$('tr[datagrid-row-index="0"]').find('td[field="ck"]').hide();
//				$('tr[datagrid-row-index="0"]').find('td[field="cuozuo"]').hide();
//				$('tr[class ="datagrid-header-row"]').find('td[field="ck"]').hide();
//				$('tr[class ="datagrid-header-row"]').find('td[field="cuozuo"]').hide();
				
			}
			layer.close(index);
		},
		onSelect: function (rowIndex, rowData) {
	        if (!IsCheckFlag) {
	            IsCheckFlag = true;
	            $("#dg").datagrid("unselectRow", rowIndex);
	        }
	    },                    
	    onUnselect: function (rowIndex, rowData) {
	        if (!IsCheckFlag) {
	            IsCheckFlag = true;
	            $("#dg").datagrid("selectRow", rowIndex);
	        }
	    },
		onClickCell:function(index,field,value){
			editIndex = index;
			if(field == 'cuozuo'){
				dataIndex = index;
				$('#dg').datagrid('selectRow', index);
				var row = $('#dg').datagrid('getSelected');
				//hidden
				$('#last_row').val(JSON.stringify([row]));
				if (row){
					if(row.SJRQ != "暂无数据"){
						$('#insertData').attr("style","display:none;");
						$('#saveData').attr("style","display:block;");
						$('#saveData').attr("style","width:90px;");
						$('#dlg').dialog('open').dialog('setTitle','编辑 '+title);
						$('#fm').form('load',row);
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
			$('div[class="datagrid-wrap panel-body panel-body-noheader"]').attr("style","width:"+($('div[class="consult-history"]').width())+"px");
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
								deleted:JSON.stringify(delData),
								delSqlId:"szlxDeleteData",
								fileName:$("#fileName").val()
							}
							$.ajax({
								type:"post",
								url:url+"ljhbController/deleteData",
								data:d_da,
								success:function(data){
									if(data == "" && data.length <= 0){
										return;
									}
									data = JSON.parse(data);
									if(data.data > 0){
										var num = 0;
										$('table[class="datagrid-btable"]').find("tr").each(function(index,obj){
											$(obj).find('td[field="ck"]').each(function(o,i){
												//获取数据第一列的值
												var ckInput = $(i).find('input').prop('checked');
												if(ckInput){
													grid.datagrid('deleteRow', (index - num));
													//解决删除过之后 索引改变
													num++;
												}
											});
						                });
										editIndex = undefined;
										initdoSearch(grid);
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
				$('#saveData').attr("style","display:none;");
				$('#insertData').attr("style","display:block;");
				$('#insertData').attr("style","width:90px;");
				$('#dlg').dialog('open').dialog('setTitle','新增 '+title);
				$('#fm').form('load',[]);
				$("#fm").form("reset");
			}
		}] 
	});
}

//提示
function diaLog(message){
	$dialog({
		title:"提示",
		content: "<div style='font-size:30px;color:red;text-align:center'>"+message+"</div>",
		icon: 'succeed',
		width:'300px',
		height:'80px',
		follow: document.getElementById('btn2')
	}).showModal();
}

//编辑保存
function saveData(){
	if (!validateSjrq(true)) {
		return false;
	}
	$('#UpdateSqlId').val("szlxUpdateData");
	$('#fm').form('submit', {  
        url:url+'ljhbController/updateData',  
        onSubmit: function(){  
                if($("#fm").form("validate")) {
                    return true  
                }else {	
                    return false;  
                } 
            },  
        success:function(data){  
            var obj = jQuery.parseJSON(data); 
            if(obj.code == 1000){  
            	if(obj.data > 0){
            		$.messager.alert('消息','保存成功！'); 
            	}else{
            		if(obj.data.length > 0){
            			$.messager.alert('消息',obj.data); 
            		}else{
            			$.messager.alert('消息','保存失败！'); 
            		} 
            	}
                $('#dlg').dialog('close');
                $('#dg').datagrid('reload'); 
                $('tr[datagrid-row-index="'+dataIndex+'"]').find('td').each(function(ind,o){
                	var fmValue = $('#fm').serialize().split('&');//获取fm表单数据
                	if(ind > 2){
                		if(ind <= fmValue.length){
                			$(o).find('div').html(fmValue[ind - 2].substring((fmValue[ind - 2].indexOf("=")+1),fmValue[ind - 2].length));
                		}
                	}
                });
                $("#fm").form("reset");  
            }else{  
                $.messager.alert('消息','保存失败！');  
            }  
        }  
      });
}
