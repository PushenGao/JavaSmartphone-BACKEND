package model;

import java.util.List;

public class AccountDAO {
	private String id;
	private String name;
	private String password;
	private String age;
	private String gender;	
	private HistoryRecord history = new HistoryRecord();
	private List<AccountPO> pendingFriends;
	private List<AccountPO> activeFriends;
	
	
	public List<AccountPO> getPendingFriends() {
		return pendingFriends;
	}

	public void setPendingFriends(List<AccountPO> pendingFriends) {
		this.pendingFriends = pendingFriends;
	}

	public List<AccountPO> getActiveFriends() {
		return activeFriends;
	}

	public void setActiveFriends(List<AccountPO> activeFriends) {
		this.activeFriends = activeFriends;
	}

	public HistoryRecord getHistory() {
		return history;
	}

	public void setHistory(HistoryRecord history) {
		this.history = history;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

}
