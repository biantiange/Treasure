package Post.service;

import java.util.List;
import java.util.Map;

import Post.dao.PostDao;
import entity.Post;
import mine.MineDBUtil;

public class PostServicelmpl {
	public List<Map<String,Object>> listPost(){
		return new PostDao().findAll();
	}
	public List<Map<String,Object>> MylistPost(int id){
		return new PostDao().findMyAll(id);
	}
	public List<Map<String,Object>> MylistPost(int id,int page){
		System.out.println(page);
		return new PostDao().findMyAll(id,page);
	}


	public List<Map<String,Object>> listPost(int start){
		return new PostDao().findAll(start);
	}

	public boolean addPost(Post post) {
		int count = new PostDao().savePost(post);
		return count>0;
	}
}
