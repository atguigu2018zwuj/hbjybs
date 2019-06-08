package com.mininglamp.currencySys.regulationFile.dao;
import java.util.List;
import java.util.Map;
import com.mininglamp.currencySys.regulationFile.bean.RegulationBean;
/**
 * 
 * <p>Description:法律文件数据访问层 </p>
 * @author zrj
 * @date 2019年2月21日
 * @version 1.0
 */
public interface RegulationDao {
    public List<Map> getDataList(String string, Map<String, Object> params);
	
	public int updateData(String string, Map<String, Object> params);
	
	public int deleteData(String string, Map<String, Object> params);

	public List<Map> getDataCount(String sqlId, Map<String, Object> params);
	
	public List<RegulationBean> getDataList(String string, RegulationBean params);

}
