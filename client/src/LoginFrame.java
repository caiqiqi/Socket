import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户名
	private JLabel jL_name = new JLabel("用户名");
	private JTextField jTF_name = new JTextField(20*2);// 参数为20个column
	
	//密码
	private JLabel jL_password = new JLabel("密码");
	private JTextField jTF_password = new JTextField(20*2);// 参数为20个column
	
	
	private JButton jB_login = new JButton("登录");

	private User mUser = new User();

	// 状态标记(成功或失败)
	private String tagStatus = "0";

	String getTagStatus() {
		return tagStatus;
	}

	void setTagStatus(String tagStatus) {
		this.tagStatus = tagStatus;
	}

	private Socket mSocket;

	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 12000;

	public LoginFrame() {

		initBox();
		initWindow();
		initListeners();
		
	}

	private void initWindow() {
		
		this.setLocation(300, 200);
		this.setTitle("Login");
		this.pack();
		//哦，默认窗口是不可见的，所以这句必不可少
		this.setVisible(true);
		//设置不可改变大小
		this.setResizable(false);
	}

	private void initBox() {
		//这是一个水平的一栏，可以包括两个控件，一个JLabel，一个JTextField
		
		//用户名
		Box box_name = Box.createHorizontalBox();
		box_name.add(Box.createHorizontalStrut(30*2));
		box_name.add(this.jL_name);
		box_name.add(Box.createHorizontalStrut(20*2));
		box_name.add(this.jTF_name);
		//密码
		Box box_password = Box.createHorizontalBox();
		box_password.add(Box.createHorizontalStrut(30*2));
		box_password.add(this.jL_password);
		box_password.add(Box.createHorizontalStrut(20*2));
		box_password.add(this.jTF_password);
		//登录按钮
		Box box_btn = Box.createHorizontalBox();
		box_btn.add(this.jB_login);
		
		
		Box mainBox = Box.createVerticalBox();
		mainBox.add(Box.createVerticalStrut(20*2));
		mainBox.add(box_name);
		mainBox.add(Box.createVerticalStrut(10*2));
		mainBox.add(box_password);
		mainBox.add(Box.createVerticalStrut(10*2));
		mainBox.add(box_btn);
		
		this.add(mainBox);
		
	}
	
	private void initListeners(){

		jB_login.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				String username = jTF_name.getText();
				String password = jTF_password.getText();
				
			    setUser(username, password);
				
				// 确保用户输入有效字符串，不能为空
				// 修改condition,做用户名或密码是否为空的判断
				if (isNullInserted()) {
					JOptionPane.showConfirmDialog(null, "User name or password null", "Warning", 
							JOptionPane.OK_CANCEL_OPTION);
					return;
				}

                //若登录成功
				if( login()){
				
					//开启新线程
					Thread clientThread = new ClientThread(mSocket, LoginFrame.this);
					clientThread.start();
					System.out.println("LoginFrame:已开启子线程");
					//等待子线程执行结束
					try{
						clientThread.join();
						System.out.println("LoginFrame:子线程完成，回到主线程");
					}catch(InterruptedException e1){
						e1.printStackTrace();
					}
					
				}
					

				System.out.println("tagStatus:" + tagStatus);
				//String用.equals()，int用==  ???
				//oh，不是，都可以，只是刚才把两个if的判断都写成Constants.TAG_SUCCESS了，导致没法进入任何一个if
				if (tagStatus == Constants.TAG_SUCCESS){
					//将输入框设置为空
					jTF_name.setText("");
					jTF_password.setText("");
					
					JOptionPane.showConfirmDialog(null, "登录成功！", "状态", 
							JOptionPane.OK_CANCEL_OPTION);
				} else 
					if (tagStatus == Constants.TAG_FAILURE){
						
						JOptionPane.showConfirmDialog(null, "登录失败，请重试！", "状态", 
								JOptionPane.OK_CANCEL_OPTION);
					}
			}
		});
	}

	/**
	 * 判断用户名或密码是否为空
	 */
	private boolean isNullInserted() {
		return (LoginFrame.this.jTF_name.getText().trim().equals("") || LoginFrame.this.jTF_password
				.getText().trim().equals(""));
	}

	private boolean login() {
		
		try {
			initSocket();
			if (mSocket != null) {
				OutputStream os = mSocket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				// 将User对象写到Socket中
				oos.writeObject(mUser);
				oos.flush();
				System.out.println("已向Socket中发出User对象");
				return true;
			}
			
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void initSocket() throws IOException {

		mSocket = new Socket(SERVER_IP, SERVER_PORT);

	}


	/**
	 * 将用户名密码打包
	 **/
	private void setUser(String username, String password) {

		mUser.setName(username);
		mUser.setPassword(password);
	}
}
