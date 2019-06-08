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
    <title>金融机构现金收支流向情况统计表</title>
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
							
							<li><span>库存级别</span>
							<select id="KCJB"  name="KCJB" class="organizationSelect rightSelect">
	                        	<option selected value="" name="--请选择--">--请选择--</option>
	                        	<option value="0">全辖</option>
	                        	<option value="1">市级</option>
	                        	<option value="2">县级</option>
	                    	</select>
							</li>
							<li><span>券别</span>
							<select id="QB"  name="QB" class="organizationSelect rightSelect">
	                        	<option selected value="" name="--请选择--">--请选择--</option>
	                        	<option value="R01">100元</option>
	                        	<option value="R02">50元</option>
	                        	<option value="R03">20元</option>
	                        	<option value="R04">10元</option>
	                        	<option value="R05">5元</option>
	                        	<option value="R061">1元纸币</option>
	                        	<option value="R062">1元硬币</option>
	                        	<option value="R071">5角纸币</option>
	                        	<option value="R072">5角硬币</option>
	                        	<option value="R081">1角纸币</option>
	                        	<option value="R082">1角硬币</option>
	                    	</select>
							</li>
							<li><span>柜面现金收入-机关</span><input type="text" id="KMXJSR_JG" name="KMXJSR_JG" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金收入-事业</span><input type="text" id="KMXJSR_SY" name="KMXJSR_SY" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金收入-企业-大</span><input type="text" id="KMXJSR_QY_D" name="KMXJSR_QY_D" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金收入-企业-中</span><input type="text" id="KMXJSR_QY_Z" name="KMXJSR_QY_Z" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金收入-企业-小</span><input type="text" id="KMXJSR_QY_X" name="KMXJSR_QY_X" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金收入-个体工商户</span><input type="text" id="KMXJSR_GTGSH" name="KMXJSR_GTGSH" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金收入-居民</span><input type="text" id="KMXJSR_JM" name="KMXJSR_JM" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金支出-机关</span><input type="text" id="KMXJZC_JG" name="KMXJZC_JG" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金支出-事业</span><input type="text" id="KMXJZC_SY" name="KMXJZC_SY" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金支出-企业-大</span><input type="text" id="KMXJZC_QY_D" name="KMXJZC_QY_D" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金支出-企业-中</span><input type="text" id="KMXJZC_QY_Z" name="KMXJZC_QY_Z" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金支出-企业-小</span><input type="text" id="KMXJZC_QY_X" name="KMXJZC_QY_X" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金支出-个体工商户</span><input type="text" id="KMXJZC_GTGSH" name="KMXJZC_GTGSH" placeholder="支持模糊查询"/></li>
							<li><span>柜面现金支出-居民</span><input type="text" id="KMXJZC_JM" name="KMXJZC_JM" placeholder="支持模糊查询"/></li>
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getSzlxData"/></span></li>
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
							<span class="consult-title">搜索结果-金融机构现金收支流向情况统计表  (半年报-增量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="szlx" name="str"/>
				            	<jsp:param value="SYYH_SZLX" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/ljhb/szlx');
		    </script>
		</div>
</body>
</html>