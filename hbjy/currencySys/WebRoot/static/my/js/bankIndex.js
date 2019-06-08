var treeData;
var url = $("#url").val();
var cookieUtil = new CookieUtil();
var msgUtil = new MsgUtil();
var httpUtil = new HttpUtil();
var windowUtil = new WindowUtil();
var validateUserPrsnInfo;
var userInfo = cookieUtil.getUserInfoFromCookie();
var userFirstLoginDlog;
define(function(require){
	require('jquery');
	require('jqueryEasyUi');
	require('validate');
	require('My97DatePicker');
	require('./bankCommon');
	require('MouseWheel');
	require('timeFormat');
	require('layer');
	require('ztree_all');
	require('ztree_exhide');
	require('fuzzysearch');
	require('select2');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	$(function(){
		// 用户首次登录时弹窗完善用户信息
		var qdzssjTemp;
		var zssxsjTemp;
		var userInfo = cookieUtil.getUserInfoFromCookie();
		// 首次登录
		if (userInfo.LOGIN_TIMES <= 0) {
			// 对话框中控件
			var editElems = '<form id="formId" style="margin-left:24px;">'
				+ '<div class="row">'
				+ '<div class="col-md-6"><span style="margin-left:30px">用户名称：</span><input id="yhNameId" type="text" name="yhName" readonly="readonly" style="width:162px;height:30px" /></div>'
				+ ' <div class="col-md-6"><span style="margin-left:62px">姓名：</span><input id="nameId" type="text" name="namev" style="width:162px;height:30px" /></div>'
				+ '</div>'
				+ '<div class="row">'
				+ ' <div class="col-md-6"><span style="margin-left:62px">性别：</span><select class="organizationSelect rightSelect" id="sexId" name="sex" style="width:162px;height:30px"> ' 
				+ ' <option selected value="1">男</option>' 
				+ ' <option value="2">女</option>' 
				+ ' </select></div>'
				+ ' <div class="col-md-6"><span style="margin-left:30px">手机号码：</span><input id="telephId" type="text" name="teleph" style="width:162px;height:30px" /></div>'
				+ '</div>'
				+ '<div class="row">'
				+ '<div class="col-md-6"><span style="margin-left:30px">身份证号：</span><input id="cardNumId" type="text" name="cardNum" style="width:162px;height:30px" /></div>'
				+ '<div class="col-md-6"><span style="margin-left:15px">内部机构号：</span><input id="nbjghId" type="text" name="nbjgh" style="width:162px;height:30px" readonly/></div>'
				+ '</div>' 
				+ '<div class="row">'
				+ ' <div class="col-md-6"><span  style="margin-left:62px">岗位：</span><select class="organizationSelect rightSelect" id="gwId" name="gw" style="width:162px;height:30px"> ' 
				+ ' <option selected value="0">管理岗</option>' 
				+ ' <option value="1">柜员</option>' 
				+ ' <option value="2">清分人员</option>' 
				+ ' </select></div>'
				+ '<div class="col-md-6">是否反假证书：<select class="organizationSelect rightSelect" id="sfqdhgzsId" name="sfqdhgzs" style="width:162px;height:30px"> ' 
				+ ' <option selected value="0">否</option>' 
				+ ' <option value="1">是</option>' 
				+ ' </select></div>'
				+ '</div>' 
				+ '<div class="row">'
				+ '<div class="col-md-6">取得证书时间：<input id="qdzssjId" type="text" name="qdzssj" style="width:162px;height:30px" readonly="readonly"/></div>'
				+ '<div class="col-md-6">证书失效时间：<input id="zssxsjId" type="text" name="zssxsj" style="width:162px;height:30px" readonly="readonly"/></div>'
				+ '</div>' 
				+ '</form>';
			// 若是否取得反假证书为“是”，则取得证书时间、证书失效时间必填
			$.validator.addMethod("fjzsSjRequired", function(value, element) {
				var flg = true;
				var sfqdhgzs = $("#sfqdhgzsId").val();
				if (sfqdhgzs == "1" && (value == null || value == '')) {
					flg = false;
				}
			    return flg;
			});
			// 判断内部机构号必须存在
			$.validator.addMethod("nbjghNeedExist", function(value, element) {
				var nbjghExistFlg = true;// 内部机构号是否存在
				httpUtil.ajax("post","/userManage/selectJgName",{'publishRange':value},function(result){
					nbjghExistFlg = result.length > 0;
				},false);
				return nbjghExistFlg;
			});
			// 判断 证书失效时间 必须大于 取得证书时间
			$.validator.addMethod("zssxsjNeedGtQdzssj", function(value, element) {
				return ($("#zssxsjId").val() == null || $("#zssxsjId").val() == "" || $("#qdzssjId").val() == null || $("#qdzssjId").val() == "") || $("#zssxsjId").val() > $("#qdzssjId").val();
			});
			
			// 对话框
			userFirstLoginDlog = $dialog({
				title : "信息确认",
				width : 600,
				dblclickClose:false,
				esc:false,
				drag:false,
				content : '<div style="height: ;overflow: hidden;">'+ editElems + '</div>',
				onshow : function() {
					// 初始化时间控件
					wdatePickerYMD("qdzssjId");
					wdatePickerYMD("zssxsjId");
					
					$('.artui-dialog-content').css({ "max-height" : "400px", "overflow" : "auto" });
					// 去除右上角关闭按钮
					$(".artui-dialog-header").eq(0).children(".artui-dialog-close").eq(0).remove();
					// 隐藏【下一步】按钮（根据用户机构的级别判断是否显示）
					$("[data-did='nextStep']").hide();
					// 修改自定义按钮的宽度、顺序
					var $nextStepBtn = $("div.artui-dialog-button").children().eq(0);
					var $okBtn = $("div.artui-dialog-button").children().eq(1);
					$nextStepBtn.css('width','112px');
					$okBtn.css('width','112px');
					$("div.artui-dialog-button").append($nextStepBtn);
					// 是否反假证书变更联动(未取得反假证书时，将反假证书时间置空)
					$("#sfqdhgzsId").change(function(e) {
						if ($(this).val() == '0') {
							$("#qdzssjId").val('');
							$("#zssxsjId").val('');
						} else {
							$("#qdzssjId").val(qdzssjTemp);
							$("#zssxsjId").val(zssxsjTemp);
						}
					});
					$("#qdzssjId").change(function(e) {
						qdzssjTemp = $(this).val();
					});
					$("#zssxsjId").change(function(e) {
						zssxsjTemp = $(this).val();
					});
					// 回显数据
					httpUtil.ajax("post","/userManage/getUserPrsnInfoByUsername",{'username':userInfo.YH_NAME},function(result){
						// 若用户机构的级别在联社及以上（1、2、3），则显示【下一步】按钮
						if ((result.jgjb == null || result.jgjb == "") || (result.jgjb != null && result.jgjb != "" && parseInt(result.jgjb) < 4)) {
							$("[data-did='nextStep']").show();
						}
						
						$("#yhNameId").val(result.username);
						$("#nameId").val(result.name);
						if(result.sex == null || result.sex == '' || result.sex == '男'){
							$("#sexId").val('1');
						}else{
							$("#sexId").val('2');
						}
						$("#telephId").val(result.teleph);
						$("#cardNumId").val(result.card_id);
						$("#nbjghId").val(result.nbjgh);
						$("#gwId").val(result.gw!=null && result.gw!='' ? result.gw : '0');
						$("#sfqdhgzsId").val(result.sfqdhgzs!=null && result.sfqdhgzs!='' ? result.sfqdhgzs : '0');
						if ($("#sfqdhgzsId").val() == '1') {
							$("#qdzssjId").val(result.qdzssj);
							$("#zssxsjId").val(result.zssxsj);
							qdzssjTemp = result.qdzssj;
							zssxsjTemp = result.zssxsj;
						}
						
						// 若身份证号不为空则设为只读
						if (result.card_id != null && result.card_id != "") {
							$("#cardNumId").prop("readonly",true);
						}
					});
				},
				okValue : '确定',
				ok : submitUserPrsnInfo,
				button:[{
					id: 'nextStep',
					value: '下一步',
					autofocus : true,
					callback:function(){
						if ($("#nbjghId").val() == null || $("#nbjghId").val() == "") {
							msgUtil.diaLog("内部机构号为空，无法进行下一步！",false);
							return false;
						}
						
						var nbjghExistFlg = false;// 内部机构号是否存在
						var jgjb = "";// 内部机构号是否存在
						httpUtil.ajax("post","/userManage/selectJgName",{'publishRange':$("#nbjghId").val()},function(result){
							nbjghExistFlg = result.length > 0;
							if (nbjghExistFlg) {
								jgjb = result[0].JGJB;
							}
						},false);
						if (!nbjghExistFlg) {
							msgUtil.diaLog("当前内部机构号不存在，请核对后再试！",false);
							return false;
						} else if (jgjb != null && jgjb != "" && parseInt(jgjb) >= 4) {
							msgUtil.diaLog("当前机构无需进行下一步！",false);
							return false;
						}
						
						xjjgDialog($("#nbjghId").val());
						return false;
					}
				}]
			}).showModal();
		}
		
		//用户名
    	$("#mouseChangeName").mousemove(function(e) {
    		this.title = '修改个人资料';
		});
    	$(".zl_logout").mousemove(function(e) {
    		this.title = '退出';
		});
    	$(".zl_logout").click(function(e) {
    		// 退出登录时清空cookie
    		cookieUtil.clearAllCookie();
		});
    	
    	//初始化首页
    	/*var di = window.document.getElementsByClassName("panel-body-noborder")[0];
    	var h = di.style.height;
    	var w = di.style.width;*/
    	$("#infoDiv");
    	/*document.getElementById("infoDiv").innerHTML ='<iframe id="infopage" frameborder="0"  src='+url+'/viewController/bankhome style="width:'+'auto'+';height:'+'auto'+'margin:0px 0px;"></iframe>';*/
    	$("#infopage").css({"width":'100%',"height":'100%'});
    	//遮罩层开始
    	var index = layer.load(1, {
    		shade : [ 0.3, '#000' ] // 0.1透明度的白色背景
    	});
		$.ajax({	
				type: "POST",
				url:"/currencySys/login/getUserData",
				async:true,
    			success:function(data){
    				//如果data="" 则说明登录失败 session消失
    				if(data == "" || data.length <= 0	){
    					window.location.href=url+"/login";
    				}else{
    					data = JSON.parse(data);
    					// 若当前用户的机构级别不是1、2、3级，则删除其权限管理菜单、报送统计菜单
    					if (userInfo.DW_JB != "1" && userInfo.DW_JB != "2" && userInfo.DW_JB != "3") {
							for (var dataIndex in data) {
								// 权限管理
								if (data[dataIndex].key == 'xtgl') {
									var childrenData = data[dataIndex].children;
									for (var cIndex in childrenData) {
										if (childrenData[cIndex].key == 'qxgl') {
											childrenData.remove(childrenData[cIndex]);
										}
									}
								}
								// 报送统计
								if (data[dataIndex].key == 'bwgl') {
									var childrenData = data[dataIndex].children;
									for (var cIndex in childrenData) {
										if (childrenData[cIndex].key == 'bsgl') {
											childrenData.remove(childrenData[cIndex]);
										}
									}
								}
							}
    					}
    					var ztreeData ="";
    					for(var i = 0; i < data.length;i++){
    						//去除第一级中没有子类的数据
    						//如果没有子类 测不添加
    						var authData = '';
							if(data[i].children != undefined && data[i].children.length > 0){
	    						if(i == 0){//第一个默认展开
	    							authData +='<li class="zl_nav_lists1 zl_menu_active1">';
	    							authData +='<h4><span class="zl_ho zl_h'+(i+13)+'">'+data[i].text+'</span><span class="zl_triangle1 zl_triangle_open1"></span></h4>';
	    						}else{
	    							authData +='<li class="zl_nav_lists1">';
	    							authData +='<h4><span class="zl_ho zl_h'+(i+13)+'">'+data[i].text+'</span><span class="zl_triangle1 zl_triangle_close1"></span></h4>';
	    						}
	    						authData += '<ul class="zl_sublist1">';
	        					var authDataLength = authData.length;
		        					for(var j = 0; j < data[i].children.length;j++){
	        							//去除第二级中没有子类的数据
	        							//如果没有子类 测不添加
		        						var authData1 = '';
	    								if(data[i].children[j].children != undefined && data[i].children[j].children.length > 0){
	    									authData1 +='<li class="zl_nav_lists">';
	    									if(j == 0){//第一个默认展开
	    										authData1 +='<h4><span class="zl_h zl_h'+(j+1)+'">'+data[i].children[j].text+'</span><span class="zl_triangle zl_triangle_open"></span></h4>';
	    		    						}else{
	    		    							authData1 +='<h4><span class="zl_h zl_h'+(j+1)+'">'+data[i].children[j].text+'</span><span class="zl_triangle zl_triangle_close"></span></h4>';
	    		    						}
	    									authData1 += '<ul class="zl_sublist">';
	    									var authDataLength1 = authData1.length;
		    									for(var x = 0; x < data[i].children[j].children.length;x++){
		    										//去除第三级中没有子类的数据
		    										//如果没有子类 测不添加
		    										var authData2 = '';
		    	    								if(data[i].children[j].children[x].children != undefined && data[i].children[j].children[x].children.length > 0){
		    	    									authData2 +='<li class="zl_nav_lists2">';
		    	    									authData2 +='<h4><span class="zl_h zl_h'+(x+1)+'">'+data[i].children[j].children[x].text+'</span><span class="zl_triangle2 zl_triangle_close2"></span></h4>';
		    	    									authData2 += '<ul class="zl_sublist2" style="display: none;">';
		    	    									var authDataLength2 = authData2.length;
		    	    									for(var y = 0; y < data[i].children[j].children[x].children.length;y++){
		    	    										authData2 += '<li class="myUl"><a id="'+data[i].children[j].children[x].children[y].key+'" href="javascript:void(0);"> '+data[i].children[j].children[x].children[y].text+'</a></li>';
		    	    									}
		    	    									if(authDataLength2 == authData2.length){
		    	    										continue;
		    	    		        					}
		    	    									authData2 += '</ul>';
		    	    									authData2 += '</li>';
		    	    								}else if(i > 0){
		    	    									for(var x = 0; x < data[i].children[j].children.length;x++){
		    	    										authData2 += '<li class="myUl"><a id="'+data[i].children[j].children[x].key+'" href="javascript:void(0);"> '+data[i].children[j].children[x].text+'</a></li>';
		    	    									}
		    	    								}
		    	    								authData1 += authData2;
		    									}
	    									if(authDataLength1 == authData1.length){
	    										continue;
	    		        					}
	    									authData1 += '</ul>';
	    									authData1 += '</li>';
	    								}else if(i > 0){
	    									for(var j = 0; j < data[i].children.length;j++){
	    										authData1 += '<li class="myUl"><a id="'+data[i].children[j].key+'" href="javascript:void(0);"> '+data[i].children[j].text+'</a></li>';
	    									}
	    								}else{
	    									for(var j = 0; j < data[i].children.length;j++){
	    										authData1 += '<li class="myUl"><a id="'+data[i].children[j].key+'" href="javascript:void(0);"> '+data[i].children[j].text+'</a></li>';
	    									}
	    								}
	    								authData += authData1;
	        						}
	        					if(authDataLength == authData.length){
									continue;
	        					}
	        					authData += '</ul>';
	        					authData += '</li>';
							}else if(i > 0){
								for(var j = 0; j < data[i].children.length;j++){
									authData += '<li class="myUl"><a id="'+data[i].key+'" href="javascript:void(0);"> '+data[i].text+'</a></li>';
								}
							}
							ztreeData += authData;
    					}
    					
    					$('#zl_menu_nav').html(ztreeData);
    					var ul = document.getElementById('zl_menu_nav');
    					//获取最底层的所有ul
    					var lis = getElementsByClassName("myUl","","");
    					for(var i=0;i<lis.length;i++){
    						//根据索引i来判断点击的那个ul
    						lis[i].onclick = function(){
    							windowUtil.openTab($(this).find('a').html().trim(), '<iframe width="100%" height="100%" frameborder="0"  src="'+url+'/viewController/'+$(this).find('a').attr('id')+'Index" style="width:100%;height:100%;margin:0px 0px;"></iframe>',$(this).find('a').attr('id'),true);  
    						}
    					}
    				}
    				layer.close(index);
    			}
    		});
		function getElementsByClassName(className, root, tagName) {    //root：父节点，tagName：该节点的标签名。 这两个参数均可有可无
		    if (root) {
		        root = typeof root == "string" ? document.getElementById(root) : root;
		    } else {
		        root = document.body;
		    }
		    tagName = tagName || "*";
		    if (document.getElementsByClassName) {                    //如果浏览器支持getElementsByClassName，就直接的用
		        return root.getElementsByClassName(className);
		    } else {
		        var tag = root.getElementsByTagName(tagName);    //获取指定元素
		        var tagAll = [];                                    //用于存储符合条件的元素
		        for (var i = 0; i < tag.length; i++) {                //遍历获得的元素
		            for (var j = 0, n = tag[i].className.split(' ') ; j < n.length; j++) {    //遍历此元素中所有class的值，如果包含指定的类名，就赋值给tagnameAll
		                if (n[j] == className) {
		                    tagAll.push(tag[i]);
		                    break;
		                }
		            }
		        }
		        return tagAll;
		    }
		}
		
		//绑定tabs的右键菜单  
		$("#tabs").tabs({
			onContextMenu : function(e, title) {
				//在每个菜单选项中添加title值  
                var $divMenu = $("#tab_rightmenu div[id]");  
                $divMenu.each(function() {  
                    $(this).attr("id", title);  
                });  
                
              //显示menu菜单 
				e.preventDefault();
				$('#tab_rightmenu').menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data("tabTitle", title);
			}
		});

		//实例化menu的onClick事件  
		$("#tab_rightmenu").menu({
			onClick : function(item) {
				CloseTab(item.target.id,item.text);
			}
		});

		//几个关闭事件的实现  
		function CloseTab(title, text) {
			if(text == '关闭全部标签') {  
	            $(".tabs li").each(function(index, obj) {  
	                  //获取所有可关闭的选项卡  
	                  var tabTitle = $(".tabs-closable", this).text();  
	                  $("#tabs").tabs("close", tabTitle);  
	             });  
	        }  
	          
	        if(text == '关闭其他标签') {  
	            $(".tabs li").each(function(index, obj) {  
	                  //获取所有可关闭的选项卡  
	                  var tabTitle = $(".tabs-closable", this).text();  
	                  if(tabTitle != title) {  
	                    $("#tabs").tabs("close", tabTitle);  
	                  }  
	             });  
	        }  
	          
	        if(text == '关闭右侧标签') {  
	             var $tabs = $(".tabs li");  
	             for(var i = $tabs.length - 1; i >= 0; i--) {  
	                //获取所有可关闭的选项卡  
	                var tabTitle = $(".tabs-closable", $tabs[i]).text();  
	                if(tabTitle != title) {  
	                    $("#tabs").tabs("close", tabTitle);  
	                } else {  
	                    break;  
	                }  
	             }  
	        }  
	          
	        if(text == '关闭左侧标签') {  
	             var $tabs = $(".tabs li");  
	             for(var i = 0; i < $tabs.length; i++) {  
	                //获取所有可关闭的选项卡  
	                var tabTitle = $(".tabs-closable", $tabs[i]).text();  
	                if(tabTitle != title) {  
	                    $("#tabs").tabs("close", tabTitle);  
	                } else {  
	                    break;  
	                }  
	             }  
	        }  
	        
		}  
    });
});

