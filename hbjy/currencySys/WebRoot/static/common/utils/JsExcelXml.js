$(document).ajaxComplete(function(event, xhr, settings) {  
    if(xhr.getResponseHeader("sessionstatus")=="timeOut"){  
        if(xhr.getResponseHeader("loginPath")){
            alert("会话过期，请重新登陆!");
            window.parent.location.href =xhr.getResponseHeader("loginPath");//iframe修改父页面url
        }else{  
            alert("请求超时请重新登陆 !");  
        }  
    }  
}); 

function downExcel(opts){
		var defaultseeting = {
	            HeadInfo: null,
	            RowInfo: null,
	            FooterInfo: null,
	            MergeCells: null,
	            CellStyles: null,
	            MainTitle: { DisplaynameLeft: '', Alignment: 'Center', BgColor: '#FFFFFF', FontSize: 14, FontColor: "#000000", IsBold: true, IsItalic: false, IsUnderLine: false ,FontName:'宋体'},
	            SecondTitle: { DisplaynameLeft: '', Alignment: 'Center', BgColor: '#FFFFFF', FontSize: 12, FontColor: "#000000", IsBold: false, IsItalic: false, IsUnderLine: false ,FontName:'宋体'},
	            ThreeTitle: { DisplaynameLeft: '', Alignment: 'Center', BgColor: '#FFFFFF', FontSize: 12, FontColor: "#000000", IsBold: false, IsItalic: false, IsUnderLine: false ,FontName:'宋体'},
	            FourTitle: { DisplaynameLeft: '', Alignment: 'Center', BgColor: '#FFFFFF', FontSize: 12, FontColor: "#000000", IsBold: false, IsItalic: false, IsUnderLine: false ,FontName:'宋体'},
	            HeadStyle: { Alignment: 'Center', Alignment: 'Center',BgColor: '#FFFFFF', FontSize: 12, FontColor: "#000000", IsBold: false, IsItalic: false, IsUnderLine: false,rowHeight :30},
	            DataStyle: { Alignment: 'Center', BgColor: '#FFFFFF', FontSize: 11, FontColor: "#000000", IsBold: false, IsItalic: false, IsUnderLine: false ,dataRowHeight:26,RowIndex:[null]},
	            FootStyle: { Alignment: 'Center', BgColor: '#FFFFFF', FontSize: 12, FontColor: "#000000", IsBold: false, IsItalic: false, IsUnderLine: false },
	            RowStart: 1,
	            ColumStart: 1,
	            SheetName: 'sheet1',
	            SaveName: "导出数据",
	            swf: 'exportexcel.swf',
	            format:'.xls'
	        };
		//去除br
    	for(var i=0;i < opts.HeadInfo.length;i++){
    		for(var j =0;j<opts.HeadInfo[i].length;j++){
    			if(opts.HeadInfo[i][j].title){
    				opts.HeadInfo[i][j].title = opts.HeadInfo[i][j].title.replace(/<br>/g, "")
    			}
    		}
    	}
		var newOpts = $.extend(true, defaultseeting, opts);
		if(newOpts.RowStart < 1){
			newOpts.RowStart =1;
		}
		if(newOpts.ColumStart < 1){
			newOpts.ColumStart =1;
		}
		if(newOpts.format == null || newOpts.format == ''){
			newOpts.format = '.xls';
		}
        var blob = new Blob([JSXmlExcel.BulidXml(newOpts)], { type: "text/xml;charset=utf-8" });
        var fs = saveAs(blob, newOpts.SaveName + newOpts.format);
	}

//获取导出合并的索引，field，Rowspan
function getMergeCells (jq, fields){
	var MergeCellsVlaue = new Array();
	var MergeCellsIndex = 0;
	var MergeCellsField = '';
	var MergeCellsRowspan = 0;
	var MergeCellsColspan = 0;
	
	var target = jq;
    if (!fields) {
        fields = target.datagrid("getColumnFields");
    }
    var rows = target.datagrid("getRows");
    var i = 0,
    j = 0,
    temp = {};
    for (i; i < rows.length; i++) {
        var row = rows[i];
        j = 0;
        for (j; j < fields.length; j++) {
            var field = fields[j];
            var tf = temp[field];
            if (!tf) {
                tf = temp[field] = {};
                tf[row[field]] = [i];
            } else {
                var tfv = tf[row[field]];
                if (tfv) {
                    tfv.push(i);
                } else {
                    tfv = tf[row[field]] = [i];
                }
            }
        }
    }
    $.each(temp, function (field, colunm) {
        $.each(colunm, function () {
            var group = this;
            if (group.length > 1) {
                var before,
                after,
                megerIndex = group[0];
                for (var i = 0; i < group.length; i++) {
                    before = group[i];
                    after = group[i + 1];
                    if (after && (after - before) == 1) {
                        continue;
                    }
                    var rowspan = before - megerIndex + 1;
                    if (rowspan > 1) {
                    	MergeCellsIndex = megerIndex;
                    	MergeCellsField = field;
                    	MergeCellsRowspan = rowspan;
                    	MergeCellsColspan = 0;
                    	
                    	if(MergeCellsField != null && MergeCellsField != ''){
                    		MergeCellsVlaue.push({index: MergeCellsIndex, field: MergeCellsField, colspan: MergeCellsColspan, rowspan: MergeCellsRowspan});
                        }
/*                        target.datagrid('mergeCells', {
                            index : megerIndex,
                            field : field,
                            rowspan : rowspan
                        });*/
                    }
                    if (after && (after - before) != 1) {
                        megerIndex = after;
                    }
                }
            }
        });
    });
    if(MergeCellsVlaue != null && MergeCellsVlaue.length != 0){
    	return MergeCellsVlaue;
    }else{
    	return ;
    }
}

