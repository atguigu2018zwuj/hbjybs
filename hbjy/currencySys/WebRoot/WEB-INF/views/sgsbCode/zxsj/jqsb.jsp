<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>机构设备信息表</title>
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
							<li><span>数据日期</span><input id="beginSjrq" name="beginSjrq" type="text" class="Wdate" placeholder="数据日期"/></li>
							<li><span>设备编号</span><input type="text" id="SBBH" name="SBBH" placeholder="支持模糊查询"/></li>
							<li><span>所属金融机构编码</span><input type="text" id="SHJRJGBM" name="SHJRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>所属金融机构内部机构号</span><input type="text" id="SHJRJGNBJGH" name="SHJRJGNBJGH" placeholder="支持模糊查询"/></li>
							<li><span>统一社会信用编码</span><input type="text" id="TYSHXYBM" name="TYSHXYBM" placeholder="支持模糊查询"/></li>
							<li><span>布放位置</span><input type="text" id="BFWZ" name="BFWZ" placeholder="支持模糊查询"/></li>
							<li><span>设备生产厂商</span><input type="text" id="SBSCCS" name="SBSCCS" placeholder="支持模糊查询"/></li>
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
							<li><span>点钞机类型</span>
							<select id="DCJLX"  name="DCJLX" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">A类</option>
		                        	<option value="1">B类</option>
		                        	<option value="2">C类</option>
		                    </select>
							</li>	
							<li><span>设备型号</span><input type="text" id="SBXH" name="SBXH" placeholder="支持模糊查询"/></li>
							<li><span>投入使用年份</span><input type="text" id="TRSYNF" name="TRSYNF" placeholder="支持模糊查询"/></li>
							<li><span>设备状态</span>
							<select id="SBZT"  name="SBZT" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">在用</option>
		                        	<option value="1">停用</option>
		                        	<option value="2">维修</option>
		                    </select>
							</li>
							<li><span>设备序列号</span><input type="text" id="SBXLH" name="SBXLH" placeholder="支持模糊查询"/></li>
							<li><span>操作系统版本</span><input type="text" id="CZXTBB" name="CZXTBB" placeholder="支持模糊查询"/></li>
							<li><span>现金机具分类</span><input type="text" id="XJJJFL" name="XJJJFL" placeholder="支持模糊查询"/></li>
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getJqsbData"/></span></li>
							<!-- <li><span><input type="hidden" id="fieldValues" name="fieldValues"/></span></li> -->
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
							<span class="consult-title">搜索结果-机构设备信息表  (月报表-拉链表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
				        <!-- 引入通用页面 -->
				            <jsp:include page="../../currencyJsp/content.jsp">
				            	<jsp:param value="jqsb" name="str"/>
				            	<jsp:param value="SPECIAL_JQSB" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
				<!--主体 end-->
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/sgsbCode/zxsj/jqsb');
		    </script>
		</div>
</body>
</html>