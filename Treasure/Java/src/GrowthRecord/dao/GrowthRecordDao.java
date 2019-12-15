package GrowthRecord.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	//时间轴查找时间
	public  List<String> findTime() {
		Connection con = DBUtil.getCon();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			pstm = con.prepareStatement("select upTime from tbl_growthrecord");
			rs = pstm.executeQuery();
			while(rs.next()) {
				String upTime=rs.getString(1);
				String upTime1=upTime.substring(0,10);
				list.add(upTime1); 
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			DBUtil.close(rs,pstm,con);
		}

	}
	//根据标签搜索记录
		public List<Map<String,Object>> findByTag(String str,int parentId){
			Connection con = DBUtil.getCon();
			PreparedStatement preparedStatement=null;
			ResultSet rs = null;
			List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
			try {
				preparedStatement = con.prepareStatement("select tbl_growthRecord.content,tbl_growthRecord.upTime,tbl_grimg.imgPath,tbl_grimg.id,tbl_grimg.growthRecordId from tbl_grimg,tbl_growthRecord where tbl_grimg.growthRecordId=tbl_growthRecord.id and tbl_growthRecord.parentId=? and tbl_grimg.tag like ?");
				preparedStatement.setObject(1,parentId);
				preparedStatement.setObject(2,"%"+str+"%");
				rs =  preparedStatement.executeQuery();
				while(rs.next()){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("content",rs.getString(1));
					map.put("upTime", rs.getString(2));
					map.put("imgPath", rs.getString(3));
					map.put("id",rs.getInt(4));
					map.put("growthRecordId", rs.getInt(5));
					lists.add(map);
				}
				//return count;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return lists;
		}
		
}	
