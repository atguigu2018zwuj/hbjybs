var url = $("#url").val();
define(function(require) {
	require('jquery');
	require('jqueryEasyUi');
	require('JsExcelXml');
	require('layer');
	require('timeFormat');
	require('My97DatePicker');
	require('layui');
	require('element');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	$(function() {
		//layui 
		layui.use('element', function(){
			  var $ = layui.jquery
			  ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
			  //触发事件
			  var active = {
			    tabChange: function(){
			      //切换到指定Tab项
			      element.tabChange('demo', '22'); //切换到：
			    }
			  };
			  
			  $('.site-demo-active').on('click', function(){
			    var othis = $(this), type = othis.data('type');
			    active[type] ? active[type].call(this, othis) : '';
			  });
			  
			  element.on('tab(test)', function(elem){
			    location.hash = 'test='+ $(this).attr('lay-id');
			  });
			  
			  // 若传的有查询参数则执行查询
			  if (searchParams != undefined && Object.keys(searchParams).length > 0) {
				    // 切换功能项
				    element.tabChange('docDemoTabBrief', searchParams.pageType);
				    // 输入查询条件
					var keys = Object.keys(searchParams);
					for (var key in keys) {
						$('.layui-show').find("#formSub").find("[name='"+keys[key]+"']").val(searchParams[keys[key]]);
					}
					// 执行查询
					if (searchParams.pageType == "sjrk") {
						wAll();
						$('#wywm').attr("disabled",false);
					} else if (searchParams.pageType == "sjzh") {
						conAll();
					} else if (searchParams.pageType == "bwgl") {
						doSearch($("#test"));
					}
			  }
			  
			});
		wdatePickerYMD("conbeginDate");
		wdatePickerYMD("wBeginDate");
		/******************源数据入库初始化 start************************/
		$('#wBeginDate').bind("change",function(){
			wAll();
			$('#wywm').attr("disabled",false);
		});
		
		//键盘松开是 触发
		$('#wywm').bind("change",function(){
			wAll();
		});
		
		//源数据入库初始化数据
		function wAll (){
			var grid = $("#wTest");
			//记录日期开始
			var beginSjrq = $('#wBeginDate').val();
			if(beginSjrq == null || beginSjrq == ''){
				diaLog('请选择数据日期');
				return false;
			}
			var data = 
			{
				beginSjrq: beginSjrq,
				ywm: $('#wywm').val()
			 }
			doSearch1(grid,"getConversionData",data,"wMyTable");
		}
		/******************源数据入库初始化 end************************/
		
		
		
		/******************数据转换初始化 start************************/
		$('#conbeginDate').bind("change",function(){
			conAll();
		});
		
		//键盘松开是 触发
		$('#conywm').bind("change",function(){
			conAll();
		});
		//键盘松开是 触发
		$('#conbm').bind("change",function(){
			conAll();
		});
		//数据转换初始化数据
		function conAll(){
			var grid = $("#conTest");
			//记录日期开始
			var beginSjrq = $('#conbeginDate').val();
			if(beginSjrq == null || beginSjrq == ''){
				diaLog('请选择数据日期');
				return false;
			}

			var data = 
			{
				beginSjrq: beginSjrq,
				ywm: $('#conywm').val(),
				bm: $('#conbm').val()
			 }
			doSearch1(grid,"getTransformation",data,"conMyTable");
		}
		/******************数据转换初始化 end************************/
		
		
		//sjzh
		$('#sjzh').bind("click",function(){
			var grid = $("#conTest");
			var kv = grid.treegrid("getCheckedNodes");
			if(kv.length <= 0){
				diaLog('请先选择转换表');
				return false;
			}
			var index = layer.load(1, {
				shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
			});
			//去除父类 只要子类
			var zlData =[];
			for(var i = 0;i < kv.length; i++){
				if(kv[i].id > 110100){
					zlData.push(kv[i]);
				}
			}
			var data = {
				beginSjrq: $('#conbeginDate').val(),
				id:'sjzh',
				ywm: $('#conywm').val(),
				bm: $('#conbm').val()
			};
			data.treeData = JSON.stringify(zlData);
			downFile1(data,index,grid,"warehousingConversion");
		});
		
		//数据入库
		$('#sjrk').bind("click",function(){
			var grid = $("#wTest");
			var kv = grid.treegrid("getCheckedNodes");
			if(kv.length <= 0){
				diaLog('请先选择入库文件');
				return false;
			}
			var index = layer.load(1, {
				shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
			});
			//去除父类 只要子类
			var zlData =[];
			for(var i = 0;i < kv.length; i++){
				if(kv[i].id > 0){
					zlData.push(kv[i]);
				}
			}
			var data = {
				beginSjrq: $('#wBeginDate').val(),
				id:'sjrk',
				ywm: $('#wywm').val()
			};
			data.treeData = JSON.stringify(zlData);
			downFile1(data,index,grid,"warehousingConversion");
		});
		
	});
});

