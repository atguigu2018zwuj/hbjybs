var dateUtil = new DateUtil();
var httpUtil = new HttpUtil();
var cookieUtil = new CookieUtil();
define(function(require) {
	//金融机构基本情况信息表
	require('My97DatePicker');
	require('JsExcelXml');
	require('ImportUtils');
	require('layer'); 
	require('insertData'); 
	require('timeFormat');
	layer.config({
		path : '../static/common/layer/' // layer.js所在的目录
	});
	
	// 处理仅admin显示的空间（非admin用户登录则删除该控件），防止没有拦截住的显示
	var userInfo = cookieUtil.getUserInfoFromCookie();
	if (userInfo.YH_NAME != "admin") {
		$(".only-admin").remove();
	}
	
	// 暂不初始化只显示当天的
//	$(function() {
//		var searchTime = null;
//		httpUtil.ajax("post","/code/getCodeConfigInfo",{codeKey:$("#fileName").val()},function(result){
//			var markingcode = "1";
//			if (result != null && result.data != null && result.data.length > 0 
//					&& result.data[0].MARKINGCODE != null && result.data[0].MARKINGCODE.length > 0) {
//				markingcode = result.data[0].MARKINGCODE;
//			}
//			// 1:日报，2:月报，3:季报，4:半年报，5:年报
//			console.log("markingcode="+markingcode);
//			var currentDate = new Date();
//			if (markingcode == '1') {
//				// 当天
//				searchTime = currentDate;
//			} else if (markingcode == '2') {
//				// 上个月末
//				searchTime = dateUtil.getLastDayInMonth(dateUtil.subtractDate(currentDate,"month",1));
//			} else if (markingcode == '3') {
//				// 上个季度末
//				searchTime = dateUtil.getLastDayInQuarter(dateUtil.subtractDate(currentDate,"month",3));
//			} else if (markingcode == '4') {
//				// 上个半年末
//				searchTime = dateUtil.getLastDayInHalfYear(dateUtil.subtractDate(currentDate,"month",6));
//			} else if (markingcode == '5') {
//				// 上年末
//				searchTime = dateUtil.getLastDayInYear(dateUtil.subtractDate(currentDate,"year",1));
//			}
//		},false);
//		initSjrq(searchTime);
//	});
});

// 根据指定数据日期进行初始化查询
function initSjrq(date) {
	if (date == null) {
		console.warn("根据指定数据日期初始化查询失败，原因：日期为空");
		return;
	}
	var timeStr = dateUtil.toStrFromDate(date,"yyyy-MM-dd",false);
	var $sjrqInput = $("#beginSjrq")==null || $("#beginSjrq").size()==0 ? $("#SJRQ") : $("#beginSjrq");
	var $searchBtn = $("#doSearch");
	$sjrqInput.val(timeStr);
	$searchBtn.click();
}

/**
 * 判断数据日期是否正确
 * @param isAlertErrorMsg 是否弹框提示错误信息（默认不提示）
 * @param sjrq 数据日期（格式：yyyy-mm-dd）
 * @param tableCodeKey 报表的四位标识字母
 * @returns 若满足以下条件，则返回true，否则返回false：月报为月末，季报为季末，半年报为半年末，年报为年末
 */
