define(function(require){
	var setModeltable=function(arrdata,flip){
		
		var data=arrdata;
		
		var topdata=data.topHeader,
			leftdata=data.leftHeader,
			bodydata=data.bodydata,
			headerTit=data.headerTit;
			
		var topfloors=topdata.length;//几级头部标题
		var leftbox=[];
		var exceldata=[];//excel数据盒子
		var leftNums=[];//左侧标题所在行
		var lefthtml='';
		//获取导出到excel所有左侧标题所在行
		var getlines=function(arr){	//计算所在行
			var box=[];datanums=0;
			for(var i=0;i<arr.length;i++){
				box.push({text:arr[i].text,lines:datanums});
				datanums+=Number(arr[i].dataNums);
			};
			return box;
		};
		for(var i=0;i<leftdata.length;i++){
			leftNums.push(getlines(leftdata[i]));
		};
		/////////////////计算统计数据，插入数据当中////////////////////////////
		var topTotal="";//翻转后统计ptl;
		var leftTotal="";
		if(flip){
			topTotal="<td style='background:darkgray;text-align:center;color:#fff;font-weight:bold' rowspan="+topdata.length+"><div class='multi-tdboxdiv'>总计</div></td>";
			for(var i=0;i<bodydata.length;i++){
				var nums=0;
				bodydata[i].map(function(item){
					if(typeof(item)=="number"){
						if(item != 0){
							nums+=item;
						}
					}else if(typeof(item)=="string"){
						nums = '--';
					}
				});
				bodydata[i].push(nums);
			};
			/*topdata[0].push({text:'总计',dataNums:1})*/
		}else{
			leftTotal="<td style='background:darkgray;text-align:center;color:#fff;font-weight:bold' colspan="+leftdata.length+"><div class='multi-tdboxdiv'>总计</div></td>";
			var totalarr=[];
			var len=bodydata[0].length;
			for(var i=0;i<len;i++){
				var nums=0
				bodydata.map(function(item,index){
					if(typeof(item[i])=="number"){
						if(item[i] != 0){
							nums+=item[i];
						}
					}else if(typeof(item[i])=="string"){
						nums = '--';
					}
				});
				totalarr.push(nums)
			};
			bodydata.push(totalarr);
			/*leftdata[0].push({text:'总计',dataNums:1})*/
		}
		
		////////////////////////////////////////////////////////////////////
		//填充左侧table；
		var fileds=headerTit;
		var leftheader1="";//左侧头部标题
		for(var i=0;i<fileds.length;i++){
			leftheader1+="<td rowspan="+topfloors+">"+fileds[i]+"</td>"
		}
		lefthtml+="<tr>"+leftheader1+"</tr>";
		for(var j=0;j<topfloors-1;j++){
			lefthtml+="<tr></tr>";
		}
		
		
		var getNums=function(arr,n){
			var nums=0;
			for(var i=0;i<n;i++){
				nums+=arr[i].dataNums;
			}
			return nums;
		}
		
		var settds=function(nums,arr){	//
			var cont=0;var text="";
			for(var i=0;i<arr.length;i++){
				if(nums==0){
					if(arr[0].updata || arr[0].downdata){//是否允许上卷或下钻
						text="<td fieldid="+arr[0].id+" baseid="+arr[0].baseid+" class='multi-excavate' downdata="+arr[0].downdata+" updata="+arr[0].updata+" fileds="+arr[0].text+" rowspan="+arr[0].dataNums+"><div class='multi-tdboxdiv'><span class='glyphicon glyphicon-glass'></span>"+arr[0].text+"</div></td>"
					}else{
						text="<td rowspan="+arr[0].dataNums+"><div class='multi-tdboxdiv'>"+arr[0].text+"</div></td>";
					}
					break
				}else if(nums>0 && nums==getNums(arr,i)){
					if(arr[i].updata || arr[i].downdata){//是否允许上卷或下钻
						text="<td fieldid="+arr[i].id+" baseid="+arr[i].baseid+"  downdata="+arr[i].downdata+" updata="+arr[i].updata+" fileds="+arr[i].text+" class='multi-excavate' rowspan="+arr[i].dataNums+"><div class='multi-tdboxdiv'><span  class='glyphicon glyphicon-glass'></span>"+arr[i].text+"</div></td>"
					}else{
						text="<td rowspan="+arr[i].dataNums+"><div class='multi-tdboxdiv'>"+arr[i].text+"</div></td>";
					}
					break
				}
			};
			return text
		};
		var datalistNums;
		if(!flip){
			datalistNums=bodydata.length-1;//出去统计哪一行的数据条数
		}else{
			datalistNums=bodydata.length
		};
		
		for(var i=0;i<datalistNums;i++){
			var ptl=""
			for(var j=0;j<fileds.length;j++){
				ptl+=settds(i,leftdata[j])
			}
			leftbox.push(ptl);
			lefthtml+="<tr>"+ptl+"</tr>";
		};
		if(leftTotal!=""){
			lefthtml+="<tr>"+leftTotal+"</tr>"
		}
		var rowsdatalength=0;//横向数据列数
		if(datalistNums!=0){
			rowsdatalength=bodydata[0].length;
		};
		
		//右侧table数据
		var rightHtml="";
		var len=topdata.length,
			titlen=headerTit.length;
		var templts="";//拼接左侧table标题到右侧，用于导出时只导出一个table。
		headerTit.map(function(item){
			templts+='<td rowspan='+len+'>'+item+'</td>';
		});
		for(var i=0;i<topdata.length;i++){
			var onerowtit=topdata[i];
			var ptl="";
			var ptldata=[];
			for(var j=0;j<onerowtit.length;j++){
				ptl+="<td colspan="+onerowtit[j].dataNums+"><div class='multi-tdboxdiv'>"+onerowtit[j].text+"</div></td>";
				for(var m=0;m<onerowtit[j].dataNums;m++){//拼接excel数据
					if(m==0){
						ptldata.push(onerowtit[j].text);
					}else{
						ptldata.push('');
					}
				}
			};
			if(i==0){	
				rightHtml+="<tr>"+templts+ptl+topTotal+"</tr>";//组合完整table
				exceldata.push(headerTit.concat(ptldata));//生成excel一行数据
			}else{
				rightHtml+="<tr>"+ptl+"</tr>";
				var arr=[];
				for(var n=0;n<headerTit.length;n++){
					arr.push('');
				};
				exceldata.push(arr.concat(ptldata));
			};
		};
		//如果翻转拼接exceldata总计字段
		if(flip){
			exceldata.map(function(item,index){
				if(index==0){
					item.push('总计')
				}else{
				   item.push('')
				}
			})
		}
		//数据区
		for(var i=0;i<bodydata.length;i++){
			if(flip){	//如果翻转
				var ptl=""
				for(var j=0;j<bodydata[i].length;j++){
					ptl+="<td class='multi-tdata'><div class='multi-tdboxdiv'>"+bodydata[i][j]+"</div></td>";
				};
				rightHtml+="<tr>"+leftbox[i]+ptl+"</tr>"//组合完整table
				var databox=bodydata[i];//一行纯数据
				var titbox=[];//左侧对应标题
				for(var m=0;m<leftNums.length;m++){
					var judge=true;
					for(var n=0;n<leftNums[m].length;n++){
						if(leftNums[m][n].lines==i){
							titbox.push(leftNums[m][n].text);
							judge=false;
						}
					};
					if(judge){titbox.push('');}
				};
				exceldata.push(titbox.concat(databox));
			}else{//如果不翻转，将总计字段添加至最后一行，同时拼接excel数据
				var ptl=""
				for(var j=0;j<bodydata[i].length;j++){
					ptl+="<td class='multi-tdata'><div class='multi-tdboxdiv'>"+bodydata[i][j]+"</div></td>";
				};
				if(i==bodydata.length-1){//如果是最后一条，插入统计
					ptl="<td colspan="+leftdata.length+">总计</td>"+ptl;
					var arr=["总计"];
					for(var x=0;x<leftdata.length-1;x++){
						arr.push('');
					};
					exceldata.push(arr.concat(bodydata[i]));
				}else{
					var databox=bodydata[i];//一行纯数据
					var titbox=[];//左侧对应标题
					for(var m=0;m<leftNums.length;m++){
						var judge=true;
						for(var n=0;n<leftNums[m].length;n++){
							if(leftNums[m][n].lines==i){
								titbox.push(leftNums[m][n].text);
								judge=false;
							}
						};
						if(judge){titbox.push('');}
					};
					exceldata.push(titbox.concat(databox));
				};
				rightHtml+="<tr>"+leftbox[i]+ptl+"</tr>"//组合完整table
			}
		};
		
		
		return {
			leftHtml:lefthtml,
			rightHtml:rightHtml,
			left:fileds.length,
			excelData:exceldata
		}
		
	}
	
	return setModeltable
	
})