<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<title>自定义报表补录</title>
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
<link href="<%=path %>/static/common/gallery/ztree/3.x/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
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

a{
	color: #08c;
}

.fitem select,.fitem input{
    padding-left: 5px;
    margin-bottom: 0px;
    width:200px;
    height:29px;
}
.fitem .left_side{
    float: left;
    margin-top: 7px;
}
.content select,.content textarea {
    float: left;
}
.content textarea {
    width:840px;
    height:100px;
    resize: none;
    padding:5px;
    background-color:white;
    cursor: auto;
}
.content-textarea {
    width: 850px;
}
.content input[type="checkbox"] {
    width: 15px;
}
div.required-flg{
	height: 100%;
    margin-top: 7px;
    font-size: 15px;
}
.left_side {
    width: 80px;
    float: left;
}
table.dialog-table{
	border:solid rgb(189,189,189); border-width:1px 0px 0px 1px;
}
/* 补录表弹框表格-字段信息表格 START */
#fieldInfoHTableTable th,#fieldInfoBTableTable td{
	width: 136px;
	text-align: center;
	border:solid rgb(189,189,189); border-width:0px 1px 1px 0px; padding:10px 0px;
}
#fieldInfoBTableTable td.last-one-model{
	width: 136px;
}
#fieldInfoBTableTable td.last-one-small{
	width: 115px;
}
/* 补录表弹框表格-字段信息表格 END */
/* 补录表弹框表格-主键索引表格 START */
#keyIndexInfoHTableTable th,#keyIndexInfoBTableTable td{
	width: 306px;
	text-align: center;
	border:solid rgb(189,189,189); border-width:0px 1px 1px 0px; padding:10px 0px;
}
#keyIndexInfoBTableTable td.last-one-model{
	width: 306px;
}
#keyIndexInfoBTableTable td.last-one-small{
	width: 286px;
}
/* 补录表弹框表格-主键索引表格 END */
/* 模板定制弹框表格-字段信息表格 START */
#fieldInfoHTableTemplate th,#fieldInfoBTableTemplate td{
	width: 154px;
	text-align: center;
	border:solid rgb(189,189,189); border-width:0px 1px 1px 0px; padding:10px 0px;
}
#fieldInfoBTableTemplate td.last-one-model{
	width: 154px;
}
#fieldInfoBTableTemplate td.last-one-small{
	width: 133px;
}
/* 模板定制弹框表格-字段信息表格 END */
</style>
</head>

