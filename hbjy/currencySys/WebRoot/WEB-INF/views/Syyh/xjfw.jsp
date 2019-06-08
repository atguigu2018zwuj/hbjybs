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
    <title>现金服务企业基本信息表</title>
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
							<li><span>金融机构编码</span><input type="text" id="JRJGBM" name="SHJRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>金融机构内部机构号</span><input type="text" id="NBJGH" name="SHJRJGNBJGH" placeholder="支持模糊查询"/></li>
							<li><span>统一社会信用编码</span><input type="text" id="XJFWDM" name="TYSHXYBM" placeholder="支持模糊查询"/></li>
							<li><span>清分服务方式-驻场式</span><input type="text" id="QF_ZCS" name="QF_ZCS" placeholder="支持模糊查询"/></li>
							<li><span>清分服务方式-离场式</span><input type="text" id="QF_LCS" name="QF_LCS" placeholder="支持模糊查询"/></li>
							<li><span>企业规模（注册资本）-500万元以下</span><input type="text" id="QYGM_1" name="QYGM_1" placeholder="支持模糊查询"/></li>
							<li><span>企业规模（注册资本）-500-1000万元</span><input type="text" id="QYGM_2" name="QYGM_2" placeholder="支持模糊查询"/></li>
							<li><span>企业规模（注册资本）-1000-3000万元</span><input type="text" id="QYGM_3" name="QYGM_3" placeholder="支持模糊查询"/></li>
							<li><span>企业规模（注册资本）-3000万元以上</span><input type="text" id="QYGM_4" name="QYGM_4" placeholder="支持模糊查询"/></li>
							<li><span>经营和服务内容-清分</span><input type="text" id="JFNR_QF" name="JFNR_QF" placeholder="支持模糊查询"/></li>
							<li><span>经营和服务内容-复点</span><input type="text" id="JFNR_FD" name="JFNR_FD" placeholder="支持模糊查询"/></li>
							<li><span>经营和服务内容-销毁</span><input type="text" id="JFNR_XH" name="JFNR_XH" placeholder="支持模糊查询"/></li>
							<li><span>经营和服务内容-押运</span><input type="text" id="JFNR_YY" name="JFNR_YY" placeholder="支持模糊查询"/></li>
							<li><span>经营和服务内容-仓储和装卸</span><input type="text" id="JFNR_CZ" name="JFNR_CZ" placeholder="支持模糊查询"/></li>
							<li><span>经营和服务内容-维保</span><input type="text" id="JFNR_WB" name="JFNR_WB" placeholder="支持模糊查询"/></li>
							<li><span>企业从事现金服务时间-1-3年</span><input type="text" id="QYFW_1" name="QYFW_1" placeholder="支持模糊查询"/></li>
							<li><span>企业从事现金服务时间-3-5年</span><input type="text" id="QYFW_2" name="QYFW_2" placeholder="支持模糊查询"/></li>
							<li><span>企业从事现金服务时间-5年以上</span><input type="text" id="QYFW_3" name="QYFW_3" placeholder="支持模糊查询"/></li>
							<li><span>人员数量</span><input type="text" id="RYSL" name="RYSL" placeholder="支持模糊查询"/></li>
							<li><span>企业具备处理能力</span><input type="text" id="QYNL" name="QYNL" placeholder="支持模糊查询"/></li>
							<li><span>实际业务处理量</span><input type="text" id="SJCL" name="SJCL" placeholder="支持模糊查询"/></li>
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getXjfwData"/></span></li>
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
							<span class="consult-title">搜索结果-现金服务企业基本信息表 		(全量表-年报)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="xjfw" name="str"/>
				            	<jsp:param value="SYYH_XJFW" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/Syyh/xjfw');
		    </script>
		</div>
</body>
</html>