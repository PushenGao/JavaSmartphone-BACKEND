package model;

import com.google.gson.Gson;

public class HistoryRecord {
	private String userId="";
	private String totalTime;
	private String totalDistance;
	private String lastLocation;
	
	public String getLast_location() {
		return lastLocation;
	}
	public void setLast_location(String last_location) {
		this.lastLocation = last_location;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
