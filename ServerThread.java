public class ServerThread implements Runnable{
  
  //当前线程处理的Socket
  Socket s;
  //当前线程所处理的Socket所对应的输入流
  BufferedReader br;
  
  public ServerThread (Socket s) throws IOException {
    this.s = s;
    //初始化该Socket对应的输入流
    br = new BufferedReader (new InputSreamReader(s.getInputStream(), "utf-8"));
  }
  
  public void run() {
    try {
      String content;
      //采用循环不断从Socket中读取客户端发送过来的数据
      while((content = readFromClient()) != null) {
        //遍历socketList中的每个Socket
        //将读到的内容向每个Socket发送一次
        for (Socket s :MyServer.socketList){
          OutputStream os = s.getOutputStream();
          os.write((content + "\n").getBytes("utf-8"));
        }
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }
  /*
  * 上述的os.write()代码将网络的字节输入流转换为字符输入流时，指定了转换所用的字符串：UTF-8.
  * 当需要编写跨平台的网络通信程序时，使用UTF-8字符集进行编码、解码是一种较好的解决方案。
  */
  
  //定义读取客户端数据的方法
  private String readFromClient(){
    try {
      return br.readLine();
    } catch (IOException e){
      //若捕获到异常，则说明该Socket对应的客户端已关闭，于是需从socketList中移除对应的客户端Socket
      MyServer.socketList.remove(s);
    }
    
    return null;
  }
}
