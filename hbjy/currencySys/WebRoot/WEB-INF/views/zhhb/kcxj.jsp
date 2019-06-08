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
    <title>企事业单位现金库存及需求情况统计表</title>
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
							<li><span>库存级别</span>
							<select id="KCJB"  name="KCJB" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">全辖</option>
		                        	<option value="1">市级</option>
		                        	<option value="2">县级</option>
		                        
		                    </select>
							</li>
							<li>
							<span>券别</span>
							<select id="QB"  name="QB" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="R01">100元</option>
		                        	<option value="R02">50元</option>
		                        	<option value="R03">20元</option>
		                        	<option value="R04">10元</option>
		                        	<option value="R05">5元</option>
		                        	<option value="R06">1元</option>
		                        	<option value="R00">无法识别</option>
		                    </select>
							</li>
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
							
							
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getKcxjData"/></span></li>
						</ul>
					</form>
					<div class="importBtn" id="importExcel"><a>导入Excel</a></div>
					<div class="exportBtn" id="downTable" ><a>导出报表</a></div>
					<div class="moreSearch" id="doSearch"><a>查询</a></div>
				</div>
				<!--查询条件end-->
				
				<!--主体 start-->
				<div class="consult-right" style="margin-right: -5px;">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-企事业单位现金库存及需求情况统计表  (全量表-半年报)</span>
						</div>
						<div id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="kcxj" name="str"/>
				            	<jsp:param value="SYYH_KCXJ" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/zhhb/kcxj');
		    </script>
		</div>
</body>
</html>