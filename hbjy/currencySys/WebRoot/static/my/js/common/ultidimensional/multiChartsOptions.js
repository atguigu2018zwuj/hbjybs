define(function(require){
	var multiBarChart=function(data){
		var bardata=data.data;
		var option = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        },
		        formatter:function(a){
		        	var s= '';
		        	for(var i=0; i<a.length;i++){
		        		if(a[i].value != 0 && a[i].value != "NaN"){
		        			s +=a[i].seriesName+":"+a[i].value+"</br>";
		        		}
		        	}
	            	 return s;
	            }
		    },
		    title:{
		    	text:data.title
		    },
		    legend:{
		    	//data:data.legend,
		    	data: function(){
		    		return data.legend
		    	}(),
		    	type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 30,
		        bottom: 20,
		        formatter:function(value,index){
		        	 return echarts.format.truncateText(value, 100, '14px Microsoft Yahei', '…');
		        },
		    },
		    grid: {
		        left: '3%',
		        right: 150,
		        bottom: 60,
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : data.axis,
		            axisTick: {
		                alignWithLabel: true
		            },
		            axisLabel:{
		            	rotate:-70,
	            	 	interval:0,
	            	    formatter:function(value,index){
			            	 return echarts.format.truncateText(value, 90, '14px Microsoft Yahei', '…');
			            }
		            }
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    //series : bardata
		    series : function(){
		    	for(var i= 0;i<bardata.length;i++){
		    		for(var j=0;j<bardata[i].data.length;j++){
		    			if(typeof(bardata[i].data[j]) == 'string'){
		    				bardata[i].data[j] = parseFloat(bardata[i].data[j].substr(0,bardata[i].data[j].length-1)).toFixed(2);
	            		}
		    		}
		    	}
		    	return bardata;
		    }()
		};
		return option

	};
	
	var multiLineChart=function(data){
		var bardata=data.data;
		var option = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        },
		        formatter:function(a){
		        	var s= '';
		        	for(var i=0; i<a.length;i++){
		        		if(a[i].value != 0 && a[i].value != "NaN"){
		        			s +=a[i].seriesName+":"+a[i].value+"</br>";
		        		}
		        	}
	            	 return s;
	            }
		    },
		    title:{
		    	text:data.title
		    },
		    legend:{
		    	data:data.legend,
		    	type: 'scroll',
		        orient: 'vertical',
		        right: 10,
		        top: 30,
		        bottom: 20,
		    	formatter:function(value,index){
	            	 return echarts.format.truncateText(value, 100, '14px Microsoft Yahei', '…');
	            }
		    },
		    grid: {
		        left: '3%',
		        right: 150,
		        bottom: 60,
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : data.axis,
		            axisTick: {
		                alignWithLabel: true
		            },
		            axisLabel:{
		            	rotate:-70,
	            	 	interval:0,
	            	    formatter:function(value,index){
			            	 return echarts.format.truncateText(value, 90, '14px Microsoft Yahei', '…');
			            }
		            }
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    //series : bardata
		    series : function(){
		    	for(var i= 0;i<bardata.length;i++){
		    		for(var j=0;j<bardata[i].data.length;j++){
		    			if(typeof(bardata[i].data[j]) == 'string'){
		    				if(bardata[i].data[j] != 0){
		    					bardata[i].data[j] = parseFloat(bardata[i].data[j].substr(0,bardata[i].data[j].length-1)).toFixed(2);
		    				}else{
		    					bardata[i].data[j] = 0;
		    				}
	            		}
		    		}
		    	}
		    	return bardata;
		    }()
		};
		return option
	};
	
	var multiPieChart=function(data){
		var option = {
		    title : {
		        //text: data.title,
		    	text: function(){
		    		for(var i=0;i<data.data.length;i++){
		    			if(data.data[i].name != '' && data.data[i].value != "NaN"){
		    				if(data.title.length >=15){
		    					return data.title.substr(0,15)+' \n '+data.title.substr(15,data.title.length);
		    				}else{
		    					return data.title;
		    				}
		    			}
		    		}
		    	}(),
		        x:'center',
		        y:"bottom",
		        textStyle:{
		        	fontSize:14
		        }
		    },
		    tooltip : {
		        trigger: 'item',
		        //formatter: "{a} <br/>{b} : {c} ({d}%)"
		        formatter:function(params){
		        	return params.name+"：<br/>"+params.value+"<br/>占比："+params.percent+"%"
		        }
		    },
		    /*legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
		    },*/
		    series : [
		        {
		            name: '',
		            type: 'pie',
		            radius : '40%',
		            center: ['50%', '50%'],
		            data:function(){
		            	for(var i=0;i<data.data.length;i++){
		            		if(typeof(data.data[i].value) == 'string'){
		            			data.data[i].value = parseFloat(data.data[i].value.substr(0,data.data[i].value.length-1)).toFixed(2);
		            		}
		            	}
		            	return data.data;
		            }(),
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                },
		                normal : {
		                    label : {
		                        show : true,
		                        formatter:function(params){   //series 中的文字进行截取  
					                	if(params.name.length >=5){
					                		return params.name.substr(0,5)+"..."
					                	}
					            }
		                    },
		                    labelLine : {
		                        show : true
		                    },
		                },
		            }
		        }
		    ]
		};
		return option
	}
	
	return {
		multiBarChart:multiBarChart,
		multiLineChart:multiLineChart,
		multiPieChart:multiPieChart
	}
})