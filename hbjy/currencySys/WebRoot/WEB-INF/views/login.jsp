<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8">
		<title>货币金银业务数据采集系统</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta http-equiv="X-UA-Compatible" content="IE=9">
		<meta name="viewport" content="width=device-width, initial-scale=1"> 
    	<link href="<%=path %>/static/my/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<link href="<%=path %>/static/my/css/bankCommon.css" rel="stylesheet">
		<%-- <link href="<%=path %>/static/my/css/style.css" rel="stylesheet"> --%>
		<script src="<%=path %>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
		<script> staticUrl = '<%=path %>/static';  </script>
		<script src="<%=path %>/static/common/config.js"></script>
		<script src="<%=path %>/static/my/config.js"></script>
		<style type="text/css">
			html{
				width:100%;height:100%;
			}
			.login .login-form {
   			 	width: 38%;
				/* zoom:1\9; */
   			 }
			.code{
				background-color:#6ed5d0;
				color:blue;
				border:0;
				padding:2px 3px;
				letter-spacing:3px;
				float:left;            
				cursor:pointer;
				width:60px;
				height:25px;
				line-height:20px;
				text-align:center;
				vertical-align:middle;
				margin-left: 5px;
	    	}
			/* .login .form-title {
					font-size: 30px;
				} */
			.login .bank-but {
				margin-top: 10px;
			}
			 .logo {
            margin-top: 65px;
            margin-left: 94px;
            } 
             .buttonOne {
		    width: 95%;
            height: 50px;
            line-height: 50px;
            font-size: 21px;
            background: #017cd3;
            border: 0;
            outline: none;
            border-radius: 5px;
            color: #fff;
            }  
            header{
            width: 100%;
            margin: 100px 0 50px 0; 
            margin-top:30px;
            line-height:0.01px;
            font-size: 28px;
            text-align: center;
            color: #fff;
            }
            .building{
        	position: absolute;
        	top: 24.14%;
            left: 8.85%;
			z-index: 1;
			width: 46rem;
			height: 46rem;
			background: url("<%=path %>/static/my/page/images/building.png") no-repeat;
			background-size: contain;
        }
       <%--  .logo {
        	position: absolute;
            top: 0.65rem;
            left: 0.94rem;
            z-index: 3;
            width: 30rem;
			height: 0.64rem;
            background: url("<%=path %>/static/my/page/images/logo.png") no-repeat;
            background-size: contain;
            overflow: hidden;
            text-indent: -100rem;
        } --%>
		</style>
</head>

<body style="width:100%; height:100%;">
<input type="hidden" value="<%=basePath %>" id="url">
		<div class="login">
		    <img class="logo" src="<%=path %>/static/my/page/images/logo.png" height="45" alt=""> 
			<!-- 主体 start -->
			<!-- <h1 class="logo">河南省农村信用社</h1> -->
		    <div class="building"></div> 
			<div class="login-form" style="width: 480px;height: 510px;transform:scale(.70);" >
				<div class="shadow_rel">
					<!-- <p class="header">货币金银业务数据采集系统</p> -->
					<header>货币金银业务数据采集系统</header>
					<form id="subForm">
						<input id="subUrl" type="hidden" value="<%=basePath %>" />
						<input type="hidden" id="password" name="password" />
						<div class="form-import">
							<div class="bank-account clearfix">
								<label class="urseName" for="urseName" style="width: 95%;height: 55px;">
									 <img src="<%=path %>/static/my/images/user_login.png" alt="账号" class="name-icon"/> 
									 <%-- <img src="<%=path %>/static/my/page/images/user-icon.png" alt="账号" class="name-icon"/> --%>
									<input type="text" id="urseName" name="user_id" placeholder="请输入柜员号"/>
								</label>
								<div class="validation-tips" id="user" style="height: 30px;"></div>
							</div>
							<div class="bank-password clearfix">
								<label class="ursePassword" for="ursePassword" style="width: 95%;height: 55px;">
									<img src="<%=path %>/static/my/images/password_login.png" alt="密码" class="password-icon"/>
									<input type="password" id="ursePassword" placeholder="请输入密码"/>
								</label>						
								<div class="validation-tips" id="password" style="height: 30px;"></div>
							</div>
							 <%-- <div class="bank-verify clearfix">
								<label class="urseVerify" for="urseVerify" style="width: 72%;height: 40px;">
									<img src="<%=path %>/static/my/images/verify_login.png" alt="验证" class="verify-icon"/>
									<input type="text" id="urseVerify"/>
								</label>
								<div class="code" id="checkCode" style="padding: 10px 3px;height: 40px;width: 95px;font-size: 22px;"></div>
								<div class="validation-tips" style="height: 30px;"></div>
							</div>  --%>
						</div>
					</form>
					
					<div class="bank-but clearfix" style="margin-top:30px;">
		                  <button type="button" class="buttonOne" id="subButton">登录  </button>  
					</div>
				</div>
			</div>
				
			<!-- 主体 end -->
			<script type="text/javascript">
			seajs.use('myJsPath/js/bankLogin');
			</script>
			
	   </div>
  </body>
</html>