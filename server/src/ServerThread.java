import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private Socket socket;
	
	private ObjectInputStream ois;
	
	private User mUser;
	
	private String line;
	
	private PrintStream out;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run(){
		
		try{
			
			//得到对象输入流
			ois = new ObjectInputStream(socket.getInputStream());
			//得到输出流
			out = new PrintStream(socket.getOutputStream());
			mUser = (User) ois.readObject();
			System.out.println(mUser);
			System.out.println("ServerThread:登录" + Util.check(mUser));
			
			//用户名密码匹配则输出“成功”
			if (Util.check(mUser)) {

				out.println(Constants.TAG_SUCCESS);
				out.flush();
				System.out.println("ServerThread:登录成功");
			} else {

				// 否则输出“失败”
				out.println(Constants.TAG_FAILURE);
				out.flush();
				System.out.println("ServerThread:账号密码不匹配");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}