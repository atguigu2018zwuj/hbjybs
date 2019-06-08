var url = $("#url").val();
var httpUtil = new HttpUtil();
var windowUtil = new WindowUtil();
var msgUtil = new MsgUtil();
//详细错误信息弹框
var $todosErrorDetailsModal = $('#todosErrorDetailsModal');
// 展示错误信息弹框的全局变量
var tableNameTemp = null;
var errorDetailsStrTemp = null;
define(function (require) {
    require('jquery');
    require('jqueryEasyUi');
    require('layer');
    require('fuzzysearch');
    require('validate');
	require('ztree_all');
	require('ztree_exhide');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});	
	$(function() {
		$('.newsTitle').click(function(){
			var titledata=$(this).text();  
			xiangqingModal(titledata);
		});
		$('.fgTitle').click(function(){
			var titledata=$(this).text();  
			//  var id=$(this).parent().("input [name='regulationId']").val();
			//  var p=$("input [name='regulationId']").val();
			// alert(p);
			fgxiangqingModal(titledata); 	   
		});
		$('#fbfw').click(function(){
			var author=$(this).val();  
			authorityDialog(author,"");
		});
		// 完成待办事项按钮
		$('#todosErrorDetailsModal button.finish-repulse').click(function(){
			$.messager.confirm('提示', '您确定当前待办事项已完成吗？', function(r){
				if (r){
					httpUtil.ajax("post","/generateMessageController/updateRepulseReportFinishDate",{
						"sjrq":$("#todosSjrq").val(),
						"ywbm":$("#todosYwbm").val(),
						"reporterCode":$("#todosReporterCode").val(),
						"jrjgCjxx":$("#todosJrjgCjxx").val()
					},function(result){
						if(result.data == undefined){
							msgUtil.diaLog("待办事项状态更新失败！",false);
						} else if (!result.data.result) {
							msgUtil.diaLog(result.data.msg,false);
						}  else {
							$todosErrorDetailsModal.modal("hide");
							msgUtil.diaLog("待办事项状态已更新！");
							loadTodoContent();
						}
					});
				}
			});
		});
		
		loadTodoContent();
	});
});

// 加载待办事项
function loadTodoContent(){
	// 清空待办事项
	$("#todoContent").children().eq(0).empty();
	// 显示加载中
	$("#todoContent").children().eq(0).html('<li><p class="todoThing" style="font-size:20px;font-weight:700;">加载中……</p></li>');
	httpUtil.ajax("post","/generateMessageController/getCurrentUserTodos",{},function(result){
		// 清空待办事项
		$("#todoContent").children().eq(0).empty(); 
		var todoContentLi = null;
		if (result.length <= 0) {
			todoContentLi = '<li><p class="todoThing">无待办事项</p></li>';
			$("#todoContent").children().eq(0).append(todoContentLi);
		} else {
			for (var i = 0; i < result.length; i++) {
				// 下级完成状态
				var childJrjgFinishedState = "";
				// 如果是待办事项最后一级，则不显示下级完成状态
				if (result[i].jrjg_cjxx.indexOf("->") > 0) {
					childJrjgFinishedState = '<p class="todoThing">【下级完成状态】：'+(result[i].isChildTodosAllFinished == true || result[i].isChildTodosAllFinished == 'true' ? '<span style="color:red">已完成</span>' : '<span style="color:rgb(87, 66, 169)">未完成</span>')+'</p>';
				}
				todoContentLi = '<li onclick="$(\'#todosErrorDetailsModal\').modal(\'show\');'
					+'todoClick(\''+result[i].tablename+'\',\''+encodeURI(result[i].error_details)+'\',\''+result[i].sjrq+'\',\''+result[i].ywbm+'\',\''+result[i].reporter_code+'\',\''+result[i].jrjg_cjxx+'\',\''+result[i].isChildTodosAllFinished+'\');" >'
				+'<p class="todoTime">[<span>'+result[i].repulse_date+'</span>]<span style="float:right;color:rgb(87, 66, 169)">数据日期：['+result[i].sjrq+']</span></p>'
				+'<p class="todoThing">【'+result[i].biaoshi_desc+'】'+result[i].ywmc+'</p>'
				+'<p class="todoThing">【数据所属机构】：'+result[i].jrjg_cjxx.replaceAll(/->/g," -> ")+'</p>'
				+ childJrjgFinishedState
				+'</li>';
				$("#todoContent").children().eq(0).append(todoContentLi);
			}
			$todosErrorDetailsModal.find(".modal-body").css("height",$todosErrorDetailsModal.height()*0.65);
			$todosErrorDetailsModal.modal({show:false});
			$todosErrorDetailsModal.on('shown.bs.modal',function () {
				if (showErrorDetailsModal(tableNameTemp,errorDetailsStrTemp)) {
//					// 成功加载，手动显示
//					$todosErrorDetailsModal.show();
				} else {
					$todosErrorDetailsModal.modal("hide");
				}
			});
		}
	});
}

