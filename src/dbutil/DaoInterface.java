package dbutil;

import java.util.List;

import model.AccountPO;
import model.Account;
import model.FriendRequest;
import model.HistoryRecord;

public interface DaoInterface {

	public AccountPO getUserInfo(String userid, String password);
	public List<String> getRecommendFriend(String userid);
	public List<String> getExistingUserid();
	public Account getSearchUser(String userid);
	public void saveUserInfo(AccountPO account);
	public void saveRunningRecord(HistoryRecord history);
	public void savefriendFequests(FriendRequest friend);
	
}
