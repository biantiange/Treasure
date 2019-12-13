package Praise.dao;

import util.DBUtil;

public class PraiseDao {
	//判断是否点赞
	public int isPraise(int userId,int postId) {
		return DBUtil.findAll("select * from tbl_praise where praiserId = "+userId+" and postId = "+postId+"").size();
	}
	//点赞
	public int savePraise(int userId,int postId ) {
		return DBUtil.executeUpdate("insert into tbl_praise(praiserId,postId) values(?,?)", new Object[] {userId,postId});
	}
}
