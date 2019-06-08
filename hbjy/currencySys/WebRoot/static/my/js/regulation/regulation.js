var url = $("#url").val();
define(function (require) {
    require('jquery');
    require('My97DatePicker');
    require('jqueryEasyUi');
	require('timeFormat');
    require('layer');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	$(function() {
		// 左侧中心支库多选
		//开始时间
		wdatePickerYMD("kssj");
		
		//数据日期
		wdatePickerYMD("jssi");
	});
});

function insertModal() {
    $("#insertModal").modal();
   /* $.ajax({
        url: url + '/regulation/findPublishRangeByUserId',
        type: 'POST',
        data: {},
        dataType: 'json',
        success: function (result) {
            var areaList = result.areaList;
            var options = "";
            for (var i = 0; i < areaList.length; i++) {
                options += "<option value=" + areaList[i].AREACODE + ">" + areaList[i].AREANAME + "</option>";
            }
            $("#insertPublishRange").append(options);
        }
    });*/
}


function save() {
    var insertRegulationTitle = $("#insertRegulationTitle").val();
    if (insertRegulationTitle == null || insertRegulationTitle == "") {
        $dialog.alert('法规题目不能为空！');
        return;
    }
    var insertPublishRange = $("#insertPublishRange").val();
    if (insertPublishRange == null || insertPublishRange == "") {
        $dialog.alert('所属机构不能为空！');
        return;
    }

    var  formData = new FormData();
    formData.append("annex", $("#annex")[0].files[0]);
    formData.append("regulationTitle", insertRegulationTitle);
    formData.append("publishRange", $("#insertPublishRange").val());
    formData.append("publishRangeText", $("#insertPublishRange").find("option:selected").text());
    $.ajax({
        url: url + '/regulation/insertRegulation',
        type: 'POST',
        data: formData,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function (result) {
            if (result.result.code == 200) {
                $dialog.alert(result.result.msg, function () {
                    setTimeout(window.location.reload(), 1000);
                });
            } else {
                $dialog.alert(result.result.msg);
            }
        }
    });

}

//打开修改窗口
function updateModal(id) {
    $("#updateModal").modal();
    $.ajax({
        url: url + 'announcement/findById',
        type: 'POST',
        data: {id: id},
        dataType: 'json',
        success: function (result) {
            var announcement = result.announcement;
            $("#updateId").val(announcement.id);
            $("#updateAnnouncementTitle").val(announcement.announcementTitle);
            $("#updateAnnouncementContent").val(announcement.announcementContent);
            $("#updateAnnexName").val(announcement.annexName);
            if (announcement.message==0) {
				$("#isnot").attr("checked",true);
			}
        	if (announcement.message==1) {
				$("#isyes").attr("checked",true);
			}
            var areaList = result.areaList;
            var options = "";
            for (var i = 0; i < areaList.length; i++) {
                options += "<option value=" + areaList[i].AREACODE + ">" + areaList[i].AREANAME + "</option>";
            }
            $("#updatePublishRanges").append(options);
            $("#updatePublishRanges").val(announcement.publishRange);
        }
    });
}

function update() {
    var updateAnnouncementTitle = $("#updateAnnouncementTitle").val();
    if (updateAnnouncementTitle == null || updateAnnouncementTitle == "") {
        $dialog.alert('法规标题不能为空！');
        return;
    }
    var updateAnnouncementContent = $("#updateAnnouncementContent").val();
    if (updateAnnouncementContent == null || updateAnnouncementContent == "") {
        $dialog.alert('公告内容不能为空！');
        return;
    }
    var formData = new FormData();
    formData.append("annex", $("#updateAnnex")[0].files[0]);
    formData.append("announcementTitle", updateAnnouncementTitle);
    formData.append("id", $("#updateId").val());
    formData.append("announcementContent", updateAnnouncementContent);
    formData.append("updatePublishRanges",$("#updatePublishRanges").val());
    formData.append("message",$("input[name='message']:checked").val());
    
    
    $.ajax({
        url: url + '/announcement/updateAnnouncement',
        type: 'POST',
        data: formData,
        dataType: 'json',
        processData: false,
        contentType: false,
        success: function (result) {
            if (result.result.code == 200) {
                $dialog.alert(result.result.msg, function () {
                    setTimeout(window.location.reload(), 1000);
                });
            } else {
                $dialog.alert(result.result.msg);
            }
        }
    });
}

function uploadAgain() {
    $("#updateAnnexName").hide();
    $("#updateAnnex").show();
}

function deletebefore(id) {
    $("#deleteModal").modal();
    $("#annId").val(id);
}

function deleteRegulation(id) {
    $.ajax({
        url: url + 'regulation/deleteRegulation',
        type: 'POST',
        data: {
            id: $("#annId").val()
        },
        dataType: 'json',
        success: function (result) {
            if (result.result.code == 200) {
                $dialog.alert(result.result.msg, function () {
                    setTimeout(window.location.reload(), 1000);
                });
            } else if (result.result.code == 400) {
                $dialog.alert(result.result.msg);
            }
        }
    });
}

//法规首页
function xiangqingModal(id) {
    $.ajax({
        url: url + 'regulation/xiangqingmodal',
        type: 'POST',
        data: {id: id},
        dataType: 'json',
        success: function (result) {
        	$("#ggtm").val(result.regulationBean.regulationTitle);
        	$("#ssjg").val(result.regulationBean.organization);
        	$("#cjz").val(result.regulationBean.creator);
        	if(result.regulationBean.annexName!="" && result.regulationBean.annexName !=null){
        		$("#fjmc").val(result.regulationBean.annexName);
        		var xzfj="<td></td><td>"
						+"<a href='"+url+result.regulationBean.annexUrl+"' download='' id='urlhref' ></a>"
						+"<button type='button' class='btn btn-primary' onclick='dianjianniu();'>下载附件</button>"
						+"</td>"
        		$("#xzfj").html(xzfj);
        		  $("#xiangqingModal").modal();
        	}else{
        		$("#fjmc").val("无附件！");
        		$("#xzfj").html("");
        	}
        }
    });
}

function dianjianniu(){
	$("#urlhref").append("<span></span>");
	$("#urlhref span").click();
}

function subfrom(){
	//遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	setTimeout(function(){
		 $("#form1").submit();
		 layer.close(index);
	},200);
}

function subfrom1(){
	//遮罩层开始
	var index = layer.load(1, {
		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
	});
	setTimeout(function(){
		 $("#stayform").submit();
		 layer.close(index);
	},200);
}
/*
function subfrom(){
	var data={
			announcementTitle:$("#announcementTitle").val(),
			creator:$("#creator").val(),
			annexName:$("#annexName").val()
	}
	$.ajax({
		url: url + 'announcement/announcementIndex',
        type: 'POST',
        data: {publishRange:publishRange,id: id},
        dataType: 'json',
	});
	
}*/