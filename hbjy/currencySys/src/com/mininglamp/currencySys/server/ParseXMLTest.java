package com.mininglamp.currencySys.server;

import spc.webos.data.Message;

public class ParseXMLTest {
	public static void main(String[] args) throws Exception {
		Message msg = new Message(
				"<transaction><header><msg><msgCd>xxxx</msgCd></msg></header><body><request><a>a1</a><a>a2</a></request></body></transaction>"
						.getBytes());
		System.out.println(msg.getMsgCd());
		System.out.println(msg.toXml(true));
	}
}
