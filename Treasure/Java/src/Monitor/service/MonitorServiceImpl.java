package Monitor.service;

import java.util.List;
import java.util.Map;

import Monitor.dao.MonitorDaoImpl;
import entity.Child;
import entity.Parent;
import mine.child.dao.ChildDao;
import util.DBUtil;

public class MonitorServiceImpl {
	public List<Child> listChildByParentId(int parentId) {
		return new MonitorDaoImpl().findChildByParentId(parentId);
	}
	
	public List<Parent> getParentIdByChildId(int childId){
		return new MonitorDaoImpl().findParentIdByChildId(childId);
	}
	
	public List<Child> getChildByParentPhoneNumberAndChildName(String phoneNumber,String childName){
		return new MonitorDaoImpl().findChildByParentPhoneNumberAndChildName(phoneNumber, childName);
	}

}