function validateSjrq(isAlertErrorMsg,sjrq,tableCodeKey) {
	// sjrq默认取SJRQ input的值
	sjrq = (sjrq == undefined || sjrq == "") ? $("input[name='SJRQ'][type='hidden']").prev().val() : sjrq;
	if (sjrq == null) return false;// 时间字符串格式错误
	// tableCodeKey默认取fileName input的值
	tableCodeKey = (tableCodeKey == undefined || tableCodeKey == "") ? $("#fileName").val() : tableCodeKey;
	var markingcode = "1";
	
	httpUtil.ajax("post","/code/getCodeConfigInfo",{codeKey:tableCodeKey},function(result){
		if (result != null && result.data != null && result.data.length > 0 
				&& result.data[0].MARKINGCODE != null && result.data[0].MARKINGCODE.length > 0) {
			markingcode = result.data[0].MARKINGCODE;
		}
	},false);
	// 1:日报，2:月报，3:季报，4:半年报，5:年报
//	console.log("markingcode="+markingcode);
	if (markingcode == '2') {
		// 月末
		if (dateUtil.getLastDayInMonth(dateUtil.toDateFromStr(sjrq,true)).getTime() != dateUtil.toDateFromStr(sjrq,true).getTime()) {
			if (isAlertErrorMsg) $.messager.alert('提示','当前报表是月报，数据日期应填写每月最后一天！','error'); 
			return false;
		}
	} else if (markingcode == '3') {
		// 季度末
		if (dateUtil.getLastDayInQuarter(dateUtil.toDateFromStr(sjrq,true)).getTime() != dateUtil.toDateFromStr(sjrq,true).getTime()) {
			if (isAlertErrorMsg) $.messager.alert('提示','当前报表是季报，数据日期应填写每个季度最后一天！','error');
			return false;
		}
	} else if (markingcode == '4') {
		// 半年末
		if (dateUtil.getLastDayInHalfYear(dateUtil.toDateFromStr(sjrq,true)).getTime() != dateUtil.toDateFromStr(sjrq,true).getTime()) {
			if (isAlertErrorMsg) $.messager.alert('提示','当前报表是半年报，数据日期应填写每个半年最后一天！','error');
			return false;
		}
	} else if (markingcode == '5') {
		// 年末
		if (dateUtil.getLastDayInYear(dateUtil.toDateFromStr(sjrq,true)).getTime() != dateUtil.toDateFromStr(sjrq,true).getTime()) {
			if (isAlertErrorMsg) $.messager.alert('提示','当前报表是年报，数据日期应填写每年最后一天！','error');
			return false;
		}
	}
	return true;
}

//------------- formatter START ----------
//营业状态
/*function YYZTFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "营业";
				break;
			case "2":
				value = "停业";
				break;
			case "3":
				value = "被合并";
				break;
		}
	} else {
		value = "";
	}
	return value;
}*/
//营业状态
function code_yeztFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "营业";
				break;
			case "2":
				value = "停业";
				break;
			case "3":
				value = "被合并";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//岗位
/*function GWFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "管理岗";
				break;
			case "1":
				value = "柜员";
				break;
			case "2":
				value = "清分人员";
				break;
		}
	} else {
		value = "";
	}
	return value;
}*/
function code_gwFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "管理岗";
				break;
			case "1":
				value = "柜员";
				break;
			case "2":
				value = "清分人员";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//现金人行业人员当前状态
/*function DQZTFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "正常从事该工作";
				break;
			case "2":
				value = "在本行内不再从事该工作";
				break;
			case "3":
				value = "离职";
				break;
		}
	} else {
		value = "";
	}
	return value;
}*/
function code_ryztFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "正常从事该工作";
				break;
			case "2":
				value = "在本行内不再从事该工作";
				break;
			case "3":
				value = "离职";
				break;
		}
	} else {
		value = "";
	}
	return value;
}

//是否取得反假币培训合格证书
/*function SFQDHGZSFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "否";
				break;
			case "1":
				value = "是";
				break;
		}
	} else {
		value = "";
	}
	return value;
}*/
function code_sfFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "否";
				break;
			case "1":
				value = "是";
				break;
		}
	} else {
		value = "";
	}
	return value;
}

function code_sexFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "女";
				break;
			case "1":
				value = "男";
				break;
		}
	} else {
		value = "";
	}
	return value;
}

