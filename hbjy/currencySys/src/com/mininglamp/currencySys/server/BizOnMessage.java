package com.mininglamp.currencySys.server;

import org.apache.log4j.Logger;

import spc.webos.data.IMessage;
import spc.webos.data.Status;
import spc.webos.data.converter.XMLConverter2;
import spc.webos.endpoint.AbstractBizOnMessage;
import spc.webos.queue.AbstractReceiverThread;
import spc.webos.queue.AccessTPool;
import spc.webos.queue.QueueMessage;

public class BizOnMessage extends AbstractBizOnMessage {
	Logger log = Logger.getLogger(getClass());
	String appCd = "ESB"; // ����ϵͳ��ţ�ECIF, CBS��

	public void onMessage(Object obj, AccessTPool pool,
			AbstractReceiverThread thread) throws Exception {
		QueueMessage qmsg = (QueueMessage) obj;
		byte[] request = qmsg.buf; // ������
		// ����ESB2��ʵ����׷��ִ�е�ǰ���������ESB2ʵ���ţ���ֹjvmֹͣû��ͣ����̨esb2ʵ��
		System.out.println("ESB2 ID:" + esb2.getInstanceId() + ", startTm:"
				+ esb2.getStartTm());
		System.out.println("request:" + new String(request));

		byte[] response = doMessage(obj, pool, thread, request); // ҵ����
		if (response != null) {
			System.out.println("response:" + new String(response));
			esb2.sendResponse(response); // ��REP���з���Ӧ����
			// ʹ��ָ�����У�ָ����Ϣ����ģʽ����Ӧ����
			// QueueMessage qmsg = new QueueMessage(response);
			// esb2.send("REP", qmsg);
		}
	}

	// ���������esb xml���ģ�����ҵ����������ҵ��Ӧ����
	public byte[] doMessage(Object obj, AccessTPool pool,
			AbstractReceiverThread thread, byte[] request) throws Exception {
		// ���ú�̨ҵ�����߼�..
		// 1. ʹ�����õ�xml�������������esb xml���Ľ���ΪIMessage�ڲ�����
		IMessage msg = XMLConverter2.getInstance().deserialize(request);

		// 2. ���� msg������������Ӧҵ�������response��Ϣ
		msg.setInResponse("hello", "world");

		// 3. ����ԭ������ͷ��Ϣ��дӦ����ͷ��Ϣ
		msg.setRequest(null); // ɾ��request��ǩ
		esb2.req2rep(msg); // ����ԭ����ͷ����ref��Ϣ
		msg.setMsgCd("ESB.00000011.01"); // ��дӦ���ı��, ��Ҫ���ݲ�ͬҵ�������ͬ��ҵ���ı��
		msg.setSndAppCd(appCd); // ���÷���ϵͳ���
		// ����Ӧ���ĵ�״̬��������������: appcd, ip, retcd
		Status status = new Status();
		status.setAppCd(appCd);
		status.setIp("127.0.0.1"); // ����IP
		status.setRetCd("000000"); // ������, ͨ����ȷ������
		msg.setStatus(status);

		return XMLConverter2.getInstance().serialize(msg);
	}
}
