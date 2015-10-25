public class ClientThread implements Runnable {
	
	private Socket s;
	
	//��UI�̷߳�����Ϣ��Handler
	private Handler handler;
	//����UI�߳���Ϣ��Handler����
	public Handler rcvHandler;
	
	//���߳��������Socket����Ӧ��������
	BufferedReader br;
	OutputStream os;
	
	public ClientThread(Handler handler){
		this.handler = handler;
	}
	
	public void run(){
		try {
			s = new Socket ("192.168.23.1",30000);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = s.getOutputStream();
			
			//����һ�����߳�����ȡ��������Ӧ������
			new Thread(){
				@Override
				public void run(){
					String content;
					//���϶�ȡSocket�������е�����
					
					try {
						while((content = br.readLine()) != null){
							//ÿ����ȡ�����Է�����������(һ��һ�е�)֮��
							//������Ϣ֪ͨ���̣߳����½���
							//�Զ���������
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = content;
							
							//***���߳��е�Handler�ᴦ���
							handler.sendMessage(msg);
						}
					} catch(IOException e){
						e.printStackTrace();
					}
				}
			}.start();
			
			//Ϊ��ǰ�̳߳�ʼ��Looper
			Looper.prepare();
			
			//����rcvHandler����
			rcvHandler = new Handler(){
				@Override
				public void handleMessage(Message msg){
					//���յ�UI�߳����û����������
					if(msg.what == 0x345){
						//���û����ı��������������д��Socket�д���
						try{
							os.write((msg.obj.toString() + "\r\n").getBytes("utf-8"));
						} catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			};
			
			//����Looper
			Looper.loop();
		} catch (SocketTimeoutException e1){
			System.out.println("�������ӳ�ʱ��");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}