<body style="padding: 0px; background: #f1f9fc;">
	<input type="hidden" value="<%=basePath%>deploan" id="searchUrl">
	<input type="hidden" value="<%=basePath%>" id="url">
	<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief" style="background: #ffffff;">
		<ul class="layui-tab-title">
			<li class="layui-this" lay-id="Datasource">数据源管理</li>
			<li lay-id="Table">补录表维护</li>
			<li lay-id="Template">模板定制</li>
		</ul>
		<!-- ****************************************** 数据源管理start **************************************************** -->
		<div class="layui-tab-content" style="height: auto;">
			<div class="layui-tab-item layui-show" style="width: 100%;">
				<div class="accurateSearchInputs" style="height: 20%;">
					<form id="formSub">
						<ul>
							<li><span>数据源名称</span><input type="text" id="dname_like" name="dname_like" placeholder="支持模糊查询"/></li>
						</ul>
					</form>
					<div class="importBtn" id="doSearchDatasource"><a>查询</a></div>
				</div>
				<div class="consult-right" style="margin-right: -5px; height: 70%">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-数据源管理</span>
						</div>
						<div class="" id="myTableDatasource" style="display: none; height: 75%; width: 100%">
							<table id="dgDatasource" class="easyui-datagrid" toolbar="#tb" pagination="true" rownumbers="false" fitcolumns="false" singleselect="false"
								style="height: auto; width: 100%">
								<thead>
									<tr>
										<th field="DNAME" width="20%">数据源名称</th>
										<th field="DRIVER" width="15%" data-options="formatter:driverFormatter">驱动类型</th>
										<th field="HOST" width="15%">地址</th>
										<th field="PORT" width="10%">端口</th>
										<th field="DBNAME" width="20%">数据库名</th>
										<th field="DBUSER" width="20%">连接用户</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- ****************************************** 数据源管理end **************************************************** -->

			<!-- ****************************************** 补录表维护start **************************************************** -->
			<div class="layui-tab-item" style="width: 100%;">
				<div class="accurateSearchInputs" style="height: 20%;">
					<form id="formSub">
						<ul>
							<li><span>数据源</span>
								<select type="text" id="datasource" name="datasource">
									<option value="">-- 请选择 --</option>
								</select>
							</li>
							<li><span>表名</span><input type="text" id="tableNo_like" name="tableNo_like" placeholder="支持模糊查询" /></li>
							<li><span>表中文名</span><input type="text" id="tableName_like" name="tableName_like" placeholder="支持模糊查询" /></li>
						</ul>
					</form>
					<div class="importBtn" id="doSearchTable"><a>查询</a></div>
				</div>
				<div class="consult-right" style="margin-right: -5px; height: 70%">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-补录表维护</span>
						</div>
						<div class="" id="myTableTable" style="display: none; height: 75%; width: 100%">
							<table id="dgTable" class="easyui-datagrid" toolbar="#tb" pagination="true" rownumbers="false" fitcolumns="false" singleselect="false"
								style="height: auto; width: 100%">
								<thead>
									<tr>
										<th data-options="field:'ck',checkbox:true" width="2%">全选</th>
										<th data-options="field:'cuozuo',align:'center', formatter:operate" width="6%">操作</th>
										<th field="TABLE_NO" width="25%">表名</th>
										<th field="TABLE_NAME" width="25%">表中文名</th>
										<th field="DATASOURCE" width="25%">数据源</th>
										<th field="CREATE_DATE" width="18%">创建日期</th>
										<th field="DATA_CONFIG" data-options="hidden:true">数据配置</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
			</div>
			<!-- ****************************************** 补录表维护end **************************************************** -->

			<!-- ****************************************** 模板定制start **************************************************** -->
			<div class="layui-tab-item" style="width: 100%;">
				<div class="accurateSearchInputs" style="height: 20%;">
					<form id="formSub">
						<ul>
							<li><span>模板名称</span><input type="text" id="templateName_like" name="templateName_like" placeholder="支持模糊查询" /></li>
							<li><span>物理表名</span><input type="text" id="tableName_like" name="tableName_like" placeholder="支持模糊查询" /></li>
						</ul>
					</form>
					<div class="importBtn" id="doSearchTemplate"><a>查询</a></div>
				</div>
				<div class="consult-right" style="margin-right: -5px;height: 70%">
					<div class="consult-history" style="padding-bottom: 50px;">
						<div class="consult-head">
							<span class="consult-title">搜索结果-报文管理</span>
						</div>
						<div class="" id="myTableTemplate" style="display: none; height: 75%; width: 100%">
							<table id="dgTemplate" class="easyui-datagrid" style="height: auto; width: auto;" toolbar="#tb" pagination="true" rownumbers="false" fitcolumns="false" singleselect="false">
								<thead>
									<tr>
										<th data-options="field:'ck',checkbox:true" width="2%">全选</th>
										<th data-options="field:'cuozuo',align:'center', formatter:operate" width="6%">操作</th>
										<th field="TEMPLATE_NAME" width="25%">模板名称</th>
										<th field="TABLE_NAME" width="25%">物理表名</th>
										<th field="REMARK" width="43%">描述</th>
										<th field="BR_NO" data-options="hidden:true">所属单位</th>
										<th field="RESPONSIBLER" data-options="hidden:true">负责人</th>
										<th field="AUTHORITY" data-options="hidden:true">权限</th>
										<th field="AUTHORITY_JGMC" data-options="hidden:true">权限-机构名称</th>
										<th field="DATA_CONFIG" data-options="hidden:true">数据配置</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>	
				</div>
			</div>
			<!-- ****************************************** 模板定制end **************************************************** -->
		</div>
		
		<!-- 补录表编辑部分 start-->
		<div id="dlgTable" class="easyui-dialog" data-options="modal:true" style="width:1300px;height:600px;padding:10px 20px;overflow:hidden;display:none;" closed="true" buttons="#dlg-buttonsTable">
			<!-- 存储补录表配置信息的json -->
			<input type="hidden" id="dataConfigsTable">
			<div class="layui-tab layui-tab-brief" lay-filter="editTableTabBrief" style="background: #ffffff;">
				<ul class="layui-tab-title">
					<li style="pointer-events: none;" class="layui-this" lay-id="baseInfo">基本信息表</li>
					<li style="pointer-events: none;" lay-id="fieldInfo">字段信息</li>
					<li style="pointer-events: none;" lay-id="keyIndexInfo">主键索引</li>
					<li style="pointer-events: none;" lay-id="generateTable">生成表</li>
				</ul>
				<div class="layui-tab-content" style="height: auto;">
					<div class="layui-tab-item layui-show" style="width: 100%;">
						<form name="fmTable" method="post" novalidate>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>数据来源：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="DATASOURCE" name="DATASOURCE" class="input-tag">
										<option value="">-- 请选择 --</option>
									</select>
									<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>表名称：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="TABLE_NO" name="TABLE_NO" class="input-tag">
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>表中文名称：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="TABLE_NAME" name="TABLE_NAME" class="input-tag">
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
						</form>
					</div>
					<div class="layui-tab-item" style="width: 100%;">
						<form name="fmTable" method="post" novalidate>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>字段名称：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="fieldIdEditTable" name="fieldIdEditTable" class="input-tag-table">
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div class="left_side" style="float: left;">
						    		<span>中文名：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="fieldNameEditTable" name="fieldNameEditTable" class="input-tag-table">
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div class="left_side" style="float: left;">
						    		<span>字段类型：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="fieldTypeEditTable" name="fieldTypeEditTable" class="input-tag-table">
										<option value="">-- 请选择 --</option>
										<option value="CHAR">CHAR</option>
										<option value="NUMBER">NUMBER</option>
										<option value="VARCHAR2">VARCHAR2</option>
										<option value="TIMESTAMP">TIMESTAMP</option>
										<option value="DATE">DATE</option>
										<option value="INTEGER">INTEGER</option>
									</select>
									<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>字段长度：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="fieldLengthEditTable" name="fieldLengthEditTable" class="input-tag-table">
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div class="left_side" style="float: left;">
						    		<span>小数位：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="fieldDecimalPlaceEditTable" name="fieldDecimalPlaceEditTable" class="input-tag-table">
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div class="left_side" style="float: left;">
						    		<span>默认值：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="fieldDefaultEditTable" name="fieldDefaultEditTable" class="input-tag-table">
								</div>
								<div class="left_side" style="float: left;">
						    		<span>可否为空：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="checkbox" id="fieldAllowNullEditTable" name="fieldAllowNullEditTable" class="input-tag-table" checked="checked">
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem" style="text-align: center;">
								<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-add" id="fieldAddTable" style="width:90px;">新增</a>
								<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-edit" id="fieldEditTable" style="width:90px;">修改</a>
								<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-more" id="fieldAddFromTableTable" style="width:90px;">从表中导入</a>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
								<hr style="background-color:rgb(189,189,189);height: 2px;width:100%;border: none;">
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
								<table id="fieldInfoHTableTable" class="dialog-table">
									<thead>
										<tr>
											<th field="index">排序</th>
											<th field="fieldIdEditTable">字段名称</th>
											<th field="fieldNameEditTable">中文名</th>
											<th field="fieldTypeEditTable">字段类型</th>
											<th field="fieldLengthEditTable">字段长度</th>
											<th field="fieldDecimalPlaceEditTable">字段精度</th>
											<th field="fieldAllowNullEditTable">可否为空</th>
											<th field="fieldDefaultEditTable">默认值</th>
											<th field="caozuo">操作</th>
										</tr>
									</thead>
								</table>
								<div style="overflow-y: auto;height: 200px;">
									<table id="fieldInfoBTableTable" class="dialog-table">
										<tbody>
