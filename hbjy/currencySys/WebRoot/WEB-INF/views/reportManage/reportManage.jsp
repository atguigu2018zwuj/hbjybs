<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String username = (String)request.getSession().getAttribute("username"); 
String provincialUsers = (String)request.getSession().getAttribute("provincialUsers"); 
String nbjgh = (String)request.getSession().getAttribute("nbjgh"); 
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>报送管理</title>
    <link href="<%=path %>/static/common/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/my/css/bootstrap.min.css" />
    <link href="<%=path %>/static/common/gallery/select2/3.5.1/select2.css" rel="stylesheet" />
    <link href="<%=path %>/static/my/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="<%=path %>/static/my/theme/default/style.css" rel="stylesheet">
    <link href="<%=path %>/static/my/css/bankCommon.css" rel="stylesheet">
    <script src="<%=path %>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
    <script>
    staticUrl = '<%=path %>/static';
    </script>
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/common/gallery/datetimepicker/2.0.0/datetimepicker.css" />
    <script src="<%=path %>/static/common/config.js"></script>
    <script src="<%=path%>/static/common/utils/myUtils.js"></script>
    <script src="<%=path %>/static/my/config.js"></script>
    <link href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/gray/easyui.css" rel="stylesheet" />
    <link href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/icon.css" rel="stylesheet" />
	<style type="text/css">
		#jyjgTable td{
				 white-space:pre-wrap;
				 text-align: center;
				 vertical-align:middle;
			}
			.accurateSearchInputs {
			    padding-bottom: 20px;
			    position: relative;
			}
	</style>
</head>

<body>
	<div class="consult common">
		<input type="hidden" value="<%=basePath %>" id="url">
		<input type="hidden" value="${userszdwjb}" id="userszdwjb">
		<input type="hidden" value="${username}" id="trueName">
		<input type="hidden" value="${userszdwbm}" id="userszdwbm">
		
			<div class="searchMainBox" style="height: 90%">
				<div class="consult-right">
				<div class="consult-history">
						<div class="consult-head">
							<span class="consult-title">查询条件</span>
						</div>
						<!--报送时间start-->
						<div class="accurateSearchInputs" style="margin-top: 15px;margin-left: 25px;">
								<span class="sp">数据日期：
                       				<input type="text" style="height: 30px;width: 90px;background-color: white;" id="bsrq" readonly="readonly" />
                       			</span>
								<span class="sp">数据类型：
									<select id="sjlx" style="width: 120px;">
										<!-- <option  value="11">专项数据</option> -->
										<option selected="selected" value="12">经常性报表</option>
									</select>
								</span>
								<span class="sp">报表名称：
									<select id="bbmc" style="width: 220px;" >
									</select>
								</span>
                       			<span class="sp">报送状态：
									<select id="sjzt" style="width: 90px;">
										<option value="1">已上报</option>
										<option selected="selected" value="2">未上报</option>
									</select>
								</span>
								<c:if test="${nbjgh == '1699999998' }">
								<div class="importBtn" id="hzsb" ><a>汇总</a></div> 
								</c:if>
								<c:if test="${snsb == 'cz' }">
								<div class="importBtn" id="hzsb" ><a>汇总</a></div> 
								</c:if>
								<c:if test="${snls == 'cz' }">
								<div class="importBtn" id="hzsb" ><a>汇总</a></div> 
								</c:if>
								
								
								<!-- <div class="exportBtn" id="importExcel"><a>查询</a></div> -->
									<!-- <span class="sp">报送机构：
										<input type="text" id="bsjg" style="height: 30px;width: 90px;" onchange="dataSearch()" placeholder="报送机构">
										<input type="hidden" id="bsjgvalue">
									</span> -->
                       			<!-- <span class="sp">
                       				<input type="button" value="汇总上报" id="uploadData" style="background-color:blue;border:none; margin-left: 50px;">
                       			</span> -->
                       			<!-- <div class="moreSearch" id="uploadData" style="cursor:pointer;" ><a>汇总上报</a></div> -->
						</div>
				</div>
			</div>
				<!--退出end-->
				<div class="consult-right" id="wtdkjjclContentDiv">
					<div class="consult-history">
						<div class="consult-head">
							<span class="consult-title" id="titleName">未报送金融机构列表</span>
						</div>
						<div id="tdiv" style="display: block">
							<!-- <table id="jyjgTable" class="grid"  style="width: 100%;">
                            <thead>
                            <tr>
                            	<th style="width: 5%">序号</th>
                            	<th style="width: 18%">文件名称</th>
                            	<th style="width: 10%">报送日期</th>
                            	<th style="width: 18%">报表名称</th>
                            	<th style="width: 18%">单位名称</th>
                            	<th style="width: 10%">单位级别</th>
                            	<th style="width: 10%">报送状态</th>
                            	<th style="width: 11%">汇总状态</th>
                            	<th style="width: 20%">退回时间</th>
                            	<th style="width: 5%">操作</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                       		</table> -->
                       		
                       		<table id="test" class="easyui-treegrid" singleSelect ="true" fitcolumns="false" 
								idField="id" treeField="text" checkbox="false" style="height: auto;width: auto">
								<thead>
									<tr>
										<th field="text" width="40%">金融机构名称</th>
										<th field="jrjgbm" width="20%">金融机构编码</th>
										<th field="key" width="20%">内部机构号</th>
										<th field="sjzt" width="20%">上报状态</th>
									</tr>
								</thead>
							</table>
						</div>
				</div>
			</div>		
			</div>
			<script type="text/javascript">
		    	seajs.use('myJsPath/js/reportManage/reportManage');
		    </script>
		</div>
</body>
</html>