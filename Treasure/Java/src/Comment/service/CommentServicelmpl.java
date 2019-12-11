package Comment.service;

import java.util.List;
import java.util.Map;

import Comment.dao.CommentDao;



public class CommentServicelmpl {
	//查出所有Comment
	public List<Map<String,Object>> listComment(){
		return new CommentDao().findAll();
	}
	//查出前三条评论
	public List<Map<String,Object>> listComment_3(int postId){
		return new CommentDao().findAll_3(postId);
	}
}
