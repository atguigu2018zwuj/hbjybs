var url = $("#url").val();
define(function (require) {
    require('jquery');
    require('My97DatePicker');
    require('jqueryEasyUi');
	require('timeFormat');
    require('layer');
    require('fuzzysearch');
    require('validate');
	require('ztree_all');
	require('ztree_exhide');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	$(function() {
		// 左侧中心支库多选
		//开始时间
		wdatePickerYMD("kssj");
		
		//数据日期
		wdatePickerYMD("jssi");
		// 添加按鈕
		var $add = $("#insertPublishRange");
		var addTitleValue = '新增公告';
		// 修改按钮
		var $Update = $("#updatePublishRange");
		var updateTitleValue = '修改公告';
		// 详情按钮
		var $check = $("#XqPublishRange");
		var checkTitleValue = '详情公告';
		/*addOrUpdateData($check, checkTitleValue);*/
		$("#insertPublishRange").click(function() {
			var author=$("#insertPublishRange").val();
			authorityDialog(author,addTitleValue);
		});
		$("#XqPublishRange").click(function() {
			var author=$("#XqPublishRange").val();
			authorityDialog(author,checkTitleValue);
		});
		$("#updatePublishRange").click(function() {
			var author=$("#updatePublishRange").val();
			authorityDialog(author,updateTitleValue);
		});
		
	});
});

function insertModal() {
    $("#insertModal").modal();
    $.ajax({
        url: url + '/announcement/findPublishRangeByUserId',
        type: 'POST',
        data: {},
        dataType: 'json',
        success: function (result) {
            var areaList = result.areaList;
            var brNoList = result.brNoList;
            var options = "";
            /*for (var i = 0; i < areaList.length; i++) {
                options += "<option value=" + areaList[i].AREACODE + ">" + areaList[i].AREANAME + "</option>";
            }
            $("#insertPublishRange").append(options);*/
            $("#insertPublishRange").val(brNoList);
            
        }
    });
}


function save() {
    var insertAnnouncementTitle = $("#insertAnnouncementTitle").val();
    if (insertAnnouncementTitle == null || insertAnnouncementTitle == "") {
        $dialog.alert('公告题目不能为空！');
        return;
    }
    var insertPublishRange = $("#insertPublishRange").val();
    if (insertPublishRange == null || insertPublishRange == "") {
        $dialog.alert('接受机构不能为空！');
        return;
    }
    var insertAnnouncementContent = $.trim($("p").text());
    if (insertAnnouncementContent == null || insertAnnouncementContent == "") {
        $dialog.alert('公告内容不能为空！');
        return;
    }
    var form = document.getElementById("insertForm");
    var formData = new FormData(form);
    formData.append("annex", $("#annex")[0].files[0]);
    formData.append("announcementTitle", insertAnnouncementTitle);
    formData.append("publishRange", $("#insertPublishRange").val());
    formData.append("publishRangeText", $("#insertPublishRange").find("option:selected").text());
    formData.append("announcementContent", insertAnnouncementContent);
    formData.append("msg", $(" input[ name='message' ]:checked ").val());
    /*var formData={
    	annex:$("#annex")[0].files[0],
    	announcementTitle:insertAnnouncementTitle,
    	publishRange:$("#insertPublishRange").val(),
    	publishRangeText:$("#insertPublishRange").find("option:selected").text(),
    	announcementContent:insertAnnouncementContent,
    	message:$(" input[ name='message' ]:checked ").val()   	
    };*/
    $.ajax({
        url: url + '/announcement/insertAnnouncement',
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
           /* var areaList = result.areaList;
            var options = "";
            for (var i = 0; i < areaList.length; i++) {
                options += "<option value=" + areaList[i].AREACODE + ">" + areaList[i].AREANAME + "</option>";
            }
            $("#insertPublishRange").append(options);*/
            $("#updatePublishRange").val(announcement.publishRange);
        }
    });
}

