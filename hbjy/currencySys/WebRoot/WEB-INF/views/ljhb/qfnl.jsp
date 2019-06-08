<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String provincialUsers = (String)request.getSession().getAttribute("provincialUsers"); 
String nbjgh = (String)request.getSession().getAttribute("nbjgh"); 
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>金融机构现金清分能力统计表</title>
    <jsp:include page="../currencyJsp/header.jsp"></jsp:include>
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
							<li><span>数据日期</span><input type="text" id="beginSjrq" name="beginSjrq" placeholder="数据日期" readonly="readonly"/></li>
							<li><span>金融机构编码</span><input type="text" id="JRJGBM" name="JRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>内部机构号</span><input type="text" id="NBJGH" name="NBJGH" placeholder="支持模糊查询"/></li>
							
							<li><span>网点总数</span><input type="text" id="WDZS" name="WDZS" placeholder="支持模糊查询"/></li>
							<li><span>网点清分-已配清分机的网点</span><input type="text" id="WDQF_YPWD" name="WDQF_YPWD" placeholder="支持模糊查询"/></li>
							<li><span>网点清分-清分机配置-纸币清分机</span><input type="text" id="WDQF_QFJPZ_ZB" name="WDQF_QFJPZ_ZB" placeholder="支持模糊查询"/></li>
							<li><span>网点清分-清分机配置-硬币清分机</span><input type="text" id="WDQF_QFJPZ_YB" name="WDQF_QFJPZ_YB" placeholder="支持模糊查询"/></li>
							<li><span>集中清分-清分中心</span><input type="text" id="JZQF_QFZX" name="JZQF_QFZX" placeholder="支持模糊查询"/></li>
							<li><span>集中清分-清分机配置-纸币清分机</span><input type="text" id="JZQF_QFJPZ_ZB" name="JZQF_QFJPZ_ZB" placeholder="支持模糊查询"/></li>
							<li><span>集中清分-清分机配置-硬币清分机</span><input type="text" id="JZQF_QFJPZ_YB" name="JZQF_QFJPZ_YB" placeholder="支持模糊查询"/></li>
							<li><span>月清分捆数</span><input type="text" id="YQFKS" name="YQFKS" placeholder="支持模糊查询"/></li>
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getQfnlData"/></span></li>
<!-- 							<li><span><input type="hidden" id="fieldValues" name="fieldValues"/></span></li> -->						</ul>
					</form>
					<div class="importBtn" id="importExcel"><a>导入Excel</a></div>
					<div class="exportBtn" id="downTable"><a>导出报表</a></div>
					<div class="moreSearch" id="doSearch"><a>查询</a></div>
					<div class="searchBtn" id="more"><a>更多</a></div>
					<div class="searchBtn" id="hide" style="display: none;"><a>隐藏</a></div>
				</div>
				<!--查询条件end-->
				
				<!--主体 start-->
				<div class="consult-right" style="margin-right: -5px;">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-金融机构现金清分能力统计表  (季报表-全量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="qfnl" name="str"/>
				            	<jsp:param value="SYYH_QFNL" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
				<!--主体 end-->
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/ljhb/qfnl');
		    </script>
		</div>
</body>
</html>