package entity;

import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User>{
	public static final User dao = new User().dao();
	private int id;
	private String phongnumber;
	private String nickName;
	private String password;
	private String headerPath;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhongnumber() {
		return phongnumber;
	}
	public void setPhongnumber(String phongnumber) {
		this.phongnumber = phongnumber;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHeaderPath() {
		return headerPath;
	}
	public void setHeaderPath(String headerPath) {
		this.headerPath = headerPath;
	}
	public static User getDao() {
		return dao;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", phongnumber=" + phongnumber + ", nickName=" + nickName + ", password=" + password
				+ ", headerPath=" + headerPath + "]";
	}
	
	
}