<!-- 											<tr> -->
<!-- 												<td>1</td> -->
<!-- 												<td>asd</td> -->
<!-- 												<td>asdtext</td> -->
<!-- 												<td>CHAR</td> -->
<!-- 												<td>5</td> -->
<!-- 												<td></td> -->
<!-- 												<td>否</td> -->
<!-- 												<td>00010</td> -->
<!-- 												<td class="last-one-model"><a href="javascript:void(0)" class="caozuo-delete">删除</a></td> -->
<!-- 											</tr> -->
										</tbody>
									</table>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
						</form>
					</div>
					<div class="layui-tab-item" style="width: 100%;">
						<form name="fmTable" method="post" novalidate>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>约束类型：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="keyIndexTypeEditTable" name="keyIndexTypeEditTable" class="input-tag-table">
										<option value="">-- 请选择 --</option>
										<option value="unique">唯一性(unique)</option>
										<option value="index">索引(index)</option>
									</select>
									<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div class="left_side" style="float: left;">
						    		<span>约束字段：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="keyIndexFieldEditTable" name="keyIndexFieldEditTable" class="input-tag-table">
									</select>
									<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem" style="text-align: center;">
								<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-add" id="keyIndexAddTable" style="width:90px;">新增</a>
								<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-edit" id="keyIndexEditTable" style="width:90px;">修改</a>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
								<hr style="background-color:rgb(189,189,189);height: 2px;width:100%;border: none;">
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
								<table id="keyIndexInfoHTableTable" class="dialog-table">
									<thead>
										<tr>
											<th field="index">排序</th>
											<th field="keyIndexTypeEditTable">主键类型</th>
											<th field="keyIndexFieldEditTable">主键字段</th>
											<th field="caozuo">操作</th>
										</tr>
									</thead>
								</table>
								<div style="overflow-y: auto;height: 200px;">
									<table id="keyIndexInfoBTableTable" class="dialog-table">
										<tbody>
