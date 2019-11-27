package User.service;

import java.io.IOException;
import java.util.List;

import entity.User;

public class UserService {

	//增
	public boolean addUser(String phone,String password) {
		boolean isAdd = false;
		List<User> users = findUser(phone);
		if(!users.isEmpty()){  //不为空，说明已经有这个手机号的用户了，插入失败
			return isAdd;
		}else{
			boolean isSaved = new User().set("phoneNumber", phone).set("password", password).save();			
			if(isSaved){
				System.out.println("存好了");
				isAdd = true;
			}
		}
		return isAdd;
	}
	//查
	public List<User> findUser(String phone){
		List<User> users = null;
		users = User.dao.find("select * from tbl_parent where phoneNumber=?",phone);
		return users;
	}
	//改
	public void editUser(){
		
	}
	//忘记密码
	public boolean forget(String phone,String password){
		boolean isSave = false;
		List<User> users = User.dao.find("select * from tbl_parent where phoneNumber=?",phone);
		if(!users.isEmpty()){
			boolean isUpdate = User.dao.findById(users.get(0).get("id")).set("password",password).update();
			if (isUpdate) {
				isSave = true;
			}
		}
		return isSave;
	}
}
