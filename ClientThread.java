public class ClientThread implements Runnable {
	
	private Socket s;
	
	//向UI线程发送消息的Handler
	private Handler handler;
	//接收UI线程消息的Handler对象
	public Handler rcvHandler;
	
	//该线程所处理的Socket所对应的输入流
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
			
			//启动一条子线程来读取服务器响应的数据
			new Thread(){
				@Override
				public void run(){
					String content;
					//不断读取Socket输入流中的内容
					
					try {
						while((content = br.readLine()) != null){
							//每当读取到来自服务器的数据(一行一行的)之后，
							//发送消息通知主线程，更新界面
							//显读到的数据
							Message msg = new Message();
							msg.what = 0x123;
							msg.obj = content;
							
							//***主线程中的Handler会处理的
							handler.sendMessage(msg);
						}
					} catch(IOException e){
						e.printStackTrace();
					}
				}
			}.start();
			
			//为当前线程初始化Looper
			Looper.prepare();
			
			//创建rcvHandler对象
			rcvHandler = new Handler(){
				@Override
				public void handleMessage(Message msg){
					//接收到UI线程中用户输入的数据
					if(msg.what == 0x345){
						//将用户在文本框内输入的内容写到Socket中传输
						try{
							os.write((msg.obj.toString() + "\r\n").getBytes("utf-8"));
						} catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			};
			
			//启动Looper
			Looper.loop();
		} catch (SocketTimeoutException e1){
			System.out.println("网络连接超时！");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}