<!-- 											<tr> -->
<!-- 												<td>1</td> -->
<!-- 												<td>唯一性(unique)</td> -->
<!-- 												<td>cxy</td> -->
<!-- 												<td class="last-one-model"><a href="javascript:void(0)" class="caozuo-delete">删除</a></td> -->
<!-- 											</tr> -->
										</tbody>
									</table>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
						</form>
					</div>
					<div class="layui-tab-item" style="width: 100%;">
						<form name="fmTable" method="post" novalidate>
					 		<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>建表语句：</span>
								</div>  
					            <div class="content" style="float: left;">
								</div>
							</div>
							<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							<div class="fitem">
								<div style="overflow-y: auto;height: 400px;padding:10px;">
									<textarea id="createTableSqlEditTable" name="createTableSqlEditTable" readonly="readonly" style="width:100%;height:100%;resize: none;padding:10px;background-color:white;cursor: auto;"></textarea>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div id="dlg-buttonsTable" style="display:none;">
<!-- 			 <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" id="saveDataTable" style="width:90px;">保存</a> -->
<!-- 			 <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" id="insertDataTable" style="width:90px;display: none;">保存</a> -->
			 <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" id="createTableTable" style="width:90px;display: none;">创建补录表</a>
			 <a href="javascript:void(0)" class="easyui-linkbutton c6" id="preStepTable" style="width:90px;display: none;">上一步</a>
			 <a href="javascript:void(0)" class="easyui-linkbutton c6" id="nextStepTable" style="width:90px;">下一步</a>
			 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgTable').dialog('close')" style="width:90px">取消</a>
		</div>
		<!-- 补录表编辑部分 end -->
		
		<!-- 模板定制编辑部分 start-->
		<div id="dlgTemplate" class="easyui-dialog" data-options="modal:true" style="width:1300px;height:600px;padding:10px 20px;overflow:hidden;display:none;" closed="true" buttons="#dlg-buttonsTemplate">
			<!-- 存储模板定制配置信息的json -->
			<input type="hidden" id="dataConfigsTemplate">
			<div class="layui-tab layui-tab-brief" lay-filter="editTemplateTabBrief" style="background: #ffffff;">
				<ul class="layui-tab-title">
					<li style="pointer-events: none;" class="layui-this" lay-id="baseTInfo">基本信息</li>
					<li style="pointer-events: none;" lay-id="fieldTInfo">字段设置</li>
					<li style="pointer-events: none;" lay-id="templateManager">补录模板</li>
				</ul>
				<div class="layui-tab-content" style="height: auto;">
					<div class="layui-tab-item layui-show" style="width: 100%;">
						<form name="fmTemplate" method="post" novalidate>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>模板名称：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="TEMPLATE_NAME" name="TEMPLATE_NAME" class="input-tag">
									<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>物理表名：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="TABLE_NAMEEditTemplate" name="TABLE_NAME"  class="input-tag">
									</select>
									<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>定义权限：</span>
								</div>
					            <div class="content" style="float: left;">
					            	<select id="AUTHORITY" name="AUTHORITY" class="input-tag">
										<option value="0">全行</option>
										<option value="1">机构</option>
									</select>
								</div>
								<div class="left_side" style="float: left;display:none;">
						    		<span>机构：</span>
								</div>
					            <div class="content" style="float: left;width:400px;display:none;">
					            	<input id="AUTHORITY_JGMC" name="AUTHORITY_JGMC" type="text" style="width:390px;height:30px;cursor:pointer;" class="input-tag" readonly="readonly"/>
					            	<input id="authorityJGEditTemplate" name="authorityJGEditTemplate" type="hidden"/>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>负责人：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="RESPONSIBLER" name="RESPONSIBLER" class="input-tag">
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>所属单位：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<input type="text" id="BR_NO" name="BR_NO" class="input-tag">
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>模板描述：</span>
								</div>  
					            <div class="content content-textarea" style="float: left;">
					            	<textarea id="REMARK" name="REMARK" class="input-tag"></textarea>
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
						</form>
					</div>
					<div class="layui-tab-item" style="width: 100%;">
						<form name="fmTemplate" method="post" novalidate>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>补录数据源：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="datasourceEditTemplate" name="datasourceEditTemplate">
					            		<option value="">-- 请选择 --</option>
									</select>
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div class="left_side" style="float: left;">
						    		<span>补录表：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="tablesEditTemplate" name="tablesEditTemplate">
					            		<option value="">-- 请选择 --</option>
									</select>
					            	<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem" style="text-align: center;">
								<br/>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
								<hr style="background-color:rgb(189,189,189);height: 2px;width:100%;border: none;">
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
								<table id="fieldInfoHTableTemplate" class="dialog-table">
									<thead>
										<tr>
											<th field="index">排序</th>
											<th field="fieldIdEditTemplate">字段名称</th>
											<th field="fieldNameEditTemplate">中文名</th>
											<th field="fieldTypeEditTemplate">字段类型</th>
											<th field="fieldLengthEditTemplate">字段长度</th>
											<th field="fieldDecimalPlaceEditTemplate">小数位</th>
											<th field="fieldAllowNullEditTemplate">可否为空</th>
											<th field="caozuo">操作</th>
										</tr>
									</thead>
								</table>
								<div style="overflow-y: auto;height: 200px;">
									<table id="fieldInfoBTableTemplate" class="dialog-table">
										<tbody>
