<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String username = (String)request.getSession().getAttribute("username"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="X-UA-Compatible" content="IE=8"> 
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>货币金银业务数据采集系统</title>  
	<link href="<%=path %>/static/common/layer/skin/default/layer.css" rel="stylesheet">
	<link href="<%=path %>/static/my/theme/default/images/favicon.ico" rel="shortcut icon">
	<link rel="stylesheet" type="text/css" href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/default/easyui.css" />  
	<link rel="stylesheet" type="text/css" href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/icon.css" /> 
	<link href="<%=path %>/static/common/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="<%=path %>/static/my/css/bootstrap.min.css" />
	<link href="<%=path %>/static/my/css/style.css" rel="stylesheet">
	<link href="<%=path %>/static/my/theme/default/style.css" rel="stylesheet">
	<link href="<%=path %>/static/common/gallery/ztree/3.x/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
	<script src="<%=path %>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
	<script>staticUrl = '<%=path %>/static';</script>
	<script src="<%=path %>/static/common/config.js"></script>
	<script src="<%=path %>/static/my/config.js"></script>
	<script src="<%=path%>/static/common/utils/myUtils.js"></script>
	<style>
	   .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber div{
			 overflow:hidden;
	 		white-space:pre-wrap;
		}
	    .col-md-6{
	    	width: 45%\9;
	    	float: left\9;
	    }
	    .col-md-6 span{
	    	 margin-left: 2px\9; 
	    	 float: left\9;
	    }
	    .consult-kead{
	       line-height: 30px;
	       width: 100px;
	       height: 30px;
	    }
	    body .artui-dialog{
	    	font-size:16px;
	    }
	    .ztree *{
	    	font-size:15px;
	    	font-family:Arial,"Microsoft Yahei","微软雅黑","宋体";
	    }
	</style>
</head>  
<body>  
    <!-- header -->
		<input type="hidden" value="<%=path %>" id="url">
		<input type="hidden" value="<%=basePath %>" id="url1">
		<div class="zl_header">
			<a href="#" class="zl_logo">
				<%-- <img src="<%=path %>/static/my/page/images/logo.png" alt="logo" class="zl_logo_img" height="60" > --%>
			</a>
			<!-- <h1>货币金银业务数据采集系统</h1> -->
			<h1 class="systemLogo left"></h1>
			<div class="zl_right_header">
				<span class="zl_user_header" href="javascript:void(0)" style="cursor:pointer" onclick="showUser()">您好&nbsp;<span id="mouseChangeName" style="cursor:pointer;"><%=username %></span></span>
				<a href="<%=basePath %>logout" class="zl_logout"><img src="<%=path %>/static/my/images/logout.png" alt="logout"></a>
				<script type="text/javascript">
				var dlog;
				define(function(require) {
					var $dialog = require('dialog');
				});
				function showUser() {
					$.ajax({
						type : "POST",
						url : $("#url1").val()+"userManage/getUser",
						data : {
							yhName:"${username}"
						},
						dataType : "json",
						success : function(result) {
							dlog=$dialog(
									{
										title : '个人中心',
										content : "<div style='font-size:14px;text-align:center;'>" 
													+"<div style='margin-bottom: 12px;'>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;：&nbsp;&nbsp;<input type='text' value='${username}' id='zh' readonly /></div>" 
													+"<div style='margin-bottom: 12px;'>新&nbsp;&nbsp;密&nbsp;&nbsp;码&nbsp;&nbsp;：&nbsp;&nbsp;<input type='password' id='xmm'/></div>"
													+"<div style='margin-bottom: 12px;'>确认密码&nbsp;&nbsp;：&nbsp;&nbsp;<input type='password' id='qrmm'/></div>"
													+"<div style='margin-bottom: 12px;'>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;：&nbsp;&nbsp;<input type='text' value='"+result.NAME+"' id='xm'/></div>"
													+"<div style='margin-bottom: 12px;'>单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位&nbsp;&nbsp;：&nbsp;&nbsp;<input type='text' value='${userszdwmc}' id='dw' readonly /></div>"
													+"<div style='margin-bottom: 12px;'>部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;门&nbsp;&nbsp;：&nbsp;&nbsp;<input type='text' value='"+result.SSBM+"' id='bm'/></div>"
													+"<div style='margin-bottom: 12px;'>电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话&nbsp;&nbsp;：&nbsp;&nbsp;<input type='text' value='"+result.TELEPH+"' id='dh'/></div>"
													+"<div style='margin-bottom: 12px;' id='cwts'></div>"
                                                +'<input type="button" onclick="noHzBs()" style="margin-right: 10px;" class="btn btn-primary" value="取消"/><input type="button" onclick="isHzBs()" class="btn btn-primary" value="确认"/></div>',
										icon : 'succeed',
										width : '400px',
										height : '350px',
										follow : document.getElementById('btn2')
									}).showModal();
							}
						});
					}
				function noHzBs(){
						dlog.close().remove();
				}
				function isHzBs(){
						var flag=false;
						var xmm= $("#xmm").val();
						var qrmm= $("#qrmm").val();
						var xm= $("#xm").val();
						var bm= $("#bm").val();
						var dh= $("#dh").val();
						var reg = /^\s*$/g;
						if(xmm!=null && xmm!=""){
							if(reg.test(xmm)||xmm.length<6||xmm.length>18){
								$("#cwts").css("color","red");
								$("#cwts").html("新密码长度为6-18位且不能出现空格");
								return;
							}
							if(qrmm!=xmm){
								$("#cwts").css("color","red");
								$("#cwts").html("确认密码与新密码不一致！");
								return;
							}
							flag=true;
						}
						if(reg.test(xm)){
							$("#cwts").css("color","red");
							$("#cwts").html("姓名不能为空且不能出现空格！");
							return;
						}
						if(reg.test(bm)){
							$("#cwts").css("color","red");
							$("#cwts").html("部门不能为空且不能出现空格！");
							return;
						}
						if(reg.test(dh)){
							$("#cwts").css("color","red");
							$("#cwts").html("电话不能为空且不能出现空格！");
							return;
						}
						$("#cwts").css("color","white");
						$.ajax({
							type : "POST",
							url : $("#url1").val()+"userManage/updateUser",
							data : {
								xmm:xmm,
								xm:xm,
								bm:bm,
								dh:dh
							},
							dataType : "text",
							success : function(result) {
								var updatemsg='系统异常，修改失败！';
								var color='red';
								if(result=="ok"){
									updatemsg='资料修改成功！';
									color='green';
									if(flag){
										updatemsg='密码修改成功！需重新登录';
									}
								}
								dlog.close().remove();
								$dialog({
									title : '修改提示',
									content : "<div style='font-size:16px;text-align:center;color:"+color+"'>"+updatemsg+"</div>",
									icon : 'succeed',
									width : '200px',
									height : '40px',
									follow : document.getElementById('btn2')
								}).showModal(); 
								if(flag){
									setTimeout('openUrl()',2000)
								}
							}
						});
					}
				function openUrl(){
					window.location.href=$("#url1").val()+'logout';
				}
				</script>
			</div>
		</div>
		<div class="zl_container">
			<div class="zl_nav">
					<p class="zl_collapse zl_collapse_open" > << </p>
					<ul id="zl_menu_nav">
						
					</ul>  
			</div>
				<!-- <div id="line"></div> -->
			
			<div class="zl_main">
				<div id="mainBox" class="zl_zcsyqll" style="height: 100%;width: 100%;">
					<div class="zl_main_content" id="zl_main_content" style="height: 100%;width: 100%;">
			        	<div id="mainBox" class="zl_zcsyqll" style="height: 100%;width: 100%;">			        		
			        		<div class="easyui-tabs" fit="true" border="false" id="tabs"> 
			        		<div title="首页" id="infoDiv"> 
				             <iframe id="infopage" frameborder="0"  src="<%=basePath %>/viewController/bankhome" style="margin:0px 0px;">
	
								    </iframe>   
				            </div>
			        		
			        		</div> 
				            
						</div>
			        </div>
		        </div>
			</div>
		</div>
		
		<!--关闭tab选项菜单-->  
		<div id="tab_rightmenu" class="easyui-menu" style="width:120px; display:none;">   
		    <div name="tab_menu-tabcloseall" id="closeAll">  
		    	关闭全部标签  
		    </div>   
		    <div name="tab_menu-tabcloseother" id="closeOthor">  
		   		关闭其他标签  
		    </div>   
		    <div class="menu-sep"></div>   
			<div name="tab_menu-tabcloseright" id="closeRight">  
				关闭右侧标签  
			</div>      
			<div name="tab_menu-tabcloseleft" id="closeLeft">  
				关闭左侧标签  
			</div>   
		</div> 
		
    <script type="text/javascript">
		   seajs.use('myJsPath/js/bankIndex');
	</script>
</body>  
</html> 