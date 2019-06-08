<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="<%=path%>/static/my/css/font-awesome-4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<link href="<%=path%>/static/my/css/bankCommon.css" rel="stylesheet">
<link
	href="<%=path%>/static/common/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet">
<script src="<%=path%>/static/common/seajs/seajs/2.2.1/sea.js"
	id="seajsnode"></script>
<link href="<%=path %>/static/common/gallery/ztree/3.x/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link href="<%=path %>/static/my/theme/default/style.css" rel="stylesheet">
<script> staticUrl = '<%=path%>/static';</script>
<script src="<%=path%>/static/common/config.js"></script>
<script src="<%=path%>/static/my/config.js"></script>
<link
	href="<%=path%>/static/my/css/umeditor/themes/default/css/umeditor.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="<%=path%>/static/my/css/umeditor/third-party/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="<%=path%>/static/my/css/umeditor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8"
	src="<%=path%>/static/my/css/umeditor/umeditor.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/static/my/css/umeditor/lang/zh-cn/zh-cn.js"></script>
<script src="<%=path%>/static/common/utils/myUtils.js"></script>
<link href="<%=path%>/static/my/css/reset.css" type="text/css"
	rel="stylesheet">
<link href="<%=path%>/static/my/css/common.css" type="text/css"
	rel="stylesheet">
<link href="<%=path%>/static/my/css/pages.css" type="text/css"
	rel="stylesheet">
<link href="<%=path%>/static/common/jquery-easyui-1.5.3/themes/bootstrap/easyui.css" type="text/css" rel="stylesheet">
<style type="text/css">
.form-group tr {
	height: 50px;
}

/* #insertModal input, select, textarea {
			width : 420px !important;
		} */
#insertAnnouncementTitle {
	width: 420px !important;
}

#updateModal  select, textarea {
	width: 420px !important;
}

#updateAnnouncementTitle, #updateAnnex, #updateAnnexName {
	width: 420px !important;
}

#xiangqingModal  select, textarea {
	width: 420px !important;
}

#ggtm, #fbfw, #fjmc {
	width: 420px !important;
}

#insertModal textarea {
	height: 100px !important;
}

#updateModal textarea {
	height: 100px !important;
}

td {
	word-wrap: break-word;
	word-break: break-all;
}

.col-md-1 {
	padding-top: 6px;
}

.shangji {
	height: 34px;
	width: 120px !important;
	float: left;
}

.xiaji {
	height: 34px;
	width: 120px !important;
	float: left;
}

label {
	height: 20px;
	display: inline-block;
	padding: 5px;
	font-weight: 100;
	float: left;
}

.title {
	top: 20px;
}
#fgtm,#ssjg,#cjz,#fgmc{
    width : 420px !important;
}
.panel-title{
	font-size:15px;
}
</style>

