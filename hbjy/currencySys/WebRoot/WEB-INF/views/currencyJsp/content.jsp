<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String str = request.getParameter("str");
String tablename = request.getParameter("tablename");
List<Map> dataList = (List<Map>)request.getSession().getAttribute(str+"dataList");
List<Map> codeList = (List<Map>)request.getSession().getAttribute("codeList");
String Number = "'"+200/dataList.size()+"%'";
%>
	<!-- 表格部分 start-->
	<table id="dg" class="easyui-data" style="width: 100%" toolbar="#tb" pagination="true" rownumbers="false" fitcolumns="false" singleselect="false" data-options="
				iconCls: 'icon-edit'">
		<!-- 需要冻结的列 -->
		<thead data-options="frozen:true">
			<tr>
				<th data-options="field:'ck',checkbox:true">全选</th>
                <th data-options="field:'cuozuo',align:'center',width:85, formatter:operate">操作</th>
            </tr>
		</thead>
		<thead>
			<tr>
				<c:forEach items="<%=dataList %>" var="s" >
					<c:set var="startName" value="${fn:indexOf(s.FIELD_NAME,'_')}"></c:set>
			 		<c:set var="endName" value="${fn:length(s.FIELD_NAME)}"></c:set>
			 		<c:choose>
			 			<%-- <c:when test="${s.TABLE_NAME == 'wdsl'}">
						<th data-options="field:'${s.FIELD_ID }',align:'center',sortable:true,width:173">${fn:substring(s.FIELD_NAME,startName+1,endName) }</th>
						</c:when> --%>
						<c:when test="${s.HAS_FORMATTER == '1'}">
						<th data-options="field:'${s.FIELD_ID }',formatter:${s.XLK}Formatter,align:'center',sortable:true,width:150">${fn:substring(s.FIELD_NAME,startName+1,endName) }</th>
						</c:when>
						<%--  <c:when test="${s.HAS_FORMATTER == '2'}">
						<th data-options="field:'${s.FIELD_ID }',formatter:${s.FIELD_ID }Formatter,align:'center',sortable:true,width:150">${fn:substring(s.FIELD_NAME,startName+1,endName) }</th>
						</c:when>   --%>
						<c:otherwise>
						<th data-options="field:'${s.FIELD_ID }',align:'center',sortable:true,width:150">${fn:substring(s.FIELD_NAME,startName+1,endName) }</th>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<!-- 具有code列的表名，有code列才有编码项 -->
				<c:forEach items="<%=codeList %>" var="c">
					<c:set var="TABLE_NAME" value="<%=tablename %>"></c:set>
					<c:if test="${c.TABLE_NAME == TABLE_NAME}">
						<th data-options="field:'CODE',align:'center', width:180" hidden="true">编码</th>
					</c:if>
				</c:forEach>
			</tr>
		</thead>
	</table>
	<!-- 表格部分 end -->
	
	<!-- 编辑部分 start-->
	<div id="dlg" class="easyui-dialog" data-options="modal:true" style="width:75%;height:500px;padding:10px 20px;" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" novalidate>
			 <c:forEach items="<%=dataList %>" var="s">
			 	<c:if test="${s.IS_INPUT_AUTO == '0'}">
				 	<div class="fitem" >
				 		<c:set var="startName" value="${fn:indexOf(s.FIELD_NAME,'_')}"></c:set>
				 		<c:set var="endName" value="${fn:length(s.FIELD_NAME)}"></c:set>
				 		
				 		<div class="left_side" style="float: left;">
				    	<c:if test="${s.IS_NULL == '1'}"><span style="color:red">*</span></c:if>	<span >${fn:substring(s.FIELD_NAME,startName+1,endName) }: </span>
						</div>  
			            <div class="content" style="float: left;">
					    	<c:choose>
					    		<c:when test="${s.FIELD_TYPE == 'date' || s.FIELD_TYPE == 'time'}">
					    			<!-- 判断是否为时间格式-->
					    			<c:choose>
					    				<c:when test="${s.FIELD_TYPE == 'date'}">
							    			<input id="XG_${s.FIELD_ID }" name="${s.FIELD_ID }" type="text" editable="false" class="easyui-datebox" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if>>
					    				</c:when>
					    				<c:otherwise>
					    					<input id="XG_${s.FIELD_ID }" name="${s.FIELD_ID }" type="text" editable="false" class="easyui-datetimebox" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if>>
					    				</c:otherwise>
					    			</c:choose>
					    		</c:when>
					    		<c:otherwise>
							 		<c:set var="endType" value="${fn:length(s.FIELD_TYPE)}"></c:set>
					    			<c:choose>
					    				<c:when test="${fn:startsWith(s.FIELD_TYPE,'c') }">
					    					<!-- 判断是否以‘c’开头 -->
							 				<c:choose>
							 					<c:when test="${fn:startsWith(s.FIELD_TYPE,'c..') }">
							 						<!-- 判断是否以‘c..’开头 -->
							 						<!-- 判断是否需要设置下拉框 -->
							 						<c:if test="${s.XLK != null && s.XLK != ''}">
							 							<c:choose>
								 							<c:when test="${s.XLK == 'nbjgh' || s.XLK == 'NBJGH'}">
								 								<!-- 判断是否为 内部机构号 需要设置树形下拉框 -->
									 							<c:choose>
										 							<c:when test="${s.TABLE_NAME == 'finf' || s.TABLE_NAME == 'FINF' || s.TABLE_NAME == 'jgxx' || s.TABLE_NAME == 'JGXX'}">
										 								<%-- <input name="${s.FIELD_ID }" class="easyui-textbox" validType="maxLength[${ fn:substring(s.FIELD_TYPE,3,endType) }]" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> > --%>
										 								<input name="${s.FIELD_ID }" class="easyui-combotree"   data-options="valueField:'mc',textField:'text',url:'/currencySys/code/getJg?sqlId=getJgData'" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if>  />
										 							</c:when>
										 							<c:otherwise>
									 									<input name="${s.FIELD_ID }" class="easyui-combotree" editable="flase" data-options="valueField:'mc',textField:'text',url:'/currencySys/code/getJg?sqlId=getJgData'" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if>  />
										 							</c:otherwise>
									 							</c:choose>
								 							</c:when>
								 							<%-- <c:when test="${s.XLK == 'code_jglb'} }">
								 							    <input name="${s.FIELD_ID }" class="easyui-combobox" editable="flase" data-options="valueField:'id',textField:'text',url:'/currencySys/code/getData?sqlId=getCodeData&table_name=${s.XLK}'" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> />
								 							</c:when> --%>
								 							<c:otherwise>
								 							    
								 								<input name="${s.FIELD_ID }" class="easyui-combobox" editable="flase" data-options="valueField:'id',textField:'text',url:'/currencySys/code/getData?sqlId=getCodeData&table_name=${s.XLK}'" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> />
								 							</c:otherwise>
							 							</c:choose>
							 						</c:if>
							 						<c:if test="${s.XLK == null || s.XLK == ''}">
							 							<input name="${s.FIELD_ID }" class="easyui-textbox" value="" validType="maxLength[${ fn:substring(s.FIELD_TYPE,3,endType) }]" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> >
							 						</c:if>
							 					</c:when>
							 					<c:otherwise>
							 						<c:if test="${s.XLK != null && s.XLK != ''}">
							 							<c:choose>
							 								<c:when test="${s.XLK == 'dqbm' || s.XLK == 'DQBM'}">
							 									<!-- 判断是否为地区编码 需要设置树形下拉框 -->
								 								<input name="${s.FIELD_ID }" class="easyui-combotree" editable="false" data-options="valueField:'mc',textField:'id',url:'/currencySys/static/my/js/city/henan.json'" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> />
							 								</c:when>
							 								<c:otherwise>
							 									<c:choose>
										 							<c:when test="${s.XLK == 'nbjgh' || s.XLK == 'NBJGH'}">
										 								<!-- 判断是否为 内部机构号 需要设置树形下拉框 -->
										 								<c:choose>
												 							<c:when test="${s.TABLE_NAME == 'finf' || s.TABLE_NAME == 'FINF' || s.TABLE_NAME == 'jgxx' || s.TABLE_NAME == 'JGXX'}">
												 								<%-- <input name="${s.FIELD_ID }" class="easyui-textbox" validType="maxLength[${ fn:substring(s.FIELD_TYPE,3,endType) }]" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> > --%>
												 								<input name="${s.FIELD_ID }" class="easyui-combotree" editable="false" data-options="valueField:'mc',textField:'id',url:'/currencySys/code/getJg?sqlId=getJgData'" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if>  />
												 							</c:when>
												 							<c:otherwise>
											 									<input name="${s.FIELD_ID }" class="easyui-combotree" editable="false" data-options="valueField:'mc',textField:'id',url:'/currencySys/code/getJg?sqlId=getJgData'" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if>  />
												 							</c:otherwise>
											 							</c:choose>
										 							</c:when>
										 							<c:otherwise>
							 											<input name="${s.FIELD_ID }" class="easyui-combobox" editable="true" data-options="valueField:'id',textField:'text',url:'/currencySys/code/getData?sqlId=getCodeData&table_name=${s.XLK}'"  <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> />
										 							</c:otherwise>
									 							</c:choose>
							 								</c:otherwise>
							 							</c:choose>
							 						</c:if>
							 						<c:if test="${s.XLK == null || s.XLK == ''}">
							 							<input name="${s.FIELD_ID }" class="easyui-textbox" value="" validType="Lengths[${ fn:substring(s.FIELD_TYPE,1,endType) }]" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> >
							 						</c:if>
							 					</c:otherwise>
							 				</c:choose>
					    				</c:when>
					    				<c:otherwise>
							 				<c:choose>
							 					<c:when test="${fn:indexOf(s.FIELD_TYPE,',') != -1}">
							 						<c:if test="${s.XLK != null && s.XLK != ''}">
							 							<!-- 判断是否包含 ‘,’ 一般为n..(12,5) -->
							 							<input name="${s.FIELD_ID }" class="easyui-combobox" editable="false" data-options="valueField:'id',textField:'text',url:'/currencySys/code/getData?sqlId=getCodeData&table_name=${s.XLK}'" <c:if test="${s.IS_NULL == '1'}"> required="required" </c:if> />
							 						</c:if>
							 						<c:if test="${s.XLK == null || s.XLK == ''}">
							 							<input name="${s.FIELD_ID }" class="easyui-textbox" value="" validType="xiaosh" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> >
							 						</c:if>
							 					</c:when>
							 					<c:otherwise>
							 						<c:if test="${s.XLK != null && s.XLK != ''}">
							 							<input name="${s.FIELD_ID }" class="easyui-combobox" editable="false" data-options="valueField:'id',textField:'text',url:'/currencySys/code/getData?sqlId=getCodeData&table_name=${s.XLK}'" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> />
							 						</c:if>
							 						<c:if test="${s.XLK == null || s.XLK == ''}">
							 							<input name="${s.FIELD_ID }" class="easyui-textbox" value="" validType="maxLength[${ fn:substring(s.FIELD_TYPE,3,endType) }]" <c:if test="${s.IS_NULL == '1'}">required="required" </c:if> >
							 						</c:if>
							 					</c:otherwise>
							 				</c:choose>
					    				</c:otherwise>
					    			</c:choose>
					    		</c:otherwise>
					    	</c:choose>
						</div>  
						<div class="right_side" style="float: left;" >${fn:substring(s.FIELD_NAME,startName+1,endName) }:	<span style="color:red;">${s.REMARK} 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></div>
						<div style="heighT:0px;overflow:hiddne;clear:both;widtH:100%:"></div>
					</div>
			 	</c:if>
			 </c:forEach>
			<c:forEach items="<%=codeList%>" var="c">
				<c:set var="TABLE_NAME" value="<%=tablename%>"></c:set>
				<c:if test="${c.TABLE_NAME == TABLE_NAME}">
					<div class="fitem" style="display:none">
						<!-- <div class="left_side" style="float: left;">
							<span>编码: </span>
						</div> -->
						<!-- <div class="content" style="float: left;"> -->
							<input name="CODE" class="easyui-textbox" type="hidden" validType="maxLength[200]" editable="false">
						<!-- </div> -->
					</div>
				</c:if>
			</c:forEach>
			<div class="fitem" style="display:none">
			    <input name="last_row" type="hidden" id="last_row">
			    <input name="fileName" type="hidden" id="fileName" value="<%=str %>">
			    <input name="tableName" type="hidden" id="tableName" value="<%=tablename %>">
			</div>
			<div class="fitem" style="display:none">
			    <input name="UpdateSqlId" type="hidden" id="UpdateSqlId">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		 <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" id="saveData" style="width:90px;">保存</a>
		 <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" id="insertData" style="width:90px;display: none;">保存</a>
		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">取消</a>
	</div>
	<!-- 编辑部分 end -->
	
	<!--上传部分 start  -->
	<div id="Excel_dlg" class="easyui-dialog" data-options="modal:true" style="width:400px;height:150px;padding:10px 20px;"
	        closed="true" buttons="#Excel_dlg-buttons">
	        <form id="Excel_fm" method="post" novalidate style="margin-top:25px;" enctype="multipart/form-data">
	        	<input id="reportDataFile" name="reportDataFile" class="easyui-filebox" data-options="prompt:'请选择文件...',buttonText:'选择',required:true" style="width:350px">
	        	<input name="fieldAll" type="hidden" id="fieldAll">
	        	<input name="tname" type="hidden" id="tname" value="<%=tablename %>">
	        </form>
	</div>
	<div id="Excel_dlg-buttons">
		 <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" id="saveExcel_dlg" style="width:90px">上传</a>
		 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#Excel_dlg').dialog('close')" style="width:90px">取消</a>
	</div>
	<!--上传部分 end  -->
	<script src="<%=path%>/static/common/utils/myUtils.js"></script>
	<script type="text/javascript">
      	seajs.use('myJsPath/js/content/content');
	</script>