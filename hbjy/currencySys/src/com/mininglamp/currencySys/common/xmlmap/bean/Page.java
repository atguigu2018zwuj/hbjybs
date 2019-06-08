package com.mininglamp.currencySys.common.xmlmap.bean;

/**********************
 * 
 * @author 刘振华 分页专用
 * 
 */
public class Page {
	// 起始条数
	private int fromSize;
	// 返回条数
	private int returnSize;

	public int getFromSize() {
		return fromSize;
	}

	public void setFromSize(int fromSize) {
		this.fromSize = fromSize;
	}

	public int getReturnSize() {
		return returnSize;
	}

	public void setReturnSize(int returnSize) {
		this.returnSize = returnSize;
	}
}