<!-- 											<tr> -->
<!-- 												<td>1</td> -->
<!-- 												<td>test</td> -->
<!-- 												<td>测试</td> -->
<!-- 												<td>NUMBER</td> -->
<!-- 												<td>8</td> -->
<!-- 												<td>4</td> -->
<!-- 												<td>否</td> -->
<!-- 												<td class="last-one-model"><a href="javascript:void(0)" class="caozuo-delete">删除</a></td> -->
<!-- 											</tr> -->
										</tbody>
									</table>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
						</form>
					</div>
					<div class="layui-tab-item" style="width: 100%;">
						<form name="fmTemplate" method="post" novalidate>
							<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>约束类型：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="keyIndexTypeEditTable" name="keyIndexTypeEditTable" class="input-tag-table">
										<option value="">-- 请选择 --</option>
										<option value="unique">唯一性(unique)</option>
										<option value="index">索引(index)</option>
									</select>
									<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div class="left_side" style="float: left;">
						    		<span>约束字段：</span>
								</div>  
					            <div class="content" style="float: left;">
					            	<select id="keyIndexFieldEditTable" name="keyIndexFieldEditTable" class="input-tag-table">
									</select>
									<div class="required-flg"><span style="color:red">*</span></div>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem" style="text-align: center;">
								<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-add" id="keyIndexAddTable" style="width:90px;">新增</a>
								<a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-edit" id="keyIndexEditTable" style="width:90px;">修改</a>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
								<hr style="background-color:rgb(189,189,189);height: 2px;width:100%;border: none;">
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
							<div class="fitem">
								<table id="keyIndexInfoHTableTable" class="dialog-table">
									<thead>
										<tr>
											<th field="index">排序</th>
											<th field="keyIndexTypeEditTable">主键类型</th>
											<th field="keyIndexFieldEditTable">主键字段</th>
											<th field="caozuo">操作</th>
										</tr>
									</thead>
								</table>
								<div style="overflow-y: auto;height: 200px;">
									<table id="keyIndexInfoBTableTable" class="dialog-table">
										<tbody>
