package mine.editUser.service;

import java.util.List;
import java.util.Map;

import mine.editUser.dao.FindUserDao;




public class FindUserService {
	public boolean editUser (String nickname,String imgpath,String phoneNumber) {
		int count = new FindUserDao().updataUser(nickname,imgpath,phoneNumber);
		return count >0;
	}
	public List<Map<String,Object>> listUser(String pi) {
		return new FindUserDao().findAll(pi);
	}
}
