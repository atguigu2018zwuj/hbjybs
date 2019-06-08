<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!--head  start-->
	<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="X-UA-Compatible" content="IE=8">
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
    <link href="<%=path %>/static/common/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/my/css/bootstrap.min.css" />
    <link href="<%=path %>/static/common/gallery/select2/3.5.1/select2.css" rel="stylesheet" />
    <link href="<%=path %>/static/my/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="<%=path %>/static/my/theme/default/style.css" rel="stylesheet">
    <link href="<%=path %>/static/my/css/bankCommon.css" rel="stylesheet">
    <script src="<%=path %>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
    <script src="<%=path%>/static/common/utils/myUtils.js"></script>
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
<!--head  end-->
