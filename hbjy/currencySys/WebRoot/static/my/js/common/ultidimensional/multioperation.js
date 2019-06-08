define(function(require){
	require('echarts4');
	require('ztree');
	require('jqueryui');
	require('./css/multianalysis.css');
	var templates=require("./multi-templates");
	var setTabledata=require("./multiCreatable");
	var formatData=require("./multiDataFormat");
	var multiChartsOption=require("./multiChartsOptions");
	var loadexcel=require("./multiLoadexcel");
	var options={};
	var flip=false;//是否翻转table;
	var calculation='统计';//计算方式;
	var treesDatabox=[];//文档树盒子
	var formatdatasbox={};//格式化数据存放盒子
	var tabledata={};//处理完成后的table数据
	var updownfield={};//上卷下钻
	var choosefiledsbox={};//已选字段
	var hideorshow=true;//收起展开功能
	//获取拖拽区字段详细
	var paramsformat=function(arr){	//参数格式化
		var obj={};
		arr.map(function(item){
			if(obj[item.baseId]==undefined){
				obj[item.baseId]=[];
				obj[item.baseId].push(item.id)
			}else{
				obj[item.baseId].push(item.id)
			};
		});
		return obj;
	}
	var getfiled=function(){
		var rows=[],
			lie=[],
			filter=[],
			value=[],
			rowsids=[],
			lieids=[],
			filterids=[],
			valueids=[];
		$('#multi-condition-row .multi-draggable').each(function(n,dom){
			rows.push($(dom).text());
			rowsids.push({id:$(dom).attr('chooseId'),baseId:$(dom).attr('baseId')});
		});
		$('#multi-condition-column .multi-draggable').each(function(n,dom){
			lie.push($(dom).text());
			lieids.push({id:$(dom).attr('chooseId'),baseId:$(dom).attr('baseId')});
		});
		$('#multi-condition-filter .multi-draggable').each(function(n,dom){
			filter.push($(dom).text());
			filterids.push({id:$(dom).attr('chooseId'),baseId:$(dom).attr('baseId')});
		});
		$('#multi-condition-size .multi-draggable').each(function(n,dom){
			value.push($(dom).text());
			valueids.push($(dom).attr('chooseId'));
		});
		var fileds={
			rows:rows,
			column:lie,
			filter:filter,
			value:value,
			updown:updownfield
		}
		var filedids={
			rows:paramsformat(rowsids),
			column:paramsformat(lieids),
			filter:paramsformat(filterids),
			value:valueids,
			updown:updownfield
		}
		return {
			fileds:fileds,
			filedids:filedids
		};
	}
	//屏幕自适应
	if(window.screen.height >= 900 && window.screen.height<1080){
		$("#analysisbox").css("height",window.screen.height/1.3);
	}else if(window.screen.height >= 1070 && window.screen.height<1100){
		$("#analysisbox").css("height",window.screen.height/1.4);
	}else{
		$("#analysisbox").css("height",window.screen.height*0.72);
	}
	 //进度条进度调整/
   	function progressload(num){
   		$('.multi-progress').css({"width":num+'%'}).text(num+'%');
   	}
	//获取table数据
	var gettabledata=function(obj){
		//获取参数
		var fildes=getfiled();
		if(fildes.fileds.rows.length==0 || fildes.fileds.column.length==0 || fildes.fileds.value.length==0){
			return;
		};
		choosefiledsbox=fildes;
		//添加加载状态
		var loadhtml='<div class="multi-loading">'+
			'<div class="progress" style="width:400px;position:absolute;top:50%;left:50%;margin-left:-200px">'+
			  '<div style="min-width: 2em;" class="progress-bar multi-progress" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">'+
			    '0%'+
			  '</div>'+
			'</div>'
		'</div>';
		$('.multi-loading').remove();
		$('.multi-tablebox').append(loadhtml);
		//模拟请求进度
		var loadtime=0;
		var setload=setInterval(function(){
			loadtime+=Math.floor(10*Math.random());
			if(loadtime<=69){
				progressload(loadtime)
			}else{
				clearInterval(setload)
			}
		},3000);
		//请求数据
		$.ajax({
			url:obj.tableDataUrl,
			type:'post',
			data: JSON.stringify(fildes.filedids),
			datatype: 'json',
			success:function(params){
					clearInterval(setload)
					progressload(85);
					
					if(JSON.parse(params).data == null || JSON.parse(params).data.length==0 || (typeof JSON.parse(params).data == "string" && JSON.parse(params).data.startsWith("msg_"))){
						$('.multi-table-rowstit').html("");
						$('.multi-table-positionbox').html("");
						$('.multi-table').html("");
						setTimeout(function(){
							$('.multi-loading').remove();
						},500);
						// 若接口返回错误信息则页面弹框提示(data为"msg_要提示的信息")
						if (JSON.parse(params).data != null && typeof JSON.parse(params).data == "string" && JSON.parse(params).data.startsWith("msg_")) {
							alert(JSON.parse(params).data.replace("msg_",""));
						}
						return; 
					}
					
					var formatdata=formatData(JSON.parse(params).data,fildes,flip,treesDatabox);
					var mydata=setTabledata(formatdata,flip);
					tabledata=mydata;
					formatdatasbox=formatdata;
					$('.multi-table-rowstit').html(mydata.leftHtml);
					$('.multi-table-positionbox').width(226*mydata.left);
					$('.multi-table').html(mydata.rightHtml);
					progressload(100);
					setTimeout(function(){
						$('.multi-loading').remove()
					},500)
			},error:function(e){console.log(e)}
		})
		/*$.ajax({
    		type: "POST",
    		url: obj.tableDataUrl,
    		data: JSON.stringify(fildes.filedids),
    		dataType: "json",
    		//contentType: 'application/json',
    		success:function(params){
				var formatdata=formatData(params.data,fildes,flip,treesDatabox);
				var mydata=setTabledata(formatdata);
				$('.multi-table-rowstit').html(mydata.leftHtml);
				$('.multi-table').css({"margin-left":mydata.left*136+'px'});
				$('.multi-table').html(mydata.rightHtml);
			}
//		,error:function(e){console.log(e)}
		})*/
	};
	
	//初始化字段树和拖拽
	var setTrees=function(obj){
		/*obj={
			data:array,
			dom:string
		}*/
		//鼠标按下事件
		var zTreeOnMouseDown = function(event, treeId, treeNode) {
			if(treeNode != null){	//判断是不是拖移节点
				//获取根节点
				function getCurrentRoot(treeNode){  
				   if(treeNode.getParentNode()!=null){    
				      var parentNode = treeNode.getParentNode();    
				      return getCurrentRoot(parentNode);    
				   }else{    
				      return treeNode.id;   
				   }  
				}  
				var baseId=getCurrentRoot(treeNode);
				var chooseId=treeNode.id;;//拖拽树形结构时，记录当前选择的id；
			 	$(event.target).attr('chooseId',chooseId);
			 	$(event.target).attr('baseId',baseId);
			}
		};
		
		//初始化字段树
		var zTreeObj,
		setting = {
			data: {
				simpleData: {
					enable: true,
					idKey: "ownId",
					pIdKey: "parentId",
					rootPId: 0
				}
			},
			view: {
				fontCss: {color:"#333333"},
				showIcon: false
			},
			callback: {
				onMouseDown: zTreeOnMouseDown,
				onExpand:updrag
			}
		};
	    zTreeObj = $.fn.zTree.init($(obj.dom), setting, obj.data);
	    var sNodes = zTreeObj.getNodes();
	    
	    //移除字段功能
	    $(document).off('click','.multifieldremove').on('click','.multifieldremove',function(){
	    	$(this).parent().remove();
	    	flip=0;//重置翻转
	    	calculation="统计";//重置计算方式
	    	updownfield={};//重置上卷下钻
	    	$('.multi-chartbox').html('');
	    	gettabledata(options);
	    });
	    
	    
	    
	    
	    //初始化字段树拖拽//限制拖拽排序盒子
	    $( ".multi-conditioncont" ).sortable({
	      revert: false,
	      update :function(event,ui){
	      	flip=0;//重置翻转
	      	$('.multi-chartbox').html('');
	      	calculation="统计";//重置计算方式
	      	updownfield={};//重置上卷下钻
	      	gettabledata(options);
	      },
	      start:function(event,ui){
	      	
	      }
	    });
	    
	    //更新拖拽和排序
	    function updrag(){
	    	//为字段树添加拖拽class
			$(obj.dom+' a span:last-child').addClass('multi-draggable');
			var $node=[];//重复选择的dom容器
	    	$( ".multi-draggable" ).draggable({
		      connectToSortable: ".multi-conditioncont",
		      helper: "clone",
		      revert: "invalid",
		      start:function(event){
		      	var text=$(event.target).text();
		      	
		      	$(".multi-conditioncont .multi-draggable").each(function(n,dom){
		      		if(text==$(dom).text()){
		      			$node=$(dom);
		      			return false;
		      		}else{$node=[]};
		      	})
		      },
		      stop: function(event,ui) {
		      	$node.remove();//移除重复选择的
		      	//互相拖动初始化
		      	$(".multi-conditioncont .multi-draggable .multifieldremove").remove();
		      	 $(".multi-conditioncont .multi-draggable").prepend("<span class='multifieldremove glyphicon glyphicon-remove'></span>")
		        $(".multi-conditioncont .multi-draggable").draggable({
		        	connectToSortable: ".multi-conditioncont",
				    helper: "original",
				    revert: "invalid",
		        });
		        
		      }
		    });
		    $('.multi-draggable').disableSelection();
	    };
	    //执行
	    updrag();
	}
	
	//查看旧模板信息回填。
	var getoldmodel=function(obj){
	
		
	}
	
	//获取树结构
	var gettrees=function(obj){
		$.ajax({
			url:obj.treeDataUrl,
			type:'get',
			success:function(data){
				var treesdata=JSON.parse(data).data;
				var keys=Object.keys(treesdata);	//生成需要的字段树格式
				var treesdataformat=[];
				for(var i=0;i<keys.length;i++){	
					var arr=treesdata[keys[i]];
					var baseid=$.grep(arr,function(item){return item.level=="0"})[0].id;
					arr.map(function(item){
						var onetreedata={
							parentId:keys[i]+item.pId,
							ownId:keys[i]+item.id,
							baseid:baseid
						};
						onetreedata=$.extend(onetreedata,item);
						treesdataformat.push(onetreedata);
					})
				}
				treesDatabox=treesdataformat.concat(obj.treevaluedata);
				setTrees({data:treesDatabox,dom:obj.dom});
			},error:function(e){console.log(e)}
		})
	};
	
	//传入参数，请求数据，初始化结构
	var setmultiTree=function(obj){
		obj.dom='#multitree';//树dom
		$(obj.content).html(templates);
		options=obj;
		gettrees(obj)
	    
	    if(obj.getType){	//是不是模板回填
	    	//获取以选择字段，回填并初始化拖拽
	    	//根据字段设置生成table
	    	getoldmodel(obj)
	    };
		
		///////////////////功能区start////////////////////////////////////////////////////////////////
		/**************保存模板***********/
		$(document).on('click','.multi-savemodel',function(){
				$ajax({
					url:obj.saveUrl,
					data:{},
					success:function(){
						$dialog.alert('保存成功','success')
					},error:function(){}
				});
		});
		
		/************翻转table**************/
		$(document).off('click','.multi-btn-change').on('click','.multi-btn-change',function(){
			flip=flip==0?1:0;//转换翻转参数
			gettabledata(options)
		})
		
		/****************导出模板***************/
		$(document).off('click','.multi-loadmodel').on('click','.multi-loadmodel',function(event){
			loadexcel({
				data: {
					tbody: tabledata.excelData
				}
			});
		})
		/*************计算方式*************/
		$(document).off('click','.multi-js-choses').on('click','.multi-js-choses',function(){
			calculation=$(this).text();
			/*gettabledata(options);*/
		});
		
		/******************悬浮窗移除设置**********************/	
		$(document).bind('click',function(e){ 	//点击空白地方移除悬浮div
			var e = e || window.event; //浏览器兼容性 
			var elem = e.target || e.srcElement; 
			while (elem) { //循环判断至跟节点，防止点击的是div子元素 
				if (elem.id && elem.id=='multiRightMouseBox') { 
					return; 
				} 
				elem = elem.parentNode; 
			}
			$('#multiRightMouseBox').remove();
		}) 
		
		//***********过滤字段,上卷下钻设置***********//
		/*var setfiledsInbox=function(methods,data){	//二级菜单生成方法
			$('.multi-menubox2').remove();
			var ptl="";
			//判断类型
			if(methods=='filterdata'){
				for(var i=0;i<data.length;i++){
					ptl+="<li class='multi-filterlists'><label><input type='checkbox' class='multi-filters-checkbox'/>"+data[i]+"</label></li>"
				}
			}else{
				for(var i=0;i<data.length;i++){
					ptl+="<li class='multi-filterlists'><label><input type='radio' name='checkfiled' class='multi-filters-radio'/>"+data[i]+"</label></li>"
				}
			};
			var contbox="<div class='multi-menubox2'>"+
				"<ul>"+ptl+"</ul>"+
				"<span class='multi-filterbtn-ok'>确认</span>"+
			"</div>";
			$("#multiRightMouseBox").append(contbox)
			//点击确认
			$(document).off('click','.multi-filterbtn-ok').on('click','.multi-filterbtn-ok',function(){
				var condition={	//字段设置
					methods:methods,
					filters:[]
				};
				if(methods=="filterdata"){
					var doms=$('.multi-filters-checkbox:checked').parent();
					for(var i=0;i<doms.length;i++){
						condition.filters.push($(doms[i]).text())
					};
				}else{
					var filed=$('.multi-filters-radio:checked').parent().text();
					condition.filters.push(filed)
				};
				
				$('#multiRightMouseBox').remove();
				//执行请求table
				
			});
		};*/
		var filterFunc=function(e){
				if(e.button==2){
					var dom=e.currentTarget;
					var value=$(dom).attr('fieldid')
					$('.multiRightMouseBox').remove();//清除
					var left=e.pageX,top=e.pageY-100;
					var filedstext=$(dom).attr('fileds');
					var baseid=$(dom).attr('baseid');
					var up=$(dom).attr('updata')==1?true:false,
						down=$(dom).attr('downdata')==1?true:false;
					var list="";
					if(up){
						list+="<li oncontextmenu='return false' baseid="+baseid+" filed="+value+" class='multi-filterlists' methods='updata'>上卷</li>";
					};
					if(down){
						list+="<li oncontextmenu='return false' baseid="+baseid+" filed="+value+" class='multi-filterlists' methods='downdata'>下钻</li>";
					};
					
					var ptl="<div id='multiRightMouseBox' style='left:"+left+"px;top:"+top+"px' class='multiRightMouseBox'>"+
							"<h5 class='title'>字段设置</h5>"+
							"<ul class='multi-filterlist-box'>"+list+"</ul>"+
						"</div>";
					$('.multiAnalysis').append(ptl);
					$('.multi-filterlists').off('click').on('click',function(){//点击具体执行方法，生成二级菜单
						var methods=$(this).attr('methods');
						var filed=$(this).attr('filed');
						var baseid=$(this).attr('baseid');
						updownfield={	//生成过滤条件
							id:filed,
							methods:methods,
							baseId:baseid
						};
						$('#multiRightMouseBox').remove();//移除右侧菜单
						gettabledata(options);
						/*setfiledsInbox(methods,data[methods]);*/
					})
				}
		};
		
		/*******添加筛选器，上卷下钻事件**********/
		/*$(document).off('mousedown','.multi-conditioncont .multi-draggable')	//点击拖拽区过滤字段
			.on('mousedown','.multi-conditioncont .multi-draggable',function(e){
				filterFunc(e)
		});*/
		$(document).off('mousedown','.multi-excavate').on('mousedown','.multi-excavate',function(e){
				filterFunc(e)
		})
		
		/****************字段树搜索********************/
		
		/*******************echarts图表选择和生成**************************/
		var barchartfunc=function(){
			var legend=formatdatasbox.leftits
			var axis=formatdatasbox.toptits;
			var value=choosefiledsbox.fileds.value;
			var bodydata=formatdatasbox.bodydata;
			var chartdatabox=[];
			var valueLen=value.length;
			value.map(function(item,index){
				var data={
					axis:axis,
					legend:legend,
					title:item,
					data:[]
				};
				
				for(var i=0;i<legend.length;i++){
					var obj={};
					var obj={
						name:legend[i],
						type:'bar',
						barMaxWidth:35,
						data:[],
						itemStyle:{normal:{color: "#"+("00000"+((Math.random()*16777215+0.5)>>0).toString(16)).slice(-6)}}
					};
					if(!flip){
						axis.map(function(m,n){
							bodydata[i].map(function(one,dataindex){
								if(valueLen*n+index==dataindex){
									obj.data.push(one);
								}
							})
						})
					}else{
						//obj.data=bodydata[valueLen*i+index]
						var s =[];
						for(var j=0;j<bodydata[valueLen*i+index].length-1;j++){
							s[j]=bodydata[valueLen*i+index][j];
						}
						obj.data =s;
					};
					
					data.data.push(obj);
				};
				chartdatabox.push(data)
			})
			$('.multi-chartbox').html('');
			chartdatabox.map(function(item,index){
				var ptl="<div id='multicharts"+index+"' style='z-index:99;margin:20px;height:600px;width:1000px;float:left'></div>"
				$('.multi-chartbox').append(ptl);
				var option=multiChartsOption.multiBarChart(item);
				var mychart=echarts.init($('#multicharts'+index)[0]);
				 mychart.setOption(option);
			})
		}
		
		var linechartfunc=function(){
			var legend=formatdatasbox.leftits
			var axis=formatdatasbox.toptits;
			var value=choosefiledsbox.fileds.value;
			var bodydata=formatdatasbox.bodydata;
			var chartdatabox=[];
			var valueLen=value.length;
			value.map(function(item,index){
				var data={
					axis:axis,
					legend:legend,
					title:item,
					data:[]
				};
				
				for(var i=0;i<legend.length;i++){
					var obj={
						name:legend[i],
						type:'line',
						smooth:true,
						data:[],
						itemStyle:{normal:{color: "#"+("00000"+((Math.random()*16777215+0.5)>>0).toString(16)).slice(-6)}}
					};
					if(!flip){
						axis.map(function(m,n){
							bodydata[i].map(function(one,dataindex){
								if(valueLen*n+index==dataindex){
									obj.data.push(one);
								}
							})
						})
					}else{
						//obj.data=bodydata[valueLen*i+index]
						var s =[];
						for(var j=0;j<bodydata[valueLen*i+index].length-1;j++){
							s[j]=bodydata[valueLen*i+index][j];
						}
						obj.data =s;
					};
					data.data.push(obj);
				};
				chartdatabox.push(data)
			})
			$('.multi-chartbox').html('');
			chartdatabox.map(function(item,index){
				var ptl="<div id='multicharts"+index+"' style='z-index:99;margin:20px;height:600px;width:1000px;float:left'></div>"
				$('.multi-chartbox').append(ptl);
				var option=multiChartsOption.multiLineChart(item);
				var mychart=echarts.init($('#multicharts'+index)[0]);
				 mychart.setOption(option);
			})
		};
		
		var peichartfunc=function(){
			var legend=formatdatasbox.leftits
			var axis=formatdatasbox.toptits;
			var value=choosefiledsbox.fileds.value;
			var bodydata=formatdatasbox.bodydata;
			var chartdatabox=[];
			var valueLen=value.length;
			value.map(function(item,index){
				axis.map(function(onepie,axisindex){
					var data={
						title:item+'_'+onepie,
						data:[]
					};
					for(var i=0;i<legend.length;i++){
						//var obj={name:legend[i],value:0};
						var obj1={name:'',value:''};
						if(!flip){
							//obj.value=bodydata[i][valueLen*axisindex+index]
							if(bodydata[i][valueLen*axisindex+index] != 0 && bodydata[i][valueLen*axisindex+index] != "0.0%"){
								obj1.name = legend[i];
								obj1.value = bodydata[i][valueLen*axisindex+index];
							}
						}else{
							//obj.value=bodydata[i][valueLen*i+index]
							if(bodydata[i*valueLen+index][axisindex] != 0 && bodydata[i*valueLen+index][axisindex] != "0.0%"){
								obj1.name = legend[i];
								obj1.value = bodydata[i*valueLen+index][axisindex];
							}
						};
						//data.data.push(obj);
						data.data.push(obj1);
					};
					chartdatabox.push(data);
				})
			});
			$('.multi-chartbox').html('');
			chartdatabox.map(function(item,index){
				var ind;
				var fla= false;
				for(var i=0;i<item.data.length;i++){
					if(item.data[i].value != ''){
						ind =index;
						fla =true;
					}
				}
				if(fla){
					var ptl="<div id='multicharts"+ind+"' style='z-index:99;margin:20px;height:380px;width:380px;float:left'></div>"
					$('.multi-chartbox').append(ptl);
					var option=multiChartsOption.multiPieChart(item);
					var mychart=echarts.init($('#multicharts'+ind)[0]);
					mychart.setOption(option);
				}
			})
		}
		
		$('.multi-btn-charts').off('click').on('click',function(){
			var type=$(this).attr('chartstype');
			if(type=='bar'){
				barchartfunc()
			}else if(type=="line"){
				linechartfunc()
			}else if(type=="pei"){
				peichartfunc()
			}
		})
		
		
		///////////////////////////////功能区end//////////////////////////////////////////////////////////////
		////////////////辅助功能star////////////////////////////////////////////////////////////
		/*****************收起展开左侧边栏****************************/
		$(document).off('click','.multi-retract').on('click','.multi-retract',function(){
			if(hideorshow){
				$('.multiAnalysis ').animate({"padding":"20px 20px 20px 40px"});
				$('.multi-sqbtn').removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-right');
				hideorshow=false;
			}else{
				$('.multiAnalysis ').animate({"padding":"20px 20px 20px 540px"});
				$('.multi-sqbtn').removeClass('glyphicon-chevron-right').addClass('glyphicon-chevron-left');
				hideorshow=true;
			}
		})
		/**************************表格区域滚动监测**********************************/
		$('.multi-tablebox').on('scroll',function(e){
			var top=e.target.scrollTop;
			$('.multi-table-rowstit').css({'top':-top+'px'});
		})
		/************禁止右键菜单**************/
		$(document).bind("contextmenu",function(e){
	        return false;
	    });
		////////////////辅助功能end/////////////////////////////////////////////////////////////
		
	};
	

	return {
		setmultiTree:setmultiTree
	}
})