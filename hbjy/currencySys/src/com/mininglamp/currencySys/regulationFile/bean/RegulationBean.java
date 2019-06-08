package com.mininglamp.currencySys.regulationFile.bean;

import java.util.Date;

/**
 * 
 * <p>Description:法律文件表实体类 </p>
 * @author zrj
 * @date 2019年2月21日
 * @version 1.0
 */
public class RegulationBean {
	/**法律文件id*/
	private String id;
	/**法律文件标题*/
	private String regulationTitle;
	/**法律文件创建者*/
	private String creator ;
	/**法律文件所属机构*/
	private String organization;
	/**创建时间*/
	private Date createTime;
	/**更新时间*/
	private Date updateTime;
	/**开始时间*/
	private String beginDate;
	/**截止时间*/
	private String endDate;
	/**上传名字*/
	private String annexName;
	/**上传路径*/
	private String annexUrl;
	public String getRegulationTitle() {
		return regulationTitle;
	}
	public void setRegulationTitle(String regulationTitle) {
		this.regulationTitle = regulationTitle;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getAnnexName() {
		return annexName;
	}
	public void setAnnexName(String annexName) {
		this.annexName = annexName;
	}
	public String getAnnexUrl() {
		return annexUrl;
	}
	public void setAnnexUrl(String annexUrl) {
		this.annexUrl = annexUrl;
	}
	
	
	
	

}
