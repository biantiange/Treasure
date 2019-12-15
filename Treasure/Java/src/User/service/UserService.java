package User.service;

import java.io.IOException;
import java.util.List;

import com.sun.javafx.tk.RenderJob;

import User.dao.UserDao;
import entity.User;

public class UserService {

	//增(返回其ID值)
	public String addUser(User user) {
		return new UserDao().addUser(user);
	}
	//查
	public int findUser(String phone){
		return new UserDao().findByPhone(phone);
	}
	//改
	public void editUser(){
		
	}
	//忘记密码
	public int forget(String phoneNumber,String password){
		return new UserDao().forget(phoneNumber, password);
	}
	//登录
	public User login(String phoneNumber,String password){
		return new UserDao().login(phoneNumber, password);
	}
}