//金融机构级别
/*function JGJBFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "总行";
				break;
			case "2":
				value = "分行";
				break;
			case "3":
				value = "支行";
				break;
			case "4":
				value = "网点";
				break;
			case "5":
				value = "事业部";
				break;
		}
	} else {
		value = "";
	}
	return value;
}*/
function code_jgjbFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "总行";
				break;
			case "2":
				value = "分行";
				break;
			case "3":
				value = "支行";
				break;
			case "4":
				value = "网点";
				break;
			case "5":
				value = "事业部";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//金融机构类别
/*function JGLBFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "B0311":
				value = "中资大型银行";
				break;
			case "B0312":
				value = "中资中型银行";
				break;
			case "B0313":
				value = "中资小型银行";
				break;
			case "B03131":
				value = "小型城市商业银行";
				break;
			case "B03132":
				value = "农村商业银行";
				break;
			case "B03133":
				value = "农村合作银行";
				break;
			case "B03134":
				value = "村镇银行";
				break;
			case "B032":
				value = "城市信用合作社";
				break;
			case "B033":
				value = "农村信用合作社";
				break;
			case "B034":
				value = "农村资金互助社";
				break;
			case "B035":
				value = "财务公司";
				break;
			case "B036":
				value = "外资银行";
				break;
			case "B041":
				value = "信托公司";
				break;
			case "B042":
				value = "金融资产管理公司";
				break;
			case "B043":
				value = "金融租赁公司";
				break;
			case "B044":
				value = "汽车金融公司";
				break;
			case "B045":
				value = "贷款公司";
				break;
			case "B046":
				value = "货币经纪公司";
				break;
			case "B05":
				value = "证券业金融机构";
				break;
			case "B06":
				value = "保险业金融机构";
				break;
			case "B07":
				value = "交易及结算类金融机构";
				break;
			case "B08":
				value = "金融控股公司";
				break;
			case "B09":
				value = "特定目的载体";
				break;
			case "B10":
				value = "其他金融机构";
				break;
			case "E012":
				value = "国外金融机构";
				break;
		}
	} else {
		value = "";
	}
	return value;
}*/
function code_jglbFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "B0311":
				value = "中资大型银行";
				break;
			case "B0312":
				value = "中资中型银行";
				break;
			case "B0313":
				value = "中资小型银行";
				break;
			case "B03131":
				value = "小型城市商业银行";
				break;
			case "B03132":
				value = "农村商业银行";
				break;
			case "B03133":
				value = "农村合作银行";
				break;
			case "B03134":
				value = "村镇银行";
				break;
			case "B032":
				value = "城市信用合作社";
				break;
			case "B033":
				value = "农村信用合作社";
				break;
			case "B034":
				value = "农村资金互助社";
				break;
			case "B035":
				value = "财务公司";
				break;
			case "B036":
				value = "外资银行";
				break;
			case "B041":
				value = "信托公司";
				break;
			case "B042":
				value = "金融资产管理公司";
				break;
			case "B043":
				value = "金融租赁公司";
				break;
			case "B044":
				value = "汽车金融公司";
				break;
			case "B045":
				value = "贷款公司";
				break;
			case "B046":
				value = "货币经纪公司";
				break;
			case "B05":
				value = "证券业金融机构";
				break;
			case "B06":
				value = "保险业金融机构";
				break;
			case "B07":
				value = "交易及结算类金融机构";
				break;
			case "B08":
				value = "金融控股公司";
				break;
			case "B09":
				value = "特定目的载体";
				break;
			case "B10":
				value = "其他金融机构";
				break;
			case "E012":
				value = "国外金融机构";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
