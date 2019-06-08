package com.mininglamp.currencySys.common.base.bean;

import java.io.Serializable;

import com.mininglamp.currencySys.common.base.CodeConst;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 统一响应数据结构
 * @author czy
 * 2016年9月19日16:26:43
 */
@ApiModel
public class ResponseBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 结果编码，参考{@link #CodeConst.class}
	 * 默认成功
	 */
	@ApiModelProperty(required=true,value="状态码")
	private String code = CodeConst.CODE_OK;
	/**
	 * 消息
	 */
	@ApiModelProperty(required=false,value="文本消息,用于解释状态码或其他说明信息")
	private String message = "成功";
	/**
	 * 结果
	 */
	@ApiModelProperty(required=false,value="内容主体")
	private Object data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
		this.message = CodeConst.getCodeMessage(code);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ResponseBean() {
	}
	public ResponseBean(Object data) {
		this.data = data;
	}
	public ResponseBean(String code,String message) {
		this.code = code;
		this.message = message;
	}
	/**
	 * 设置编码
	 * TODO 后续可以
	 * @param code
	 */
	public void code(String code){
		this.code = code;
		this.message = CodeConst.getCodeMessage(code);
	}
}
