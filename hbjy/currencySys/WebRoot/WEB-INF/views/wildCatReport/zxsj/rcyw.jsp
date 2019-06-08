<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>商业银行日常业务表</title>
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
							<li><span>记录日期</span><input type="text" id="JLRQ" name="JLRQ" placeholder="支持模糊查询"/></li>
							<!-- <li><span>记录日期</span><input type="text" id="JLRQ" name="JLRQ" placeholder="记录日期" readonly="readonly"/></li> -->
							<li><span>报送银行金融机构编码</span><input type="text" id="BSJRJGBM" name="BSJRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>报送银行内部机构号</span><input type="text" id="BSNBJGH" name="BSNBJGH" placeholder="支持模糊查询"/></li>
							<li><span>生成网点金融机构编码</span><input type="text" id="WDJRJGBM" name="WDJRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>生成网点内部机构号</span><input type="text" id="WDNBJGH" name="WDNBJGH" placeholder="支持模糊查询"/></li>
							<li><span>业务类型</span>
								<select id="YWLX"  name="YWLX" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">未定义</option>
		                        	<option value="1">现金收入</option>
		                        	<option value="2">现金付出</option>
		                        	<option value="3">清分业务</option>
		                  	    </select>
							</li>
							<li><span>是否现金清分中心</span>
								<select id="SFXJQSZX"  name="SFXJQSZX" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">否</option>
		                        	<option value="1">是</option>
		                  	    </select>
							</li>
							<li><span>设备类型</span>
								<select id="SBLX"  name="SBLX" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">未定义</option>
		                        	<option value="1">清分机具</option>
		                        	<option value="2">存取款一体机</option>
		                        	<option value="3">点钞机</option>
		                        	<option value="4">取款机</option>
		                        	<option value="5">兑换机具</option>  
		                  	    </select>
							</li>
							<li><span>机型</span><input type="text" id="JX" name="JX" placeholder="支持模糊查询"/></li>
							<li><span>设备编号</span><input type="text" id="SBBH" name="SBBH" placeholder="支持模糊查询"/></li>
							<li><span>交易类型</span><input type="text" id="JYLX" name="JYLX" placeholder="支持模糊查询"/></li>
							<li><span>上报业务序列号</span><input type="text" id="SBYWXLH" name="SBYWXLH" placeholder="支持模糊查询"/></li>
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getRcywData"/></span></li>
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
							<span class="consult-title">搜索结果-商业银行日常业务表     (日报表-增量表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../../currencyJsp/content.jsp">
				            	<jsp:param value="rcyw" name="str"/>
				            	<jsp:param value="SPECIAL_RCYW" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
				<!--主体 end-->
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/wildCatReport/zxsj/rcyw');
		    </script>
		</div>
</body>
</html>