/*跨行调款物流信息表*/
//币种标识 code_bz
function code_bzFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "AUD":
				value = "澳大利亚元";
				break;
			case "BWB":
				value = "本外币合计";
				break;
			case "CAD":
				value = "加拿大元";
				break;
			case "CHF":
				value = "瑞士法郎";
				break;
			case "CNY":
				value = "人民币";
				break;
			case "DEM":
				value = "马克";
				break;
			case "DKK":
				value = "丹麦克朗";
				break;
			case "EUR":
				value = "欧元";
				break;
			case "FRF":
				value = "法国法郎";
				break;
			case "GBP":
				value = "英镑";
				break;
			case "HKD":
				value = "港币";
				break;
			case "ITL":
				value = "意大利里拉";
				break;
			case "JPY":
				value = "日元";
				break;
			case "KRW":
				value = "韩国元";
				break;
			case "MOP":
				value = "澳门元";
				break;
			case "NLG":
				value = "荷兰盾";
				break;
			case "NOK":
				value = "挪威克朗";
				break;
			case "NZD":
				value = "新西兰元";
				break;
			case "PHP":
				value = "菲律宾比索";
				break;
			case "SEK":
				value = "瑞典克朗";
				break;
			case "SGD":
				value = "新加坡元";
				break;
			case "SUR":
				value = "卢布";
				break;
			case "THB":
				value = "泰铢";
				break;
			case "TWD":
				value = "新台币";
				break;
			case "USD":
				value = "美元";
				break;
				
		}
	} else {
		value = "";
	}
	return value;
}
/*（rcyw）商业银行日常业务表*/
//业务类型 code_ywlx
function code_ywlxFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "未定义";
				break;
			case "1":
				value = "现金收入";
				break;
			case "2":
				value = "现金付出";
				break;
			case "3":
				value = "清分业务";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//设备类型 code_jjzl
function code_jjzlFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "未定义";
				break;
			case "1":
				value = "清分机具";
				break;
			case "11":
				value = "纸币清分机";
				break;
			case "12":
				value = "硬币清分机";
				break;
			case "13":
				value = "硬币清分处理机";
				break;
			case "2":
				value = "存取款一体机";
				break;
			case "51":
				value = "纸硬币兑换机";
				break;
			case "21":
				value = "纸币存取款一体机";
				break;
			case "22":
				value = "硬币存取款一体机";
				break;
			case "3":
				value = "点钞机";
				break;
			case "4":
				value = "取款机";
				break;
			case "5":
				value = "兑换机具";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（gzhm）冠字号文件信息表*/
//钞票状态 code_cpzt
function code_cpztFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "真币";
				break;
			case "2":
				value = "假币";
				break;
			case "3":
				value = "残币";
				break;
			case "4":
				value = "旧币";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//钞票类型 code_cplx
function code_cplxFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "未定义";
				break;
			case "1":
				value = "自动柜员配钞券";
				break;
			case "2":
				value = "一般完整券";
				break;
			case "3":
				value = "可疑券";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//冠字号币值 code_qb
function code_qbFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "R01":
				value = "100元";
				break;
			case "R02":
				value = "50元";
				break;
			case "R03":
				value = "20元";
				break;
			case "R04":
				value = "10元";
				break;
			case "R05":
				value = "5元";
				break;
			case "R06":
				value = "1元";
				break;
			case "R07":
				value = "5角";
				break;
			case "R08":
				value = "1角";
				break;
			case "R09":
				value = "2元";
				break;
			case "R10":
				value = "2角";
				break;
			case "R11":
				value = "分币";
				break;
			case "ALL":
				value = "小计";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//冠字号币种 code_bz
