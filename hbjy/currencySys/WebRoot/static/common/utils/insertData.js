define(function(require) {
	$(function() {
		require('jqueryEasyUi');
		layer.config({
			path : '../static/common/layer/' // layer.js所在的目录
		});
		
		//insert数据 到数据库中
		$('#insertData').click(function(){
			if ($("#fileName").val() != "jgxx" && !validateSjrq(true)) {
				return false;
			}
			var fields = $('#dg').datagrid('getColumnFields'); //获取datagrid的所有fields
			$('#last_row').val(JSON.stringify(fields));
			$('#fm').form('submit', {  
		        url:'/currencySys/insertDataController/insertData',  
		        onSubmit: function(){  
			        if($("#fm").form("validate")) {
			        	return true  
			        }else {	
			        	return false;  
			        } 
		        },  
		        success:function(data){  
		        	data = JSON.parse(data);
		        	if(data.data == 1){
		        		diaLog("新增成功");
		        		var grid = $("#dg");
		        		initdoSearch(grid);
		        	}else{
		        		diaLog(data.data);
		        	}
		        	$('#dlg').dialog('close');
		        	$("#fm").form("reset");  
		        }  
		    });
		});
	});
	
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
});

