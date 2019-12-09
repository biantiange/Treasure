package Comment.dao;

import java.util.List;
import java.util.Map;

import util.DBUtil;

public class CommentDao {
	public List<Map<String,Object>> findAll(){
		return DBUtil.findAll("select * from tbl_comment");
	}
	//查出前三条评论（根据时间先后顺序）
	public List<Map<String,Object>> findAll_3(int postId){
		return DBUtil.findAll("select * from tbl_comment where postId = '"+postId+"' order by time limit 0,3 ");
	}
}
