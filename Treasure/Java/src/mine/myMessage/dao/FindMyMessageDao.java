package mine.myMessage.dao;

import java.util.List;
import java.util.Map;
import mine.MineDBUtil;

public class FindMyMessageDao {
	
	public List<Map<String,Object>> findCommentTome(int id) {
		if(id!=0){
		return MineDBUtil.findAll("select * from tbl_comment where responderId=? order by time desc",new Object[]{id});
		}
		else {
			return null;
		}
	}	
	public List<Map<String,Object>> findMycomment(int id) {
		if(id!=0){
		return MineDBUtil.findAll("select * from tbl_comment where id=?",new Object[]{id});
		}
		else {
			return null;
		}
	}	
	public List<Map<String,Object>> findPost(String postId) {
		if(postId!=null){
		return MineDBUtil.findAll("select * from tbl_post where id=?",new Object[]{postId});
		}
		else {
			return null;
		}
	}	
	public List<Map<String,Object>> findImg(String postId) {
		if(postId!=null){
		return MineDBUtil.findAll("select path from tbl_postimg where postId=?",new Object[]{postId});
		}
		else {
			return null;
		}
	}	


}
