package model;

import com.google.gson.Gson;

public class Account {
	private String id;
	private String name;
	private String age;
	private String gender;
	private HistoryRecord historyRecord = new HistoryRecord();
	
	public HistoryRecord getHistory() {
		return historyRecord;
	}
	public void setHistory(HistoryRecord record) {
		this.historyRecord = record;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
