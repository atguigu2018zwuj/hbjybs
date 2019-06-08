package com.mininglamp.currencySys.regulationFile.service.impl;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mininglamp.currencySys.regulationFile.bean.RegulationBean;
import com.mininglamp.currencySys.regulationFile.dao.RegulationDao;
import com.mininglamp.currencySys.regulationFile.service.RegulationService;
/**
 * 
 * <p>Description:法律文件业务逻辑实现 </p>
 * @author zrj
 * @date 2019年2月21日
 * @version 1.0
 */
@Service(value="regulationService")
public class RegulationServiceImpl implements RegulationService {
    @Autowired
    private RegulationDao regulationDao;
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		
		return regulationDao.getDataList(sqlId, params);
	}

	@Override
	public Object updateData(String string, Map<String, Object> params) {
		
		return regulationDao.updateData(string, params);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		
		return regulationDao.deleteData(sqlId, params);
	}

	@Override
	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		
		return regulationDao.getDataCount(sqlId, params);
	}

	@Override
	public List<RegulationBean> getDataList(String sqlId, RegulationBean params) {
		
		return regulationDao.getDataList(sqlId, params);
	}

}
