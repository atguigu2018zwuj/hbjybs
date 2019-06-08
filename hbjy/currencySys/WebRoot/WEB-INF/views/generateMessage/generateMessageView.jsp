<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>生成报文</title>
    <link href="<%=path %>/static/common/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/my/css/bootstrap.min.css" />
    <link href="<%=path %>/static/common/gallery/select2/3.5.1/select2.css" rel="stylesheet" />
    <link href="<%=path %>/static/my/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="<%=path %>/static/my/theme/default/style.css" rel="stylesheet">
    <link href="<%=path %>/static/my/css/bankCommon.css" rel="stylesheet">
    <script src="<%=path %>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
    <script>
    staticUrl = '<%=request.getContextPath() %>/static';
    </script>
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/common/gallery/datetimepicker/2.0.0/datetimepicker.css" />
    <script src="<%=path %>/static/common/config.js"></script>
    <script src="<%=path %>/static/my/config.js"></script>
    <link href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/gray/easyui.css" rel="stylesheet" />
    <link href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/icon.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/common/jquery-easyui-1.5.3/demo/demo.css">
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/default/easyui.css">
    <style type="text/css">
    .accurateSearchInputs ul{
		height: 40px;
	}
	.searchMainBox{
		margin-bottom: 0px;
		width: 100%;
	}
    </style>
</head>

<body style="padding:0px;">
<!-- 	<div class="consult common" id="sssss"> -->
		<input type="hidden" value="<%=basePath %>deploan" id="searchUrl">
		<input type="hidden" value="<%=basePath %>" id="url">
			<!-- <div class="searchMainBox" style="height: auto"> -->
				<!--报送时间start-->
				<div class="accurateSearchInputs">
					<form id="formSub">
						<ul>
							<li><span>数据日期</span><input type="text" id="beginDate" name="beginDate" placeholder="数据日期" readonly="readonly"/></li>
							<li><span>业务名</span><input type="text" id="ywm" name="ywm" placeholder="支持模糊查询"/></li>
							<li><span>表名</span><input type="text" id="bm" name="bm" placeholder="支持模糊查询"/></li>
						</ul>
					</form>
					<div class="importBtn" id="bwsbId"><a>报文上报</a></div>
					<div class="moreSearch" id="downFile"><a>报文下载</a></div>
					<div class="exportBtn" id="checkData" ><a>报文校验</a></div>
					<div class="searchBtn" id="scbwId"><a>报文生成</a></div>
				</div>
				<!--报送时间end-->
				<!--退出end-->
				<div class="consult-right" style="margin-right: -5px;height: 70%">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-报文管理</span>
						</div>
						<div class="" id="myTable" style="display:none;height: 75%;width: auto">
							<table id="test" class="easyui-treegrid" singleSelect ="true" fitcolumns="false" 
								idField="id" treeField="text" checkbox="true" style="height: auto;width: 100%">
							<thead>
								<tr>
									<th field="text" width="60%">业务名</th>
									<th field="key" width="30%">表名</th>
									<th field="scbwxx" width="10%" data-options="formatter:feneratemessage">标识</th>
								</tr>
							</thead>
						</table>
						</div>
					</div>	
				</div>
			<!-- </div> -->
			<script type="text/javascript">
			seajs.use('myJsPath/js/generateMessage/generateMessageView');
		    </script>
		<!-- </div> -->
</body>
</html>