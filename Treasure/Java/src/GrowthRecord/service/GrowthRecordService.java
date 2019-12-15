package GrowthRecord.service;

import java.util.List;
import java.util.Map;

import GrowthRecord.dao.GrowthRecordDao;
import entity.GrowthRecord;

public class GrowthRecordService {
	
	public int addGrowthRecord(GrowthRecord growthRecord){
		return new GrowthRecordDao().addGrowthRecord(growthRecord);
	}
	
	public List<String> listTimes(){
		return new  GrowthRecordDao().findTime();
	}
	public List<Map<String,Object>> findByTag(String str,int parentId){
		return new GrowthRecordDao().findByTag(str, parentId);
	}
}