<!-- 											<tr> -->
<!-- 												<td>1</td> -->
<!-- 												<td>唯一性(unique)</td> -->
<!-- 												<td>cxy</td> -->
<!-- 												<td class="last-one-model"><a href="javascript:void(0)" class="caozuo-delete">删除</a></td> -->
<!-- 											</tr> -->
										</tbody>
									</table>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
						</form>
					</div>
					<div class="layui-tab-item" style="width: 100%;">
						<form name="fmTemplate" method="post" novalidate>
					 		<div class="fitem">
						 		<div class="left_side" style="float: left;">
						    		<span>建表语句：</span>
								</div>  
					            <div class="content" style="float: left;">
								</div>
							</div>
							<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							<div class="fitem">
								<div style="overflow-y: auto;height: 400px;padding:10px;">
									<textarea id="createTableSqlEditTable" name="createTableSqlEditTable" readonly="readonly" style="width:100%;height:100%;resize: none;padding:10px;background-color:white;cursor: auto;"></textarea>
								</div>
								<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div id="dlg-buttonsTemplate" style="display:none;">
			 <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" id="saveDataTemplate" style="width:90px;display: none;">保存</a>
			 <a href="javascript:void(0)" class="easyui-linkbutton c6" id="preStepTemplate" style="width:90px;display: none;">上一步</a>
			 <a href="javascript:void(0)" class="easyui-linkbutton c6" id="nextStepTemplate" style="width:90px;">下一步</a>
			 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlgTemplate').dialog('close')" style="width:90px">取消</a>
		</div>
		<!-- 模板定制编辑部分 end -->
		<script type="text/javascript">
			seajs.use('myJsPath/js/customReportAdditional/dataSourceManage');
			seajs.use('myJsPath/js/customReportAdditional/tableManage');
			seajs.use('myJsPath/js/customReportAdditional/templateManage');
		</script>
	</div>
</body>
</html>