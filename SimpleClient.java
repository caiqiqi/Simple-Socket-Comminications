public class SimpleClient extends Activity {
  
  EditText et_show;
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    /*
    *网络操作放在一个新的线程中。虽然是在所谓的"新线程"，但由于这个所谓的"新线程"其实还是可以共享主线程(也就是这个new Thread
    *前面的这个Activity)所在范围的变量，如这里的show变量。于是这样就使得在新线程中更新UI成为了可能。
    */
    
    new Thread () {
      @Override
      public void run() {
        try {
          //建立连接到远程服务器的Socket
          Socket socket = new Socket ("server IP", 30000)
          //将Socket对应的输入流包装成BufferedReader
          BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          //进行I/O操作
          String line = br.readLine();
          show.setText("来自服务器的数据：" + line)；
          //关闭输入流、socket
          br.close();
          socket.close();
        } catch (IOException e){
          e.printStackTrace();
        }
      }
    }.start();
  }
}
