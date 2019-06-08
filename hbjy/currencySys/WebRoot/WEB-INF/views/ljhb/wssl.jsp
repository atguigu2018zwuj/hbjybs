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
    <title>网点_自助机具_清分设备表</title>
    <jsp:include page="../currencyJsp/header.jsp"></jsp:include>
</head>

<body>
	<div class="consult common" id="sssss">
		<input type="hidden" value="<%=basePath %>deploan" id="searchUrl">
		<input type="hidden" value="<%=basePath %>" id="url">
			<div class="searchMainBox" style="height: auto;">
				<!--报送时间start-->
				<div class="accurateSearchInputs">
					<form id="formSub">
						<ul>
							<li><span>数据日期</span><input type="text" id="beginSjrq" name="beginSjrq" placeholder="数据日期" readonly="readonly"/></li>
							<li><span>金融机构编码</span><input type="text" id="JRJGBM" name="JRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>内部机构号</span><input type="text" id="NBJGH" name="NBJGH" placeholder="支持模糊查询"/></li>
							<li><span>全辖银行营业网点（个）</span><input type="text" id="QXYH_YYWD" name="QXYH_YYWD" placeholder="支持模糊查询"/></li>
							<li><span>抽样银行营业网点（个）</span><input type="text" id="CYYH_YYWD" name="CYYH_YYWD" placeholder="支持模糊查询"/></li>
							<li><span>全辖银行营业网点抽样比例</span><input type="text" id="QXYH_CYBL" name="QXYH_CYBL" placeholder="支持模糊查询"/></li>
							<li><span>全辖银行自助机具（台）</span><input type="text" id="QXYH_ZZJJ" name="QXYH_ZZJJ" placeholder="支持模糊查询"/></li>
							<li><span>抽样银行自助机具（台）</span><input type="text" id="CYYH_ZZJJ" name="CYYH_ZZJJ" placeholder="支持模糊查询"/></li>
							<li><span>全辖银行自助机具抽样比例</span><input type="text" id="QXYH_ZZCYBL" name="QXYH_ZZCYBL" placeholder="支持模糊查询"/></li>
							<!-- <li><span>全辖银行清分设备（台）</span><input type="text" id="QXYH_QFSB" name="QXYH_QFSB" placeholder="支持模糊查询"/></li> -->
							<li><span>清分中心清分设备</span><input type="text" id="QFZX_QFSB" name="QFZX_QFSB" placeholder="支持模糊查询"/></li>
							<li><span>银行网点清分设备</span><input type="text" id="YHWD_QFSB" name="YHWD_QFSB" placeholder="支持模糊查询"/></li>
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getWsslData"/></span></li>
							<!-- <li><span><input type="hidden" id="fieldValues" name="fieldValues"/></span></li> -->
						</ul>
					</form>
					<div class="importBtn" id="importExcel"><a>导入Excel</a></div>
					<div class="exportBtn" id="downTable"><a>导出报表</a></div>
					<div class="moreSearch" id="doSearch"><a>查询</a></div>
					<div class="searchBtn" id="more"><a>更多</a></div>
					<div class="searchBtn" id="hide" style="display: none;"><a>隐藏</a></div>
				</div>
				<!--报送时间end-->
				<!--退出end-->
				<div class="consult-right" style="margin-right: -5px;">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-网点_自助机具_清分设备表  (季报表-全量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="wssl" name="str"/>
				            	<jsp:param value="SYYH_WSSL" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/ljhb/wssl');
		    </script>
		</div>
</body>
</html>