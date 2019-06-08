<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>自定义报表</title>
    <link rel="stylesheet" href="<%=path %>/static/my/js/common/wijmo/wijmo.min.css">
    <link rel="stylesheet" href="<%=path %>/static/my/js/common/wijmo/material.indigo-red.min.css" />
    <link href="<%=path %>/static/my/theme/default/images/favicon.ico" rel="shortcut icon">
    <link href="<%=path %>/static/common/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path %>/static/my/theme/default/style.css" rel="stylesheet">
    <link href="<%=path %>/static/my/css/bankCommon.css" rel="stylesheet">
    <script src="<%=path %>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
    <script>staticUrl = '<%=path %>/static';</script>
    <script src="<%=path %>/static/common/config.js"></script>
    <script src="<%=path %>/static/my/config.js"></script>
    <script src="<%=path%>/static/common/utils/myUtils.js"></script>
</head>

<body>
    <div class="consult common">
        <!-- 主体 start -->
<%--        <jsp:include page="../header.jsp"></jsp:include> --%>
       
        <div class="mainAnalysis">
        <div style="margin-left: 40px;">
        </div>
        <!-- 多维页面 -->
       		<div id="analysisbox" style="width:100%;height:700px">
       			
       		</div>
        </div>
        <!-- 主体 end -->
    </div>
    <script type="text/javascript">seajs.use('myJsPath/js/customReport/customReport');</script>
</body>

</html>
