<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
	<link href="<%=path%>/static/my/css/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
	<link href="<%=path%>/static/my/css/bankCommon.css" rel="stylesheet">
	<link href="<%=path%>/static/common/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
	<link href="<%=path %>/static/my/theme/default/style.css" rel="stylesheet">
	<link href="<%=path %>/static/common/gallery/ztree/3.x/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
	<script src="<%=path%>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
	<script> staticUrl = '<%=path%>/static';</script>
	<script src="<%=path%>/static/common/config.js"></script>
	<script src="<%=path%>/static/my/config.js"></script>
	<link href="<%=path%>/static/my/css/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="<%=path%>/static/my/css/umeditor/third-party/jquery.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=path%>/static/my/css/umeditor/umeditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=path%>/static/my/css/umeditor/umeditor.min.js"></script>
    <script type="text/javascript" src="<%=path%>/static/my/css/umeditor/lang/zh-cn/zh-cn.js"></script>
	<style type="text/css">
		.form-group tr {
			height: 50px;
		}

		/* #insertModal input, select, textarea {
			width : 420px !important;
		} */
		#insertAnnouncementTitle,#insertPublishRange,#XqPublishRange,#updatePublishRange{
		   width : 420px !important;
		}
		#updateModal  select, textarea {
			width : 420px !important;
		}
		#updateAnnouncementTitle,#updateAnnex,#updateAnnexName{
		    width : 420px !important;
		}
		#xiangqingModal  select, textarea {
			width : 420px !important;
		}
        #ggtm,#fbfw,#fjmc{
            width : 420px !important;
        }  
		#insertModal textarea {
			height : 100px !important;
		}
		#updateModal textarea {
			height : 100px !important;
		}
		td{
		word-wrap:break-word;
		word-break:break-all;
		}
		.col-md-1{
			padding-top: 6px;
		}
		 .shangji{
		   height:34px;
		   width : 120px !important;
		   float:left;
		 } 
		 .xiaji{
		   height:34px;
		   width : 120px !important;
		   float:left;
		 } 
		/*  #isyes{
		 float:left;
		 margin-right:-300px
		 } */
		/* input[type=radio]{
		float:left;
		
		 }  */
		 label{
		    height: 20px;
            display: inline-block;
            padding: 5px;
            font-weight:100;
            float:left;
		 }
		 .title{
		   top: 20px;
		 }
		/*  p{
		 width: 420px;
		 } */
		 .kssj{
		    display: block;
		    width: 100%;
		    height: 34px;
		    padding: 6px 12px;
		    font-size: 14px;
		    line-height: 1.42857143;
		    color: #555;
		    background-color: #fff;
		    background-image: none;
		    border: 1px solid #ccc;
		    border-radius: 4px;
		 }
	</style>

	<title>公告管理</title>
</head>
<body>

<div class="container-fluid">
	<input type="hidden" value="<%=basePath %>" id="url">
	<br>
	<div class="row">
		<form action="<%=basePath%>/viewController/ggglIndex" id="form1">
			<div style="line-height:34px; width:70px;float:left;text-align: center;margin-left: 10px; margin-left: 10px;">公告标题:</div>
			<div style="float:left; margin-left: 10px;width:120px;">
				<input type="text" class="form-control easyui-validatebox" id="announcementTitle" name="announcementTitle">
			</div>
			<div style="line-height:34px; width:60px;float:left;text-align: center;margin-left: 10px; margin-left: 10px;">创建者:</div>
			<div style="float:left; margin-left: 10px;width:120px;">
				<input type="text" class="form-control easyui-validatebox" id="creator" name="creator">
			</div>
			<div style="line-height:34px; width:60px;float:left;text-align: center;margin-left: 10px; margin-left: 10px;">发布时间:</div>
			<div style="float:left; margin-left: 10px;width:120px;">
				<input type="text" class="form-control easyui-validatebox" id="kssj" name="beginDate" placeholder="开始日期">
			</div>
			<div style="float:left; margin-left: 10px;width:120px;">
				<input type="text" class="form-control easyui-validatebox" id="jssi" name="endDate" placeholder="截止日期">
			</div>
			<%-- <div style="line-height:34px; width:90px;float:left;text-align: center;margin-left: 10px; margin-left: 10px;">附件名称:</div>
			<div style="float:left; margin-left: 10px;width:150px;">
				<input type="text" class="form-control easyui-validatebox" id="annexName" name="annexName" value="${announcementBean.annexName }">
			</div> --%>
			<div style="float:left; margin-left: 10px;">
				<!-- <input type="submit" class="btn btn-primary btn-sm" value="查询"> -->
						<button type="button" class="btn btn-primary btn-sm" onclick="subfrom();" style="float:left; margin-left: 10px;">
							<span class="glyphicon glyphicon-search">&nbsp;</span>公告查询
						</button>
				        <c:if test="${nbjgh=='1699999998' }">
						<button type="button" class="btn btn-primary btn-sm" onclick="insertModal();" style="float:left; margin-left: 10px;">
							<span class="glyphicon glyphicon-plus">&nbsp;</span>公告发布
						</button>
						</c:if>
						
						
				
			</div>

		</form>
	</div>
