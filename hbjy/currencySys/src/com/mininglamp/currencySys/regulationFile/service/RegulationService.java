package com.mininglamp.currencySys.regulationFile.service;
import java.util.List;
import java.util.Map;
import com.mininglamp.currencySys.regulationFile.bean.RegulationBean;
/**
 * <p>Description:法律文件业务逻辑层 </p>
 * @author zrj
 * @date 2019年2月21日
 * @version 1.0
 */
public interface RegulationService {
	public List<Map> getDataList(String sqlId, Map<String, Object> params);

	public Object updateData(String string, Map<String, Object> params);
	
	public int deleteData(String sqlId, Map<String, Object> params);

	public List<Map> getDataCount(String sqlId, Map<String, Object> params);
	
	public List<RegulationBean> getDataList(String sqlId, RegulationBean params);

}