/**
 * 设置临时全局变量，弹出对话框
 * @param tableName 业务库表名
 * @param errorDetailsStr 错误详细信息对象的JSON字符串
 * @param sjrq 数据日期
 * @param ywbm 业务编码
 * @param reporterCode 上报者金融机构内部机构号
 * @param jrjgCjxx 金融机构层级信息
 * @param isChildTodosAllFinished 下级待办事项是否全部已完成
 */
function todoClick(tableName,errorDetailsStr,sjrq,ywbm,reporterCode,jrjgCjxx,isChildTodosAllFinished) {
	// 设置临时变量
	tableNameTemp = tableName;
	errorDetailsStrTemp = errorDetailsStr;
	$("#todosSjrq").val(sjrq);
	$("#todosYwbm").val(ywbm);
	$("#todosReporterCode").val(reporterCode);
	$("#todosJrjgCjxx").val(jrjgCjxx);
	$("#todosIsChildTodosAllFinished").val(isChildTodosAllFinished);
	// 移除所有折叠面板
	while ($todosErrorDetailsModal.find(".easyui-accordion").children().size() > 0) {
		$todosErrorDetailsModal.find(".easyui-accordion").accordion('remove',0);
	}
	$todosErrorDetailsModal.modal("show");
}

/**
 * 弹出对话框，显示详细错误信息
 * @param tableName 业务库表名
 * @param errorDetailsStr 错误详细信息对象的JSON字符串
 */
function showErrorDetailsModal(tableName,errorDetailsStr){
	if (errorDetailsStr == undefined || errorDetailsStr.length <= 0) {
		msgUtil.diaLog('当前打回项没有错误详细信息！',false);
		return false;
	}
	errorDetailsStr = decodeURI(errorDetailsStr);
	
	// 错误详细信息
	var errorDetailsObj = JSON.parse(errorDetailsStr);
	// 错误详细数据信息（包含完整数据）
	var errorDetailsDataObj = null;
	// 当前业务的表字段信息
	var tableFieldInfos = null;
	// 当前业务的表字段（以半角逗号分隔）
	var tableFieldsStr = "";
	
	// 获取当前业务的表字段信息
	httpUtil.ajax("post","/generateMessageController/getTableFieldInfo",{"tableName":tableName},function(result){
		if (result.length > 0) {
			tableFieldInfos = result;
			for (var i = 0; i < tableFieldInfos.length; i++) {
				tableFieldsStr = tableFieldsStr + tableFieldInfos[i].FIELD_ID + ",";
			}
			tableFieldsStr = tableFieldsStr.replaceAll(/,$/g,"");
		}
	},false);
	if (tableFieldInfos == null) {
		msgUtil.diaLog('未查询到当前业务类型！',false);
		return false;
	}
	
	// 获取错误详细信息（将code转换成完整数据）
	httpUtil.ajax("post","/generateMessageController/getErrorDetailsDataInfo",{"tableName":tableName,"tableFields":tableFieldsStr,"errorDetailsJson":errorDetailsStr},function(result){
		if (Object.keys(result).length > 0) {
			errorDetailsDataObj = result;
		}
	},false);
	if (errorDetailsDataObj == null) {
		msgUtil.diaLog('当前打回项没有错误详细信息！',false);
		return false;
	}
	
	// 构造数据网络的列信息
	var datagridColumns = [];
	for (var i = 0; i < tableFieldInfos.length; i++) {
		var datagridColumn = {field:'',title:'',width:100};
		datagridColumn.field = tableFieldInfos[i].FIELD_ID;
		datagridColumn.title = tableFieldInfos[i].FIELD_NAME;
		datagridColumns.push(datagridColumn);
		tableFieldsStr = tableFieldsStr + tableFieldInfos[i].FIELD_ID + ",";
	}
	// 根据详细错误信息添加折叠面板
	// 设置数据表格的最大高度
	var errorDetailsdgMaxHeight = $todosErrorDetailsModal.find(".modal-body").eq(0).height()*0.95-$todosErrorDetailsModal.find(".accordion-header").eq(0).height();
	var errorDetailKeys = Object.keys(errorDetailsDataObj);
	for (var i = 0; i < errorDetailKeys.length; i++) {
		// 错误信息
		var errorMsg = errorDetailKeys[i];
		// 错误的数据
		var errorDatas = errorDetailsDataObj[errorMsg];
		// 向折叠面板中添加一个新的面板
		$todosErrorDetailsModal.find(".easyui-accordion").accordion('add', {
			title: errorMsg+'（共 '+errorDatas.length+' 条）',
			content: '<div class="errorDetailsDatagridDiv" style="padding: 10px 5px 20px 5px;max-height:'+errorDetailsdgMaxHeight+'px"><table id="errorDetailsdg'+i+'" style="max-height:'+(errorDetailsdgMaxHeight-20)+'px"></table></div>',
//			selected: i == 0 //仅第一条展开
			selected: false //由于初始展开时数据网络会出现显示问题，故暂时全部不展开
		});
		// 初始化数据网络
		$("#errorDetailsdg"+i).datagrid({
		    data:errorDatas,
		    columns:[datagridColumns],
		    striped:true,
		    rownumbers:true,
		    scrollbarSize:5
		});
	}
	// 数据网络强制出现横向滚动条
	$(".datagrid-body").css("overflow-x","scroll");
	return true;
}

