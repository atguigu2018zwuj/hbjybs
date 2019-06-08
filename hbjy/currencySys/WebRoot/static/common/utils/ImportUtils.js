define(function(require) {
	$(function() {
		require('jqueryEasyUi');
		layer.config({
			path : '../static/common/layer/' // layer.js所在的目录
		});
		var fileName = $('#fileName').val();//四位标识
		if(fileName != 'jgxx'){
			$('#downTable').click(function(){
				var beginSjrq = $('#beginSjrq').val();
				if(beginSjrq == undefined){
					beginSjrq = $('#SJRQ').val();
				}
				if(beginSjrq == null || beginSjrq == ''){
					diaLog('请选择数据日期');
					return false;
				}
				//var wantData = grid.datagrid('getSelections');
				download();
			});
		}
		
		//导出
		function download(){
			$dialog.confirm({
		        title:'提示',
		        width:280,
		        height:50,
		        content:'您确定要 <B class=red>导出</B> 吗？',
		        okValue: '确定',
		        ok: function(){
		        	var fields = $('#dg').datagrid('getColumnFields');
		        	var fieldValues = [];
		        	$('#dg').find('th').each(function(index,obj){
		        		fieldValues.push($(obj).html());
		        	});
		        	var wantData = $('#dg').datagrid('getSelections');
		        	var codeList="";
		        	for (var i = 0; i < wantData.length; i++) {
		        		codeList += ((wantData[i].CODE==undefined || wantData[i].CODE.length<=0) ? 0 : wantData[i].CODE) +","
		        	}
		        	var param = $('#formSub').serialize();
		        	var title = $('title').html();
		        	var tableName = $('#tableName').val();//表名
		        	var fileName = $('#fileName').val();//四位标识
		        	/*var addName="";
		        	addName=fileName+codeList;*/
		        	fileName=fileName+codeList;
		        	//window.location.href = url+"export/down?"+param+"&fields="+encodeURIComponent(JSON.stringify(fields))+"&fileName="+fileName+"&tableName="+tableName+"&fileTitle="+encodeURIComponent(title)+"&fieldValues="+encodeURIComponent(JSON.stringify(fieldValues);
		        	window.location.href = url+"export/down?"+param+"&fields="+encodeURIComponent(JSON.stringify(fields))+"&fileName="+fileName+"&tableName="+tableName+"&fileTitle="+encodeURIComponent(title)+"&fieldValues="+encodeURIComponent(JSON.stringify(fieldValues));	
		        	//window.location.href = url+"export/down?"+param+"&fields="+encodeURIComponent(JSON.stringify(fields))+"&fileName="+fileName+"&tableName="+tableName+"&fileTitle="+encodeURIComponent(title)+"&fieldValues="+encodeURIComponent(JSON.stringify(fieldValues)+"&codeList="+encodeURIComponent(JSON.stringify(codeList));
		        },
		        cancelValue: '取消',
		            cancel: true,
		            onclose: function(){}
		       }).showModal();
		}
		
		//弹出导出dlg
		$('#importExcel').click(function(){
			$('#Excel_dlg').dialog('open').dialog('setTitle','导入Excel');
		});
		
		//导入Excel保存
		$('#saveExcel_dlg').click(function(){
			if($("input[type='file']").val() != null && $("input[type='file']").val() != '') {
				var fields = $('#dg').datagrid('getColumnFields'); //获取datagrid的所有fields
				$('#fieldAll').val(JSON.stringify(fields));
				//遮罩层开始
				var index = layer.load(1, {
					shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
				});
				$("#Excel_fm").form('submit', {
		            url: '/currencySys/test/reportDataInsert',
		            success: function (result) {
		            	var obj = jQuery.parseJSON(result); 
		            	if(obj.code == 1000){
		            		if(obj.data.length != 0 && obj.data != null){
		            			if(obj.data[0].code != "导入成功！"){
		            				if(obj.data[0].code.indexOf("行") != -1 && obj.data[0].code.indexOf("列") != -1){
		            					$.messager.show({
		            						showSpeed : 600,  
		            						title : '信息',  
		            						timeout : 0, 
		            						modal:true,
		            						width:300,
		            						height:200,
		            						msg : obj.data[0].code,  
		            						showType : 'show',  
		            						style : {  
		            							right : '',  
		            							top : '100px;',  
		            							bottom	 : ''//窗口离右边距离,于left互斥  
		            						},
		            						buttons:[{
		            							text: '下载',
		            							handler: function () {  
		            								window.location.href = url+"test/errLogs?tname="+$('#tname').val();
		            							}
		            						},{
		            							text: '确定',
		            							handler: function () {  
		            								$(".messager-body").window('close');
		            							}
		            						}]
		            					});  
		            				}else{
		            					$.messager.alert('消息',""+obj.data[0].code);
		            				}
		            			}else{
		            				$.messager.alert('消息',""+obj.data[0].code);
		            			}
		                        $('#Excel_dlg').dialog('close');                      
		                        //遮罩层结束
		            			layer.close(index);
		            			var grid = $("#dg");
				        		initdoSearch(grid);
		            			
		            		}else{
		            			$.messager.alert('信息',obj.data[0].code);  
		            			$('#Excel_dlg').dialog('close');
		            			//遮罩层结束
		            			layer.close(index);
		            			
		            		}
		            		return ;
		                }
		            }
		        });
			}
		});
	});
});