function code_bzFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "AUD":
				value = "澳大利亚元";
				break;
			case "BWB":
				value = "本外币合计";
				break;
			case "CAD":
				value = "加拿大元";
				break;
			case "CHF":
				value = "瑞士法郎";
				break;
			case "CNY":
				value = "人民币";
				break;
			case "DEM":
				value = "马克";
				break;
			case "DKK":
				value = "丹麦克朗";
				break;
			case "EUR":
				value = "欧元";
				break;
			case "FRF":
				value = "法国法郎";
				break;
			case "GBP":
				value = "英镑";
				break;
			case "HKD":
				value = "港币";
				break;
			case "ITL":
				value = "意大利里拉";
				break;
			case "JPY":
				value = "日元";
				break;
			case "KRW":
				value = "韩国元";
				break;
			case "MOP":
				value = "澳门元";
				break;
			case "NLG":
				value = "荷兰盾";
				break;
			case "NOK":
				value = "挪威克朗";
				break;
			case "NZD":
				value = "新西兰元";
				break;
			case "PHP":
				value = "菲律宾比索";
				break;
			case "SEK":
				value = "瑞典克朗";
				break;
			case "SGD":
				value = "新加坡元";
				break;
			case "SUR":
				value = "卢布";
				break;
			case "THB":
				value = "泰铢";
				break;
			case "TWD":
				value = "新台币";
				break;
			case "USD":
				value = "美元";
				break;
				
		}
	} else {
		value = "";
	}
	return value;
}
//冠字号版别 code_cpbb
function code_cpbbFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "B05":
				value = "1990版";
				break;
			case "B06":
				value = "1980版";
				break;
			case "B01":
				value = "1996版";
				break;
			case "B02":
				value = "1999版";
				break;
			case "B03":
				value = "2005版";
				break;
			case "B04":
				value = "2015版";
				break;
			case "B99":
				value = "外币";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（kcqk）金融机构业务库存信息表*/
//券別复用 code_qb
//材质  code_qbcz
function code_qbczFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "硬币";
				break;
			case "1":
				value = "纸币";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（xjls）金融机构现金流水明细表*/
//券別 复用代码
//材质 复用代码
/*（jqsb）机构设备信息表*/
//点钞机的类型  code_dcjjjlx
function code_dcjjjlxFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "A类";
				break;
			case "1":
				value = "B类";
				break;
			case "2":
				value = "C类";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//设备类型 code_jjzl
function code_jjzlFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "未定义";
				break;
			case "1":
				value = "清分机具";
				break;
			case "11":
				value = "纸币清分机";
				break;
			case "12":
				value = "硬币清分机";
				break;
			case "13":
				value = "硬币清分处理机";
				break;
			case "2":
				value = "存取款一体机";
				break;
			case "51":
				value = "纸硬币兑换机";
				break;
			case "21":
				value = "纸币存取款一体机";
				break;
			case "22":
				value = "硬币存取款一体机";
				break;
			case "3":
				value = "点钞机";
				break;
			case "4":
				value = "取款机";
				break;
			case "5":
				value = "兑换机具";
				break;
		}
	} else {
		value = "";
	}
	return value;
}

//设备状态 code_sbzt
function code_sbztFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "在用";
				break;
			case "1":
				value = "停用";
				break;
			case "2":
				value = "维修";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//现金机具分类
function code_xjjjflFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "C0":
				value = "硬币鉴别机具";
				break;
			case "C01":
				value = "单枚硬币鉴别装置";
				break;
			case "C011":
				value = "硬币自动鉴别仪";
				break;
			case "C012":
				value = "集成单枚鉴别模块的机具";
				break;
			case "C02":
				value = "批量硬币鉴别机具";
				break;
			case "C021":
				value = "非自助硬币鉴别机具";
				break;
			case "C0211":
				value = "金融机构用硬币鉴别机具";
				break;
			case "C0212":
				value = "非金融机构用硬币鉴别机具";
				break;
			case "C022":
				value = "自助硬币鉴别机具";
				break;
			case "C0221":
				value = "具有硬币自循环功能的自助硬币鉴别机具";
				break;
			case "C0222":
				value = "无硬币自循环功能的自助硬币鉴别机具";
				break;
			case "C03":
				value = "其他硬币鉴别机具";
				break;
			case "C1":
				value = "纸币鉴别机具";
				break;
			case "C11":
				value = "单张纸币鉴别装置";
				break;
			case "C111":
				value = "纸币自动鉴别仪";
				break;
			case "C112":
				value = "集成单张鉴别模块的机具";
				break;
			case "C12":
				value = "批量纸币鉴别机具";
				break;
			case "C121":
				value = "非自助纸币鉴别机具";
				break;
			case "C1211":
				value = "无拒钞仓的鉴别机具";
				break;
			case "C12111":
				value = "金融机构用无拒钞仓的鉴别机具";
				break;
			case "C12112":
				value = "非金融机构用无拒钞仓的鉴别机具";
				break;
			case "C1212":
				value = "有拒钞仓的鉴别机具";
				break;
			case "C122":
				value = "自助纸币鉴别机具";
				break;
			case "C13":
				value = "其他纸币鉴别机具";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（jcdb）中国人民银行郑州中心支行汇总银行监测点信息表*/
