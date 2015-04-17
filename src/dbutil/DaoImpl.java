package dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.AccountDAO;
import model.AccountPO;
import model.FriendRequest;
import model.HistoryRecord;
import sql.SQL;
import util.DistanceUtil;

public class DaoImpl implements DaoInterface{
	Connection con;
	public DaoImpl() {
		con = DBUtil.getConnection();
		this.checkTable();
	}
	
	//create two tables in the database
	public void checkTable() {
		try {
			Statement stmt = con.createStatement();
			String createTB1 = SQL.CREATE_BASE_INFO;
			String createTB2 = SQL.CREATE_CONTACT_INFO;
			stmt.executeUpdate(createTB1);
			stmt.executeUpdate(createTB2);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//return a user accountdao to the front end
	@Override
	public AccountDAO getUserInfo(String userid, String password) {
		AccountDAO userAccount = new AccountDAO();
		try {
			PreparedStatement stmt = con.prepareStatement(SQL.GET_USER_INFO);
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(!rs.getString(3).equals(password))
					return userAccount;
				userAccount.setId(rs.getString(1));
				userAccount.setName(rs.getString(2));
				userAccount.setPassword(rs.getString(3));
				userAccount.setAge(rs.getString(4));
				userAccount.setGender(rs.getString(5));
				userAccount.getHistory().setLast_location(rs.getString(6));
				userAccount.getHistory().setTotalDistance(rs.getString(7));
				userAccount.getHistory().setTotalTime(rs.getString(8));
			}
			rs.close();
			userAccount.setActiveFriends(this.getContacts(userid, "active"));
			userAccount.setPendingFriends(this.getContacts(userid, "pending"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userAccount;
	}
		    
	
	
	//algorithm to compute difference of distance then return recommend
	@Override
	public List<AccountPO> getRecommendFriend(String userid) {
		String selfLocation = "";
		String otherLocation = "";
		List<AccountPO> apos = new ArrayList<AccountPO>();
		try{
			PreparedStatement stmt = con.prepareStatement(SQL.GET_USER_INFO);
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				selfLocation = rs.getString(6);
			}
			rs.close();
			stmt = con.prepareStatement(SQL.GET_USERS);
			ResultSet rs1 = stmt.executeQuery();
			while(rs1.next()){
				if(!rs1.getString(1).equals(userid)){
					otherLocation = rs1.getString(6);
					AccountPO apo = this.checkDistance(selfLocation, otherLocation,rs1.getString(1));
					if(apo.getId() != null)
						apos.add(apo);						
				}
			}
		}catch(SQLException e ){
			e.printStackTrace();
		}
		return apos;
	}		
	
	//return a list of existing user and check to see if the register's userid is repeated
	@Override
	public List<String> getExistingUserid() {
		List<String> userids = new ArrayList<String>();
		try
		{
			PreparedStatement stmt = con.prepareStatement(SQL.GET_USERS);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String accountid = rs.getString(1);
				userids.add(accountid);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return userids;
	}

	//save the registered user info into db
	@Override
	public void saveUserInfo(AccountDAO account) {
		try (
			PreparedStatement stmt = con.prepareStatement(SQL.INSERT_BASE)) {
			stmt.setString(1, account.getId());
			stmt.setString(2, account.getName());
			stmt.setString(3, account.getPassword());
			stmt.setString(4, account.getAge());
			stmt.setString(5, account.getGender());
			stmt.setString(6, "");
			stmt.setString(7, "");
			stmt.setString(8, "");
			stmt.executeUpdate();
			stmt.close();			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	//update the running record into db
	@Override
	public void saveRunningRecord(HistoryRecord history) {
		try (PreparedStatement stmt = con.prepareStatement(SQL.UPDATE_RUNING_RECORD)) {
				stmt.setString(1, history.getLast_location());
				stmt.setString(2, history.getTotalDistance());
				stmt.setString(3, history.getTotalTime());
				stmt.setString(4, history.getUserId());
				stmt.executeUpdate();
				stmt.close();			
			} catch (SQLException e) {
				System.out.println(e);
			}
		
	}
	//update the friend request into db
	@Override
	public void savefriendFequests(FriendRequest friend) {
		int[] ifExists = this.checkContact(friend);
		try{
			
			if(friend.getAction().equals("add")){
				PreparedStatement stmt = con.prepareStatement(SQL.UPDATE_CONTACT);
				stmt.setString(1, friend.getSenderId());
				stmt.setString(2, friend.getReceiverId());
				stmt.executeUpdate();
				
				stmt.setString(1, friend.getReceiverId());
				stmt.setString(2, friend.getSenderId());
				stmt.executeUpdate();
				stmt.close();
			}
			else if(friend.getAction().equals("delete")){
				PreparedStatement stmt = con.prepareStatement(SQL.DELETE_CONTACT);
				stmt.setString(1, friend.getSenderId());
				stmt.setString(2, friend.getReceiverId());
				stmt.executeUpdate();
				
				stmt.setString(1, friend.getReceiverId());
				stmt.setString(2, friend.getSenderId());
				stmt.executeUpdate();
				stmt.close();
			}else if(friend.getAction().equals("request")){
				if(ifExists[0] == 0){
					PreparedStatement stmt = con.prepareStatement(SQL.INSERT_CONTACT);
					stmt.setString(1, friend.getSenderId());
					stmt.setString(2, friend.getReceiverId());
					stmt.setString(3, "pending");
					stmt.executeUpdate();
					stmt.close();
				}
				if(ifExists[1] == 0){
					PreparedStatement stmt = con.prepareStatement(SQL.INSERT_CONTACT);
					stmt.setString(1, friend.getReceiverId());
					stmt.setString(2, friend.getSenderId());
					stmt.setString(3, "pending");
					stmt.executeUpdate();
					stmt.close();
				}
			}else if(friend.getAction().equals("reject")){
				PreparedStatement stmt = con.prepareStatement(SQL.REJECT_CONTACT);
				stmt.setString(1, friend.getSenderId());
				stmt.setString(2, friend.getReceiverId());
				stmt.executeUpdate();
				
				stmt.setString(1, friend.getReceiverId());
				stmt.setString(2, friend.getSenderId());
				stmt.executeUpdate();
				stmt.close();
			}else{
				return;
			}
		}catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
	//get account info the user has searched
	@Override
	public AccountPO getSearchUser(String userid) {
		AccountPO apo = new AccountPO();
		try {
			PreparedStatement stmt = con.prepareStatement(SQL.GET_USER_INFO);
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				apo.setAge(rs.getString(4));
				apo.setGender(rs.getString(5));
				apo.setId(rs.getString(1));
				apo.setName(rs.getString(2));
				apo.getRecord().setLast_location(rs.getString(6));
				apo.getRecord().setTotalDistance(rs.getString(7));
				apo.getRecord().setTotalTime(rs.getString(8));
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return apo;
	}
	
	//check if the friend already exists in the db
	private int[] checkContact(FriendRequest friend){
		int[] ifExists = new int[2];
		try
		{
			PreparedStatement stmt = con.prepareStatement(SQL.CHECK_IF_CONTACT_EXISTS);
			stmt.setString(1, friend.getSenderId());
			stmt.setString(2, friend.getReceiverId());
			ResultSet rs1 = stmt.executeQuery();
			if(rs1.next())
				ifExists[0] = 1;
			rs1.close();
			
			stmt.setString(1, friend.getReceiverId());
			stmt.setString(2, friend.getSenderId());
			ResultSet rs2 = stmt.executeQuery();
			if(rs2.next())
				ifExists[1] = 1;
			rs2.close();
		
		} catch (SQLException e) {
			System.out.println(e);
		}
		return ifExists;
	}
	
	private AccountPO checkDistance(String location1, String location2, String userId){
		AccountPO apo = new AccountPO();
		double distance = DistanceUtil.getDistance(location1, location2);
		if(distance < 3000){
			try{
				PreparedStatement stmt = con.prepareStatement(SQL.GET_USER_INFO);
				stmt.setString(1, userId);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
					apo.setAge(rs.getString(4));
					apo.setGender(rs.getString(5));
					apo.setId(rs.getString(1));
					apo.setName(rs.getString(2));
					apo.getRecord().setLast_location(rs.getString(6));
					apo.getRecord().setTotalDistance(rs.getString(7));
					apo.getRecord().setTotalTime(rs.getString(8));
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		return apo;
	}
	
	//get the information of user contacts, divided by status 
	private List<AccountPO> getContacts(String userId, String status){
		List<AccountPO> pos = new ArrayList<AccountPO>();
		List<String> names = new ArrayList<String>();
		try{
			PreparedStatement stmt = con.prepareStatement(SQL.GET_CONTACT_INFO);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(rs.getString(4).equals(status))
					names.add(rs.getString(3));
			}
			rs.close();
			stmt.close();
			
			for(int i = 0 ; i < names.size(); i++){
				PreparedStatement stmt1 = con.prepareStatement(SQL.GET_USER_INFO);				
				stmt1.setString(1, names.get(i));
				ResultSet rs1 = stmt1.executeQuery();
				while(rs1.next()){
					AccountPO apo = new AccountPO();
					apo.setAge(rs1.getString(4));
					apo.setGender(rs1.getString(5));
					apo.setId(rs1.getString(1));
					apo.setName(rs1.getString(2));
					apo.getRecord().setLast_location(rs1.getString(6));
					apo.getRecord().setTotalDistance(rs1.getString(7));
					apo.getRecord().setTotalTime(rs1.getString(8));
					pos.add(apo);
				}
			}
		}catch(SQLException e ){
			e.printStackTrace();
		}
		return pos;
	}


	

}
