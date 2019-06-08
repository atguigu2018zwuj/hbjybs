<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String ssjg=(String)request.getSession().getAttribute("ssjg");
	String snsh=(String)request.getSession().getAttribute("snsh");
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
	<script src="<%=path%>/static/common/seajs/seajs/2.2.1/sea.js" id="seajsnode"></script>
	<script> staticUrl = '<%=path%>/static';</script>
 	<script src="<%=path%>/static/common/config.js"></script>
	<script src="<%=path%>/static/my/config.js"></script>  
	<link href="<%=path%>/static/my/css/umeditor/themes/default/css/umeditor.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="<%=path%>/static/my/css/umeditor/third-party/jquery.min.js"></script>
	<style type="text/css">
		.form-group tr {
			height: 50px;
		}

		/* #insertModal input, select, textarea {
			width : 420px !important;
		} */
		#insertRegulationTitle,#insertPublishRange{
		   width : 420px !important;
		}
		#updateModal  select, textarea {
			width : 420px !important;
		}
		#updateregulationTitle,#updateAnnex,#updateAnnexName{
		    width : 420px !important;
		}
		#xiangqingModal  select, textarea {
			width : 420px !important;
		}
        #ggtm,#fbfw,#fjmc{
            width : 420px !important;
        } 
        #ggtm,#ssjg,#cjz{
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
	</style>

	<title>法规文件</title>
</head>
<body>

<div class="container-fluid">
	<input type="hidden" value="<%=basePath %>" id="url">
	<br>
	<div class="row">
		<form action="<%=basePath%>/viewController/fgwjIndex" id="form1">
			<div style="line-height:34px; width:70px;float:left;text-align: center;margin-left: 10px; margin-left: 10px;">法规标题:</div>
			<div style="float:left; margin-left: 10px;width:120px;">
				<input type="text" class="form-control easyui-validatebox" id="regulationTitle" name="regulationTitle">
			</div>
			<!-- <div style="line-height:34px; width:60px;float:left;text-align: center;margin-left: 10px; margin-left: 10px;">创建者:</div>
			<div style="float:left; margin-left: 10px;width:120px;">
				<input type="text" class="form-control easyui-validatebox" id="creator" name="creator">
			</div> -->
			<div style="line-height:34px; width:60px;float:left;text-align: center;margin-left: 10px; margin-left: 10px;">发布时间:</div>
			<div style="float:left; margin-left: 10px;width:120px;">
				<input type="text" class="form-control easyui-validatebox" id="kssj" name="beginDate" placeholder="开始日期">
			</div>
			<div style="float:left; margin-left: 10px;width:120px;">
				<input type="text" class="form-control easyui-validatebox" id="jssi" name="endDate" placeholder="截止日期">
			</div>
			<%-- <div style="line-height:34px; width:90px;float:left;text-align: center;margin-left: 10px; margin-left: 10px;">附件名称:</div>
			<div style="float:left; margin-left: 10px;width:150px;">
				<input type="text" class="form-control easyui-validatebox" id="annexName" name="annexName" value="${regulationBean.annexName }">
			</div> --%>
			<div style="float:left; margin-left: 10px;">
				<!-- <input type="submit" class="btn btn-primary btn-sm" value="查询"> -->
						<button type="button" class="btn btn-primary btn-sm" onclick="subfrom();" style="float:left; margin-left: 10px;">
							<span class="glyphicon glyphicon-search">&nbsp;</span>法规查询
						</button>
				        <c:if test="${nbjgh =='1699999998' }">
						<button type="button" class="btn btn-primary btn-sm" onclick="insertModal();" style="float:left; margin-left: 10px;">
							<span class="glyphicon glyphicon-plus">&nbsp;</span>法规发布
						</button>
						</c:if>
						<%-- <c:if test="${snsb =='cz' }">
						<button type="button" class="btn btn-primary btn-sm" onclick="insertModal();" style="float:left; margin-left: 10px;">
							<span class="glyphicon glyphicon-plus">&nbsp;</span>法规发布
						</button>
						</c:if> --%>
						
				
			</div>

		</form>
	</div>
