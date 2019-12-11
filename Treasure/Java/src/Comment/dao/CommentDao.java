package Comment.dao;

import java.util.List;
import java.util.Map;

import entity.Comment;
import util.DBUtil;

public class CommentDao {
	public List<Map<String,Object>> findAll(int postId){
		return DBUtil.findAll("select * from tbl_comment where postId ="+postId);
	}
	//查出前三条评论（根据时间先后顺序）
	public List<Map<String,Object>> findAll_3(int postId){
		return DBUtil.findAll("select * from tbl_comment where postId = '"+postId+"' order by time limit 0,3 ");
	}
	public Comment findById(int commentId) {
		return (Comment) DBUtil.findById(Comment.class, commentId);
	}
	public int AddComment(Comment comment) {
		return DBUtil.executeUpdate("insert into tbl_comment(postId,commentatorId,resComId,responderId,content,time) "
				+ "values(?,?,?,?,?,?)"
				, new Object[] {comment.getPostId(),comment.getCommentatorId(),comment.getResComId(),comment.getResponderId(),comment.getContent(),comment.getTime()});
	}
}
