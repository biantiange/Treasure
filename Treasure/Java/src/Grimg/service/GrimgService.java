package Grimg.service;

import java.util.List;
import java.util.Map;

import Grimg.dao.GrimgDao;
import entity.Grimg;

public class GrimgService {
	public int addGrimgPath(String path){
		return new GrimgDao().addGrimgPath(path);
	}
	public int addGrimgOther(Grimg grimg){
		return new GrimgDao().addGrimgOther(grimg);
	}
	
	public List<Map<String,Object>> listPictureByTime(String time){
		return new  GrimgDao().findPictureByTime(time);
	}
	public List<Map<String,Object>> listContentByTime(String time){
		return new  GrimgDao().findContentByTime(time);
	}
}
