package com.mininglamp.currencySys.announcement.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ibm.mq.jmqi.JmqiException;
import com.mininglamp.currencySys.announcement.bean.Announcement;
import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
import com.mininglamp.currencySys.announcement.bean.ResultPojo;
import com.mininglamp.currencySys.announcement.service.AnnouncementService;
import com.mininglamp.currencySys.common.base.bean.ResponseBean;
import com.mininglamp.currencySys.common.base.controller.BaseController;
import com.mininglamp.currencySys.common.util.CommonUtil;
import com.mininglamp.currencySys.insertData.service.InsertDataService;
import com.mininglamp.currencySys.util.SnowflakeIdWorker;
import spc.webos.endpoint.ESB2;
import spc.webos.endpoint.Executable;

/**
 * <p>
 * Description:首页公告控制层
 * </p>
 * 
 * @author zrj
 * @date 2019年1月29日
 * @version 1.0
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController extends BaseController {
	@Autowired
	AnnouncementService announcementService;
	@Autowired
	InsertDataService insertDataService;
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");

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
		AnnouncementBean bean = new AnnouncementBean();
		bean.setId(id);
		List<AnnouncementBean> list = announcementService.getDataList("selectByPrimaryKey", bean);
		List<Map> areaList = announcementService.getDataList("selectPublishRange", params);
		map.put("areaList", areaList);
		map.put("announcement", list.get(0));
		return map;
	}

	/**
	 * 修改公告
	 * 
	 * @param announcement
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	@RequestMapping(value = "/updateAnnouncement")
	@ResponseBody
	public Map<String, Object> updateAnnouncement(MultipartFile annex, String announcementTitle, String id,
			String announcementContent, String updatePublishRanges, String message, HttpServletRequest request)
			throws IllegalStateException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		ResultPojo repojo = new ResultPojo();
		String annexUrl = "";
		String annexName = "";
		if (annex != null) {
			if (!annex.isEmpty()) {
				annexName = annex.getOriginalFilename();
				String basePath = request.getSession().getServletContext().getRealPath("/");
				Date dt = new Date();
				// 最后的aa表示“上午”或“下午” HH表示24小时制 如果换成hh表示12小时制
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				annexUrl = "upload/" + sdf.format(dt) + "/" + annexName;
				String url = basePath + File.separator + annexUrl;
				File dir = new File(url);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				annex.transferTo(dir);
			}
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("announcementTitle", announcementTitle);
		param.put("publishRange", updatePublishRanges);
		param.put("announcementContent", announcementContent);
		param.put("updateTime", new Date());
		if (null != annex) {
			param.put("annexName", annexName);
			param.put("annexUrl", annexUrl);
		}
		param.put("message", message);
		int result = (int) announcementService.updateData("updateByPrimaryKeySelective", param);
		if (result > 0) {
			repojo.setCode(ResultPojo.CODE_SUCCES);
			repojo.setMsg("公告修改成功！");
		} else {
			repojo.setCode(ResultPojo.CODE_FAILURE);
			repojo.setMsg("修改失败，请稍后再试！");
		}
		map.put("result", repojo);
		return map;
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteAnnouncement")
	@ResponseBody
	public Map<String, Object> deleteAnnouncement(@RequestParam(required = false) String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		ResultPojo repojo = new ResultPojo();
		int result = announcementService.deleteData("deleteById", params);
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

	// 根据公告的地区编码查询地区编码的名字
	@RequestMapping(value = "/xiangqingmodal")
	@ResponseBody
	public Map<String, Object> xiangqingModal(String publishRange, String id, String title) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		AnnouncementBean bean = new AnnouncementBean();
		bean.setId(id);
		if (null != title) {
			if (title.length() > 20) {
				bean.setAnnouncementTitle(title.substring(0, 20));
				param.put("announcementTitle", title.substring(0, 20));
			} else {
				bean.setAnnouncementTitle(title);
				param.put("announcementTitle", title);
			}
		}
		List<AnnouncementBean> list = announcementService.getDataList("selectByPrimaryKey", bean);
		param.put("id", id);
		param.put("viewNum", list.get(0).getViewNum() + 1);
		Object result = announcementService.updateData("updateAnnounce", param);
		List<Map> dataList = announcementService.getDataList("selectJgNameById", param);
		map.put("area", dataList.get(0));
		map.put("announcementBean", list.get(0));
		return map;
	}

	/**
	 * 发布范围
	 *
	 * @return
	 */
	@RequestMapping(value = "/findPublishRangeByUserId")
	@ResponseBody
	public Map<String, Object> findPublishRangeByUserId() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map> areaList = announcementService.getDataList("selectPublishRange", param);
		List<Map> list = announcementService.getDataList("selectBrNoListBySL", param);
		StringBuffer buffer = new StringBuffer();
		for (Map b : list) {
			String brNo = (String) b.get("id");
			buffer.append(brNo);
			buffer.append(",");

		}
		String newbuffer = buffer.substring(0, (buffer.length() - 1));
		map.put("areaList", areaList);
		map.put("brNoList", newbuffer);
		return map;
	}

	/**
	 * 新增保存公告
	 * 
	 * @param announcement
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertAnnouncement")
	@ResponseBody
	public Map<String, Object> insertAnnouncement(MultipartFile annex, String announcementTitle, String publishRange,
			String announcementContent, String publishRangeText, String msg, HttpServletRequest request)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		ResultPojo repojo = new ResultPojo();
		String annexUrl = "";
		String annexName = "";
		if (annex != null) {
			if (!annex.isEmpty()) {
				annexName = annex.getOriginalFilename();
				String basePath = request.getSession().getServletContext().getRealPath("/");
				annexUrl = "/upload/announcement/" + System.currentTimeMillis() + "/" + annexName;
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
		params.put("announcementTitle", announcementTitle);
		params.put("announcementContent", announcementContent);
		params.put("publishRange", publishRange);
		if (annex != null) {
			params.put("annexName", annexName);
			params.put("annexUrl", annexUrl);
		}
		params.put("createTime", new Date());
		params.put("viewNum", 0);
		params.put("userName", userName);
		params.put("message", msg);
		/*
		 * List<Map> dataList = announcementService.getDataList("selectJgName", params);
		 * params.put("JRJGMC", dataList.get(0).get("JRJGMC"));
		 */
		int result = insertDataService.insertData("insertSelective", params);
		// 发送短信逻辑
		String chanDate = sdf1.format(new Date());
		//StringBuilder message = "货币金银采集系统中有您一条未完成任务，请前往处理！";
		StringBuffer message=new StringBuffer("货币金银采集系统中有您一条未完成任务:");
		message.append(announcementTitle).append("，请前往处理！");
		String phone = (String) request.getSession().getAttribute("teleph");
		String tlrNo = (String) request.getSession().getAttribute("yh_name");
		/*
		 * List<Map> dataList2 = announcementService.getDataList("selectBrNoByBrNo",
		 * params); if (message.equals("1")) { if (dataList2.size() > 0) { List<Map>
		 * data = announcementService.getDataList("selectBrNoByBrNoTwo", params); for
		 * (Map map2 : data) { String brno = (String) map2.get("id");
		 * CommonUtil.sendMobileMessage(phone, msg, "", request, brno); } } else {
		 * CommonUtil.sendMobileMessage(phone, msg, "", request, publishRange); }
		 * 
		 * }
		 */
		List<String> arrayList = new ArrayList<String>();
		List<Map> brNoList = announcementService.getDataList("selectBrNoListBySL", params);
		String[] split = publishRange.split(",");
		// 获取前段传过来的内部机构号集合
		for (int i = 0; i < split.length; i++) {
			String string = split[i];
			arrayList.add(string);
		}
		String info=message.toString();
		List<Map> listInfo = announcementService.getDataMapList("selectFRHinfo", arrayList);		
			for (Map map2 : listInfo) {
				String SMSNOTICE=(String) map2.get("SMSNOTICE");
				String id = (String) map2.get("id");
				String ph = (String) map2.get("TELEPH");
				if (SMSNOTICE.equals("1")) {
					CommonUtil.sendMobileMessage(ph, info, "", request, id);	
				}
		
			}			
		if (result > 0) {
			repojo.setCode(ResultPojo.CODE_SUCCES);
			repojo.setMsg("公告发布成功！");
		} else {
			repojo.setCode(ResultPojo.CODE_FAILURE);
			repojo.setMsg("发布失败，请稍后再试！");
		}
		map.put("result", repojo);
		return map;
	}

	/**
	 * 所属机构树形查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getGanizatonList")
	@ResponseBody
	public ResponseBean getUserOrganization(HttpServletRequest request, HttpServletResponse response) {
		ResponseBean responseBean = new ResponseBean();
		Map<String, Object> params = getParamters(request);
		List<Map> list = announcementService.getDataList("getGanizatonList", params);
		responseBean.setData(list);
		return responseBean;
	}

}
