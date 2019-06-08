package com.mininglamp.currencySys.jrjg.service.impl;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mininglamp.currencySys.jrjg.dao.JrjgDao;
import com.mininglamp.currencySys.jrjg.service.JrjgService;
/**
 * 
 * <p>Description:服务层实现类 </p>
 * @author zrj
 * @date 2019年1月16日
 * @version 1.0
 */
@Service(value="jrjgService")
public class JrjgServiceImpl implements JrjgService {
	
	@Autowired
	private JrjgDao jrjgDao;
	
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return jrjgDao.getDataList(sqlId, params);
	}

	@Override
	public Object updateData(String sqlId, Map<String, Object> params) {
		return jrjgDao.updateData(sqlId, params);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return jrjgDao.deleteData(sqlId, params);
	}

	@Override
	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		return jrjgDao.getDataCount(sqlId, params);
	}

}
