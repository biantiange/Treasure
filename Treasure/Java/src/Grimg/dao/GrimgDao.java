package Grimg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Grimg;
import util.DBUtil;

public class GrimgDao {
	//先插入图片路径，返回记录id
	public int addGrimgPath(String path){
		Connection con = DBUtil.getCon();
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		int count = 0;
		int id=-1;
		try {
			preparedStatement = con.prepareStatement("insert into tbl_grimg(imgPath) values (?)");
			preparedStatement.setObject(1,path);
			count =  preparedStatement.executeUpdate();
			if(count !=0){
				//获取新插入的ID值
				PreparedStatement pstm = con.prepareStatement("select MAX(id) from tbl_grimg");
				rs=pstm.executeQuery();
				if(rs.next()){
					id = rs.getInt(1);  
				}
			}
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return count;
		return id;
	}
	public int addGrimgOther(Grimg grimg){
		Connection con = DBUtil.getCon();
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		int count = 0;
		int id=-1;
		try {
			preparedStatement = con.prepareStatement("update tbl_grimg set growthRecordId=?,upTime=?,tag=? where id=?");
			//preparedStatement = con.prepareStatement("update tbl_grimg set growthRecordId=?,upTime=? where id=?");
			preparedStatement.setObject(1,grimg.getGrowthRecordId());
			preparedStatement.setObject(2, grimg.getUpTime());
			preparedStatement.setObject(3, grimg.getTag());
			preparedStatement.setObject(4, grimg.getId());
			count =  preparedStatement.executeUpdate();
			/*if(count !=0){
				//获取新插入的ID值
				PreparedStatement pstm = con.prepareStatement("select MAX(id) from tbl_img");
				rs=pstm.executeQuery();
				if(rs.next()){
					id = rs.getInt(1);  
				}
			}
			return id;*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return count;
		return count;
	}
}