function ggglClick(sjrq) {
	windowUtil.openTab("公告管理", '/viewController/ggglIndex','gggl',false);
}
function fgwjClick(sjrq) {
	windowUtil.openTab("法规文件", '/viewController/fgwjIndex','fgwj',false);
}
function insertModal() {
    $("#insertModal").modal();
    $.ajax({
        url: url + '/announcement/findPublishRangeByUserId',
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

    var formData = new FormData();
    formData.append("annex", $("#annex")[0].files[0]);
    formData.append("announcementTitle", insertAnnouncementTitle);
    formData.append("publishRange", $("#insertPublishRange").val());
    formData.append("publishRangeText", $("#insertPublishRange").find("option:selected").text());
    formData.append("announcementContent", insertAnnouncementContent);
    formData.append("message", $(" input[ name='message' ]:checked ").val());
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
function xiangqingModal(titledata) {
    $.ajax({
        url: url + 'announcement/xiangqingmodal',
        type: 'POST',
        data: {title:titledata},
        dataType: 'json',
        success: function (result) {
        	/*if (result.announcementBean.announcementTitle.length > 20) {
        		$("#ggtm").val(result.announcementBean.announcementTitle.substring(0,20)+"......");
			}else{
				$("#ggtm").val(result.announcementBean.announcementTitle);
			}	*/
        	$("#ggtm").val(result.announcementBean.announcementTitle);
        	$("#fbfw").val(result.area.AREANAME);
        	$("#ggnr").val(result.announcementBean.announcementContent);
        	if (result.announcementBean.message==1) {
				$("#xqisyes").attr("checked",true);
			}
        	if (result.announcementBean.message==0) {
				$("#xqisnot").attr("checked",true);
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

//法规首页
function fgxiangqingModal(titledata) {
    $.ajax({
        url: url + 'regulation/xiangqingmodal',
        type: 'POST',
        data: {title: titledata},
        dataType: 'json',
        success: function (result) {
        	$("#fgtm").val(result.regulationBean.regulationTitle);
        	$("#ssjg").val(result.regulationBean.organization);
        	$("#cjz").val(result.regulationBean.creator);
        	if(result.regulationBean.annexName!="" && result.regulationBean.annexName !=null){
        		$("#fgmc").val(result.regulationBean.annexName);
        		var xzfj="<td></td><td>"
						+"<a href='"+url+result.regulationBean.annexUrl+"' download='' id='urlhref' ></a>"
						+"<button type='button' class='btn btn-primary' onclick='dianjianniu();'>下载附件</button>"
						+"</td>"
        		$("#fgfj").html(xzfj);
        		$("#fgxiangqingModal").modal();
        	}else{
        		$("#fgmc").val("无附件！");
        		$("#fgfj").html("");
        		$("#fgxiangqingModal").modal();
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
						$.fn.zTree.init($("#treeDiv"), setting, result.data);
						treeObj = $.fn.zTree.getZTreeObj("treeDiv");
						nodes = getCheckedLength(treeObj);
						fuzzySearch('treeDiv','#keyword',null,true); //初始化模糊搜索方法
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
					if (!treeData[i].isParent) {
						str += treeData[i].KEY + ",";
					}
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
				"Y" : "ps",
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

