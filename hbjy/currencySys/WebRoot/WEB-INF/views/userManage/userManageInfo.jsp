<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String username = (String)request.getSession().getAttribute("username"); 
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户信息管理</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="X-UA-Compatible" content="IE=9">
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<%=path %>/static/common/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/my/css/bootstrap.min.css" />
    <link href="<%=path %>/static/my/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="<%=path %>/static/my/theme/default/style.css" rel="stylesheet">
    <link href="<%=path %>/static/my/css/bankCommon.css" rel="stylesheet">
    <link href="<%=path %>/static/common/gallery/ztree/3.x/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
    <script src="<%=path %>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
    <script>
    staticUrl = '<%=request.getContextPath() %>/static';
    </script>
    <script src="<%=path%>/static/common/utils/myUtils.js"></script>
    <script src="<%=path %>/static/common/config.js"></script>
    <script src="<%=path %>/static/my/config.js"></script>
    <link href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/gray/easyui.css" rel="stylesheet" />
    <link href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/icon.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/common/jquery-easyui-1.5.3/demo/demo.css">
    <link rel="stylesheet" type="text/css" href="<%=path %>/static/common/jquery-easyui-1.5.3/themes/default/easyui.css">
    <style>
	   .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber div{
			 overflow:hidden;
	 		white-space:pre-wrap;
		}
    .col-md-6{
    	width: 45%\9;
    	float: left\9;
    }
    .col-md-6 span{
    	 margin-left: 2px\9; 
    	 float: left\9;
    }
    .consult-kead{
       line-height: 30px;
       width: 100px;
       height: 30px;
    }
	</style>
</head>

<body>
    <div class="consult common">
        <!-- 分析条件start -->
        <div class="searchMainBox">
            <!--报送时间start-->
            <input type="hidden" value="<%=basePath %>" id="url">
            <input type="hidden" value="" id="dishiLevel">
            <input type="hidden" value="<%=username %>" id="username">
            <!--退出end-->
            <div class="accurateSearchInputs">
					<form id="formSub">
						<ul>
							<li><span>柜员号</span><input type="text" id="YHNAME" name="YHNAME" placeholder="支持模糊查询"/></li>
							<li><span>姓名</span><input type="text" id="NAME" name="NAME" placeholder="支持模糊查询"/></li>	
							<li><span>手机号</span><input type="text" id="TELEPH" name="TELEPH" placeholder="支持模糊查询"/></li>
							<li><span>所属部门</span><input type="text" id="SSBM" name="SSBM" placeholder="支持模糊查询"/></li>
							<li><span>单位级别</span><input type="text" id="JGJB" name="JGJB" placeholder="支持模糊查询"/></li>
							<li><span>所在单位</span><input type="text" id="JRJGMC" name="JRJGMC" placeholder="支持模糊查询"/></li>
							<li><span>单位编码</span><input type="text" id="NBJGH_LIKE" name="NBJGH_LIKE" placeholder="支持模糊查询"/></li>
						    <div class="importBtn" id="checkSearch"><a>查询</a></div> 
						    <!--  <button id ="checkSearch" type="button" class="chaxun" style="width:80px;height:32px;">新增</button> -->
							<li><span><input type="hidden" id="excelSqlid" name="excelSqlid" value="getXqmyData"/></span></li>
						</ul>
					</form>	
				</div>
            <div class="consult-right">
                <div class="consult-history">
                    <div>
                        <button id ="addId" type="button" class="btn btn-success" style="width:80px;height:32px;">新增</button>
                        <button id ="updateId" type="button" class="btn btn-warning" style="width:80px;height:32px;">修改</button>
                        <button id ="deleteId" type="button" class="btn btn-danger" style="width:80px;height:32px;">删除</button>
                        <button id ="rechargeId" type="button" class="btn btn-danger" style="width:80px;height:32px;">重置密码</button>		
                    </div>
                    <div class="consult-kead"></div>
                    <div class="consult-tab" id="myTable" style="display:none">
                        <table id="gridConsult" class="easyui-datagrid" style="width:100%;" pagination="true">
                            <thead>
                                <tr>
                                	<th data-options="field:'ck',checkbox:true"></th>
                                    <th data-options="field:'xh',align:'center',width:50" rowspan="1" colspan="1">序号</th>
                                    <th data-options="field:'YH_NAME',align:'center',width:200" rowspan="1" colspan="1">柜员号</th>
                                    <th data-options="field:'YH_PWD',align:'center',width:300" rowspan="1" colspan="1">用户密码</th>
                                    <th data-options="field:'NAME',align:'center',width:81" rowspan="1" colspan="1">姓名</th>
                                    <th data-options="field:'TELEPH',align:'center',width:100" rowspan="1" colspan="1">手机号码</th>
                                    <th data-options="field:'SSBM',align:'center',width:200" rowspan="1" colspan="1">所属部门</th>
                                    <th data-options="field:'DWJB',align:'center',width:100" rowspan="1" colspan="1">单位级别</th>
                                    <th data-options="field:'DWMC',align:'center',width:200" rowspan="1" colspan="1">所在单位</th>
                                    <th data-options="field:'DWBM',align:'center',width:200 " rowspan="1" colspan="1">单位编码</th>
                                    <th data-options="field:'SMSNOTICE',align:'center',width:200 " rowspan="1" colspan="1">短信通知</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
   <script type="text/javascript">
   seajs.use('myJsPath/js/userManage/userManageInfo');
    </script>
</body>
</html>