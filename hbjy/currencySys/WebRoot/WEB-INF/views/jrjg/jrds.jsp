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
    <title>银行业金融机构对私现金业务统计表</title>
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
							<li><span>项目名称</span>
							<select id="XMMC"  name="XMMC" class="organizationSelect rightSelect">
			                        <option selected value="" name="--请选择--">--请选择--</option>
									<option value="A01">A01-储蓄业务</option>
									<option value="A011">A011-5万元以下</option>
									<option value="A012">A012-5万元(含)-20万元</option>
									<option value="A013">A013-20万元(含)-100万元</option>
									<option value="A014">A014-100万元(含)以上</option>
									<option value="A02">A02-外币兑换业务</option>
									<option value="A03">A03-贵金属买卖业务</option>
									<option value="A04">A04-其他业务</option>
									<option value="A041">A041-现金缴纳水电、煤气、电话等生活费用</option>
									<option value="A042">A042-现金缴纳罚款、税款等</option>
			                    </select>
							</li>
							<li><span>本季度支出金额</span><input type="text" id="JEBJDZC" name="JEBJDZC" placeholder="支持模糊查询"/></li>
							<li><span>本季度收入金额</span><input type="text" id="JEBJDSR" name="JEBJDSR" placeholder="支持模糊查询"/></li>
							<!-- <li><span>充分</span><input type="text" id="CF" name="CF" placeholder="支持模糊查询"/></li> -->
							<c:if test="${provincialUsers ==  nbjgh}">
								<li><span>上报类型</span>
								<select id="SBLX"  name="SBLX" class="organizationSelect rightSelect">
			                        	<option selected value="" name="--请选择--">--请选择--</option>
			                        	<option value="0">0-省级</option>
			                        	<option value="1">1-地市级</option>
			                        	<option value="2">2-县级</option>
			                        	<option value="3">3-营业网点</option>
			                    </select>
								</li>
							</c:if>
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getJrdsData"/></span></li>
							<!-- <li><span><input type="hidden" id="fieldValues" name="fieldValues"/></span></li> -->
						</ul>
					</form>
					<div class="importBtn" id="importExcel"><a>导入Excel</a></div>
					<div class="exportBtn" id="downTable"><a>导出报表</a></div>
					<div class="moreSearch" id="doSearch"><a>查询</a></div>
					<!-- <div class="searchBtn" id="more"><a>更多</a></div> -->
					<div class="searchBtn" id="hide" style="display: none;"><a>隐藏</a></div>
				</div>
				<!--报送时间end-->
				<!--退出end-->
				<div class="consult-right" style="margin-right: -5px;">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-银行业金融机构对私现金业务统计表  (季报表-增量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="jrds" name="str"/>
				            	<jsp:param value="SYYH_JRDS" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/jrjg/jrds');
		    </script>
		</div>
</body>
</html>