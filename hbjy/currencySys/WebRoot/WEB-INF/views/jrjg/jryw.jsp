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
    <title>银行业金融机构对公现金业务统计表（按行业类别）</title>
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
							<li><span>行业名称</span>
							<input type="text" id="HYMC" name="HYMC" placeholder="支持模糊查询"/>
							<!-- <select id="HYMC"  name="HYMC" class="organizationSelect rightSelect">
			                        <option selected value="" name="--请选择--">--请选择--</option>
									<option value="A01">A01-农、林、牧、渔业</option>
									<option value="A02">A02-采矿业</option>
									<option value="A03">A03-制造业</option>
									<option value="A04">A04-电力、热力、燃气及水生产和供应业</option>
									<option value="A05">A05-建筑业</option>
									<option value="A06">A06-批发和零售业</option>
									<option value="A07">A07-交通运输、仓储和邮政业</option>
									<option value="A08">A08-住宿和餐饮业</option>
									<option value="A09">A09-信息传输、软件和信息技术服务业</option>
									<option value="A10">A10-金融业</option>
									<option value="A11">A11-房地产业</option>
									<option value="A12">A12-租赁和商务服务业</option>
									<option value="A13">A13-科学研究和技术服务业</option>
									<option value="A14">A14-水利、环境和公共设施管理业 </option>
									<option value="A15">A15-居民服务、修理和其他服务业 </option>
									<option value="A16">A16-教育 </option>
									<option value="A17">A17-卫生和社会工作</option>
									<option value="A18">A18-文化、体育和娱乐业</option>
									<option value="A19">A19-公共管理、社会保障和社会组织</option>
									<option value="A20">A20-其他行业</option>

			                    </select> -->
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getJrywData"/></span></li>
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
							<span class="consult-title">搜索结果-银行业金融机构对公现金业务统计表（按行业类别） (季报表-增量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="jryw" name="str"/>
				            	<jsp:param value="SYYH_JRYW" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/jrjg/jryw');
		    </script>
		</div>
</body>
</html>