//jquery的validate校验
validateUserPrsnInfo = {
	textvalidate : function() {//userFirstLoginFormId
		return $("#formId").validate({
			rules : {
				yhName : {
					required : true,
					minlength : 1,
					maxlength : 100
				},
				namev : {
					required : true,
					minlength : 1,
					maxlength : 100
				},
				teleph : {
					required : true,
					digits : true,
					rangelength : [ 11, 11 ]
				},
				cardNum : {
					required : true,
					rangelength : [ 15, 18 ]
				},
				nbjgh : {
					required : true,
					nbjghNeedExist :true,
					minlength : 1,
					maxlength : 30
				},
				qdzssj : {
					fjzsSjRequired : true
				},
				zssxsj : {
					fjzsSjRequired : true,
					zssxsjNeedGtQdzssj : true
				}
			},
			messages : {
				yhName : {
					required : "必填项",
					minlength : "最小长度1个字符",
					maxlength : "最大长度100个字符"
				},
				namev : {
					required : "必填项",
					minlength : "最小长度1个字符",
					maxlength : "最大长度100个字符"
				},
				teleph : {
					required : "必填项",
					digits : "必须为数字",
					rangelength : "手机号为11为数字"
				},
				cardNum : {
					required : "必填项",
					rangelength : "身份证号为15位或18为字符串"
				},
				nbjgh : {
					required : "必填项",
					nbjghNeedExist : "当前内部机构号不存在",
					minlength : "最小长度1个字符",
					maxlength : "最大长度30个字符"
				},
				qdzssj : {
					fjzsSjRequired : "必填项"
				},
				zssxsj : {
					fjzsSjRequired : "必填项",
					zssxsjNeedGtQdzssj : "证书失效时间需在取得证书时间之后"
				}
			}
		});
	}
};

