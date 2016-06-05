import java.net.ServerSocket;
import java.net.Socket;

public class Server{
	
	private ServerSocket mServerSocket;
	private static final int PORT = 12000;
	
	public Server(){
		
		try{
			
			this.mServerSocket =  new ServerSocket(PORT);
			System.out.println("Server started...");
		
			while(true){
				//监听Socket连接
				Socket s = this.mServerSocket.accept();
				//启动服务器线程
				new ServerThread(s).start();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
}