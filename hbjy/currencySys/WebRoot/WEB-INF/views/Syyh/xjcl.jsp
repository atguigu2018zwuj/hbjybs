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
    <title>银行业金融机构现金处理情况调查表</title>
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
							<li><span>数据日期</span><input type="text" id="SJRQ" name="SJRQ" placeholder="数据日期" readonly="readonly"/></li>
							<li><span>金融机构编码</span><input type="text" id="JRJGBM" name="JRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>金融机构内部机构号</span><input type="text" id="NBJGH" name="NBJGH" placeholder="支持模糊查询"/></li>
							<li><span>清分方式-本行自行组织现金处理</span><input type="text" id="QFFS_ZXCL" name="QFFS_ZXCL" placeholder="支持模糊查询"/></li>
							<li><span>清分方式-劳务外包-驻场式</span><input type="text" id="QFFS_ZCS" name="QFFS_ZCS" placeholder="支持模糊查询"/></li>
							<li><span>清分方式-劳务外包-离场式</span><input type="text" id="QFFS_LCS" name="QFFS_LCS" placeholder="支持模糊查询"/></li>
							<li><span>清分组织-后台集中清分</span><input type="text" id="QFFS_JZQF" name="QFFS_JZQF" placeholder="支持模糊查询"/></li>
							<li><span>清分组织-网点分散清分</span><input type="text" id="QFFS_FSQF" name="QFFS_FSQF" placeholder="支持模糊查询"/></li>
							<li><span>是否硬币处理</span><input type="text" id="YBCL" name="YBCL" placeholder="支持模糊查询"/></li>
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getXjclData"/></span></li>
						</ul>
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
							<span class="consult-title">搜索结果-银行业金融机构现金处理情况调查表		(全量表-年报)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="xjcl" name="str"/>
				            	<jsp:param value="SYYH_XJCL" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/Syyh/xjcl');
		    </script>
		</div>
</body>
</html>