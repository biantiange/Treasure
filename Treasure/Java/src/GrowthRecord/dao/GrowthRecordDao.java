package GrowthRecord.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.GrowthRecord;
import util.DBUtil;

public class GrowthRecordDao {
	public int addGrowthRecord(GrowthRecord growthRecord){
		Connection con = DBUtil.getCon();
		PreparedStatement preparedStatement=null;
		ResultSet rs = null;
		int count = 0;
		int id=0;
		try {
			preparedStatement = con.prepareStatement("insert into tbl_growthrecord(upTime,parentId,content) values (?,?,?)");
			preparedStatement.setObject(1,growthRecord.getUpTime());
			preparedStatement.setObject(2,growthRecord.getParentId());
			preparedStatement.setObject(3, growthRecord.getContent());
			count =  preparedStatement.executeUpdate();
			if(count !=0){
				//获取新插入的ID值
				PreparedStatement pstm = con.prepareStatement("select MAX(id) from tbl_growthrecord");
				rs=pstm.executeQuery();
				if(rs.next()){
					id = rs.getInt(1);  
				}
			}
			//return count;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
}
