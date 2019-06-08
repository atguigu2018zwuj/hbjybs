package com.mininglamp.currencySys.ManaPro.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mininglamp.currencySys.ManaPro.dao.impl.ManaProDaoImpl;
import com.mininglamp.currencySys.ManaPro.service.ManaProService;
import com.mininglamp.currencySys.util.JDBCUtils;

@Service(value="manaProService")
public class ManaProServiceImpl implements ManaProService{

	@Autowired
	private ManaProDaoImpl manaProDaoImpl;
	
	@Override
	public List<Map> getDataList(String sqlId, Map<String, Object> params) {
		return manaProDaoImpl.getDataList(sqlId, params);
	}

	@Override
	public Object updateData(String sqlId, Map<String, Object> params) {
		return manaProDaoImpl.updateData(sqlId, params);
	}

	@Override
	public int deleteData(String sqlId, Map<String, Object> params) {
		return manaProDaoImpl.deleteData(sqlId, params);
	}

	@Override
	public List<Map> getDataCount(String sqlId, Map<String, Object> params) {
		return manaProDaoImpl.getDataCount(sqlId, params);
	}
	
	/**
	 * zstl表删除，增加
	 * @param con
	 * @param params
	 * @param excelList
	 * @param ps
	 * @param ps1
	 * @return
	 *//*
	@SuppressWarnings("unused")
	public static int[] saveAndDelzstl(Connection con, Map<String, Object> params, List<Map> excelList,
			PreparedStatement ps, PreparedStatement ps1, List<Map> paramList) {
		int[] zstl = null;
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
			//增加
			//String sql = "INSERT into ASSET_MAN_PRODUCT_ZSTL (SJRQ,JRJGBM,NBJGH,CPDM,HTH,TSMDZTLX,TSMDZTMC,TSMDZTBM,FXJGMC,FXJGJRJGBM,XKZH,FXJGZFHH,FXJGTYSHDM,FXJGDQDM,GLFS,STZZ,YXFS,SYBZBS,BJBZBS,YJZGSYL,YJZDSYL,RGRQ,DQRQ,JYSJ,JYRQ,JYFX,LSH,JYBZ,JYJE,JYJEZRMB,JYZHH,JYZHHMC,JYZHKHH,JYZHKHHMC,JYDSZHH,JYDSZHHMC,JYDSZHKHH,JYDSZHKHHMC,CODE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			StringBuffer sql = new StringBuffer().append("INSERT into ASSET_MAN_PRODUCT_ZSTL ");
			//删除
			StringBuffer delSql = new StringBuffer().append("delete from ASSET_MAN_PRODUCT_ZSTL t where ");
			List<Object> list = new ArrayList<Object>();
			List<Object> list1 = new ArrayList<Object>();
			if(paramList.size() > 0){
				//删除
				JDBCUtils.addStatement(paramList,list, delSql);		
				ps1 = con.prepareStatement(delSql.toString());
				JDBCUtils.pstSetObject(ps1, list);
				ps1.execute();
				//插入
				JDBCUtils.insertStatement(excelList,list1, sql);	
				ps = con.prepareStatement(sql.toString());
				JDBCUtils.psSetObject(ps, list1,zstl,con,excelList);
				zstl = ps.executeBatch();
				con.commit();
			}else{
				return zstl;
			}
		} catch (Exception e) {
			System.out.println(e+"在Up..Impl的114行");
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return zstl;
		}
		return zstl;
	}*/

}
