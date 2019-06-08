package com.mininglamp.currencySys.common.base.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于分页查询
 * @author czy
 * 2016年9月19日20:17:20
 */
@SuppressWarnings("unchecked")
public class PageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 开始索引
	 */
	private Long indexStart;
	/**
	 * 一页条数
	 */
	private Integer pageSize;
	/**
	 * 总数
	 */
	private Long count;
	/**
	 * 格式转换编号 对应transcoder.xml
	 */
	private String transcodeId;
	
	/**
	 * 一页的数据
	 */
	private List list = new ArrayList();
	public Long getIndexStart() {
		return indexStart;
	}
	public void setIndexStart(Long indexStart) {
		this.indexStart = indexStart;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public String getTranscodeId() {
		return transcodeId;
	}
	public void setTranscodeId(String transcodeId) {
		this.transcodeId = transcodeId;
	}
}
