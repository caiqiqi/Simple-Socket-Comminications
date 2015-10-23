public class SimpleServer {
  
  /**
   * 一个简单的单进程的Socket服务端的类
   **/
  public static void main(String[] args) throws IOException {
    
    //创建一个ServerSocket,用于监听客户端Socket的连接请求
    ServerSocket ss = new ServerSocket(30000);
    //采用循环不断接收来自客户端的请求
    while(true) {
      //每当接收到客户端的Socket的请求，服务器端也对应产生一个Socket
      Socket s = ss.accept();
      OutputStream os = s.getOutputStream();
      os.write(".......\n".getBytes("utf-8"));
      //关闭输出流，关闭Socket
      os.close();
      s.close();
    }
  }
