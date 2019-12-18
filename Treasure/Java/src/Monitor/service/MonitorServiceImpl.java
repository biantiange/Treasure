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
	public int EditIsRegistChildById(int childId) {
		return new MonitorDaoImpl().EditIsRegistChildById(childId);
	}
	public int EditDeviceIdChildById(String deviceId,int childId) {
		return new MonitorDaoImpl(). EditDeviceIdChildById(deviceId,childId);
	}
	public int EditDeviceIdParentById(String deviceId,int parentId) {
		return new MonitorDaoImpl(). EditDeviceIdParentById(deviceId,parentId);
	}
	
	public List<Parent> getParentIdByChildId(int childId){
		return new MonitorDaoImpl().findParentIdByChildId(childId);
	}
	
	public List<Child> getChildByParentPhoneNumberAndChildName(String phoneNumber,String childName){
		return new MonitorDaoImpl().findChildByParentPhoneNumberAndChildName(phoneNumber, childName);
	}

}
