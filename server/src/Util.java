/**
 * 检查用户名与密码是否匹配
 * @author caiqiqi
 *
 */
public class Util {

	public static boolean check(User user){
		//哦，这里不能写 ==,而应该写equals
			return "caiqiqi".equals( user.getName())
					&& "caiqiqi".equals( user.getPassword());
	}
}
