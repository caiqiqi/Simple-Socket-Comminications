public class MultiThreadClient extends Activity {
	
	/**
	*可以看出Handler是定义在主线程中的，并且在主线程中接收来自子线程的消息。
	*在消息处理函数handleMessage(Message msg)接收来自子线程的Message,
	*然后可以完全调用主线程的任何资源，包括刷新界面
	**/
	
	//界面的两个文本框
	EditText input;
	TextView show;
	//界面上的一个按钮
	Button send;
	Handler handler;
	//与服务器通信的子线程
	ClientThread clientThread;
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		input = (EditText)findViewById(R.id.input);
		send = (Button)findViewById(R.id.send);
		show = (TextView)findViewById(R.id.show);
		
		/*
		*这个主线程中的Handler处理ClientThread中的匿名子线程中发送过来的消息
		*/
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				//处理子线程中接收的来自服务器的信息
				if(msg.what == 0x123){
					//将读到的信息追加显示在文本框中
					show.append("\n" + msg.obj.toString());
				}
			}
		};
		
		clientThread = new ClientThread(handler);
		//在主线程中启动ClientThread线程创建网络连接、读取来自服务器的数据
		new Thread(clientThread).start();
		
		send.setOnClickListener(new OnClickListener){
			@Override
			public void onClick(View v){
				try{
					//当用户按下发送按钮后，将用户的输入数据封装成Message
					//然后利用clientThread中的Handler的sendMessage方法将消息发给子线程的Handler
					Message msg = new Message();
					msg.what = 0x345;
					msg.obj = input.getText().toString();
					/*
					*这里，由于这里还是在主线程中，然而又由于rcvHandler这个Handler是public的，于是可以在这里sendMessage()，
					*而在ClientThread类的内部的rcvHandler的handleMessage()中来处理从这里send出去的Message
					*/
					clientThread.rcvHandler.sengMessage(msg);
					
					//清空input文本框;
					input.setText("");
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		};
	}
}
