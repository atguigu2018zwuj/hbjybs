<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>冠字号文件信息表</title>
    <jsp:include page="../../currencyJsp/header.jsp"></jsp:include>
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
							<li><span>上报业务序列号</span><input type="text" id="SBYWXLH" name="SBYWXLH" placeholder="支持模糊查询"/></li>
							<li><span>钞票状态</span>
								<select id="CPZT"  name="CPZT" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="1">真币</option>
		                        	<option value="2">假币</option>
		                        	<option value="3">残币</option>
		                        	<option value="4">旧币</option>
		                  	    </select>
							</li>
							<li><span>错误代码</span><input type="text" id="CWDM" name="CWDM" placeholder="支持模糊查询"/></li>
							<li><span>币种</span>
							<input type="text" id="BZ" name="BZ" placeholder="支持模糊查询"/>
								<!-- <select id="BZ"  name="BZ" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="USD">美元</option>
		                        	<option value="EUR">欧元</option>
		                        	<option value="GBP">英镑</option>
		                        	<option value="CNY">人民币</option>
		                        	<option value="JPY">日元</option>
		                        	<option value="AUD">澳元</option>
		                        	<option value="HKD">港元</option>
		                        	<option value="TL">新里拉</option>
		                        	<option value="CAD">加元</option>
		                        	<option value="RUB">卢布</option>
		                        	<option value="KRW">韩元</option>
		                  	    </select> -->
							</li>
							<li><span>钞票版别</span>
								<select id="CPBB"  name="CPBB" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="B01">1996版</option>
		                        	<option value="B02">1999版</option>
		                        	<option value="B03">2005版</option>
		                        	<option value="B04">2015版</option>
		                        	<option value="B99">外币</option>
		                  	    </select>
							</li>
							<li><span>币值</span>
								<select id="BIZHI"  name="BIZHI" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="R00">无法识别</option>
		                        	<option value="R061">1元</option>
		                        	<option value="R05">5元</option>
		                        	<option value="R04">10元</option>
		                        	<option value="R03">20元</option>
		                        	<option value="R02">50元</option>
		                        	<option value="R01">100元</option>
		                  	    </select>
							</li>
							<li><span>冠字号文本</span><input type="text" id="GZHWB" name="GZHWB" placeholder="支持模糊查询"/></li>
							<li><span>钞票类型</span>
								<select id="CPLX"  name="CPLX" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">未定义</option>
		                        	<option value="1">自动柜员配钞券</option>
		                        	<option value="2">一般完整券</option>
		                        	<option value="3">可疑券</option>
		                  	    </select>
							</li>
							<li><span>机具编码</span><input type="text" id="JIJUBM" name="JIJUBM" placeholder="支持模糊查询"/></li>
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getGzhmData"/></span></li>
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
							<span class="consult-title">搜索结果-冠字号文件信息表     (日报表-增量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../../currencyJsp/content.jsp">
				            	<jsp:param value="gzhm" name="str"/>
				            	<jsp:param value="SPECIAL_GZHM" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
				<!--主体 end-->
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/wildCatReport/zxsj/gzhm');
		    </script>
		</div>
</body>
</html>