//面额 复用
//版别 复用
//假币来源
function code_jblyFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "后台清分";
				break;
			case "2":
				value = "柜面收缴";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//上报类型
function code_sblxFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "省级";
				break;
			case "1":
				value = "地级市";
				break;
			case "2":
				value = "县级";
				break;
			case "3":
				value = "营业网点";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（ymkc）月末商业银行现金库存*/
//券別 复用
//上报类型 复用
/*（crkb）郑州市银行业金融机构出入库业务登记表*/
//代码复用
/*（xqmy）金融机构现金需求满意度统计表*/
//券别版本  上报类型 代码复用
//库存级别 code_dylx
function code_dylxFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
		case "0":
			value = "全辖";
			break;
		case "1":
			value = "全市";
			break;
		case "2":
			value = "全县";
			break;
		case "B02":
			value = "1999版";
			break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（jrkc）银行业金融机构现金库存券别统计表*/
//券別名称 代码复用
/*（jrjb）银行业金融机构现金库存券别结构统计表*/
//上报类型 复用代码
//单位名称
function code_jrjgFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
		case "A01":
			value = "国有商业银行";
			break;
		case "A011":
			value = "中国工商银行";
			break;
		case "A012":
			value = "中国农业银行";
			break;
		case "A013":
			value = "中国银行";
			break;
		case "A014":
			value = "中国建设银行";
			break;
		case "A02":
			value = "股份制商业银行";
			break;
		case "A021":
			value = "交通银行";
			break;
		case "A022":
			value = "中信银行";
			break;
		case "A023":
			value = "中国光大银行";
			break;
		case "A024":
			value = "中国民生银行";
			break;
		case "A025":
			value = "华夏银行";
			break;
		case "A026":
			value = "广发银行";
			break;
		case "A027":
			value = "招商银行";
			break;
		case "A028":
			value = "平安银行";
			break;
		case "A029":
			value = "浦发银行";
			break;
		case "A0210":
			value = "兴业银行";
			break;
		case "A0211":
			value = "恒丰银行";
			break;
		case "A0212":
			value = "浙商银行";
			break;
		case "A0213":
			value = "渤海银行";
			break;
		case "A03":
			value = "中国邮政储蓄银行";
			break;
		case "A04":
			value = "城市商业银行";
			break;
		case "A05":
			value = "农村商业银行";
			break;
		case "A06":
			value = "农村合作银行";
			break;
		case "A07":
			value = "农村信用社";
			break;
		case "A08":
			value = "外资银行";
			break;
		case "A09":
			value = "其他";
			break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（qfnl）金融机构现金清分能力统计表
（kcxb）郑州市银行业金融机构空钞箱统计表
（ybzx）硬币自循环情况-硬币自循环数量
（pbsl）硬币自循环情况-硬币自助设备配备数量
代码复用
*/
/*（szqk）银行业金融机构现金收支情况统计表*/
//项目名称
function code_xmmcFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
		case "A00":
			value = "对公收入";
			break;
		case "A01":
			value = "对私收入";
			break;
		case "A010":
			value = "柜面业务收入";
			break;
		case "A011":
			value = "自助设备收入";
			break;
		case "A02":
			value = "外部项目收入合计";
			break;
		case "A03":
			value = "同业间现金存入";
			break;
		case "A04":
			value = "现金收入总计";
			break;
		case "B00":
			value = "对公支出";
			break;
		case "B01":
			value = "对私支出";
			break;
		case "B011":
			value = "自助设备支出";
			break;
		case "B02":
			value = "外部项目支出合计";
			break;
		case "B03":
			value = "同业间现金支出";
			break;
		case "B04":
			value = "现金支出总计";
			break;
		case "C00":
			value = "净投放（+）或净回笼（-）";
			break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（zxth）中小面额人民币投放、回笼情况统计表*/
