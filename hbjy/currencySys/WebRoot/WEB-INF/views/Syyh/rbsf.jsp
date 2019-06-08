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
    <title>银行业金融机构人民币收付业务开展情况统计表</title>
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
							<li><span>所属金融机构编码</span><input type="text" id="JRJGBM" name="JRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>所属金融机构内部机构号</span><input type="text" id="NBJGH" name="NBJGH" placeholder="支持模糊查询"/></li>
							<li><span>所在区域</span><input type="text" id="SZQY" name="SZQY" placeholder="支持模糊查询"/></li>
							<li><span>现金收入-柜面业务收入-本期</span><input type="text" id="XJSR_GM_BQ" name="XJSR_GM_BQ" placeholder="支持模糊查询"/></li>
							<li><span>现金收入-柜面业务收入-年累计</span><input type="text" id="XJSR_GM_NLJ" name="XJSR_GM_NLJ" placeholder="支持模糊查询"/></li>
							<li><span>现金收入-自助设备收入-本期</span><input type="text" id="XJSR_ZZ_BQ" name="XJSR_ZZ_BQ" placeholder="支持模糊查询"/></li>
							<li><span>现金收入-自助设备收入-年累计</span><input type="text" id="XJSR_ZZ_NLJ" name="XJSR_ZZ_NLJ" placeholder="支持模糊查询"/></li>
							<li><span>现金支出-柜面业务支出-本期</span><input type="text" id="XJZC_GM_BQ" name="XJZC_GM_BQ" placeholder="支持模糊查询"/></li>
							<li><span>现金支出-柜面业务支出-年累计</span><input type="text" id="XJZC_GM_NLJ" name="XJZC_GM_NLJ" placeholder="支持模糊查询"/></li>
							<li><span>现金支出-自助设备支出-本期</span><input type="text" id="XJZC_ZZ_BQ" name="XJZC_ZZ_BQ" placeholder="支持模糊查询"/></li>
							<li><span>现金支出-自助设备支出-年累计</span><input type="text" id="XJZC_ZZ_NLJ" name="XJZC_ZZ_NLJ" placeholder="支持模糊查询"/></li>
							<li><span>残损币兑换-笔数-本期</span><input type="text" id="CSBDH_BS_BQ" name="CSBDH_BS_BQ" placeholder="支持模糊查询"/></li>
							<li><span>残损币兑换-笔数-年累计</span><input type="text" id="CSBDH_BS_NLJ" name="CSBDH_BS_NLJ" placeholder="支持模糊查询"/></li>
							<li><span>残损币兑换-金额-本期</span><input type="text" id="CSBDH_JE_BQ" name="CSBDH_JE_BQ" placeholder="支持模糊查询"/></li>
							<li><span>残损币兑换-金额-年累计</span><input type="text" id="CSBDH_JE_NLJ" name="CSBDH_JE_NLJ" placeholder="支持模糊查询"/></li>
							<li><span>券别调剂-笔数-本期</span><input type="text" id="QBTJ_BS_BQ" name="QBTJ_BS_BQ" placeholder="支持模糊查询"/></li>
							<li><span>券别调剂-笔数-年累计</span><input type="text" id="QBTJ_BS_NLJ" name="QBTJ_BS_NLJ" placeholder="支持模糊查询"/></li>
							<li><span>券别调剂-金额-本期</span><input type="text" id="QBTJ_JE_BQ" name="QBTJ_JE_BQ" placeholder="支持模糊查询"/></li>
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getRbsfData"/></span></li>
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
							<span class="consult-title">搜索结果-银行业金融机构人民币收付业务开展情况统计表		(全量表-季报)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="rbsf" name="str"/>
				            	<jsp:param value="SYYH_RBSF" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/Syyh/rbsf');
		    </script>
		</div>
</body>
</html>