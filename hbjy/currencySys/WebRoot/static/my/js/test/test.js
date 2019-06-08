define(function(require) {
	$(function() {
		$.ajax({
			type:"post",
			url:url+"manaProController/getEditData",
			data:{editTableName:"finf"},
			success:function(data){
				/*if(data == "" && data.length <= 0){
					return;
				}
				data = JSON.parse(data);
				
				var fmValue = "";
				for(var i = 0;i < data.length;i++){
					var tablename = data[i].FIELD_NAME.substring(data[i].FIELD_NAME.indexOf("_")+1,data[i].FIELD_NAME.length);
					fmValue += '<div class="fitem">';
						fmValue += '<span>'+tablename+'</span>';
						if(data[i].FIELD_TYPE == "date"){
							fmValue += '<input id="XG_'+data[i].FIELD_ID+'" name="'+data[i].FIELD_ID+'" placeholder="数据日期" readonly="readonly" required="required"/>'
						}else{
							fmValue += '<input name="'+data[i].FIELD_ID+'" class="easyui-textbox" validType="Lengths[14]" required="required">'
						}
					fmValue += '<div class="remark" style="color:red;">'+data[i].REMARK+'</div>';
					fmValue += '</div>';
				}
				$('#fm').html(fmValue);*/
			}
		});
	});
});