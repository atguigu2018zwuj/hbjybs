package com.mininglamp.currencySys.customReportSummary.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mininglamp.currencySys.common.base.CodeConst;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.CustomSummaryExcelUtil;
import com.mininglamp.currencySys.common.util.StringUtils;
import com.mininglamp.currencySys.uploadAndDownload.controller.UploadAndAownloadController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 自定义报表汇总管理
 */
@Controller
@RequestMapping(value = "/cusReportSummaryController")
public class CustomReportSummaryController extends BaseController{
	/**
	 * 模板、汇总文件上传，并返回上传后的文件路径
	 * @param request
	 * @param response
	 * @param templateUpload 模板文件
	 * @param summaryUploads 汇总文件数组
	 * @return 上传后的文件路径（字段说明：<br/>
	 * 		templatePath：[string] 模板文件路径；<br/>
	 * 		summaryPaths：[string[]] 汇总文件路径列表；<br/>
	 * 		templateHeaders：[Map<String, String>] 模板文件中表头与单元格编号的对应关系；）
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/uploadSummaryFiles")
	@ResponseBody
	public ResponseBean uploadSummaryFiles(HttpServletRequest request,
			HttpServletResponse response,@RequestParam MultipartFile templateUpload,@RequestParam("summaryUpload") MultipartFile[] summaryUploads) throws Exception{
		ResponseBean result = new ResponseBean();
		// 结果json
		JSONObject resultDate = new JSONObject();
		Map<String, Object> params = getParamters(request);
		
		// 模板路径
		resultDate.put("templatePath", UploadAndAownloadController.uploadFile(templateUpload, null, null, request));
		// 模板上传失败
		if (resultDate.get("templatePath") == null) {
			return result;
		}
		
		// 解析表头
		List<Map<String, String>> templateHeaders = CustomSummaryExcelUtil.readExcel(resultDate.getString("templatePath"));
		// 各单元格加总汇总时，需截掉数据开始行号之后的所有行
		if (StringUtils.isNotEmpty(params.get("summaryType")) && "CELL_SUM".equals(params.get("summaryType")) 
				&& StringUtils.isNotEmpty(params.get("dataRowNumStart")) && StringUtils.isNotEmpty(params.get("dataRowNumEnd"))) {
			List<Map<String, String>> needRemoveTemplateHeaders = new ArrayList<Map<String, String>>();
			for (int templateHeadersIx = Integer.parseInt(StringUtils.parseString(params.get("dataRowNumStart")))-1; templateHeadersIx < templateHeaders.size(); templateHeadersIx++) {
				needRemoveTemplateHeaders.add(templateHeaders.get(templateHeadersIx));
			}
			templateHeaders.removeAll(needRemoveTemplateHeaders);
		}
		// 取表头最后一行，并去除值为空的项
		if (templateHeaders != null && !templateHeaders.isEmpty()) {
			// 仅最后一行表头，无父类，{"B2":"金融机构编码","C2":"内部机构号","D2":"许可证号"}
			if (CustomSummaryExcelUtil.getTemplateHeadersWithoutParent(templateHeaders) != null && !CustomSummaryExcelUtil.getTemplateHeadersWithoutParent(templateHeaders).isEmpty()) {
				resultDate.put("templateHeaders",CustomSummaryExcelUtil.getTemplateHeadersWithoutParent(templateHeaders));
			}
			
			// 有父类
			if (templateHeaders.size() >= 2) {
				// 有父类，{"B2":"金融机构-金融机构编码","C2":"金融机构-内部机构号","D2":"金融机构-许可证号"}
				if (CustomSummaryExcelUtil.getTemplateHeadersWithParent(templateHeaders) != null && !CustomSummaryExcelUtil.getTemplateHeadersWithParent(templateHeaders).isEmpty()) {
					resultDate.put("templateHeadersWithParent",CustomSummaryExcelUtil.getTemplateHeadersWithParent(templateHeaders));
				}
				
				// 除最后一行的纯父类，{"B1_C1_D1_E1_F1_G1":"金融机构","H1_I1_O1":"上级金融机构"}
				if (CustomSummaryExcelUtil.getTemplateHeaderParents(templateHeaders) != null && !CustomSummaryExcelUtil.getTemplateHeaderParents(templateHeaders).isEmpty()) {
					resultDate.put("templateHeaderParents",CustomSummaryExcelUtil.getTemplateHeaderParents(templateHeaders));
				}
			}
		}
		// 取得表头失败可能是汇总方式选择错误，或数据行范围填写错误(此时检测不出数据行范围超出范围的问题)
		if (!resultDate.containsKey("templateHeaders") || !resultDate.containsKey("templateHeadersWithParent") || !resultDate.containsKey("templateHeaderParents")) {
			result.setCode(CodeConst.CODE_ERROR);
			if ("CELL_SUM".equals(params.get("summaryType"))) {
				result.setMessage("取得表头失败，请检查数据行范围是否正确！");
			} else {
				result.setMessage("取得表头失败，请检查汇总方式是否选择正确！");
			}
			return result;
		}
		
		// 汇总文件路径
		JSONArray summaryPaths = new JSONArray();
		String summaryPath = null;
		// 标识当前文件是第几个
		int fileIx = 1;
		for (MultipartFile summaryUpload : summaryUploads) {
			// 重命名，防止get请求url超长
			summaryPath = UploadAndAownloadController.uploadFile(summaryUpload, null, "summaryFile"+(fileIx++), request);
			if (StringUtils.isEmpty(summaryPath)) {
				return result;
			} else {
				summaryPaths.add(summaryPath);
			}
		}
		resultDate.put("summaryPaths", summaryPaths);
		
		// 校验参数，若有错误则返回错误信息
		if ("CELL_SUM".equals(params.get("summaryType"))) {
			JSONObject simpleSummaryOption = new JSONObject();
			simpleSummaryOption.put("dataStartRowNum", StringUtils.parseString(params.get("dataRowNumStart")));
			simpleSummaryOption.put("dataEndRowNum", StringUtils.parseString(params.get("dataRowNumEnd")));
			List<Map<String,String>> tempResult = CustomSummaryExcelUtil.summary(resultDate.getString("templatePath"), resultDate.getJSONArray("summaryPaths"), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), StringUtils.parseString(params.get("summaryType")), simpleSummaryOption);
			if (tempResult != null && !tempResult.isEmpty() && tempResult.size() == 1 && tempResult.get(0).size() == 1) {
				result.setCode(CodeConst.CODE_ERROR);
				result.setMessage(tempResult.get(0).get("A1"));
				return result;
			}
		}
		
		result.setData(resultDate);
		return result;
	}
	
	/**
	 * 汇总Excel
	 * @param request 参数详见： CustomSummaryExcelUtil.summary(JSONObject)
	 */
	@RequestMapping(value = "/customSummaryExcel")
	public void customSummaryExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> params = getParamters(request);
		String paramJson = StringUtils.parseString(params.get("params"));
		
