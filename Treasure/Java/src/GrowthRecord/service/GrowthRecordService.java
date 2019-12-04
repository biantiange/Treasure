package GrowthRecord.service;

import GrowthRecord.dao.GrowthRecordDao;
import entity.GrowthRecord;

public class GrowthRecordService {
	
	public int addGrowthRecord(GrowthRecord growthRecord){
		return new GrowthRecordDao().addGrowthRecord(growthRecord);
	}
}
