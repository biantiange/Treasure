package Grimg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	//轮播图
	
	public  List<Map<String,Object>> findPictureByTime(String time) {
		Connection con = DBUtil.getCon();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			pstm = con.prepareStatement("select imgPath,growthRecordId from tbl_grimg where upTime like ?");
			pstm.setString(1, "%"+time+"%");
			rs = pstm.executeQuery();
			while(rs.next()) {
				Map<String,Object> map=new HashMap<>();
				String imgPath=rs.getString(1);
				int growthRecordId=rs.getInt(2);
				map.put("imgPath",imgPath);
				map.put("recordId",growthRecordId);
				System.out.println(imgPath);
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			DBUtil.close(rs,pstm,con);
		}

	}
	
	public  List<Map<String,Object>> findContentByTime(String time) {
		Connection con = DBUtil.getCon();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			pstm = con.prepareStatement("select id, content from tbl_growthrecord where upTime like ?");
			pstm.setString(1, "%"+time+"%");
			rs = pstm.executeQuery();
			while(rs.next()) {
				Map<String,Object> map=new HashMap<>();
				int id=rs.getInt(1);
				String content=rs.getString(2);
				map.put("id",id);
				map.put("content",content);
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			DBUtil.close(rs,pstm,con);
		}

	}
}
