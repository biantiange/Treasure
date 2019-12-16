package Monitor.dao;

import java.util.List;
import java.util.Map;
import com.mysql.jdbc.log.Log;

import entity.Child;
import entity.Parent;
import util.DBUtil;

public class MonitorDaoImpl {
	// 根据父母Id查找孩子
	public List<Child> findChildByParentId(int parentId) {
		return DBUtil.find(Child.class, "select * from tbl_children where parentId=?", new Object[] {parentId});
	}
	// 根据孩子Id查找孩子
	public int EditIsRegistChildById(int childId) {
		return DBUtil.executeUpdate("update tbl_children set isResign=? where id=?",new Object[] { "1", childId});
	}
	//根据孩子ID查找父母
	public List<Parent> findParentIdByChildId(int childId) {
		List<Child> childs= DBUtil.find(Child.class, "select * from tbl_children where id=?", new Object[] {childId});
		return DBUtil.find(Parent.class, "select * from tbl_parent where id=?", new Object[] {childs.get(0).getParentId()});
	}
	
	//根据家长的手机号和孩子姓名查询孩子的ID
	public List<Child> findChildByParentPhoneNumberAndChildName(String phoneNumber,String childName){
		List<Parent> parents=DBUtil.find(Parent.class, "select * from tbl_parent where phoneNumber=?", new Object[] {phoneNumber});
		if(parents!=null){
			System.out.println(parents.get(0).getId());
			return DBUtil.find(Child.class, "select * from tbl_children where parentId=? && name=?", new Object[] {parents.get(0).getId(),childName});
		}else{
			return null;
		}
	}
}
