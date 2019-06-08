package com.mininglamp.currencySys.viewController.controller;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mininglamp.currencySys.announcement.bean.AnnouncementBean;
import com.mininglamp.currencySys.announcement.service.AnnouncementService;
import com.mininglamp.currencySys.regulationFile.bean.RegulationBean;
import com.mininglamp.currencySys.regulationFile.service.RegulationService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/viewController")
public class ViewController {
	private static final Logger logger = LoggerFactory.getLogger(ViewController.class);
	@Autowired
	AnnouncementService announcementService;
	@Autowired
	RegulationService regulationService;
	
	protected HttpSession session;
	/**
	 * 用户信息管理：请求jsp页面
	 * @return jsp页面
	 */
	@RequestMapping("qxglIndex")
	public String userManageIndex(){
		return "userManage/userManageInfo";
	}
	
	// 现金从业人员信息
	@RequestMapping("prsnIndex")
	public String prsn() {
		return "wildCatReport/zxsj/prsn";
	}
	
	// 现金外包服务企业信息表
	@RequestMapping("xjwbIndex")
	public String xjwb() {
		return "wildCatReport/zxsj/xjwb";
	}

	// 跨行贷款物流信息
	@RequestMapping("khdkIndex")
	public String khdk() {
		return "wildCatReport/zxsj/khdk";
	}

	//数据校验
	@RequestMapping("storageAfterCheckIndex")
	public String storageAfterCheck() {
	    return "dataReport/storageAfterCheck";
	}
		
	//2-金融机构基本情况表
	@RequestMapping("finfIndex")
	public String finf() {
		return "sgsbCode/zxsj/finf";
	}

	// 商业银行日常业务表
	@RequestMapping("rcywIndex")
	public String rcyw() {
		return "wildCatReport/zxsj/rcyw";
	}
	
	// 冠字号文件信息表
	@RequestMapping("gzhmIndex")
	public String gzhm() {
		return "wildCatReport/zxsj/gzhm";
	}
	
	// 金融机构网点现金收支每日情况表
	@RequestMapping("xjszIndex")
	public String xjsz() {
		return "wildCatReport/zxsj/xjsz";
	}
	
	// 金融机构现金流水明细表
	@RequestMapping("xjlsIndex")
	public String xjls() {
		return "wildCatReport/zxsj/xjls";
	}
	
	// 金融机构业务库存情况表
	@RequestMapping("kcqkIndex")
	public String kcqk() {
		return "sgsbCode/zxsj/kcqk";
	}
	
	// 机构设备信息表
	@RequestMapping("jqsbIndex")
	public String jqsb() {
		return "sgsbCode/zxsj/jqsb";
	}
	
	// 生成报文
	@RequestMapping("gmvIndex")
	public String generateMessage() {
		return "generateMessage/generateMessageView";
	}
	
	// 报送统计
	@RequestMapping("bsglIndex")
	public String reportManage() {
		return "reportManage/reportManage";
	}
		
	//2-金融机构基本情况表
	@RequestMapping("finfwhIndex")
	public String finfwh(HttpServletRequest request) {
		Map params = new HashMap();
		for (Object key : request.getParameterMap().keySet()) {
			params.put(key, request.getParameterMap().get(key));
			if (params.get(key).getClass().isArray()) {
				// 字符串型数组
				if (Array.getLength(params.get(key)) > 0 && Array.get(params.get(key), 0) instanceof String) {
					params.put(key, Array.get(params.get(key), 0));
				}
			}
		}
		request.setAttribute("params", JSONObject.fromObject(params).toString());
		return "sgsbCode/finfwh/finf";
	}
	
	//企事业单位现金库存及需求情况统计表
	@RequestMapping("xqqkIndex")
	public String xqqk() {
		return "zhhb/xqqk";
	}
	
	//金融机构库存及现金收支情况统计表
	@RequestMapping("kcxjIndex")
	public String kcxj() {
		return "zhhb/kcxj";
	}	
	
	//中小面额人民币投放、回笼情况统计表
	@RequestMapping("zxthIndex")
	public String zxth() {
		return "zhhb/zxth";
	}	
	
