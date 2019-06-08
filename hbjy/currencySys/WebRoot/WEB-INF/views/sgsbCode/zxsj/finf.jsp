<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>金融机构基本情况表</title>
    <jsp:include page="../../currencyJsp/header.jsp"></jsp:include>
    <style type="text/css">
    	td.highlight {border: none !important;padding: 1px 0 1px 1px !important;background: none !important;overflow:hidden;}
        td.highlight a {background: #AABBCC !important;  border: 1px #88a276 solid !important;}
    </style>
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
							<li><span>金融机构编码</span><input type="text" id="JRJGBM" name="JRJGBM" placeholder="支持模糊查询"/></li>
							<li><span>内部机构号</span><input type="text" id="NBJGH" name="NBJGH" placeholder="支持模糊查询"/></li>
							<li><span>金融机构名称</span><input type="text" id="JRJGMC" name="JRJGMC" placeholder="支持模糊查询"/></li>
							<li><span>许可证号</span><input type="text" id="XKZH" name="XKZH" placeholder="支持模糊查询"/></li>
							<li><span>支付行号</span><input type="text" id="ZFHH" name="ZFHH" placeholder="支持模糊查询"/></li>
							<li><span>统一社会信用代码</span><input type="text" id="TYSHXYDM" name="TYSHXYDM" placeholder="支持模糊查询"/></li>
							<li><span>机构级别</span><select id="JGJB" name="JGJB" >
							<option value=''>--请选择--</option>
							<option value='1'>1（总行）</option>
							<option value='2'>2（分行）</option>
							<option value='3'>3（支行）</option>
							<option value='4'>4（网点）</option>
							<option value='5'>5（事业部）</option>
							</select></li>
							<li><span>上级管理机构名称</span><input type="text" id="SJGLJGMC" name="SJGLJGMC" placeholder="支持模糊查询"/></li>
							<li><span>上级管理机构金融机构编码</span><input type="text" id="SJGLJGBM" name="SJGLJGBM" placeholder="支持模糊查询"/></li>
							<li><span>上级管理机构内部机构号</span><input type="text" id="SJGLJGNBJGH" name="SJGLJGNBJGH" placeholder="支持模糊查询"/></li>
							<li><span>机构类别</span><input type="text" id="JGLB" name="JGLB" placeholder="支持模糊查询"/></li>
							<li><span>地区代码</span><input type="text" id="DQDM" name="DQDM" placeholder="支持模糊查询"/></li>
							<li><span>成立时间</span><input type="text" id="CLSJ" name="CLSJ" placeholder="成立时间" readonly="readonly"/></li>
							<li><span>营业状态</span><select id="YYZT" name="YYZT" >
							<option value=''>--请选择--</option>
							<option value='1'>1（营业）</option>
							<option value='2'>2（停业）</option>
							<option value='3'>3（被合并）</option>
							</select></li>
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getFinfData"/></span></li>
							<!-- <li><span><input type="hidden" id="fieldValues" name="fieldValues"/></span></li> -->
						</ul>
					</form>
					<div class="importBtn" id="importExcel"><a>导入Excel</a></div>
					<div class="exportBtn" id="downTable"  ><a>导出报表</a></div>
					<div class="moreSearch" id="doSearch"><a>查询</a></div>
					<div class="searchBtn" id="more"><a>更多</a></div>
					<div class="searchBtn" id="hide" style="display: none;"><a>隐藏</a></div>
				</div>
				<!--查询条件end-->
				
				<!--主体 start-->
				<div class="consult-right" style="margin-right: -5px;">
					<div class="consult-history" style="padding-bottom: 50px;"> 
						<div class="consult-head">
							<span class="consult-title">搜索结果-金融机构信息维护  (日报表-拉链表)</span>
						</div>
						<div class="" id="myTable" style="display:none;">
							<!-- 引入通用页面 -->
				            <jsp:include page="../../currencyJsp/content.jsp">
				            	<jsp:param value="finf" name="str"/>
				            	<jsp:param value="SPECIAL_FINF" name="tablename"/>
				            </jsp:include>
						</div>
					</div>
				</div>
				<!--主体 end-->
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/sgsbCode/zxsj/finf');
		    </script>
		</div>
</body>
</html>