<%-- 	 <p>${announcementList}</p> 
	 <p>${announcementList.list}</p>  --%>
	<div class="row">
		<div class="table-responsive" style="margin:30px 20px 20px 20px;">
			<table class="table table-bordered table-hover">
				<thead  align="center">
				<tr style="font-weight:bolder;">
					<td width="6%">序号</td>
					<td width="23%" >公告标题</td>
					<td width="20%">附件名称</td>
					<td width="6%">创建者</td>
					<td width="13%">创建时间</td>
					<td width="8%">浏览次数</td>
					<td width="12%">操作</td>
				</tr>
				</thead>
				<tbody  align="center">
				<c:forEach items="${announcementList.list}" var="announcement" varStatus="vs">
					<tr>
					  <td>${(announcementList.pageNum-1)*10+(vs.index+1)}</td> 
						<td width="50%" align="left">
							<input type="hidden" value="${announcement.id}">
							<c:choose>
                                <c:when test="${fn:length(announcement.announcementTitle) > 20 }">
                                ${fn:substring(announcement.announcementTitle,0,18)}......	
                                </c:when>
                                <c:otherwise>
                                ${announcement.announcementTitle}
                                </c:otherwise>
                                </c:choose>					
							<%-- ${announcement.announcementTitle} --%>
						</td>
						<td>${announcement.annexName}</td>
						<td>${announcement.creator}</td>
						<td>
							<fmt:formatDate value="${announcement.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>

						</td>
						<td >${announcement.viewNum}</td>
						<td>				
						 <button type="button" class="btn btn-primary btn-sm" onclick="xiangqingModal('${announcement.publishRange}','${announcement.id }');"data-toggle="modal" data-target="#xiangqingModal">
										<span class="glyphicon glyphicon-search"></span>
									</button> 
									<c:if test="${nbjgh=='1699999998' }">
									<button type="button" class="btn btn-primary btn-sm" onclick="updateModal('${announcement.id}');">
										<span class="glyphicon glyphicon-edit"></span>
									</button>
									<button type="button" class="btn btn-primary btn-sm" onclick="deletebefore('${announcement.id}');">
										<span class="glyphicon glyphicon-trash"></span>
									</button>
									</c:if>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div class="row" style="text-align:right;">
		<div class="col-md-11">
			<nav aria-label="Page navigation">
				<ul class="pagination">
					<c:choose>
						<c:when test="${announcementList.isFirstPage }">
							<li class="disabled"><a href="#" aria-label="Previous">&laquo;</a></li>
							<li class="disabled"><a href="#">&lt;</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="<%=basePath%>/viewController/ggglIndex?pageNum=${announcementList.firstPage }&announcementTitle=${announcementBean.announcementTitle }&creator=${announcementBean.creator }&annexName=${announcementBean.annexName }"  aria-label="Previous">

								&laquo;
							</a>
							</li>
							<li><a href="<%=basePath%>/viewController/ggglIndex?pageNum=${announcementList.prePage }&announcementTitle=${announcementBean.announcementTitle }&creator=${announcementBean.creator }&annexName=${announcementBean.annexName }">&lt;</a></li>

						</c:otherwise>
					</c:choose>
					<c:forEach items="${announcementList.navigatepageNums }" var="i">
						<c:choose>
							<c:when test="${i == announcementList.pageNum }">
								<li class="active"><a href="<%=basePath%>/viewController/ggglIndex?pageNum=${i }&announcementTitle=${announcementBean.announcementTitle }&creator=${announcementBean.creator }&annexName=${announcementBean.annexName }" >${i }</a></li>

							</c:when>
							<c:otherwise>
								<li><a href="<%=basePath%>/viewController/ggglIndex?pageNum=${i }&announcementTitle=${announcementBean.announcementTitle }&creator=${announcementBean.creator }&annexName=${announcementBean.annexName }" >${i }</a></li>

							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:choose>
						<c:when test="${announcementList.isLastPage }">
							<li class="disabled"><a href="#">&gt;</a></li>
							<li class="disabled"><a href="#" aria-label="Previous"> &raquo;</a></li>
						</c:when>
						<c:otherwise>

							<li><a href="<%=basePath%>/viewController/ggglIndex?pageNum=${announcementList.nextPage }&announcementTitle=${announcementBean.announcementTitle }&creator=${announcementBean.creator }&annexName=${announcementBean.annexName }" >&gt;</a></li>

							<li>
								<a href="<%=basePath%>/viewController/ggglIndex?pageNum=${announcementList.pages }&announcementTitle=${announcementBean.announcementTitle }&creator=${announcementBean.creator }&annexName=${announcementBean.annexName }" aria-label="Previous">
									&raquo;
								</a>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</nav>
			<span class="label label-info">共${announcementList.pages }页</span>
			<span class="label label-info">共${announcementList.total }条</span>
		</div>
		<div class="col-md-2"></div>
	</div>
	<div>
		<div class="modal fade" id="insertModal" tabindex="-1" role="dialog" aria-labelledby="insertModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="insertModalLabel">新增公告</h4>
					</div>
					<div class="modal-body" style="text-align:center;">
						<form class="form-inline" id="insertForm" >
							<table class="form-group">
								<tr>
									<td>公告题目：</td>
									<td>
										<input type="text" class="form-control easyui-validatebox" id="insertAnnouncementTitle"/>
									</td>
								</tr>
								<tr>
									<td>接受机构：</td>
									<td>
										<!-- <select  class="form-control" id="insertPublishRange">

										</select> -->
										<input type="text" class="form-control easyui-validatebox" id="insertPublishRange" />
									</td>

								</tr>
								 <tr> 
								   <td>是否发短信：</td>
								   
								   <td>
								   <label><input type="radio" name="message" value="1" checked/> 是</label>
								    <label><input type="radio" name="message" value="0" /> 否</label>
					               </td>
					                
								   
								</tr> 
								<!-- <tr>
									<td>发布范围：</td>
									 <td>
										<select  class="xiaji" id="insertPublishRange"></select>
									</td> 
								</tr> -->
								<tr>
								    <!-- <span class="title"><td>公告内容</td></span> -->
									 <td>公告内容：</td> 
									 <td>
                                    <script id="container" name="content" type="text/plain" style="width:420%;height:100px;">					                                                
					                </script>
					                 </td> 	    
                                 </tr>
								<tr>
									<td>附件：</td>
									<td>
										<input type="file" class="from-control easyui-validatebox" id="annex" />
									</td>
								</tr>
							</table>

						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" onclick="save();">确认提交</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">放弃返回</button>
					</div>
				</div>
			</div>
		</div>
		<div>
			<div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="updateModalLabel">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
							<h4 class="modal-title" id="insertModalLabel">修改公告</h4>
						</div>
						<div class="modal-body" style="text-align:center;">
							<form class="form-inline" id="updateForm">
								<table class="form-group">
									<tr>
										<td>公告题目：</td>
										<td>
											<input type="hidden" id="updateId"/>
											<input type="text" class="form-control easyui-validatebox" id="updateAnnouncementTitle"/>
										</td>
									</tr>

									<tr>
										<td>接受机构：</td>
										<td>
											<!-- <select  class="form-control" id="updatePublishRanges"></select> -->
											<input type="text" class="form-control easyui-validatebox" id="updatePublishRange"/>
										</td>
										
									</tr>
									<tr> 
								   <td>是否发短信：</td>
								   
								   <td>
								   <label><input type="radio" name="messag" value="1" id="isyes" /> 是</label>
								   <label><input type="radio" name="messag" value="0" id="isnot" /> 否</label> 
					               </td>				   
								   </tr> 
									<tr>
										<td>公告内容：</td>
										<td><textarea class="form-control easyui-validatebox" id="updateAnnouncementContent"></textarea></td>
									</tr>
									<tr>
										<td>附件：</td>
										<td>
											<input type="text" readonly class="form-control easyui-validatebox" id="updateAnnexName" />
											<input type="file" style="display:none" class="from-control easyui-validatebox" id="updateAnnex" />
										</td>
									</tr>
									<tr>
										<td></td>
										<td>
											<button type="button" class="btn btn-primary" onclick="uploadAgain();">重新上传附件</button>
										</td>
									</tr>
								</table>

							</form>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" onclick="update();">确认提交</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">放弃返回</button>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div>
			<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
							<h4 class="modal-title" id="deleteModalLabel">确认要删除该公告吗？</h4>
							<input type="hidden" id="annId" />
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" onclick="deleteAnnouncement();">是</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">否</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div>
		<div class="modal fade" id="xiangqingModal" tabindex="-1" role="dialog" aria-labelledby="xiangqingModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="xiangqingModalLabel">公告详情	</h4>
					</div>
					<div class="modal-body" style="text-align:center;">
						<form class="form-inline" id="updateForm" >
							<table class="form-group">
								<tr>
									<td>公告题目：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox" value="" id="ggtm"/></td>
								</tr>
								<tr>
									<td>接受机构：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox"  id="XqPublishRange"/></td>
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
</div>
<script type="text/javascript">
    seajs.use('myJsPath/js/announcement/announcement');
</script>
<script type="text/javascript">
			var UE = UM.getEditor('container');
		</script>
</body>
</html>
