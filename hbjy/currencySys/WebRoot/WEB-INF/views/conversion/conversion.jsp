<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<title>生成报文</title>
<link href="<%=path%>/static/common/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<%=path%>/static/my/css/bootstrap.min.css" />
<link href="<%=path%>/static/common/gallery/select2/3.5.1/select2.css" rel="stylesheet" />
<link href="<%=path%>/static/my/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
<link href="<%=path%>/static/my/theme/default/style.css" rel="stylesheet">
<link href="<%=path%>/static/my/css/bankCommon.css" rel="stylesheet">
<link href="<%=path%>/static/common/gallery/layui/css/layui.css" rel="stylesheet">
<script src="<%=path%>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
<script> staticUrl = '<%=path%>/static';</script>
<link rel="stylesheet" type="text/css" href="<%=path%>/static/common/gallery/datetimepicker/2.0.0/datetimepicker.css" />
<script src="<%=path%>/static/common/config.js"></script>
<script src="<%=path%>/static/common/utils/myUtils.js"></script>
<script src="<%=path%>/static/my/config.js"></script>
<link href="<%=path%>/static/common/jquery-easyui-1.5.3/themes/gray/easyui.css" rel="stylesheet" />
<link href="<%=path%>/static/common/jquery-easyui-1.5.3/themes/icon.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="<%=path%>/static/common/jquery-easyui-1.5.3/demo/demo.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/static/common/jquery-easyui-1.5.3/themes/default/easyui.css">
<style type="text/css">
.layui-tab-title li {
	min-width: 190px;
}

.consult-head {
	width: 100%;
	height: 40px;
	line-height: 40px;
	font-size: 16px;
	color: #ffffff;
}

.layui-tab-title {
	background-color: #d9e6f7;
}

.accurateSearchInputs ul{
	height: 40px;
}
</style>
</head>

