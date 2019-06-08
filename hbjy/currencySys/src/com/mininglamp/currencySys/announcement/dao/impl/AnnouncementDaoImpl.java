package com.mininglamp.currencySys.announcement.dao.impl;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
import com.mininglamp.currencySys.announcement.dao.AnnouncementDao;
import com.mininglamp.currencySys.common.base.dao.OracleBaseDao;
/**
 * 
 * <p>Description: </p>
 * @author zrj
 * @date 2019年1月29日
 * @version 1.0
 */
@Repository(value="announcementDao")
public class AnnouncementDaoImpl extends OracleBaseDao implements AnnouncementDao {
	@Override
	protected String getMapper() {
		
		return "AnnouncementMapper";
	}
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}

	@Override
	public int updateData(String sqlId, Map<String, Object> params) {
		return this.normalUpdate(params, sqlId);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return this.normalDelete(params, sqlId);
	}

	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		return this.normalSelectList(params, sqlId);
	}
	@Override
	public List<AnnouncementBean> getDataList(String string, AnnouncementBean params) {
		// TODO Auto-generated method stub
		return this.normalSelectObjectList(params, string);
	}
	public List<Map> getDataMapList(String string, List params) {
		// TODO Auto-generated method stub
		return this.messageSelectObjectList(params, string);
	}

	

}