	//银行业金融机构现金库存情况统计表
	@RequestMapping("xjkcIndex")
	public String xjkc() {
		return "zhhb/xjkc";
	}
	
	//银行业金融机构现金清分情况统计表
	@RequestMapping("xjqfIndex")
	public String xjqf() {
		return "zhhb/xjqf";
	}	
	
	//银行业金融机构现金收支情况统计表
	@RequestMapping("szqkIndex")
	public String szqk() {
		return "zhhb/szqk";
	}	
	
	//郑州市银行业金融机构空钞箱统计表
	@RequestMapping("kcxbIndex")
	public String kcxb() {
		return "zhhb/kcxb";
	}	
	
	//郑州市银行业金融机构出入库业务登记表
	@RequestMapping("crkbIndex")
	public String crkb() {
		return "zhhb/crkb";
	}	
	
	//企事业单位统计表
	@RequestMapping("sydwIndex")
	public String sydw() {
		return "ljhb/sydw";
	}	

	//网点_自助机具_清分设备表
	@RequestMapping("wsslIndex")
	public String wssl() {
		return "ljhb/wssl";
	}	

	//金融机构现金需求满意度统计表
	@RequestMapping("xqmyIndex")
	public String xqmy() {
		return "ljhb/xqmy";
	}	

	//金融机构现金收支流向情况统计表
	@RequestMapping("szlxIndex")
	public String szlx() {
		return "ljhb/szlx";
	}	

	//流通人民币整洁度状况调查表
	@RequestMapping("bzjdIndex")
	public String bzjd() {
		return "ljhb/bzjd";
	}	

	//报告期内金融机构网点统计表
	@RequestMapping("wdslIndex")
	public String wdsl() {
		return "ljhb/wdsl";
	}	

	//金融机构现金清分能力统计表
	@RequestMapping("qfnlIndex")
	public String qfnl() {
		return "ljhb/qfnl";
	}	

	//月末商业银行现金库存情况统计表
	@RequestMapping("ymkcIndex")
	public String ymkc() {
		return "ljhb/ymkc";
	}
	
	//硬币自循环情况-硬币自循环数量
	@RequestMapping("ybzxIndex")
	public String ybzx() {
		return "Syyh/ybzx";
	}
	//银行业金融机构人民币收付业务开展情况统计表
	@RequestMapping("rbsfIndex")
	public String rbsf() {
		return "Syyh/rbsf";
	}
	//银行业金融机构在用机具设备情况调查表
	@RequestMapping("jjsbIndex")
	public String jjsb() {
		return "Syyh/jjsb";
	}
	//银行业金融机构现金处理情况调查表
	@RequestMapping("xjclIndex")
	public String xjcl() {
		return "Syyh/xjcl";
	}
	//场地条件及安全防范设施状况调查表
	@RequestMapping("ssztIndex")
	public String sszt() {
		return "Syyh/sszt";
	}
	//现金服务企业基本信息表
	@RequestMapping("xjfwIndex")
	public String xjfw() {
		return "Syyh/xjfw";
	}
	//中国人民银行郑州中心支行汇总银行监测点信息表
	@RequestMapping("jcdbIndex")
	public String jcbd() {
		return "Syyh/jcdb";
	}
	
	//硬币自循环情况-硬币自助设备配备数量
	@RequestMapping("pbslIndex")
	public String pbsl() {
		return "Syyh/pbsl";
	}
	