<body style="padding: 0px; background: #f1f9fc;">
	<input type="hidden" value="<%=basePath%>deploan" id="searchUrl">
	<input type="hidden" value="<%=basePath%>" id="url">
	<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief" style="background: #ffffff;">
		<ul class="layui-tab-title">
			<li class="layui-this" lay-id="sjrk">源数据入库</li>
			<li lay-id="sjzh">数据转换</li>
			<li lay-id="bwgl">报文管理</li>
		</ul>
		<!-- ******************************************入库start**************************************************** -->
		<div class="layui-tab-content" style="height: auto;">
			<div class="layui-tab-item layui-show" style="width: 100%;">
				<div class="accurateSearchInputs" style="height: 20%;">
					<form id="formSub">
						<ul>
							<li><span>数据日期</span><input type="text" id="wBeginDate" name="wBeginDate" placeholder="数据日期" readonly="readonly" /></li>
							<li><span>表名</span><input type="text" id="wywm" name="wywm" placeholder="支持模糊查询"  disabled="disabled" /></li>
						</ul>
					</form>
					<div class="importBtn" id="sjrk">
						<a>数据入库</a>
					</div>
				</div>
				<!--报送时间end-->
				<!--退出end-->
				<div class="consult-right" style="margin-right: -5px; height: 70%">
					<div class="consult-history-con" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-源数据入库</span>
						</div>
						<div class="" id="wMyTable" style="display: none; height: 75%; width: 100%">
							<table id="wTest" class="easyui-treegrid" singleSelect="true"
								fitcolumns="false" idField="text" treeField="text" checkbox="true"
								style="height: auto; width: 100%">
								<thead>
									<tr>
										<th field="text" width="80%">表名</th>
										<!-- <th field="key" width="30%">业务名</th> -->
										<th field="scbwxx" width="20%" data-options="formatter:feneratemessage">标识</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- ******************************************入库end**************************************************** -->

			<!-- ******************************************转换start**************************************************** -->
			<div class="layui-tab-item" style="width: 100%;">
				<div class="accurateSearchInputs" style="height: 20%;">
					<form id="formSub">
						<ul>
							<li><span>数据日期</span><input type="text" id="conbeginDate" name="conbeginDate" placeholder="数据日期" readonly="readonly" /></li>
							<li><span>业务名</span><input type="text" id="conywm" name="conywm" placeholder="支持模糊查询" /></li>
							<li><span>表名</span><input type="text" id="conbm" name="conbm" placeholder="支持模糊查询" /></li>
						</ul>
					</form>
					<div class="importBtn" id="sjzh"> <a>数据转换</a> </div>
				</div>
				<!--报送时间end-->
				<!--退出end-->
				<div class="consult-right" style="margin-right: -5px; height: 70%">
					<div class="consult-history-w" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-数据转换</span>
						</div>
						<div class="" id="conMyTable" style="display: none; height: 75%; width: 100%">
							<table id="conTest" class="easyui-treegrid" singleSelect="true"
								fitcolumns="false" idField="text" treeField="text" checkbox="true"
								style="height: auto; width: 100%">
								<thead>
									<tr>
										<th field="text" width="60%">业务名</th>
										<th field="key" width="30%">表名</th>
										<th field="scbwxx" width="10%" data-options="formatter:feneratemessage">标识</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- ******************************************转换end**************************************************** -->

			<!-- ******************************************生成报文start**************************************************** -->
			<div class="layui-tab-item" style="width: 100%;">
				<%-- <jsp:include page="../generateMessage/generateMessageView.jsp"></jsp:include> --%>
				<div class="accurateSearchInputs">
					<form id="formSub">
						<ul>
							<li><span>数据日期</span><input type="text" id="beginDate" name="beginDate" placeholder="数据日期" readonly="readonly"/></li>
							<li><span>业务名</span><input type="text" id="ywm" name="ywm" placeholder="支持模糊查询"/></li>
							<li><span>表名</span><input type="text" id="bm" name="bm" placeholder="支持模糊查询"/></li>
							<li><span>是否通过</span><select id="ispass" name="ispass">
							  <option value="">--请选择--</option>
							  <option value="true">是</option>
							  <option value="false">否</option>
							</select></li>
						</ul>
					</form>
					<div class="importBtn" id="bwsbId"><a>报文上报</a></div>
					<div class="moreSearch" id="downFile"><a>报文下载</a></div>
					<div class="exportBtn" id="checkData" ><a>报文校验</a></div>
					<div class="searchBtn" id="scbwId"><a>报文生成</a></div>
				</div>
				<!--报送时间end-->
				<!--退出end-->
				<div class="consult-right" style="margin-right: -5px;height: 70%">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-报文管理</span>
						</div>
						<div class="" id="myTable" style="display:none;height: 75%;width: auto">
							<table id="test" class="easyui-treegrid" singleSelect ="true" fitcolumns="false" 
								idField="id" treeField="text" checkbox="true" style="height: auto;width: 100%">
							<thead>
								<tr>
									<th field="text" data-options="formatter:feneratemessage,width:45">业务名</th>
									<th field="key" data-options="width:15">表名</th>
									<th field="scbwxx" data-options="formatter:feneratemessage,width:10">标识</th>
									<th field="ispass" data-options="formatter:ispassFormatter,width:10">是否通过</th>
									<th field="cz" data-options="formatter:operationFormatter,width:10">操作</th>
									<th field="issend" data-options="formatter:sendBtnFormatter,width:10">是否发短信</th>
									<!--隐藏域-->
									<th field="scbwxx_code" data-options="hidden:true">标识编码</th>
									<th field="ywbm" data-options="hidden:true">业务编码</th>
								</tr>
							</thead>
						</table>
						</div>
					</div>	
				</div>
			</div>
			<!-- ******************************************生成报文end**************************************************** -->
		</div>
		<script type="text/javascript">
			var searchParams = ${params};
			seajs.use('myJsPath/js/generateMessage/generateMessageView');
			seajs.use('myJsPath/js/conversion/conversion');
		</script>
	</div>
</body>
</html>