package mine.editUser.dao;

import java.util.List;
import java.util.Map;

import entity.User;
import mine.MineDBUtil;



public class FindUserDao {
	public List<Map<String,Object>> findAll(String pi) {
		if(pi!=null){
		return MineDBUtil.findAll("select * from tbl_parent where phoneNumber=?",new Object[]{pi});
		}
		else {
			return null;
		}
	}
	public List<Map<String,Object>> User(String pi) {
		if(pi!=null){
		return MineDBUtil.findAll("select * from tbl_parent where id=?",new Object[]{pi});
		}
		else {
			return null;
		}
	}
	public List<User> findAllChilds(String pi){
		if(pi!=null){
			int p=Integer.parseInt(pi);
		
		return MineDBUtil.find(User.class, "select * from tbl_parent where phoneNumber=?",new Object[]{p} );
		
	} else {
		return null;
	}
	}

	public User findById(String phoneNumber) {
		Object object = MineDBUtil.findById(User.class,phoneNumber);
		return object != null ? (User)object : null;
	}
	
	public int  updataUser(String nickname,String imgpath,String phoneNumber) {
		return MineDBUtil.executeUpdate(
				"update tbl_parent set nickname=?,headerPath=? where phoneNumber=?",new Object[]{nickname,imgpath,phoneNumber});
		
	
		
	}
	
}
