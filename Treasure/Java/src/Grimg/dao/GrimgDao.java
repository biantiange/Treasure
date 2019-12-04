package Grimg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Grimg;
import util.DBUtil;

public class GrimgDao {
	public int addGrimg(Grimg grimg){
		Connection con = DBUtil.getCon();
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		int count = 0;
		int id=-1;
		try {
			preparedStatement = con.prepareStatement("insert into tbl_grimg(imgPath,tag,growthRecordId,upTime) values (?,?,?,?)");
			preparedStatement.setObject(1,grimg.getImgPath());
			preparedStatement.setObject(2,grimg.getTag());
			preparedStatement.setObject(3, grimg.getGrowthRecordId());
			preparedStatement.setObject(4, grimg.getUpTime());
			count =  preparedStatement.executeUpdate();
			/*if(count !=0){
				//获取新插入的ID值
				PreparedStatement pstm = con.prepareStatement("select MAX(id) from tbl_growthrecord");
				rs=pstm.executeQuery();
				if(rs.next()){
					id = rs.getInt(1);  
				}
			}*/
			return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
		//return id;
	}
}
