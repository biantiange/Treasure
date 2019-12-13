package User.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.jws.soap.SOAPBinding.Use;

import entity.User;
import util.DBUtil;

public class UserDao {

	//增(返回其ID值)
	public int addUser(User user) {
		int id = findByPhone(user.getPhoneNumber());  //添加用户时，看看数据库表是否已经有这个用户，=-1说明没有
		int idd=-1; //默认-1
		if(id==-1){
			Connection con = DBUtil.getCon();
			PreparedStatement preparedStatement=null;
			ResultSet rs = null;
			int count = 0;
			try {
				preparedStatement = con.prepareStatement("insert into tbl_parent(phoneNumber,password) values (?,?)");
				preparedStatement.setObject(1,user.getPhoneNumber());
				preparedStatement.setObject(2, user.getPassword());
				count =  preparedStatement.executeUpdate();
				if(count !=0){
					//获取新插入的ID值
					PreparedStatement pstm = con.prepareStatement("select MAX(id) from tbl_parent");
					rs=pstm.executeQuery();
					if(rs.next()){
						idd = rs.getInt(1);  
					}
				}
				//return count;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//return count;
		}
		return idd;
		
	}
	//忘记密码
	public int forget(String phoneNumber,String password){
		int id = findByPhone(phoneNumber);
		if(id != -1){  //有这个人
			Connection con = DBUtil.getCon();
			PreparedStatement preparedStatement=null;
			ResultSet rs = null;
			int count = 0;
			try {
				preparedStatement = con.prepareStatement("update tbl_parent set password=? where phoneNumber=?");
				preparedStatement.setObject(1,password);
				preparedStatement.setObject(2, phoneNumber);
				count =  preparedStatement.executeUpdate();
				//return count;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return count;
			//return id;
		}else{
			return -1;
		}
		
	}
	//根据手机号查询id
	public int findByPhone(String phoneNumber){
		Connection con = DBUtil.getCon();
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		int count = 0;
		int id=-1;  //默认-1
		try {
			preparedStatement = con.prepareStatement("select id from tbl_parent where phoneNumber=?");
			preparedStatement.setObject(1,phoneNumber);
			rs =  preparedStatement.executeQuery();
			if(rs.next()){
				id = rs.getInt(1);
			}
			//return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return count;
		return id;
	}
	public int login(String phoneNumber,String password){
		Connection con = DBUtil.getCon();
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		int count = 0;
		int id=-1;  //默认-1
		try {
			preparedStatement = con.prepareStatement("select id from tbl_parent where phoneNumber=? and password=?");
			preparedStatement.setObject(1,phoneNumber);
			preparedStatement.setObject(2, password);
			rs =  preparedStatement.executeQuery();
			if(rs.next()){
				id = rs.getInt(1);
			}
			//return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return count;
		return id;
	}
	//根据id查用户 
	public User findById(int id) {
		Object object =  DBUtil.findByIdForParent(User.class, id);
		return object != null ? (User)object : null;
	}
}
