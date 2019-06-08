package com.mininglamp.currencySys.test;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.mq.jmqi.JmqiException;
import com.mininglamp.currencySys.announcement.service.AnnouncementService;
import com.mininglamp.currencySys.channel.AsynResponseOnMessage;
import com.mininglamp.currencySys.common.util.CommonUtil;
import com.mininglamp.currencySys.server.BizOnMessage;
import spc.webos.endpoint.ESB2;
import spc.webos.endpoint.Executable;
public class TestTwo {
	@Autowired
	private AnnouncementService announcementService;
	
	public static void main(String[] args) throws Exception {
		//ESBHBJY.shareQ("20190218", "20190218154547", "15981943396", "admin", "1600199998");
		/*ESB2 esb2 = ESB2.getInstance();
		esb2.setAsynResponseOnMessage(new AsynResponseOnMessage()); 
		esb2.setRequestOnMessage(new BizOnMessage()); 
		shareQ(esb2,"20190218","20190218155123","15981943396","admin","1600199998"); */
		HttpServletRequest request=null;
		CommonUtil.sendMobileMessage("17337730844","你好","",request,"1600299999");
	}
	
	static void shareQ(ESB2 esb2,String chanDate,String chanSeqNo,String phone,String tlrNo,String txBranch) throws Exception {
		esb2.init("esb.properties");		
			String seqNb = esb2.getSeqNb();  
			String dt = "20130909"; 
			Executable exe = new Executable();  
			exe.request=("<transaction> "
				   	  + "<header> <msg> <sndAppCd>"
					+ "HBJY</sndAppCd> <callTyp>SYN</callTyp> <msgCd>"
					+ "SMS.000010020.01"
					+ "</msgCd><seqNb>"
					+ seqNb
					+ "</seqNb><sndTm>070001</sndTm><sndDt>"
					+ "20170524</sndDt><rcvAppCd>SMS</rcvAppCd></msg>"
					  + "</header>"
					    + "<body>"
					+ "<request><smsBizCd>TYZZY001</smsBizCd><chanDate>"
					+ "20190218"   //系统请求时间
					+ "</chanDate><chanSeqNo>"
					+ seqNb   //流水账号
					+ "</chanSeqNo><chanNo>95</chanNo><telNum>"
					+ "18951228335"   //手机号
					+ "</telNum><smsTemplatFlg>0</smsTemplatFlg><tlrNo>"
					+ "00210074"   //柜员号
					+ "</tlrNo><smsCntt>"
					+ "货币金银采集系统中有您一条未完成任务，请前往处理！"
					+ "</smsCntt><txBranch>"
					+ "1600299999"  //发卡机构号
					+ "</txBranch></request>"
					    + "</body>"
					+ "</transaction>")
					.getBytes();
			exe.timeout = 60;
			exe.correlationID = ("20170524" + "-" + seqNb).getBytes();
			System.out.println(seqNb);
			System.out.println("request :" +"::"
					+ new String(exe.request));
			try {
				esb2.execute(exe);
				System.out.println("response :" +  "::"
						+ new String(exe.response));
			} catch(JmqiException mqe){
				esb2.destory();
				System.out.println("catch JmqiException and reload");
				esb2.init("esb_shareQ.properties");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(2000); 
		}
	

}
