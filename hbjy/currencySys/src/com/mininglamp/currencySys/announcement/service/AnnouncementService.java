package com.mininglamp.currencySys.announcement.service;
import java.util.List;
import java.util.Map;
import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
/**
 * <p>Description: 公告首页业务逻辑层</p>
 * @author zrj
 * @date 2019年1月29日
 * @version 1.0
 */
public interface AnnouncementService {
	public List<Map> getDataList(String sqlId, Map<String, Object> params);

	public Object updateData(String string, Map<String, Object> params);
	
	public int deleteData(String sqlId, Map<String, Object> params);

	public List<Map> getDataCount(String sqlId, Map<String, Object> params);
	
	public List<AnnouncementBean> getDataList(String sqlId, AnnouncementBean params);
	public List<Map> getDataMapList(String string, List params);
}
