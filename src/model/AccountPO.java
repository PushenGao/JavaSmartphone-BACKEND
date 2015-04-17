package model;

public class AccountPO {
	private String id;
	private String name;
	private String age;
	private String gender;
	private HistoryRecord record = new HistoryRecord();
	
	public HistoryRecord getRecord() {
		return record;
	}
	public void setRecord(HistoryRecord record) {
		this.record = record;
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
}
