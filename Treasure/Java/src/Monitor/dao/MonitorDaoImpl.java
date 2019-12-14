package Monitor.dao;

import java.util.List;
import java.util.Map;

import entity.Child;
import util.DBUtil;

public class MonitorDaoImpl {
	// 根据父母Id查找孩子
	public List<Child> findChildByParentId(int parentId) {
		return DBUtil.find(Child.class, "select * from tbl_children where id=?", new Object[] {parentId});
	}
}
