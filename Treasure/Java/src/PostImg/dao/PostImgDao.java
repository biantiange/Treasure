package PostImg.dao;

import java.util.List;
import java.util.Map;

import entity.PostImg;
import util.DBUtil;

public class PostImgDao {
	public List<Map<String,Object>> findAll(){
		return DBUtil.findAll("select * from tbl_postImg");
	}
	
	public List<Map<String,Object>> findAll(int postId){
		return DBUtil.findAll("select * from tbl_postImg where postId = '"+postId+"'");
	}

	public int savePostImg(PostImg img) {
		return DBUtil.executeUpdate("insert into tbl_postImg(path,postId,time) "
				+ "values(?,?,?)"
				,new Object[] {img.getPath(),img.getPostId(),img.getTime()});
	}
}
