package mine.child.service;
import java.util.List;
import java.util.Map;

import entity.Child;
import mine.child.dao.ChildDao;
public class ChildService {
	public List<Child> listAllChilds(){	
		return new ChildDao().findAllChilds();
	}
	public boolean addChild (int parentId,String birthday,String imgpath,String nickname) {
		System.out.print("service");
		int count1 = new ChildDao().saveChild(parentId, birthday, imgpath, nickname);
		return count1 >0;
	}
	
	public boolean dropChild(int id) {
		int count=new ChildDao().deleteChild(id);
		return count>0;
		
	}
	public boolean editChild (int id,String nickname,String birthday,String imgpath,int parentId) {
		int count = new ChildDao().updataChild(id,nickname,birthday,imgpath,parentId);
		return count >0;
	}
	
	public List<Map<String,Object>> listChilds(String pi) {
		return new ChildDao().findAll(pi);
	}
	
}