//格式化单元格数据保留两位小数
function formatPrice(num,row){
	if(typeof(num) == 'number'){
		num = parseFloat(num).toFixed(2);
		//将num中的$,去掉，将num变成一个纯粹的数据格式字符串
		num = num.toString().replace(/\$|\,/g,'');
		//如果num不是数字，则将num置0，并返回
		if(''==num || isNaN(num)){return 'Not a Number ! ';}
		//如果num是负数，则获取她的符号
		var sign = num.indexOf("-")> 0 ? '-' : '';
		//如果存在小数点，则获取数字的小数部分
		var cents = num.indexOf(".")> 0 ? num.substr(num.indexOf(".")) : '';
		cents = cents.length>1 ? cents : '' ;//注意：这里如果是使用change方法不断的调用，小数是输入不了的
		//获取数字的整数数部分
		num = num.indexOf(".")>0 ? num.substring(0,(num.indexOf("."))) : num ;
		//如果没有小数点，整数部分不能以0开头
		if('' == cents){ if(num.length>1 && '0' == num.substr(0,1)){return 'Not a Number ! ';}}
		//如果有小数点，且整数的部分的长度大于1，则整数部分不能以0开头
		else{if(num.length>1 && '0' == num.substr(0,1)){return 'Not a Number ! ';}}
		//针对整数部分进行格式化处理，这是此方法的核心，也是稍难理解的一个地方，逆向的来思考或者采用简单的事例来实现就容易多了
		
//		也可以这样想象，现在有一串数字字符串在你面前，如果让你给他家千分位的逗号的话，你是怎么来思考和操作的?
//		字符串长度为0/1/2/3时都不用添加          
//		字符串长度大于3的时候，从右往左数，有三位字符就加一个逗号，然后继续往前数，直到不到往前数少于三位字符为止          
		
		for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		{
			num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
		}
		//将数据（符号、整数部分、小数部分）整体组合返回
		return (sign + num + cents);    
	}else{
		return num;
	}
	
}
//格式化单元格数据保留两位小数
function formatNum(num,row){
	if(typeof(num) == 'number'){
		return parseFloat(num).toFixed(2);
	}else{
		return num;
	}
}

//格式化单元格数据保留两位小数
function formatInt(num,row){
	if(typeof(num) == 'number'){
		num = parseInt(num);
		//将num中的$,去掉，将num变成一个纯粹的数据格式字符串
		num = num.toString().replace(/\$|\,/g,'');
		//如果num不是数字，则将num置0，并返回
		if(''==num || isNaN(num)){return 'Not a Number ! ';}
		//如果num是负数，则获取她的符号
		var sign = num.indexOf("-")> 0 ? '-' : '';
		//如果存在小数点，则获取数字的小数部分
		var cents = num.indexOf(".")> 0 ? num.substr(num.indexOf(".")) : '';
		cents = cents.length>1 ? cents : '' ;//注意：这里如果是使用change方法不断的调用，小数是输入不了的
		//获取数字的整数数部分
		num = num.indexOf(".")>0 ? num.substring(0,(num.indexOf("."))) : num ;
		//如果没有小数点，整数部分不能以0开头
		if('' == cents){ if(num.length>1 && '0' == num.substr(0,1)){return 'Not a Number ! ';}}
		//如果有小数点，且整数的部分的长度大于1，则整数部分不能以0开头
		else{if(num.length>1 && '0' == num.substr(0,1)){return 'Not a Number ! ';}}
		//针对整数部分进行格式化处理，这是此方法的核心，也是稍难理解的一个地方，逆向的来思考或者采用简单的事例来实现就容易多了
		
//		也可以这样想象，现在有一串数字字符串在你面前，如果让你给他家千分位的逗号的话，你是怎么来思考和操作的?
//		字符串长度为0/1/2/3时都不用添加          
//		字符串长度大于3的时候，从右往左数，有三位字符就加一个逗号，然后继续往前数，直到不到往前数少于三位字符为止          
		
		for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		{
			num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
		}
		//将数据（符号、整数部分、小数部分）整体组合返回
		return (sign + num + cents);    
	}else{
		return num;
	}
	
}


// 时间格式化
function formatDate(myDate) {
	myDate.getYear(); //获取当前年份(2位)  
	myDate.getFullYear(); //获取完整的年份(4位,1970-????)  
	myDate.getMonth(); //获取当前月份(0-11,0代表1月)         // 所以获取当前月份是myDate.getMonth()+1;   
	myDate.getDate(); //获取当前日(1-31)  
	myDate.getDay(); //获取当前星期X(0-6,0代表星期天)  
	myDate.getTime(); //获取当前时间(从1970.1.1开始的毫秒数)  
	myDate.getHours(); //获取当前小时数(0-23)  
	myDate.getMinutes(); //获取当前分钟数(0-59)  
	myDate.getSeconds(); //获取当前秒数(0-59)  
	myDate.getMilliseconds(); //获取当前毫秒数(0-999)  
	myDate.toLocaleDateString(); //获取当前日期  
	myDate = myDate.getFullYear() + "-"+ (myDate.getMonth()+1) +  "-"+ myDate.getDate() ;
    return myDate;
};

function operate(value, row, index) {
    return '<a href="javascript:void(0)" >编辑</a>';
}
//生成报文标识
function feneratemessage(value, row, index) {
	if(value == "校验失败" || value == "强制性校验错误"){
		return '<a href="javascript:void(0)" >'+value+'</a>';
	}else{
		return value;
	}
}