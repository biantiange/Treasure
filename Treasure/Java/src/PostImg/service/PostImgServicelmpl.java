package PostImg.service;

import java.util.List;
import java.util.Map;

import PostImg.dao.PostImgDao;
import entity.PostImg;

public class PostImgServicelmpl {
	public List<Map<String,Object>> listPostImg(){
		return new PostImgDao().findAll();
	}
	
	public List<Map<String,Object>> listPostImg(int postId){
		return new PostImgDao().findAll(postId);
	}
	
	public boolean addPostImg(PostImg img) {
		int count = new PostImgDao().savePostImg(img);
		return count>0;
	}
}
