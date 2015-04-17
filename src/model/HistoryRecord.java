package model;

public class HistoryRecord {
	private String userId="";
	private String totalTime;
	private String totalDistance;
	private String last_location;
	
	public String getLast_location() {
		return last_location;
	}
	public void setLast_location(String last_location) {
		this.last_location = last_location;
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
	
}
