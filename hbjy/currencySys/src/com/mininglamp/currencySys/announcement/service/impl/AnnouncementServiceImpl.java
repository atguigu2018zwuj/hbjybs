package com.mininglamp.currencySys.announcement.service.impl;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
import com.mininglamp.currencySys.announcement.dao.AnnouncementDao;
import com.mininglamp.currencySys.announcement.service.AnnouncementService;
/**
 * 
 * <p>Description:首页公告业务逻辑层实现类 </p>
 * @author zrj
 * @date 2019年1月29日
 * @version 1.0
 */
@Service(value="announcementService")
public class AnnouncementServiceImpl implements AnnouncementService {
	
	@Autowired
	private AnnouncementDao announcementDao;

	

	@Override
	public Object updateData(String string, Map<String, Object> params) {
		
		return announcementDao.updateData(string, params);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		
		return announcementDao.deleteData(sqlId, params);
	}

	@Override
	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		
		return announcementDao.getDataCount(sqlId, params);
	}

	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		
		return announcementDao.getDataList(sqlId, params);
	}

	@Override
	public List<AnnouncementBean> getDataList(String sqlId, AnnouncementBean params) {
		// TODO Auto-generated method stub
		return announcementDao.getDataList(sqlId, params);
	}

	@Override
	public List<Map> getDataMapList(String string, List params) {
		// TODO Auto-generated method stub
		return announcementDao.getDataMapList(string, params);
	}

	

	
	
	

}
