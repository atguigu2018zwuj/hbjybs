package com.mininglamp.currencySys.server;

import spc.webos.endpoint.ESB2;
import spc.webos.queue.IOnMessage;

public class ESBServerTest {
	public static void main(String[] args) throws Exception {

		server(ESB2.getInstance(), new BizOnMessage()); // һ��jvm�����е�ʵ��esb2ģʽ����
		// server2(new ESB2(), new BizOnMessage()); // �ǵ���ģʽ���ʺ�һ������ϵͳ���Ӷ���ESB����

		Thread.sleep(1000000l);
	}

	// ʹ�������ļ�����ʵ������ESB����
	static void server(ESB2 esb2, IOnMessage onMessage) throws Exception {
		esb2.setRequestOnMessage(onMessage); // ʹ�õ�ǰMQ��ȡ�߳���Ϊҵ�����߳�
		esb2.init("esb.properties"); // ����classpathĿ¼��esb.properties��������Ϣ�������ڲ��̳߳ص����
		// jvmֹͣ����Ҫ destroy ESB�������رպ�̨�̵߳ȸ������
	}

	// �������ļ�ģʽ����ESB
	static void server2(ESB2 esb2, IOnMessage onMessage) throws Exception {
		esb2.setRequestOnMessage(onMessage); // ʹ�õ�ǰMQ��ȡ�߳���Ϊҵ�����߳�
		// ˫���й�������MQ�����ӳ�������Ϣ�����ڷ���Ӧ����
		esb2.setAccess("[{name:'GWIN',maxCnnNum:1,channel:'219.143.38.252:33300/SVRCONN_GW'},{name:'GWOUT',maxCnnNum:1,channel:'219.143.38.252:33399/SVRCONN_GW'}]");
		// ˫���й�����MQ��Ϣ��ȡ�̳߳أ����ڶ�ȡREQ.ECIFҵ����е���Ϣ:
		// size:2��ʾ2��������MQ���ж�ȡ�߳�, Ĭ��Ϊ1�� ��ֵ����̫���������5��MQ��ȡ�����൱��
		// bizPoolSize:50��ʾ��50��ҵ���߳�������ҵ������
		// maxBlockSize:0��ʾҵ���̵߳�����������������������״�50��ҵ���̶߳�����ִ��ʱ�Ƿ����������߳��������µ����������������Ϣ�ἷѹ��MQ�����У�
		// Ĭ��Ϊ0
		// channel:��MQ��������Ϣ
		esb2.setRequestPools("[{qname:'REQ.ESB',size:1,bizPoolSize:50,channel:'219.143.38.252:33300/SVRCONN_GW'},{qname:'REQ.ESB',size:1,bizPoolSize:50,channel:'219.143.38.252:33399/SVRCONN_GW'}]");
		esb2.init(); // �����ڲ��̳߳ص����
		// jvmֹͣ����Ҫ destroy ESB�������رպ�̨�̵߳ȸ������
	}

	// ���Զ�ҵ���̴߳���
	// static void bizpool() throws Exception {
	// IOnMessage onMessage = new IOnMessage() {
	// public void onMessage(Object obj, AccessTPool pool,
	// AbstractReceiverThread thread) throws Exception {
	// System.out.println("start " + obj + ":"
	// + Thread.currentThread().getName());
	// Thread.sleep(1000);
	// System.out.println("end " + obj + ":"
	// + Thread.currentThread().getName());
	// }
	// };
	// BizPoolOnMessage pool = new BizPoolOnMessage(2, onMessage, 0);
	// for (int i = 0; i < 5; i++) {
	// System.out.println("add: " + i);
	// pool.onMessage("i=" + i, null, null);
	// System.out.println("suc add: " + i);
	// }
	// }
}
