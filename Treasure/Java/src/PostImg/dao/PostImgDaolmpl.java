package PostImg.dao;

import java.util.List;
import java.util.Map;

import entity.PostImg;
import util.DBUtil;

public class PostImgDaolmpl {
	public List<Map<String,Object>> findAll(){
		return DBUtil.findAll("select * from tbl_postImg");
	}

	public int savePostImg(PostImg img) {
		return DBUtil.executeUpdate("insert into tbl_postImg(path,postId,time) "
				+ "values(?,?,?)"
				,new Object[] {img.getPath(),img.getPostId(),img.getTime()});
	}
}