	//入库、转换
	@RequestMapping("conversionIndex")
	public String generateMessageView(HttpServletRequest request) {
		Map params = new HashMap();
		for (Object key : request.getParameterMap().keySet()) {
			params.put(key, request.getParameterMap().get(key));
			if (params.get(key).getClass().isArray()) {
				// 字符串型数组
				if (Array.getLength(params.get(key)) > 0 && Array.get(params.get(key), 0) instanceof String) {
					params.put(key, Array.get(params.get(key), 0));
				}
			}
		}
		request.setAttribute("params", JSONObject.fromObject(params).toString());
		return "conversion/conversion";
	}
	//首页页面视图跳转
	@RequestMapping("bankhome")
	public String bankhome(Model model, AnnouncementBean announcementBean, HttpServletRequest request,RegulationBean regulationBean,
			HttpServletResponse response){
		List<AnnouncementBean> announcementList = announcementService.getDataList("getAnnounFirstList", announcementBean);
		List<RegulationBean> regulationList = regulationService.getDataList("getReguFirstList", regulationBean);
		model.addAttribute("listann", announcementList);
		model.addAttribute("regulationList", regulationList);
		return "userManage/bankHome";
	}
	/*@RequestMapping("fgwjIndex")
	public String fgwj(Model model, RegulationBean regulationBean, HttpServletRequest request,
			HttpServletResponse response){
		List<RegulationBean> regulationList = regulationService.getDataList("getReguList", regulationBean);
		model.addAttribute("listann", regulationList);
		return "regulation/regulation";
	}*/
	//硬币自循环情况-硬币自助设备配备数量
	@RequestMapping("jrkcIndex")
	public String jrkc() {
		return "jrjg/jrkc";
	}
	//银行业金融机构现金库存券别结构统计表
	@RequestMapping("jrjbIndex")
	public String jrjb() {
		return "jrjg/jrjb";
	}
	//银行业金融机构现金收支情况统计表
	@RequestMapping("jrszIndex")
	public String jrsz() {
		return "jrjg/jrsz";
	}
	//银行业金融机构对公现金业务统计表（按行业类别)
	@RequestMapping("jrywIndex")
	public String jryw() {
		return "jrjg/jryw";
	}
	//银行业金融机构对公现金业务统计表（按存款人类别）
	@RequestMapping("jrytIndex")
	public String jryt() {
		return "jrjg/jryt";
	}
	//银行业金融机构对私现金业务统计表
	@RequestMapping("jrdsIndex")
	public String jrds() {
		return "jrjg/jrds";
	}
	//银行业金融机构对客户现金收支统计表
	@RequestMapping("jrtjIndex")
	public String jrtj() {
		return "jrjg/jrtj";
	}
	//公告管理
	@RequestMapping("ggglIndex")
	public String announcementIndex(Model model, AnnouncementBean announcementBean, HttpServletRequest request,
	HttpServletResponse response, @RequestParam(defaultValue = "1") int pageNum) {
	Page<?> page = PageHelper.startPage(pageNum, 10);
	List<AnnouncementBean> announcementList = announcementService.getDataList("getAnnounList", announcementBean);
	PageInfo<AnnouncementBean> pageInfo=new PageInfo<>(announcementList);
	model.addAttribute("announcementList", pageInfo);
	model.addAttribute("announcementBean", announcementBean);
	return "announcement/announcement";
	}
	//法规文件
	@RequestMapping("fgwjIndex")
	public String fgwjIndex(Model model, RegulationBean regulationBean, HttpServletRequest request,
	HttpServletResponse response, @RequestParam(defaultValue = "1") int pageNum) {
	@SuppressWarnings("unused")
	Page<?> page = PageHelper.startPage(pageNum, 10);
	List<RegulationBean> regulationList = regulationService.getDataList("getReguList", regulationBean);
	PageInfo<RegulationBean> pageInfo=new PageInfo<>(regulationList);
	String nbjgh=(String) request.getSession().getAttribute("nbjgh");
	
	/*List<String> list =new ArrayList();	
	for (Map map2 : listMap) {
		String a=(String) map2.get("id");
		list.add(a);
	}
	boolean b=list.contains(nbjgh);
	if (b) {
		request.getSession().setAttribute("snsh","cz");
	}*/
	model.addAttribute("regulationList", pageInfo);
	model.addAttribute("regulationBean", regulationBean);
	
	return "regulation/regulation";
	}
	
	// 自定义选择
	@RequestMapping("customReportIndex")
	public String customReport() {
		return "customReport/customReport";
	}
	
	// 自定义编辑
	@RequestMapping("customReportAdditionalIndex")
	public String customReportAdditional() {
		return "customReportAdditional/customReportAdditional";
	}
	
	// 自定义汇总
	@RequestMapping("customReportSummaryIndex")
	public String customReportSummary() {
		return "customReportSummary/customReportSummary";
	}
}
