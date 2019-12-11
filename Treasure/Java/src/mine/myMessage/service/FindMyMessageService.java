package mine.myMessage.service;

import java.util.List;
import java.util.Map;

import mine.editUser.dao.FindUserDao;
import mine.myMessage.dao.FindMyMessageDao;

public class FindMyMessageService {
	public List<Map<String,Object>> listMessage(int id) {
		return new FindMyMessageDao().findCommentTome(id);
	}
	public List<Map<String,Object>> listUserMessage(int resId) {
		return new FindMyMessageDao().findMycomment(resId);
	}


}
