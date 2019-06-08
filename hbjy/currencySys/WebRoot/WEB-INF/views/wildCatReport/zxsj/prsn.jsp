<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>现金从业人员信息表</title>
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
							<li><span>姓名</span><input type="text" id="NAME" name="NAME" placeholder="支持模糊查询"/></li>
							<li><span>性别</span>
								<select id="SEX"  name="SEX" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="男">男</option>
		                        	<option value="女">女</option>
		                  	    </select>
							</li>
							<li><span>身份证号</span><input type="text" id="CARD_ID" name="CARD_ID" placeholder="支持模糊查询"/></li>
							<li><span>所属单位</span><input type="text" id="UNIT" name="UNIT" placeholder="支持模糊查询"/></li>
							<li><span>所属单位统一信息代码</span><input type="text" id="SSDWTYXXDM" name="SSDWTYXXDM" placeholder="支持模糊查询"/></li>
							<li><span>所属单位金融机构编码</span><input type="text" id="DWJRJGBM" name="DWJRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>所属单位内部机构号</span><input type="text" id="DWNBJGH" name="DWNBJGH" placeholder="支持模糊查询"/></li>
							<li><span>岗位</span>
								<select id="GW"  name="GW" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">管理岗</option>
		                        	<option value="1">柜员</option>
		                        	<option value="2">清分人员</option>
		                  	    </select>
							</li>
							<li><span>是否取得反假币培训合格证书</span>
								<select id="SFQDHGZS"  name="SFQDHGZS" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="0">否</option>
		                        	<option value="1">是</option>
		                  	    </select>
							</li>
							<li><span>取得证书时间</span><input type="text" id="QDZSSJ" name="QDZSSJ" readonly="readonly"/></li>
							<li><span>证书失效时间</span><input type="text" id="ZSSXSJ" name="ZSSXSJ" readonly="readonly"/></li>
							<li><span>当前状态</span>
								<select id="DQZT"  name="DQZT" class="organizationSelect rightSelect">
		                        	<option selected value="" name="--请选择--">--请选择--</option>
		                        	<option value="1">正常从事该工作</option>
		                        	<option value="2">在本行内不再从事该工作</option>
		                        	<option value="3">离职</option>
		                  	    </select>
							</li>
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getPrsnData"/></span></li>
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
							<span class="consult-title">搜索结果-现金从业人员信息     (月报表-拉链表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../../currencyJsp/content.jsp">
				            	<jsp:param value="prsn" name="str"/>
				            	<jsp:param value="SPECIAL_PRSN" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
				<!--主体 end-->
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/wildCatReport/zxsj/prsn');
		    </script>
		</div>
</body>
</html>