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
    <title>银行业金融机构现金库存券别结构统计表</title>
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
							<li><span>单位名称</span>
							<select id="DWMC"  name="DWMC" class="organizationSelect rightSelect">
			                        <option selected value="" name="--请选择--">--请选择--</option>
									<option value="A01">A01-国有商业银行</option>
									<option value="A011">A011-中国工商银行</option>
									<option value="A012">A012-中国农业银行</option>
									<option value="A013">A013-中国银行</option>
									<option value="A014">A014-中国建设银行</option>
									<option value="A02">A02-股份制商业银行</option>
									<option value="A021">A021-交通银行</option>
									<option value="A022">A022-中信银行</option>
									<option value="A023">A023-中国光大银行</option>
									<option value="A024">A024-中国民生银行</option>
									<option value="A025">A025-华夏银行</option>
									<option value="A026">A026-广发银行</option>
									<option value="A027">A027-招商银行</option>
									<option value="A028">A028-平安银行</option>
									<option value="A029">A029-浦发银行</option>
									<option value="A0210">A0210-兴业银行</option>
									<option value="A0211">A0211-恒丰银行</option>
									<option value="A0212">A0212-浙商银行</option>
									<option value="A0213">A0213-渤海银行</option>
									<option value="A03">A03-中国邮政储蓄银行</option>
									<option value="A04">A04-城市商业银行</option>
									<option value="A05">A05-农村商业银行</option>
									<option value="A06">A06-农村合作银行</option>
									<option value="A07">A07-农村信用社</option>
									<option value="A08">A08-外资银行</option>
									<option value="A09">A09-其他</option>	
			               </select>
							</li>
							<!-- <li><span>分币金额</span><input type="text" id="FB" name="FB" placeholder="支持模糊查询"/></li> -->
							<li><span>合币金额</span><input type="text" id="HJ" name="HJ" placeholder="支持模糊查询"/></li>
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
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getJrjbData"/></span></li>
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
							<span class="consult-title">搜索结果-银行业金融机构现金库存券别结构统计表  (月报表-增量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../currencyJsp/content.jsp">
				            	<jsp:param value="jrjb" name="str"/>
				            	<jsp:param value="SYYH_JRJB" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/jrjg/jrjb');
		    </script>
		</div>
</body>
</html>