function update() {
    var updateAnnouncementTitle = $("#updateAnnouncementTitle").val();
    if (updateAnnouncementTitle == null || updateAnnouncementTitle == "") {
        $dialog.alert('公告题目不能为空！');
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
    formData.append("updatePublishRanges",$("#updatePublishRange").val());
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

function deleteAnnouncement(id) {
    $.ajax({
        url: url + 'announcement/deleteAnnouncement',
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

//公告首页，打开详情查看发布范围
function xiangqingModal(publishRange,id) {
    $.ajax({
        url: url + 'announcement/xiangqingmodal',
        type: 'POST',
        data: {publishRange:publishRange,id: id},
        dataType: 'json',
        success: function (result) {
        	/*if (result.announcementBean.announcementTitle.length > 20) {
        		$("#ggtm").val(result.announcementBean.announcementTitle.substring(0,20)+"......");
			}else{
				$("#ggtm").val(result.announcementBean.announcementTitle);
			}	*/
        	$("#ggtm").val(result.announcementBean.announcementTitle);
        	$("#XqPublishRange").val(result.announcementBean.publishRange);
        	$("#ggnr").val(result.announcementBean.announcementContent);
        	if (result.announcementBean.message==1) {
				$("#xqisyes").attr("checked",true);
				/*$("#xqisyes").prop("disabled",true);*/
				/*$("#xqisyes").prop("disabled",true);*/
			}
        	if (result.announcementBean.message==0) {
				$("#xqisnot").attr("checked",true);
				/*$("#xqisnot").attr("disabled",true);*/
				/*$("#xqisnot").prop("disabled",true);*/
			}
        	if(result.announcementBean.annexName!="" && result.announcementBean.annexName !=null){
        		$("#fjmc").val(result.announcementBean.annexName);
        		var xzfj="<td></td><td>"
						+"<a href='"+url+result.announcementBean.annexUrl+"' download='' id='urlhref' ></a>"
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

//初始化添加对话框
/*function addOrUpdateData($addOrUpdate, titleValue) {
	$addOrUpdate.click(function() {
		// 初始化权限控件的单击事件
			if (titleValue.indexOf("新增") >= 0) {
				authorityDialog($("#insertPublishRange").val());
			} else{
				authorityDialog($("#insertPublishRange").val());
			}	   
	   });
	}*/
/*function insertPublishRange(){
	authorityDialog($("#insertPublishRange").val());
}*/

//权限列树形结构
function authorityDialog(author,titleValue) {
	var treeObj;
	var nodes;
	var bzIds;
	$dialog({
		title : '请选择机构',
		width : 600,
		content:'<div style="width: 450pxpx">'
			+'<input id="keyword" type="search" placeHolder="搜索关键字"/>'
		    +'</div>'
		    +'<div style="height:150px ;overflow: auto;"><ul id="treeDiv" class="ztree"></ul></div>',
		onshow : function() {
			$('.artui-dialog-content').css({ "max-height" : "250px", "overflow" : "auto"});
			$.ajax({
				type : "POST",
				url : url + "announcement/getGanizatonList",
				data : '',
				dataType : "json",
				success : function(result) {
					if(result == "" && result.length <= 0){
						return;
					}
					if (result.code == 1000) {
						var treeCheckedData = $.trim(author).split(",");
						if (treeCheckedData != undefined) {
							for (var j = 0; j < treeCheckedData.length; j++) {
								$.each(result.data,function(i, obj) {
									var rootId = null;
									if (treeCheckedData[j] == obj.KEY) {
										result.data[i].checked = true;
										rootId = obj.PID;
									}
									if (rootId != null) {
										if (result.data[i].KEY == rootId) {
											result.data[i].checked = true;
										}
									}
								});
							}
						}
						var ztreeOne=$.fn.zTree.init($("#treeDiv"), setting, result.data);
						treeObj = $.fn.zTree.getZTreeObj("treeDiv");
						nodes = getCheckedLength(treeObj);
						/*fuzzySearch('treeDiv','#keyword',null,true);*/ //初始化模糊搜索方法
					} else {
						$dialog.alert('拉取列表失败！', 'warning');
					}
				}
			});
		},
		okValue : '保存',
		ok : function() {
			// 获取树节点的id,以json的格式存入array数组
			var str = '';
			var treeData = $.fn.zTree.getZTreeObj("treeDiv").getCheckedNodes();
			if (treeData.length != 0) {
				for (var i = 0; i < treeData.length; i++) {
					/*if (!treeData[i].isParent) {
						str += treeData[i].KEY + ",";
					}*/
						str += treeData[i].KEY + ",";
				}
				if (titleValue.indexOf("新增") >=0) {
					$("#insertPublishRange").val(str.substr(0, str.length - 1));
				}else if(titleValue.indexOf("修改") >=0){
					$("#updatePublishRange").val(str.substr(0, str.length - 1));
				}else{
					$("#insertPublishRange").val(str.substr(0, str.length - 1));
				}
				
			}
		},
		cancelValue : '取消',
		cancel : function() {
		}
	}).showModal();
}

var setting = {
		check : {
			enable : true,
			chkStyle : "checkbox",
			chkboxType : {
				"Y" : "",
				"N" : "ps"
			}
		},
		view : {
			dblClickExpand : false,// 屏蔽掉双击事件
			selectedMulti : true // 可以多选
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "ID",
				pIdKey : "PID",
				rootPId : 0
			},
			key : {
				name : "VALUE"// 上面data里的数据包含一个codeTypeDes属性，也就是说从数据库中对应的表必须有这个字段
			}
		}
	};

	function getCheckedLength(treeObj) {
		var nodes = treeObj.getCheckedNodes(true);
		var newNodes = new Array();
		if (nodes) {
			for (var i = 0; i < nodes.length; i++) {
				if (!nodes[i].isParent) {// isParent判断是否为父级，也就是是否有下级
					newNodes.push(nodes[i].KEY);// 存放所选节点的名称
				}
			}
		}
		return newNodes;
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