		// js监听下载文件是否准备好
		Cookie cookie = new Cookie("downloadToken", StringUtils.parseString(params.get("downloadToken")));
		cookie.setPath("/");
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
		
		// 特殊字符处理
		// "add"对应"+"号
		paramJson = StringUtils.isNotEmpty(paramJson) ? paramJson.replaceAll("add", "+") : "";
		
		// 汇总后的文件路径
		String summaryedFilePath = CustomSummaryExcelUtil.summaryExcel(paramJson);
		if (StringUtils.isNotEmpty(summaryedFilePath)) {
			// 汇总成功，下载文件
			UploadAndAownloadController.downloadFile(null, summaryedFilePath, request, response);
			
			// 删除临时文件
			// 汇总后的文件
			File summaryedFile = new File(summaryedFilePath);
			if (summaryedFile.exists()) {
				summaryedFile.delete();
			}
			// TODO （暂不考虑临时文件不删的情况）
//			// 模板文件
//			JSONObject paramObj = JSONObject.fromObject(paramJson);
//			File templateFile = new File(paramObj.getString("templatePath"));
//			if (templateFile.exists()) {
//				templateFile.delete();
//			}
//			// 汇总文件
//			@SuppressWarnings("unchecked")
//			List<String> summaryFilePaths = paramObj.getJSONArray("filePathList");
//			File summaryFile = null;
//			for (String summaryFilePath : summaryFilePaths) {
//				summaryFile = new File(summaryFilePath);
//				if (summaryFile.exists()) {
//					summaryFile.delete();
//				}
//			}
		}
	}
}

