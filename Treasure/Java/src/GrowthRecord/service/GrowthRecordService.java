package GrowthRecord.service;

import java.util.List;
import GrowthRecord.dao.GrowthRecordDao;
import entity.GrowthRecord;

public class GrowthRecordService {
	
	public int addGrowthRecord(GrowthRecord growthRecord){
		return new GrowthRecordDao().addGrowthRecord(growthRecord);
	}
	
	public List<String> listTimes(){
		return new  GrowthRecordDao().findTime();
	}
}
