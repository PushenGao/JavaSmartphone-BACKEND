package dbutil;

import java.util.List;

import model.AccountDAO;
import model.AccountPO;
import model.FriendRequest;
import model.HistoryRecord;

public interface DaoInterface {

	public AccountDAO getUserInfo(String userid, String password);
	public List<AccountPO> getRecommendFriend(String userid);
	public List<String> getExistingUserid();
	public AccountPO getSearchUser(String userid);
	public void saveUserInfo(AccountDAO account);
	public void saveRunningRecord(HistoryRecord history);
	public void savefriendFequests(FriendRequest friend);
	
}
