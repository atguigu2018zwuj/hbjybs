package com.mininglamp.currencySys.channel;
import spc.webos.queue.AbstractReceiverThread;
import spc.webos.queue.AccessTPool;
import spc.webos.queue.IOnMessage;
import spc.webos.queue.QueueMessage;
public class AsynResponseOnMessage implements IOnMessage {
	public void onMessage(Object obj, AccessTPool pool,
			AbstractReceiverThread thread) throws Exception {
		QueueMessage qmsg = (QueueMessage) obj;
		byte[] request = qmsg.buf; // ������
		System.out.println("Asyn Response qname:" + qmsg.qname + ", MQMD:"
				+ qmsg.applicationIdData + "," + qmsg.applicationOriginData
				+ ",replyToQ:" + qmsg.replyToQ + "," + qmsg.replyToQMgr + ","
				+ qmsg.expirySeconds + "," + qmsg.putAppName + ",corId:"
				+ new String(qmsg.correlationId) + ","
				+ new String(qmsg.messageId) + "\nrequest:"
				+ new String(request));
	}
}
