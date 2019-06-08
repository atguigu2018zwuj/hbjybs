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
    <title>银行业金融机构现金收支情况统计表</title>
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
										<option value="A00">A00-对公收入</option>
										<option value="A01">A01-对私收入</option>
										<option value="A010">A010-柜面业务收入</option>
										<option value="A011">A011-自助设备收入</option>
										<option value="A02">A02-外部项目收入合计</option>
										<option value="A03">A03-同业间现金存入</option>
										<option value="A04">A04-现金收入总计</option>
										<option value="B00">B00-对公支出</option>
										<option value="B01">B01-对私支出</option>
										<option value="B010">B010-柜面业务支出</option>
										<option value="B011">B011-自助设备支出</option>
										<option value="B02">B02-外部项目支出合计</option>
										<option value="B03">B03-同业间现金支出</option>
										<option value="B04">B04-现金支出总计</option>
										<option value="C00">C00-净投放（+）或净回笼（-）</option>
			                    </select>
							</li>
							<li><span>金额本期</span><input type="text" id="JEBQ" name="JEBQ" placeholder="支持模糊查询"/></li>
							<li><span>金额年累计</span><input type="text" id="JENLJ" name="JENLJ" placeholder="支持模糊查询"/></li>
							<!-- <li><span>充分</span><input type="text" id="CF" name="CF" placeholder="支持模糊查询"/></li> -->
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getJrszData"/></span></li>
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
							<span class="consult-title">搜索结果-银行业金融机构现金收支情况统计表  (季报表-增量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="jrsz" name="str"/>
				            	<jsp:param value="SYYH_JRSZ" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/jrjg/jrsz');
		    </script>
		</div>
</body>
</html>