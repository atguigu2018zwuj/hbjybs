<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>金融机构信息维护</title>
    <jsp:include page="../currencyJsp/header.jsp"></jsp:include>
    <link href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/icon.css" rel="stylesheet">
    <style type="text/css">
    	.accurateSearchInputs{
    		-webkit-user-select:none;
   			-moz-user-select:none;
   			-ms-user-select:none;
   			user-select:none;
    	}
    	.accurateSearchInputs .can-user-select{
    		-webkit-user-select:text;
   			-moz-user-select:text;
   			-ms-user-select:text;
   			user-select:text;
    	}
    	.accurateSearchInputs ul.download-show{
    		height: auto;
    	}
    	.download-show{
    		display:none;
    	}
    	.upload-show>li,.download-show>li{
    		width: 100%;
    		height: 32px;
    	}
    	.upload-show>li>span,.download-show>li>span{
    		width:100px;
    	}
    	.upload-show>li>span>a>span{
    		width:44px;
    	}
    	.upload-show>li>span>a>span>span{
    		width:35px;
    	}
    	.download-show>li>span.horizontal-summary-operator{
    		text-align: center;
    		width:30px;
    		height: 32px;
    		font-size: 14px;
    	}
    	.download-show>li>input.vertical-formula{
    		height: 30px;
    		margin-left: 0px;
    	}
    	.download-show>li>input.vertical-formula.small{
    		width: 10%;
    	}
    	.download-show>li>input.vertical-formula.medium{
    		width: 20%;
    	}
    	.download-show>li>input.vertical-formula.big{
    		width: 40%;
    	}
    	.download-show>li>span.vertical-formula-operator{
    		text-align: center;
    		width:30px;
    		height: 32px;
    		font-size: 14px;
    	}
    	.download-show>li>span.vertical-formula-operator.sum-left{
    		text-align: left;
    		width: 40px;
    	}
    	.download-show>li>span.vertical-formula-operator.sum-right{
    		text-align: right;
    		width: 13px;
    	}
    	.accurateSearchInputs li>.summary-type-option{
    		display: none;
    	}
    	.accurateSearchInputs li>input.summary-type-option{
    		height: 30px;
    		margin-left: 0px;
    	}
    	.accurateSearchInputs li>input.summary-type-option.small{
    		width: 80px;
    	}
    	.accurateSearchInputs li>input.summary-type-option.medium{
    		width: 200px;
    	}
    	.accurateSearchInputs li>input.summary-type-option.big{
    		width: 400px;
    	}
    	.accurateSearchInputs li>span.summary-type-option-operator{
    		text-align: center;
    		width:20px;
    		height: 32px;
    	}
    	.accurateSearchInputs span.required-flg{
    		color: red;
    		width: 30px;
    		text-align: center;
    		font-size: 15px;
    	}
    	.accurateSearchInputs a.add-option-row-btn{
    		width: 26px;
    		height: 26px;
    		float: left;
    		margin-left: 10px;
    		position: relative;
    		top: 2px;
    	}
    	.accurateSearchInputs a.add-option-row-btn>span.l-btn-left.l-btn-icon-left{
    		width: 100%;
    	}
    	.accurateSearchInputs a.add-option-row-btn>span.l-btn-left.l-btn-icon-left>span.l-btn-text{
    		width: 0px;
    		color: black;
    	}
    	.accurateSearchInputs a.add-option-row-btn>span.l-btn-left.l-btn-icon-left>span.l-btn-icon{
    		width: 16px;
    	}
    	.accurateSearchInputs a.add-option-row-btn.vertical-formula{
    		width: 70px;
    	}
    	.accurateSearchInputs a.add-option-row-btn.vertical-formula>span.l-btn-left.l-btn-icon-left>span.l-btn-text{
    		width: 40px;
    	}
    	/* 校验提示*/
    	form#summaryForm li>span.validate-msg{
	    	width: auto;
		    text-align: left;
		    font-size: 14px;
		    font-weight: bold;
		    color: red;
		    padding-left: 20px;
    	}
    	form#summaryForm li>span.common-num{
	    	width: auto;
		    text-align: left;
		    font-size: 16px;
		    font-weight: bold;
		    color: red;
		    height: 30px;
		    padding-left: 10px;
    	}
    	form#summaryForm>ul.common-info>li{
	    	margin-bottom: 0px;
    	}
	</style>
</head>

