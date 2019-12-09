package Post.dao;

import java.util.List;
import java.util.Map;

import entity.Post;
import util.DBUtil;

public class PostDao {
	public List<Map<String,Object>> findAll(){
		return DBUtil.findAll("select * from tbl_post");
	}

	public int savePost(Post post) {
		// TODO Auto-generated method stub
		return DBUtil.executeUpdate("insert into tbl_post(content,time,praiseCount,posterId)"
				+ " values(?,?,?,?)"
				,new Object[] {post.getContent(),post.getTime(),post.getPraiseCount(),post.getPosterId()});
	}
}