<title>首页管理</title>
</head>
<body>
	<div class="acquisitionSystem">
	    <input type="hidden" value="<%=basePath %>" id="url">
		<div class="bottom">
			<div class="contentBox right">
				<div class="content left">
					<div class="notice">
						<div class="title">
							<div class="left">
								<span class="leftIcon"></span> <span class="txt">通知公告</span>
							</div>
							<div class="right">
								<%-- <a href="<%=path%>/viewController/ggglIndex" target="_self"> <span class="txt">更多</span>
									<span class="rightIcon"></span>
								</a> --%>
								<a href="" target="_self" onclick="ggglClick()"> <span class="txt">更多</span>
									<span class="rightIcon"></span>
								</a>
							</div>
						</div>
						<ul>
						    <c:if test="${!empty listann}">
							<c:forEach items="${listann}" var="announcementgjl"
										varStatus="vs">
							     <li>
                            <a href="#" >
                                <c:choose>
                                <c:when test="${fn:length(announcementgjl.announcementTitle) > 20 }">
                                <span class="newsTitle left" id="ggTitle">${fn:substring(announcementgjl.announcementTitle,0,20)}......</span>	
                                </c:when>
                                <c:otherwise>
                                <span class="newsTitle left" id="ggTitle">${announcementgjl.announcementTitle}</span>
                                </c:otherwise>
                                </c:choose>
                                
                                <span class="newsTime right"><fmt:formatDate value="${announcementgjl.createTime}" pattern="yyyy-MM-dd HH:mm " /></span>
                                <input type="hidden" value="${announcementgjl.id}" name="announcementId">
                            </a>
                                 </li> 
							</c:forEach>
							</c:if>
							<c:if test="${empty listann}">
							   <a href="#" >
                                <span class="newsTitle left" id="" style="color:rgb(0, 92, 199)">暂无通知公告</span>              
                               </a>
							</c:if>
						</ul>
	<div>
		<div class="modal fade" id="xiangqingModal" tabindex="-1" role="dialog" aria-labelledby="xiangqingModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="xiangqingModalLabel">公告详情	</h4>
					</div>
					<div class="modal-body" style="text-align:center;">
						<form class="form-inline" id="insertForm" >
							<table class="form-group">
								<tr>
									<td>公告题目：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox" value="" id="ggtm"/></td>
								</tr>
								<tr>
									<td>接受机构：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox"  id="fbfw"/></td>
								</tr>
								<tr> 
								   <td>是否发短信：</td>
								   
								   <td>
								   <label ><input type="radio" name="message" value="1" id="xqisyes" disabled/> 是</label>
								    <label><input type="radio" name="message" value="0" id="xqisnot" disabled/> 否</label> 
					               </td>				   
								</tr> 
								<tr>
									<td>公告内容：</td>
									<td><textarea class="form-control easyui-validatebox" id="ggnr" readonly style="height: 100PX"></textarea></td>
								</tr>
								<tr>
									<td>附件名称：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox"  id="fjmc"/></td>
								</tr>
								<tr id="xzfj">
								</tr>
							</table>

						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
					</div>
				</div>
			</div>
		</div>
	</div>
					</div>
					<div class="documents">
						<div class="title">
							<div class="left">
								<span class="leftIcon"></span> <span class="txt">法规文件</span>
							</div>
							<div class="right">
								<%-- <a href="<%=path%>/viewController/fgwjIndex"> <span class="txt">更多</span>
									<span class="rightIcon"></span>
								</a> --%>
								<a href="" onclick="fgwjClick()"> <span class="txt">更多</span>
									<span class="rightIcon"></span>
								</a>
							</div>
						</div>
						<ul>
							<!-- <li><a href="#"> <span class="newsTitle left" id="zhongy">《中央企业债券发行管理暂行办法》</span>
									<span class="newsTime right">2019-01-31 13:39</span>
							</a></li> -->
							<c:if test="${!empty regulationList}">
						    <c:forEach items="${regulationList}" var="regulation"
										varStatus="vs">
							     <li>
                            <a href="#" >
                               <c:choose>
                                <c:when test="${fn:length(regulation.regulationTitle) > 20 }">
                                <span class="fgTitle left" value="${regulation.id}">${fn:substring(regulation.regulationTitle,0,20)}......</span>	
                                </c:when>
                                <c:otherwise>
                                <span class="fgTitle left"  value="${regulation.id}">${regulation.regulationTitle}</span>
                                </c:otherwise>
                                </c:choose>
                                <%-- <span class="fgTitle left"  value="${regulation.id}">${regulation.regulationTitle}</span> --%>
                                <span class="newsTime right"><fmt:formatDate value="${regulation.createTime}" pattern="yyyy-MM-dd HH:mm " /></span>
                                <input type="hidden" value="${regulation.id}" name="regulationId">
                            </a>
                                 </li> 
							</c:forEach>
							</c:if>
							<c:if test="${empty regulationList}">
							<a href="#" >
                                <span class="fgTitle left" id="" style="color:rgb(0, 92, 199)">暂无法规文件</span>              
                               </a>
                               </c:if>
							<!-- 重复 -->
						</ul>
	<div>
		<div class="modal fade" id="fgxiangqingModal" tabindex="-1" role="dialog" aria-labelledby="xiangqingModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="xiangqingModalLabel">法规详情	</h4>
					</div>
					<div class="modal-body" style="text-align:center;">
						<form class="form-inline" id="insertForm" >
							<table class="form-group">
								<tr>
									<td>法规题目：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox" value="" id="fgtm"/></td>
								</tr>
								<tr>
									<td>所属机构：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox"  id="ssjg"/></td>
								</tr>
								<tr>
									<td>创建者：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox"  id="cjz"/></td>
								</tr>
								<tr>
									<td>附件名称：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox"  id="fgmc"/></td>
								</tr>
								<tr id="fgfj">
								</tr>
							</table>

						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
					</div>
				</div>
			</div>
		</div>
	</div>
					</div>
				</div>
				<!-- 待办事项 START -->
				<div class="todoBox right">
					<div class="todo">
						<div class="todoTitle">
							<span class="todoIcon"></span> <span class="txt">待办事项</span>
						</div>
						<div class="todoContent" id="todoContent">
							<ul>
								<li><p class="todoThing" style="font-size:20px;font-weight:700;">加载中……</p></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="modal fade" id="todosErrorDetailsModal" tabindex="-1" role="dialog" aria-labelledby="todosErrorDetailsModalLabel">
					<input type="hidden" id="todosSjrq">
					<input type="hidden" id="todosYwbm">
					<input type="hidden" id="todosReporterCode">
					<input type="hidden" id="todosJrjgCjxx">
					<input type="hidden" id="todosIsChildTodosAllFinished">
					<div class="modal-dialog" role="document" style="width:85%;">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
								<h4 class="modal-title" id="todosErrorDetailsModalLabel">错误详情	</h4>
							</div>
							<div class="modal-body" style="text-align:center;height:80%;overflow-y:auto;overflow-x:visible;">
								<div class="easyui-accordion" data-options="multiple:true" style="width:98%;margin:0 auto;overflow-x:visible;">
								</div>
							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-primary finish-repulse">完成待办事项</button>
								<button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
							</div>
						</div>
					</div>
				</div>
				<!-- 待办事项 END -->
			</div>
		</div>
	</div>
	<script type="text/javascript">
    seajs.use('myJsPath/js/userManage/bankHome');
</script>
</body>
</html>
