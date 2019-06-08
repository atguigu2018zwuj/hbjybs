<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String provincialUsers = (String)request.getSession().getAttribute("provincialUsers"); 
String nbjgh = (String)request.getSession().getAttribute("nbjgh"); 
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>郑州市银行业金融机构空钞箱统计表</title>
    <jsp:include page="../currencyJsp/header.jsp"></jsp:include>
    <style type="text/css">
    	.datagrid-view {
		　　height: 98px;
		}
		.datagrid-body{
			margin-top: 20px;
		}
    </style>
</head>

<body style="padding:0px;">
	<div class="consult common" id="sssss">
		<input type="hidden" value="<%=basePath %>deploan" id="searchUrl">
		<input type="hidden" value="<%=basePath %>" id="url">
			<div class="searchMainBox" style="height: auto;">
				<!--查询条件start-->
				<div class="accurateSearchInputs">
					<form id="formSub">
						<ul>
							<li><span>数据日期</span><input type="text" id="beginSjrq" name="beginSjrq" placeholder="数据日期" readonly="readonly"/></li>
							<li><span>金融机构编码</span><input type="text" id="JRJGBM" name="JRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>内部机构号</span><input type="text" id="NBJGH" name="NBJGH" placeholder="支持模糊查询"/></li>
						
							<li><span>已销毁数量</span><input type="text" id="YXHSL" name="YXHSL" placeholder="支持模糊查询"/></li>
						    <li><span>未销毁数量</span><input type="text" id="WXHSL" name="WXHSL" placeholder="支持模糊查询"/></li>
						    <li><span>未拆封数量</span><input type="text" id="WCFSL" name="WCFSL" placeholder="支持模糊查询"/></li>
							<li><span>合计</span><input type="text" id="HJ" name="HJ" placeholder="支持模糊查询"/></li>							
							<c:if test="${provincialUsers ==  nbjgh}">
								<li><span>上报类型</span>
								<select id="SBLX"  name="SBLX" class="organizationSelect rightSelect">
			                        	<option selected value="" name="--请选择--">--请选择--</option>
			                        	<option value="0">省级</option>
			                        	<option value="1">地市级</option>
			                        	<option value="2">县级</option>
			                        	<option value="3">营业网点</option>
			                    </select>
								</li>
							</c:if>
							
							
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getKcxbData"/></span></li>
						</ul>
					</form>
					
					<div class="importBtn" id="importExcel"><a>导入Excel</a></div>
					<div class="exportBtn" id="downTable"  ><a>导出报表</a></div>
					<div class="moreSearch" id="doSearch"><a>查询</a></div>
					
				</div>
				<!--查询条件end-->
				
				<!--主体 start-->
				<div class="consult-right" style="margin-right: -5px;">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-郑州市银行业金融机构空钞箱统计表		(增量表-季报)</span>
						</div>
						<div id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="kcxb" name="str"/>
				            	<jsp:param value="SYYH_KCXB" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/zhhb/kcxb');
		    </script>
		</div>
</body>
</html>