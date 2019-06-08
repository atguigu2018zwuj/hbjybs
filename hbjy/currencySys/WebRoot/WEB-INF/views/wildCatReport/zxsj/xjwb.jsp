<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>现金处理外包服务信息表</title>
    <jsp:include page="../../currencyJsp/header.jsp"></jsp:include>
</head>

<body>
	<div class="consult common" id="sssss">
		<input type="hidden" value="<%=basePath %>deploan" id="searchUrl">
		<input type="hidden" value="<%=basePath %>" id="url">
			<div class="searchMainBox" style="height: auto;">
				<!--查询条件start-->
				<div class="accurateSearchInputs">
					<form id="formSub">
						<ul>
							<li><span>数据日期</span><input id="beginSjrq" name="beginSjrq" type="text" class="Wdate" placeholder="数据日期"/></li>
							<li><span>采购外包服务单位金融机构编码</span><input type="text" id="WBFWJRJGBM" name="WBFWJRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>采购外包服务单位内部机构号</span><input type="text" id="WBFWNBJGH" name="WBFWNBJGH" placeholder="支持模糊查询"/></li>
							<li><span>现金处理外包服务商</span><input type="text" id="XJCLWBS" name="XJCLWBS" placeholder="支持模糊查询"/></li>
							<li><span>外包服务商统一社会社会信用代码</span><input type="text" id="WBFWTYSHXYDM" name="WBFWTYSHXYDM" placeholder="支持模糊查询"/></li>
							<li><span>合同开始时间</span><input type="text" id="HTKSSJ" name="HTKSSJ" placeholder="数据日期" readonly="readonly"/></li>
							<li><span>合同结束时间</span><input type="text" id="HTJSSJ" name="HTJSSJ" placeholder="数据日期" readonly="readonly"/></li>
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getXjwbData"/></span></li>
						</ul>
					</form>
					<div class="importBtn" id="importExcel"><a>导入Excel</a></div>
					<div class="exportBtn" id="downTable"><a>导出报表</a></div>
					<div class="moreSearch" id="doSearch"><a>查询</a></div>
				</div>
				<!--查询条件end-->
				
				<!--主体 start-->
				<div class="consult-right" style="margin-right: -5px;">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-现金处理外包服务信息     (日报表-拉链表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../../currencyJsp/content.jsp">
				            	<jsp:param value="xjwb" name="str"/>
				            	<jsp:param value="SPECIAL_XJWB" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
				<!--主体 end-->
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/wildCatReport/zxsj/xjwb');
		    </script>
		</div>
</body>
</html>