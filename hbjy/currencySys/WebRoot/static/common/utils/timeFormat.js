function timeYMD(id){
    $('#'+id).dblclick(function(){
		$('#'+id).val("");
	});
	$('#'+id).datetimepicker({  
        format: 'yyyy-mm-dd',  
        weekStart: 1,  
        todayHighlight:true,
        autoclose: true,  
        startView: 2,  
        minView: 2,  
        forceParse: false,  
        language: 'zh-CN'  
   });
}

function timeHMS(id){
    $('#'+id).dblclick(function(){
		$('#'+id).val("");
	});
	$('#'+id).datetimepicker({  
		format: 'hh:ii:ss',  
	     todayHighlight:true,
	     autoclose: true,  
	     startView: 1,
	     maxView:1,
	     minView: 0,  
	     forceParse: false,  
	     language: 'zh-CN' 
	});
}

function wdatePickerYMD(id,sjrqStrDefault,specialDates){
	specialDates = specialDates == undefined ? "" : specialDates;
	$('#'+id).click(function(){
		WdatePicker({onpicked:wdatePickerOnpicked,specialDates:specialDates.split(","),readOnly:true,skin:'whyGreen'});
	});
	$('#'+id).addClass("Wdate");
	// 为输入框添加小手图标（类Wdate上添加会被bootstrap样式覆盖）
	$('#'+id).css("cursor","pointer");
	// 设置初始值
	if (sjrqStrDefault != undefined && sjrqStrDefault != "") {
		$('#'+id).val(sjrqStrDefault);
	}
}

function wdatePickerYMDHMS(id,sjrqStrDefault,specialDates){
	specialDates = specialDates == undefined ? "" : specialDates;
	$('#'+id).click(function(){
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:wdatePickerOnpicked,specialDates:specialDates.split(","),readOnly:true,skin:'whyGreen'});
	});
	$('#'+id).addClass("Wdate");
	$('#'+id).css("cursor","pointer");
	if (sjrqStrDefault != undefined && sjrqStrDefault != "") {
		$('#'+id).val(sjrqStrDefault);
	}
}

function wdatePickerHMS(id,sjrqStrDefault,specialDates){
	specialDates = specialDates == undefined ? "" : specialDates;
	$('#'+id).click(function(){
		WdatePicker({dateFmt:'HH:mm:ss',onpicked:wdatePickerOnpicked,specialDates:specialDates.split(","),readOnly:true,skin:'whyGreen'});
	});
	$('#'+id).addClass("Wdate");
	$('#'+id).css("cursor","pointer");
	if (sjrqStrDefault != undefined && sjrqStrDefault != "") {
		$('#'+id).val(sjrqStrDefault);
	}
}

function wdatePickerOnpicked(dp){
	// 输入框的触发onchange时间
	$(dp.el).change();
}

