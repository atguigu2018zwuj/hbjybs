<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>金融机构业务库存情况表</title>
    <jsp:include page="../../currencyJsp/header.jsp"></jsp:include>
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
							<li><span>券别</span>
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
							<li><span>材质</span>
							<select id="CZ"  name="CZ" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">硬币</option>
		                        	<option value="1">纸币</option>
		                    </select>
							</li>
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getKcqkData"/></span></li>
							<!-- <li><span><input type="hidden" id="fieldValues" name="fieldValues"/></span></li> -->
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
							<span class="consult-title">搜索结果-金融机构业务库存情况表  (日报表-增量表)</span>
						</div>
						<div id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../../currencyJsp/content.jsp">
				            	<jsp:param value="kcqk" name="str"/>
				            	<jsp:param value="SPECIAL_KCQK" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/sgsbCode/zxsj/kcqk');
		    </script>
		</div>
</body>
</html>