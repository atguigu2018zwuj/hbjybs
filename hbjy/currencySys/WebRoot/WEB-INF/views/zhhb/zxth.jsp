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
    <title>中小面额人民币投放、回笼情况统计表</title>
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
							<li><span>单位</span>
							<select id="DW"  name="DW" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>		                        	
		                        	<option value="1">公交公司</option>
		                        	<option value="2">火车站/汽车站</option>
		                        	<option value="3">百货商场</option>
		                        	<option value="4">超市</option>
		                        	<option value="5">农贸市场</option>
		                        	<option value="6">医院</option>
		                        	<option value="7">餐饮店</option>
		                        	<option value="8">个人</option>
		                        	<option value="9">其它</option> 
		                    </select>
							</li>
							<li><span>投放-5元—20元-金额</span><input type="text" id="TF_1_JE" name="TF_1_JE" placeholder="支持模糊查询"/></li>
						    <li><span>投放-1元及以下-金额</span><input type="text" id="TF_2_JE" name="TF_2_JE" placeholder="支持模糊查询"/></li>
						    <li><span>回笼-5元—20元-金额</span><input type="text" id="HL_1_JE" name="HL_1_JE" placeholder="支持模糊查询"/></li>
							<li><span>回笼-1元及以下-金额</span><input type="text" id="HL_2_JE" name="HL_2_JE" placeholder="支持模糊查询"/></li>							
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getZxthData"/></span></li>
						</ul>
					</form>
					<div class="importBtn" id="importExcel"><a>导入Excel</a></div>
					<div class="exportBtn" id="downTable"  ><a>导出报表</a></div>
					<div class="moreSearch" id="doSearch"><a>查询</a></div>
					<c:if test="${provincialUsers ==  nbjgh}">
						<div class="searchBtn" id="more"><a>更多</a></div>
						<div class="searchBtn" id="hide" style="display: none;"><a>隐藏</a></div>
					</c:if>
			
				</div>
				<!--查询条件end-->
				
				<!--主体 start-->
				<div class="consult-right" style="margin-right: -5px;">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-中小面额人民币投放、回笼情况统计表			(增量表-季报)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="zxth" name="str"/>
				            	<jsp:param value="SYYH_ZXTH" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/zhhb/zxth');
		    </script>
		</div>
</body>
</html>