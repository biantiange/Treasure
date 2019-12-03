package Post.service;

import java.util.List;
import java.util.Map;

import Post.dao.PostDaolmpl;
import entity.Post;

public class PostServicelmpl {
	public List<Map<String,Object>> listPost(){
		return new PostDaolmpl().findAll();
	}

	public boolean addPost(Post post) {
		int count = new PostDaolmpl().savePost(post);
		return count>0;
	}
}
