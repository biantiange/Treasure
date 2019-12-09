package Grimg.service;

import Grimg.dao.GrimgDao;
import entity.Grimg;

public class GrimgService {
	public int addGrimgPath(String path){
		return new GrimgDao().addGrimgPath(path);
	}
	public int addGrimgOther(Grimg grimg){
		return new GrimgDao().addGrimgOther(grimg);
	}
}
