package mine.child.dao;

import java.util.List;
import java.util.Map;


import entity.Child;
import mine.MineDBUtil;

public class ChildDao {
	public List<Map<String,Object>> findAll(String pi) {
		if(pi!=null){
			int p=Integer.parseInt(pi);
		
		return MineDBUtil.findAll("select * from tbl_children where parentId=?",new Object[]{p});
		}
		else {
			return null;
		}
	}
	public List<Child> findAllChilds(){
		return MineDBUtil.find(Child.class, "select * from tbl_children", null);
	} 
	public int saveChild(int parentId,int birthday,String imgpath,String nickname) {
		System.out.print("dao");
		return MineDBUtil.executeUpdate(
				"insert into tbl_children(name,age,headerPath,parentId) values(?,?,?,?)"
				, new Object[] {nickname,birthday,imgpath,parentId});
		
	}

	public Child findById(int id) {
		Object object = MineDBUtil.findById(Child.class,id);
		return object != null ? (Child)object : null;
	}
	
	public int  updataChild(int id,String nickname,int birthday,String imgpath,int parentId) {
		return MineDBUtil.executeUpdate(
				"update tbl_children set name=?,age=?,headerPath=? where id=? and parentId=?",new Object[]{nickname,birthday,imgpath,id,parentId});
		
	}
	
	public int deleteChild(int id) {
		return MineDBUtil.executeUpdate("delete from tbl_children where id=?",new Object[] {id});
		
	}
}
