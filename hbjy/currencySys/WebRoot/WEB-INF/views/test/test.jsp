<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String str = request.getParameter("str");
List<Map> dataList = (List<Map>)request.getSession().getAttribute(str+"dataList");
%>
	<table id="dg" class="easyui-datagrid" style="width: 100%" toolbar="#tb" pagination="true" rownumbers="false" fitcolumns="false" singleselect="false" data-options="
				iconCls: 'icon-edit'">
		<thead>
			<tr>
				<th data-options="field:'ck',checkbox:true">全选</th>
				<th data-options="field:'cuozuo',align:'center',width:85, formatter:operate">操作</th>
				<c:forEach items="<%=dataList %>" var="s">
					<c:set var="startName" value="${fn:indexOf(s.FIELD_NAME,'_')}"></c:set>
			 		<c:set var="endName" value="${fn:length(s.FIELD_NAME)}"></c:set>
					<th data-options="field:'${s.FIELD_ID }',align:'center',width:120">${fn:substring(s.FIELD_NAME,startName+1,endName) }</th>
				</c:forEach>
			</tr>
		</thead>
	</table>
	<div id="dlg" class="easyui-dialog" data-options="modal:true" style="width:75%;height:500px;padding:10px 20px;" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" novalidate>
			 <c:forEach items="<%=dataList %>" var="s">
			 	<div class="fitem" >
			 		<c:set var="startName" value="${fn:indexOf(s.FIELD_NAME,'_')}"></c:set>
			 		<c:set var="endName" value="${fn:length(s.FIELD_NAME)}"></c:set>
			 		
			 		<div class="left_side" style="float: left;">
			    		<span >${fn:substring(s.FIELD_NAME,startName+1,endName) }: </span>
					</div>  
		            <div class="content" style="float: left;">
				    	<c:choose>
				    		<c:when test="${s.FIELD_TYPE == 'date' || s.FIELD_TYPE == 'time'}">
				    			<input id="XG_${s.FIELD_ID }" name="${s.FIELD_ID }" placeholder="数据日期" readonly="readonly" required="required"/>
				    		</c:when>
				    		<c:otherwise>
				    			<c:choose>
				    				<c:when test="${fn:startsWith(s.FIELD_TYPE,'c') }">
						 				<c:set var="endType" value="${fn:length(s.FIELD_TYPE)}"></c:set>
						 				<c:choose>
						 					<c:when test="${fn:startsWith(s.FIELD_TYPE,'c..') }">
						 						<input name="${s.FIELD_ID }" class="easyui-textbox" validType="maxLength[${ fn:substring(s.FIELD_TYPE,3,endType) }]" required="required">
						 					</c:when>
						 					<c:otherwise>
						 						<input name="${s.FIELD_ID }" class="easyui-textbox" validType="Lengths[${ fn:substring(s.FIELD_TYPE,1,endType) }]" required="required">
						 					</c:otherwise>
						 				</c:choose>
				    				</c:when>
				    				<c:otherwise>
				    					<c:set var="endType" value="${fn:length(s.FIELD_TYPE)}"></c:set>
						 				<c:choose>
						 					<c:when test="${fn:indexOf(s.FIELD_TYPE,',') }">
						 						<input name="${s.FIELD_ID }" class="easyui-textbox" validType="xiaosh" required="required">
						 					</c:when>
						 					<c:otherwise>
						 						<input name="${s.FIELD_ID }" class="easyui-textbox" validType="maxLength[${ fn:substring(s.FIELD_TYPE,3,endType) }]" required="required">
						 					</c:otherwise>
						 				</c:choose>
				    				</c:otherwise>
				    			</c:choose>
				    		</c:otherwise>
				    	</c:choose>
					</div>  
					<div class="right_side" style="float: left;" >${fn:substring(s.FIELD_NAME,startName+1,endName) }:	<span style="color:red;">${s.REMARK} </span></div>
					<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
				</div>
			 </c:forEach>
			 <div class="fitem">
			    <input name="last_row" type="hidden" id="last_row">
			</div>
			<div class="fitem">
			    <input name="UpdateSqlId" type="hidden" id="UpdateSqlId">
			</div>
		</form>
	</div>
