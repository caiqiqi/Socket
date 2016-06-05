import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread {
	
	private Socket mSocket;
	
	private String line;
	
	private LoginFrame mContext;
	
	public ClientThread(Socket socket, LoginFrame loginFrame){
		
		this.mSocket = socket;
		this.mContext = loginFrame;
	}
	
	public void run() {
		
		try{
			System.out.println("ClientThread:进入ClientThread");
			
			InputStream is = mSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			while( (line = br.readLine()) != null){
				System.out.println("ClientThread:已读到服务器发来的数据");
				if(line.equals(Constants.TAG_SUCCESS)){
					
					mContext.setTagStatus(Constants.TAG_SUCCESS);
					System.out.println("ClientThread:TAG_SUCCESS");
					break;
				} else 
					if(line.equals(Constants.TAG_FAILURE)){
						
						mContext.setTagStatus(Constants.TAG_FAILURE);
						System.out.println("ClientThread:TAG_FAILURE");
						break;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
}