// 提交用户现金从业人员信息
function submitUserPrsnInfo(showMsg){
	// 校验失败则返回
	if (!validateUserPrsnInfo.textvalidate().form()) {
		$("label.error").css({ "color" : "red", "margin-left" : "81px" });
		if (showMsg) msgUtil.diaLog("请先完善确认信息！",false);
		return false;
	}
	// 获取表单数据
	var yhName = $("#yhNameId").val();
	var name = $("#nameId").val();
	var sex = $("#sexId").val();
	var teleph = $("#telephId").val();
	var cardNum = $("#cardNumId").val();
	var nbjgh = $("#nbjghId").val();
	var gw = $("#gwId").val();
	var sfqdhgzs = $("#sfqdhgzsId").val();
	var qdzssj = $("#qdzssjId").val();
	var zssxsj = $("#zssxsjId").val();
	// 拼接成json对象
	var params = {
		yhName : yhName,
		name : name,
		sex : sex,
		teleph : teleph,
		cardNum : cardNum,
		nbjgh : nbjgh,
		gw : gw,
		sfqdhgzs : sfqdhgzs,
		qdzssj : sfqdhgzs == '1' ? qdzssj : null,
		zssxsj : sfqdhgzs == '1' ? zssxsj : null
	};
	// 保存用户现金从业人员信息
	var results;
	httpUtil.ajax("post","/userManage/saveUserPrsnInfo",params,function(result){
		results = result;
	},false);
	if(results.data == undefined){
		msgUtil.diaLog("保存失败，请联系管理员！",false);
		return false;
	} else if (!results.data.result) {
		msgUtil.diaLog(results.data.msg,false);
		return false;
	} else {
		// 保存成功（将登录次数加1）
		cookieUtil.setUserInfoToCookie('LOGIN_TIMES',userInfo.LOGIN_TIMES + 1);
		msgUtil.diaLog(results.data.msg);
	}
	return true;
}

