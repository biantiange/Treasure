package Post.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import entity.Post;
import mine.MineDBUtil;
import util.DBUtil;

public class PostDao {
	public List<Map<String,Object>> findAll(){
		return DBUtil.findAll("select * from tbl_post order by time desc limit 0,5");
	}
	public List<Map<String,Object>> findMyAll(int id){
		return MineDBUtil.findAll("select * from tbl_post where posterId=? order by time desc limit 0,5",new Object[]{id});
	}
	
	public List<Map<String,Object>> findMyAll(int id,int page){
		return DBUtil.findAll("select * from tbl_post where posterId="+id+" order by time desc limit "+page+",5");

	}
	
	public List<Map<String,Object>> findAll(int start){
		return DBUtil.findAll("select * from tbl_post order by time desc limit "+start+",5");
	}

	public int savePost(Post post) {
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date(time));
		return DBUtil.executeUpdate("insert into tbl_post(content,time,praiseCount,posterId)"
				+ " values(?,?,?,?)"
				,new Object[] {post.getContent(),now,post.getPraiseCount(),post.getPosterId()});
	}
	public int addPraise(int postId,int count) {
		return DBUtil.executeUpdate("UPDATE tbl_post SET praiseCount = ? WHERE id = ?"
				, new Object[] {count,postId});
	}
	
	public Post maxId(){
		return DBUtil.findMaxId();
	}
}
