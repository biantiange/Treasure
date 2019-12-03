package User.controller;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.render.Render;
import com.mysql.fabric.Response;

import User.service.UserService;
import entity.User;

public class UserController extends Controller{
	
	//增
	public void addUser(){
		String phone = getPara(0);
		String password = getPara(1);
		boolean isSave = new UserService().addUser(phone, password);
		if(isSave){
			renderJson("OK");
		}else{
			renderJson("FAIL");
		}
	}
	//查
	public void findUser(){
		String phone = getPara(0);
		List<User> users = null;
		users = new UserService().findUser(phone);
		if(!users.isEmpty()){
			renderJson("OK");
		}else{
			renderJson("FAIL");
		}
	}
	
	//忘记密码
	public void forget(){
		String phone = getPara(0);
		String password = getPara(1);
		boolean isSave = new UserService().forget(phone, password);
		if(isSave){
			renderJson("OK");
		}else{
			renderJson("FAIL");
		}
	}
	//改
	public void editUser(){
		
	}
}
