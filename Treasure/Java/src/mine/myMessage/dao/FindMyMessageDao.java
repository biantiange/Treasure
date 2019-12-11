package mine.myMessage.dao;

import java.util.List;
import java.util.Map;
import mine.MineDBUtil;

public class FindMyMessageDao {
	
	public List<Map<String,Object>> findCommentTome(int id) {
		if(id!=0){
		return MineDBUtil.findAll("select * from tbl_comment where responderId=?",new Object[]{id});
		}
		else {
			return null;
		}
	}	
	public List<Map<String,Object>> findMycomment(int id) {
		if(id!=0){
		return MineDBUtil.findAll("select * from tbl_comment where id=?",new Object[]{id});
		}
		else {
			return null;
		}
	}	


}
