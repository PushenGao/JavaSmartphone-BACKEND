package test;

import model.HistoryRecord;
import dbutil.DaoImpl;

public class TestUpdateRunningRecord {
	public static void main(String args[]){
		DaoImpl daoImpl = new DaoImpl();
		HistoryRecord history = new HistoryRecord();
		history.setLast_location("120.55,80.46");
		history.setTotalDistance("2400");
		history.setUserId("zhengl");
		history.setTotalTime("3100");
		daoImpl.saveRunningRecord(history);
	}
}
