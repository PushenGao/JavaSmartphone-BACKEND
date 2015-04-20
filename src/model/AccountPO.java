package model;

import java.util.List;

import com.google.gson.Gson;

public class AccountPO {
	private String password;
	private Account account = new Account();
	private List<Account> pendingFriends;
	private List<Account> activeFriends;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

List<Account> getPendingFriends() {
		return pendingFriends;
	}

	public void setPendingFriends(List<Account> pendingFriends) {
		this.pendingFriends = pendingFriends;
	}

	public List<Account> getActiveFriends() {
		return activeFriends;
	}

	public void setActiveFriends(List<Account> activeFriends) {
		this.activeFriends = activeFriends;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
