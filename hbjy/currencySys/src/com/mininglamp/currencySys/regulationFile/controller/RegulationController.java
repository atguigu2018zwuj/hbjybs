package com.mininglamp.currencySys.regulationFile.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.mininglamp.currencySys.announcement.bean.ResultPojo;
import com.mininglamp.currencySys.insertData.service.InsertDataService;
import com.mininglamp.currencySys.regulationFile.bean.RegulationBean;
import com.mininglamp.currencySys.regulationFile.service.RegulationService;
import com.mininglamp.currencySys.util.SnowflakeIdWorker;

/**
 * 
 * <p>
 * Description:法律文件控制层
 * </p>
 * 
 * @author zrj
 * @date 2019年2月22日
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/regulation")
public class RegulationController {
	@Autowired
	RegulationService regulationService;
	@Autowired
	InsertDataService insertDataService;

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findById")
	@ResponseBody
	public Map<String, Object> findById(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		RegulationBean bean = new RegulationBean();
		bean.setId(id);
		List<RegulationBean> list = regulationService.getDataList("selectByPrimaryKey", bean);
		List<Map> areaList = regulationService.getDataList("selectPublishRange", params);
		map.put("areaList", areaList);
		map.put("announcement", list.get(0));
		return map;
	}

	/**
	 * 法规文件查看详情
	 * 
	 * @param publishRange
	 * @param id
	 * @param title
	 * @return
	 */
	@RequestMapping(value = "/xiangqingmodal")
	@ResponseBody
	public Map<String, Object> xiangqingModal(String id, String title) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		RegulationBean bean = new RegulationBean();
		bean.setId(id);
		if (null != title) {		
			if (title.length()>20) {
				bean.setRegulationTitle(title.substring(0, 20));
			}else {
				bean.setRegulationTitle(title);
			}		
			}
		List<RegulationBean> list = regulationService.getDataList("selectByPrimaryKey", bean);
		param.put("id", id);
		map.put("regulationBean", list.get(0));
		return map;
	}

	/**
	 * 法规文件删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteRegulation")
	@ResponseBody
	public Map<String, Object> deleteRegulation(@RequestParam(required = false) String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		ResultPojo repojo = new ResultPojo();
		int result = regulationService.deleteData("deleteById", params);
		if (result > 0) {
			repojo.setCode(ResultPojo.CODE_SUCCES);
			repojo.setMsg("删除成功！");
			map.put("result", repojo);
		} else {
			repojo.setCode(ResultPojo.CODE_FAILURE);
			repojo.setMsg("删除失败，请稍后再试！");
			map.put("result", repojo);
		}

		return map;
	}

	/**
	 * 所属机构查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findPublishRangeByUserId")
	@ResponseBody
	public Map<String, Object> findPublishRangeByUserId() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map> areaList = regulationService.getDataList("selectPublishRange", param);
		map.put("areaList", areaList);
		return map;
	}

	/**
	 * 保存法规标题
	 * 
	 * @param regulationTitle
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping(value = "/insertRegulation")
	@ResponseBody
	public Map<String, Object> insertAnnouncement(MultipartFile annex, String regulationTitle, String publishRange,
			String publishRangeText, HttpServletRequest request) throws IllegalStateException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		ResultPojo repojo = new ResultPojo();
		// 上传附件
		String annexUrl = "";
		String annexName = "";
		if (annex != null) {
			if (!annex.isEmpty()) {
				annexName = annex.getOriginalFilename();
				String basePath = request.getSession().getServletContext().getRealPath("/");
				annexUrl = "/upload/regulation/" + System.currentTimeMillis() + "/" + annexName;
				String url = basePath + File.separator + annexUrl;
				File dir = new File(url);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				annex.transferTo(dir);
			}
		}
		String userName = (String) request.getSession().getAttribute("name");
		params.put("id", SnowflakeIdWorker.nextId());
		params.put("regulationTitle", regulationTitle);
		params.put("publishRange", publishRange);
		params.put("username", userName);
		params.put("createTime", new Date());
		if (annex != null) {
			params.put("annexName", annexName);
			params.put("annexUrl", annexUrl);
		}
		int result = insertDataService.insertData("insertfgwjSelective", params);
		if (result > 0) {
			repojo.setCode(ResultPojo.CODE_SUCCES);
			repojo.setMsg("法规发布成功！");
		} else {
			repojo.setCode(ResultPojo.CODE_FAILURE);
			repojo.setMsg("发布失败，请稍后再试！");
		}
		map.put("result", repojo);
		return map;
	}

}