<%-- 	 <p>${regulationList}</p> 
	 <p>${regulationList.list}</p>  --%>
	<div class="row">
		<div class="table-responsive" style="margin:30px 20px 20px 20px;">
			<table class="table table-bordered table-hover">
				<thead  align="center">
				<tr style="font-weight:bolder;">
					<td width="6%">序号</td>
					<td width="23%" >法规标题</td>
					<td width="20%">所属机构</td>
					<td width="6%">创建者</td>
					<td width="13%">创建时间</td>
					<td width="12%">操作</td>
				</tr>
				</thead>
				<tbody  align="center">
				<c:forEach items="${regulationList.list}" var="regulation" varStatus="vs">
					<tr>
					  <td>${(regulationList.pageNum-1)*10+(vs.index+1)}</td> 
						<td width="50%" align="left">
							<input type="hidden" value="${regulation.id}">					
							<%-- ${regulation.regulationTitle} --%>
							<c:choose>
                                <c:when test="${fn:length(regulation.regulationTitle) > 20 }">
                                ${fn:substring(regulation.regulationTitle,0,18)}......	
                                </c:when>
                                <c:otherwise>
                                ${regulation.regulationTitle}
                                </c:otherwise>
                                </c:choose>	
						</td>
						<td>${regulation.organization}</td>
						<td>${regulation.creator}</td>
						<td>
							<fmt:formatDate value="${regulation.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>

						</td>
						<td>				
						 <button type="button" class="btn btn-primary btn-sm" onclick="xiangqingModal('${regulation.id }');"data-toggle="modal" data-target="#xiangqingModal">
										<span class="glyphicon glyphicon-search"></span>
									</button> 
									<%-- <button type="button" class="btn btn-primary btn-sm" onclick="updateModal('${regulation.id}');">
										<span class="glyphicon glyphicon-edit"></span>
									</button> --%>
									<c:choose>
									<c:when test="${nbjgh == '1699999998' }">
									       <button type="button" class="btn btn-primary btn-sm" onclick="deletebefore('${regulation.id}');">
										<span class="glyphicon glyphicon-trash"></span>
									      </button>
									</c:when>
									<%-- <c:when test="${snsb == 'cz' }">
									       <button type="button" class="btn btn-primary btn-sm" onclick="deletebefore('${regulation.id}');">
										<span class="glyphicon glyphicon-trash"></span>
									      </button>
									</c:when> --%>
									<c:otherwise></c:otherwise>
									</c:choose>
									
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
						<c:when test="${regulationList.isFirstPage }">
							<li class="disabled"><a href="#" aria-label="Previous">&laquo;</a></li>
							<li class="disabled"><a href="#">&lt;</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="<%=basePath%>/viewController/fgwjIndex?pageNum=${regulationList.firstPage }&regulationTitle=${regulationBean.regulationTitle }&creator=${regulationBean.creator }"  aria-label="Previous">

								&laquo;
							</a>
							</li>
							<li><a href="<%=basePath%>/viewController/fgwjIndex?pageNum=${regulationList.prePage }&regulationTitle=${regulationBean.regulationTitle }&creator=${regulationBean.creator }">&lt;</a></li>

						</c:otherwise>
					</c:choose>
					<c:forEach items="${regulationList.navigatepageNums }" var="i">
						<c:choose>
							<c:when test="${i == regulationList.pageNum }">
								<li class="active"><a href="<%=basePath%>/viewController/fgwjIndex?pageNum=${i }&regulationTitle=${regulationBean.regulationTitle }&creator=${regulationBean.creator }" >${i }</a></li>

							</c:when>
							<c:otherwise>
								<li><a href="<%=basePath%>/viewController/fgwjIndex?pageNum=${i }&regulationTitle=${regulationBean.regulationTitle }&creator=${regulationBean.creator }" >${i }</a></li>

							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:choose>
						<c:when test="${regulationList.isLastPage }">
							<li class="disabled"><a href="#">&gt;</a></li>
							<li class="disabled"><a href="#" aria-label="Previous"> &raquo;</a></li>
						</c:when>
						<c:otherwise>

							<li><a href="<%=basePath%>/viewController/fgwjIndex?pageNum=${regulationList.nextPage }&regulationTitle=${regulationBean.regulationTitle }&creator=${regulationBean.creator }" >&gt;</a></li>

							<li>
								<a href="<%=basePath%>/viewController/fgwjIndex?pageNum=${regulationList.pages }&regulationTitle=${regulationBean.regulationTitle }&creator=${regulationBean.creator }" aria-label="Previous">
									&raquo;
								</a>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</nav>
			<span class="label label-info">共${regulationList.pages }页</span>
			<span class="label label-info">共${regulationList.total }条</span>
		</div>
		<div class="col-md-2"></div>
	</div>
	<div>
		<div class="modal fade" id="insertModal" tabindex="-1" role="dialog" aria-labelledby="insertModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="insertModalLabel">新增法规</h4>
					</div>
					<div class="modal-body" style="text-align:center;">
						<form class="form-inline" id="insertForm" >
							<table class="form-group">
								<tr>
									<td>法规标题：</td>
									<td>
										<input type="text" class="form-control easyui-validatebox" id="insertRegulationTitle"/>
									</td>
								</tr>
								<tr>
									<td>所属机构：</td>
									<td>
										<!-- <select  class="form-control" id="insertPublishRange">
										

										</select> -->
										<input type="text" class="form-control easyui-validatebox" id="insertPublishRange" value="<%=ssjg%>">
									</td>

								</tr>
							<!--  	<tr>
									 <td>公告内容：</td> 
									 <td>
                                    <script id="container" name="content" type="text/plain" style="width:420%;height:100px;">					                                                
					                </script>
					                 </td> 	    
                                 </tr> -->
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
											<input type="text" class="form-control easyui-validatebox" id="updateregulationTitle"/>
										</td>
									</tr>

									<tr>
										<td>接受机构：</td>
										<td>
											<select  class="form-control" id="updatePublishRanges"></select>
										</td>
										
									</tr>
									<tr> 
								   <td>是否发短信：</td>
								   
								   <td>
								   <label><input type="radio" name="message" value="1" id="isyes" /> 是</label>
								   <label><input type="radio" name="message" value="0" id="isnot" /> 否</label> 
					               </td>				   
								   </tr> 
									<tr>
										<td>公告内容：</td>
										<td><textarea class="form-control easyui-validatebox" id="updateregulationContent"></textarea></td>
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
							<h4 class="modal-title" id="deleteModalLabel">确认要删除该法规吗？</h4>
							<input type="hidden" id="annId" />
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary" onclick="deleteRegulation();">是</button>
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
						<h4 class="modal-title" id="xiangqingModalLabel">法规详情	</h4>
					</div>
					<div class="modal-body" style="text-align:center;">
						<form class="form-inline" id="insertForm" >
							<table class="form-group">
								<tr>
									<td>法规题目：</td>
									<td><input type="text" readonly  class="form-control easyui-validatebox" value="" id="ggtm"/></td>
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
    seajs.use('myJsPath/js/regulation/regulation');
</script>
<script type="text/javascript">
			var UE = UM.getEditor('container');
		</script>
</body>
</html>
