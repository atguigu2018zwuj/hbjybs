package com.mininglamp.currencySys.channel;
import com.ibm.mq.jmqi.JmqiException;
import com.mininglamp.currencySys.server.BizOnMessage;
import spc.webos.endpoint.ESB2;
import spc.webos.endpoint.Executable;
public class ESB2Test {
	public static void main(String[] args) throws Exception {
		ESB2 esb2 = ESB2.getInstance();
		esb2.setAsynResponseOnMessage(new AsynResponseOnMessage()); 
		esb2.setRequestOnMessage(new BizOnMessage()); 
		shareQ(esb2,"20190218","20190218155123","15981943396","admin","1600199998"); 
		esb2.destory();
	}
	    static void shareQ(ESB2 esb2,String chanDate, String chanSeqNo, String phone, String tlrNo, String txBranch) throws Exception {
		esb2.init("esb.properties");   
		    int i=0;
			String seqNb = esb2.getSeqNb(); // ���ı�����ESB2�������ɵ���ˮ�š� 
			String dt = "20130909"; 
			Executable exe = new Executable();  //���� Executable����  
			/*exe.request = ("<transaction><header><msg><msgCd>ESB.00000010.01</msgCd><seqNb>"
					+ seqNb
					+ "</seqNb><sndTm>"
					+ "0909009"
					+ "</sndTm><sndDt>"
					+ dt + "</sndDt><sndAppCd>ESB</sndAppCd></msg></header><body><request><_a>a</_a></request></body></transaction>")
					.getBytes();*/
			//Message msg = new Message("<transaction> <header> <ver>1.0</ver> <msg> <sndAppCd>HBJY</sndAppCd> <callTyp>SYN</callTyp> <msgCd>SMS.000010020.01</msgCd> <replyToQ>REP.HBJY.SYN</replyToQ> <seqNb>Sa8040108573736</seqNb>             <sndTm>070001</sndTm>             <sndDt>20170524</sndDt>             <rcvAppCd>SMS</rcvAppCd>         </msg>     </header>     <body>         <request>             <smsBizCd>TYZZY001</smsBizCd>             <chanDate>20170524</chanDate>             <chanSeqNo>Sa8040108573736</chanSeqNo>             <chanNo>95</chanNo>             <telNum>18951228335</telNum>             <smsTemplatFlg>0</smsTemplatFlg>             <tlrNo>00210074</tlrNo>             <smsCntt>����ʧ�ܣ�</smsCntt>             <txBranch>1600299999</txBranch>         </request>     </body> </transaction>".getBytes());
			exe.request=("<transaction>"
					+ "<header>"
					+ "<msg>"
					+ "<sndAppCd>HBJY</sndAppCd>"
					+ "<callTyp>SYN</callTyp>"
					+ "<msgCd>SMS.000010020.01</msgCd>"
					+ "<seqNb>"+seqNb+"</seqNb>"
					+ "<sndTm>070001</sndTm>"
					+ "<sndDt>"+dt+"</sndDt>"
					+ "<rcvAppCd>SMS</rcvAppCd>"
					+ "</msg>"
					+ "</header>"
					+ "<body>"
					+ "<request>"
					+ "<smsBizCd>TYZZY001</smsBizCd>"
					+ "<chanDate>"
					+chanDate+"</chanDate>"
					+ "<chanSeqNo>"+seqNb+"</chanSeqNo>"
					+ "<chanNo>95</chanNo>"
					+ "<telNum>"
					+ phone+"</telNum>"
					+ "<smsTemplatFlg>0</smsTemplatFlg>"
					+ "<tlrNo>"
					+ tlrNo+"</tlrNo>"
					+ "<smsCntt>测试</smsCntt>"
					+ "<txBranch>"
					+ txBranch+"</txBranch>"
					+ "</request>"
					+ "</body>"
					+ "</transaction>").getBytes();
			exe.timeout = 60;
			exe.correlationID = (dt + "-" + seqNb).getBytes();
			System.out.println("request i:" + i + "::"
					+ new String(exe.request));
			try {
				esb2.execute(exe);
				System.out.println("response i:" + i + "::"
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
	