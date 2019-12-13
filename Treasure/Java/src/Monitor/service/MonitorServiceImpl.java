package Monitor.service;

import java.util.List;
import java.util.Map;

import Monitor.dao.MonitorDaoImpl;
import entity.Child;
import mine.child.dao.ChildDao;
import util.DBUtil;

public class MonitorServiceImpl {
	public List<Child>listChildByParentId(int parentId) {
		return new MonitorDaoImpl().findChildByParentId(parentId);
	}

}