//查询
function doSearch1(grid,sqlId,data,id){
	$(window).resize(function(){
		grid.datagrid('resize', {
			width:function(){return document.body.clientWidth;},
			height:function(){return document.body.clientHeight;},
		});
	});
	//遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	$('div[id="'+id+'"] div[class="datagrid-wrap panel-body panel-body-noheader"]').attr("style","width:100%");
	$("#"+id).css("display", "block");
	downFile1(data,index,grid,sqlId,id);
}

//
function downFile1(treegrid,index,grid,methodName,id){
	$.ajax({
		type:"post",
		url:url + "conversionController/"+methodName,
		data:treegrid,
		success:function(data){
			if(data == "" && data.length <= 0){
				return false;
			}else if(data.indexOf("文件不存在") != -1){
				layer.open({
					  title: ''
					  ,content: '源数据文件不存在'
					});  
				layer.close(index);
				grid.treegrid('loadData', { total: 0, rows: [] }); 
				return false;
			}else if(data.indexOf("解压失败") != -1){
				layer.open({
					  title: ''
					  ,content: '源数据文件解压失败'
				});  
				layer.close(index);
				grid.treegrid('loadData', { total: 0, rows: [] }); 
				return false;
			}else if(data.indexOf("异常") != -1){
				layer.open({
					  title: ''
					  ,content: '系统出现异常，请联系管理员'
				});  
				layer.close(index);
				grid.treegrid('loadData', { total: 0, rows: [] }); 
				return false;
			}
			$('tr[node-id="数据报送"]').find('span[class="tree-checkbox tree-checkbox0"]').click();
			$('tr[node-id="数据报送"]').find('span[class="tree-checkbox tree-checkbox2"]').click();
			$('tr[node-id="数据报送"]').find('span[class="tree-checkbox tree-checkbox1"]').click();
			//展示树形表格
			grid.treegrid({
				data:JSON.parse(data),
				fitColumns: false,
				nowrap:true,
				onLoadSuccess:function(ddata){
					if(id != null){
						$('div[id="'+id+'"] div[class="datagrid-wrap panel-body panel-body-noheader"]').attr("style","width:100%");
					}
					/*if(treegrid.treeData != undefined && treegrid.treeData != null && treegrid.treeData != ""){
						var treeData = JSON.parse(treegrid.treeData);
						for(var i=0;i < treeData.length;i++){
							var kv = grid.treegrid("checkNode",treeData[i].id);  //根据ID勾选节点 
						}
					}*/
					layer.close(index);
				},
				onClickCell:function(field,index,value){
					if(field == 'scbwxx'){
						if(index.scbwxx == '校验失败' || index.scbwxx == '强制性校验错误'){
							//下载失败的文件
							window.location.href = url + 'conversionController/downFlie?beginSjrq='+$('#conbeginDate').val()+'&fileName='+index.key;
						}
					}
				},
				onCollapse:function(node){//当节点折叠时触发。
					$('div[id="'+id+'"] div[class="datagrid-wrap panel-body panel-body-noheader"]').attr("style","width:100%");
				} 
			});
		}
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