<body>
	<div class="consult common" id="sssss">
		<input type="hidden" value="<%=basePath %>deploan" id="searchUrl">
		<input type="hidden" value="<%=basePath %>" id="url">
			<div class="searchMainBox" style="height: auto">
				<!--查询条件start-->
				<div class="accurateSearchInputs">
					<form id="uploadExcelForm" method="post" novalidate enctype="multipart/form-data">
						<!-- 上传模板、汇总文件 -->
						<ul class="upload-show">
							<li>
								<span class="can-user-select">模板上传：</span><input id="templateUpload" name="templateUpload" class="easyui-filebox" data-options="prompt:'请选择文件...',buttonText:'选择'">
								<span class="required-flg">*</span>
							</li>
						</ul>
						<ul class="upload-show">
							<li>
								<span class="can-user-select">汇总文件上传：</span><input id="summaryUpload" name="summaryUpload" class="easyui-filebox" data-options="prompt:'请选择文件...',buttonText:'选择',multiple:true">
								<span class="required-flg">*</span>
							</li>
						</ul>
						<ul class="upload-show">
							<li>
								<span class="can-user-select">汇总方式：</span>
								<select id="summaryType" name="summaryType" class="input-tag-table" style="width: 40%;height: 30px;">
									<option value="ROW_PUSH">多行归总汇总（将所有汇总文件的所有行直接复制进最终汇总表中）</option>
									<option value="CELL_SUM">各单元格加总汇总（将所有汇总文件的指定单元格的值得加和设置到最终汇总表中的对应单元格上）</option>
								</select>
								<span class="required-flg">*</span>
								<span class="summary-type-option CELL_SUM">数据行范围：</span>
								<input id="dataRowNumStart" name="dataRowNumStart" class="summary-type-option CELL_SUM small" placeholder="开始行号">
								<span class="summary-type-option CELL_SUM summary-type-option-operator">~</span>
								<input id="dataRowNumEnd" name="dataRowNumEnd" class="summary-type-option CELL_SUM small" placeholder="结束行号">
								<span class="required-flg summary-type-option CELL_SUM">*</span>
							</li>
						</ul>
					</form>
					<form id="summaryForm" method="post" novalidate>
						<!-- 模板路径 -->
						<input type="hidden" id="templatePath">
						<!-- 汇总文件路径 -->
						<input type="hidden" id="summaryPathsJson">
						<!-- 上一个模板的路径 -->
						<input type="hidden" id="lastTemplatePath">
						<!-- 设置汇总条件，下载汇总表格 -->
						<ul class="download-show">
							<li><span class="can-user-select">纵向汇总的列：</span>
								<select id="verticalSummaryList" name="verticalSummaryList" class="input-tag-table" style="width: 40%;height: 30px;">
								</select>
							</li>
						</ul>
						<ul class="download-show">
							<li>
								<span class="can-user-select">横向汇总公式：</span>
								<select id="horizontalSummaryResult_1" name="horizontalSummaryResult" class="input-tag-table horizontal-summary" style="width: 8%;height: 30px;">
								</select>
								<span class="horizontal-summary-operator">=</span>
								<select id="horizontalSummaryEle_1_1" name="horizontalSummaryEle_1" class="input-tag-table horizontal-summary" style="width: 8%;height: 30px;">
								</select>
								<span class="horizontal-summary-operator">+</span>
								<select id="horizontalSummaryEle_1_2" name="horizontalSummaryEle_1" class="input-tag-table horizontal-summary" style="width: 8%;height: 30px;">
								</select>
								<span class="common-num">①</span>
								<div id="horizontalSummaryElemOperat_1">
									<a href="javascript:void(0)" onclick="javascript:addOptionElem('horizontalSummary',this)" id="horizontalSummaryAddBtn_1" class="easyui-linkbutton add-option-row-btn" data-options="iconCls:'icon-add'"></a>
									<a href="javascript:void(0)" onclick="javascript:removeOptionElem('horizontalSummary',this)" id="horizontalSummaryRemoveBtn_1" class="easyui-linkbutton add-option-row-btn" data-options="iconCls:'icon-remove'"></a>
								</div>
								<div id="horizontalSummaryRowOperat">
									<a href="javascript:addOptionRow('horizontalSummary')" id="horizontalSummaryAddRowBtn" class="easyui-linkbutton add-option-row-btn" data-options="iconCls:'icon-more'"></a>
									<a href="javascript:removeOptionRow('horizontalSummary')" id="horizontalSummaryRemoveRowBtn" class="easyui-linkbutton add-option-row-btn" data-options="iconCls:'icon-clear'" style="display: none;"></a>
								</div>
								<br/>
							</li>
						</ul>
						<ul class="download-show">
							<li>
								<span class="can-user-select">纵向汇总公式：</span>
								<input id="verticalFormulaResult_1" name="verticalFormulaResult" class="vertical-formula elem-collection small" placeholder="结果行号">
								<span class="vertical-formula-operator">=</span>
								<span class="vertical-formula-operator sum-left">SUM(</span>
								<input id="verticalFormulaEle_1_1" name="verticalFormulaEle_1" class="vertical-formula elem-collection big" placeholder="加总行号（以顿号分隔）">
								<span class="vertical-formula-operator sum-right">)</span>
								<span class="common-num">②</span>
								<div id="verticalFormulaRowOperat">
									<a href="javascript:addOptionRow('verticalFormula','range')" id="verticalFormulaAddRangeRowBtn" class="easyui-linkbutton vertical-formula add-option-row-btn" data-options="iconCls:'icon-more'">行范围</a>
									<a href="javascript:addOptionRow('verticalFormula','collection')" id="verticalFormulaAddCollectionRowBtn" class="easyui-linkbutton vertical-formula add-option-row-btn" data-options="iconCls:'icon-more'">行集合</a>
									<a href="javascript:removeOptionRow('verticalFormula')" id="verticalFormulaRemoveRowBtn" class="easyui-linkbutton add-option-row-btn" data-options="iconCls:'icon-clear'" style="display: none;"></a>
								</div>
								<br/>
							</li>
						</ul>
						<!-- 汇总选项注释 -->
						<ul class="download-show common-info">
							<li>
								<span class="validate-msg can-user-select">①：【横向汇总公式】，若等号左边的结果列未选择、或等号右边的加和项全部未选择，则当前公式将不被执行。</span>
							</li>
							<li>
								<span class="validate-msg can-user-select">②：【纵向汇总公式】，结果行号、加总行号、开始行号、结束行号均为必填，且需填写正整数，加总行号需以顿号【、】分隔，若当前公式未按要求填写，则当前公式将不被执行。</span>
							</li>
						</ul>
					</form>
					<div class="importBtn upload-show" id="importExcel"><a>上传</a></div>
					<div class="exportBtn download-show" id="downExcel"><a>汇总</a></div>
					<div class="importBtn download-show" id="cancel"><a>返回</a></div>
				</div>
				<!--查询条件end-->
			</div>
			<script type="text/javascript">seajs.use('myJsPath/js/customReportSummary/customReportSummary');</script>
		</div>
</body>
</html>