//单位
function code_thdwFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
		case "0":
			value = "公交公司";
			break;
		case "1":
			value = "火车站/汽车站";
			break;
		case "2":
			value = "百货商场";
			break;
		case "4":
			value = "超市";
			break;
		case "5":
			value = "农贸市场";
			break;
		case "6":
			value = "医院";
			break;
		case "7":
			value = "餐饮店";
			break;
		case "8":
			value = "个人";
			break;
		case "9":
			value = "其它";
			break;
		}
	} else {
		value = "";
	}
	return value;
}
/*（jryw）银行业金融机构对公现金业务统计表（按行业类别）*/
//行业名称
function code_hylbFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
		case "A07":
			value = "交通运输、仓储和邮政业";
			break;
		case "A15":
			value = "居民服务、修理和其他服务业 ";
			break;
		case "A20":
			value = "其他行业";
			break;
		case "A11":
			value = "房地产业";
			break;
		case "A17":
			value = "卫生和社会工作";
			break;
		case "A03":
			value = "制造业";
			break;
		case "A04":
			value = "电力、热力、燃气及水生产和供应业";
			break;
		case "A08":
			value = "住宿和餐饮业";
			break;
		case "A10":
			value = "金融业";
			break;
		case "A02":
			value = "采矿业";
			break;
		case "A18":
			value = "文化、体育和娱乐业";
			break;
		case "A19":
			value = "公共管理、社会保障和社会组织";
			break;
		case "A12":
			value = "租赁和商务服务业";
			break;
		case "A01":
			value = "农、林、牧、渔业";
			break;
		case "A05":
			value = "建筑业";
			break;
		case "A06":
			value = "批发和零售业";
			break;
		case "A09":
			value = "信息传输、软件和信息技术服务业";
			break;
		case "A13":
			value = "科学研究和技术服务业";
			break;
		case "A14":
			value = "水利、环境和公共设施管理业 ";
			break;
		case "A16":
			value = "教育 ";
			break;
		}
	} else {
		value = "";
	}
	return value;
}
//单位项目名称
function CODE_DSXMMCFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
		case "A011":
			value = "5万元以下";
			break;
		case "A012":
			value = "5万元(含)20万元";
			break;
		case "A03":
			value = "贵金属买卖业务";
			break;
		case "A041":
			value = "现金缴纳水电、煤气、电话等生活费用";
			break;
		case "A02":
			value = "外币兑换业务";
			break;
		case "A042":
			value = "现金缴纳罚款、税款等";
			break;
		case "A04":
			value = "其他业务";
			break;
		case "A014":
			value = "100万元(含)以上";
			break;
		case "A01":
			value = "储蓄业务";
			break;
		case "A013":
			value = "20万元(含)100万元";
			break;
		}
	} else {
		value = "";
	}
	return value;
}
//所在区域
function code_szqyFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
		case "1":
			value = "省会级以上城市";
			break;
		case "2":
			value = "副省级城市";
			break;
		case "3":
			value = "地市级城市";
			break;
		case "4":
			value = "县及县以下地区";
			break;
		}
	} else {
		value = "";
	}
	return value;
}
//样本分类
function code_ybflFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
		case "A01":
			value = "机关事业单位";
			break;
		case "A02":
			value = "工业企业";
			break;
		case "A03":
			value = "建筑业企业";
			break;
		case "A04":
			value = "交通运输及仓储业企业";
			break;
		case "A05":
			value = "邮电业企业";
			break;
		case "A06":
			value = "批发及零售企业";
			break;
		case "A07":
			value = "餐饮业企业";
			break;
		case "A08":
			value = "房地产企业";
			break;
		case "A09":
			value = "社会服务业";
			break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_fxfffsFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "0":
				value = "保险";
				break;
			case "1":
				value = "抵押金";
				break;
			case "2":
				value = "其他";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_cpzlFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "1":
				value = "纸币性";
				break;
			case "2":
				value = "硬币性";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_jbmeFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "R01":
				value = "100元";
				break;
			case "R02":
				value = "50元";
				break;
			case "R03":
				value = "20元";
				break;
			case "R04":
				value = "10元";
				break;
			case "R05":
				value = "5元";
				break;
			case "R061":
				value = "1元纸币";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_xmmc_jrytFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "A01":
				value = "企业";
				break;
			case "A02":
				value = "机关、事业单位";
				break;
			case "A03":
				value = "个体工商户";
				break;
			case "A031":
				value = "5万元以下";
				break;
			case "A032":
				value = "5万元（含）-20万元";
				break;
			case "A033":
				value = "20万元（含）-100万元";
				break;
			case "A034":
				value = "100万元（含）以上";
				break;
			case "A04":
				value = "其他";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_qb_kcxjFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "R01":
				value = "100元";
				break;
			case "R02":
				value = "50元";
				break;
			case "R03":
				value = "20元";
				break;
			case "R04":
				value = "10元";
				break;
			case "R05":
				value = "5元";
				break;
			case "R06":
				value = "1元";
				break;
			case "R07":
				value = "5角";
				break;
			case "R08":
				value = "1角";
				break;
			case "ALL":
				value = "小计";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_qb_szlxFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "R01":
				value = "100元";
				break;
			case "R02":
				value = "50元";
				break;
			case "R03":
				value = "20元";
				break;
			case "R04":
				value = "10元";
				break;
			case "R05":
				value = "5元";
				break;
			case "R061":
				value = "1元(纸)";
				break;
			case "R062":
				value = "1元(硬)";
				break;
			case "R071":
				value = "5角(纸)";
				break;
			case "R072":
				value = "5角(硬)";
				break;
			case "R081":
				value = "1角(纸)";
				break;
			case "R082":
				value = "1角(硬)";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_qb_ybzxFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "R062":
				value = "1元(硬)";
				break;
			case "R072":
				value = "5角(硬)";
				break;
			case "R082":
				value = "1角(硬)";
				break;
			case "ALL":
				value = "小计";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_qb_ymkcFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "R01":
				value = "100元";
				break;
			case "R02":
				value = "50元";
				break;
			case "R03":
				value = "20元";
				break;
			case "R04":
				value = "10元";
				break;
			case "R05":
				value = "5元";
				break;
			case "R061":
				value = "1元(纸)";
				break;
			case "R062":
				value = "1元(硬)";
				break;
			case "R072":
				value = "5角(硬)";
				break;
			case "R082":
				value = "1角(硬)";
				break;
			case "ALL":
				value = "小计";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
function code_dsxmmcFormatter(value, row, index) {
	if (value != undefined && value != "") {
		switch (value) {
			case "A01":
				value = "储蓄业务";
				break;
			case "A011":
				value = "5万元以下";
				break;
			case "A012":
				value = "5万元(含)20万元";
				break;
			case "A013":
				value = "20万元(含)100万元";
				break;
			case "A014":
				value = "100万元(含)以上";
				break;
			case "A02":
				value = "外币兑换业务";
				break;
			case "A03":
				value = "贵金属买卖业务";
				break;
			case "A04":
				value = "其他业务";
				break;
			case "A041":
				value = "现金缴纳水电、煤气、电话等生活费用";
				break;
			case "A042":
				value = "现金缴纳罚款、税款等";
				break;
		}
	} else {
		value = "";
	}
	return value;
}
//------------- formatter END ----------
