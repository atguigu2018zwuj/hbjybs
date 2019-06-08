package com.mininglamp.currencySys.channel;
import com.ibm.mq.jmqi.JmqiException;
import com.mininglamp.currencySys.server.BizOnMessage;
import spc.webos.endpoint.ESB2;
import spc.webos.endpoint.Executable;
public class ESBHBJY {
	public static void main(String[] args) throws Exception {
		ESB2 esb2 = ESB2.getInstance();
		esb2.setAsynResponseOnMessage(new AsynResponseOnMessage()); 
		esb2.setRequestOnMessage(new BizOnMessage()); 
		shareQ(esb2,"20190218","20190218155123","15981943396","admin","1600199998"); 
		esb2.destory();
	}
	
	 public static void shareQ(ESB2 esb2,String chanDate, String chanSeqNo, String phone, String tlrNo, String txBranch)
			throws Exception {
		/*ESB2 esb2 = ESB2.getInstance();
		esb2.setAsynResponseOnMessage(new AsynResponseOnMessage());
		esb2.setRequestOnMessage(new BizOnMessage());*/
		esb2.init("esb.properties");
		String seqNb = esb2.getSeqNb();
		String dt = "20130909";
		Executable exe = new Executable();
		exe.request = ("<transaction> " + "<header> <msg> <sndAppCd>" + "HBJY</sndAppCd> <callTyp>SYN</callTyp> <msgCd>"
				+ "SMS.000010020.01" + "</msgCd><seqNb>" + seqNb + "</seqNb><sndTm>070001</sndTm><sndDt>"
				+ "20170524</sndDt><rcvAppCd>SMS</rcvAppCd></msg>" + "</header>" + "<body>"
				+ "<request><smsBizCd>TYZZY001</smsBizCd><chanDate>" 
				+ chanDate // 系统请求时间
				+ "</chanDate><chanSeqNo>" 
				+ chanSeqNo // 流水账号
				+ "</chanSeqNo><chanNo>95</chanNo><telNum>"
				+ phone // 手机号
				+ "</telNum><smsTemplatFlg>0</smsTemplatFlg><tlrNo>" 
				+ tlrNo // 柜员号
				+ "</tlrNo><smsCntt>" + "货币金银采集系统中有您一条未完成任务，请前往处理！" + "</smsCntt><txBranch>" 
				+ txBranch // 发卡机构号
				+ "</txBranch></request>" + "</body>" + "</transaction>").getBytes();
		exe.timeout = 60;
		exe.correlationID = ("20170524" + "-" + seqNb).getBytes();
		System.out.println("request :" + "::" + new String(exe.request));
		try {
			esb2.execute(exe);
			System.out.println("response :" + "::" + new String(exe.response));
		} catch (JmqiException mqe) {
			esb2.destory();
			System.out.println("catch JmqiException and reload");
			esb2.init("esb_shareQ.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
	}

}
