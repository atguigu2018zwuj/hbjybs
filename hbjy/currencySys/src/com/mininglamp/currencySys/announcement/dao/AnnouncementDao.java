package com.mininglamp.currencySys.announcement.dao;
import java.util.List;
import java.util.Map;
import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
/**
 * 
 * <p>Description:首页公告dao层 </p>
 * @author zrj
 * @date 2019年1月29日
 * @version 1.0
 */
public interface AnnouncementDao {
    public List<Map> getDataList(String string, Map<String, Object> params);
	
	public int updateData(String string, Map<String, Object> params);
	
	public int deleteData(String string, Map<String, Object> params);

	public List<Map> getDataCount(String sqlId, Map<String, Object> params);
	
	public List<AnnouncementBean> getDataList(String string, AnnouncementBean params);

	public List<Map> getDataMapList(String string, List params);
}