var setting = {
	data:{
		simpleData:{
			enable:true
		}
	}
};

function xjjgDialog(nbjgh){
	//所属单位树形结构
	var treeObj;
	var nodes;
	var areaIds;
	var xjjgDialogObj = $dialog({
		title:'下级机构信息确认',
	    title_html:true,
		width:600,
		height:300,
		drag:false,
		content:'<div id="xjjgDialogDiv" style="width:100%;height: 100%;margin:0 auto;">'
			+'<div style="width: 25%;height: 100%;float:left;"><span style="font-size:20px">下级机构名称</span></br><a id="updateJrjgHref" href="javascript:void(0)"><span style="font-size:18">信息有误</span></br><span style="font-size:18">前去修改</span></a></div>'
			+'<div style="width: 75%;height: 100%;float:left;text-align:left;border:2px solid rgb(228,228,228);padding: 5px 5px 5px 5px;">'
		    +'<div style="height: 100%;overflow: auto;"><ul id="treeDiv" class="ztree"></ul></div>'
		    +'</div>',
		onshow:function(){
			$('.artui-dialog-content').css({"max-height":"400px","overflow":"auto"});
			$('#xjjgDialogDiv').parent().css('text-align','center');
			// 将下级机构树弹框置于最顶层
			var $formDialog = $('#formId').parents("[role='alertdialog']");
			var $formDialogShade = $('#formId').parents("[role='alertdialog']").next();
			var $xjjgDialog = $('#xjjgDialogDiv').parents("[role='alertdialog']");
			var $xjjgDialogShade = $('#xjjgDialogDiv').parents("[role='alertdialog']").next();
			$formDialog.css('z-index',parseInt($xjjgDialogShade.css('z-index'))-1);
			$formDialogShade.css('z-index',parseInt($xjjgDialogShade.css('z-index'))-2);
			
			// 点击“信息有误，前去修改”时执行保存方法
			$("#updateJrjgHref").click(function(){
				if (submitUserPrsnInfo(true)) {
					userFirstLoginDlog.close();
					xjjgDialogObj.close();
					// 打开金融机构信息管理页面(传内部机构号)
					windowUtil.openTab("金融机构", '/viewController/finfwhIndex?SJGLJGNBJGH='+nbjgh,'finfwh',true);
				} else {
					return;
				}
			});
			
			// 保存用户现金从业人员信息
			httpUtil.ajax("post","/userManage/selectSubordinateOrgTree",{nbjgh:nbjgh},function(result){
				var zTree = $.fn.zTree.init($("#treeDiv"),setting,result);
				zTree.expandAll(true);
			});
			
//			$.ajax({
//		    	type:"POST",
//				url: url+"userManage/queryDwCode",
//				data:'',
//				dataType:"json",
//				success: function(result) {
//					if(result == "" && result.length <= 0){
//						return;
//					}
//					if(result.code==1000){
//						var treeCheckedData = $('#szdwMcid').val();
//						if(treeCheckedData != undefined){
//							for(var j=0; j < treeCheckedData.length; j++){
//								$.each(result.data,function(i,obj){
//		    				    	if(treeCheckedData == obj.id){ 
//		    				    		result.data[i].checked = true;
//		    				    	}
//		 	    				});
//					        }
//						}
//						$.fn.zTree.init($("#treeDiv"),setting1,result.data);
//						treeObj = $.fn.zTree.getZTreeObj("treeDiv");
//			 			nodes = getCheckedLength(treeObj);
//			 			fuzzySearch('treeDiv','#keyword',null,true); //初始化模糊搜索方法
//					}else{
//						$dialog.alert('拉取地区列表失败！','warning');
//					}
//				}
//		    });
		},
		okValue: '保存',
		ok: function(){
			if (submitUserPrsnInfo(true)) {
				userFirstLoginDlog.close();
			} else {
				return false;
			}
		},
		cancelValue:'返回',
		cancel:function(){}
	}).showModal();
}