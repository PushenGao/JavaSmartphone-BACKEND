package dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.AccountPO;
import model.Account;
import model.ChatRecord;
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
	
	public void closeConnection(){
		try {
			this.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//create two tables in the database
	public void checkTable() {
		try {
			Statement stmt = con.createStatement();
			String createTB1 = SQL.CREATE_BASE_INFO;
			String createTB2 = SQL.CREATE_CONTACT_INFO;
			String createTB3 = SQL.CREATE_CHAT_INFO;
			stmt.executeUpdate(createTB1);
			stmt.executeUpdate(createTB2);
			stmt.executeUpdate(createTB3);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//return a user accountdao to the front end
	@Override
	public AccountPO getUserInfo(String userid, String password) {
		AccountPO userAccount = new AccountPO();
		try {
			PreparedStatement stmt = con.prepareStatement(SQL.GET_USER_INFO);
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(!rs.getString(3).equals(password))
					return userAccount;
				userAccount.getAccount().setId(rs.getString(1));
				userAccount.getAccount().setName(rs.getString(2));
				userAccount.setPassword(rs.getString(3));
				userAccount.getAccount().setAge(rs.getString(4));
				userAccount.getAccount().setGender(rs.getString(5));
				userAccount.getAccount().getHistory().setLast_location(rs.getString(6));
				userAccount.getAccount().getHistory().setTotalDistance(rs.getString(7));
				userAccount.getAccount().getHistory().setTotalTime(rs.getString(8));
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
	
	//get all chat record of this user 
	@Override
	public List<ChatRecord> getAndDeleteChatRecord(String userid) {
		List<ChatRecord> chatRecords = new ArrayList<ChatRecord>();
		try{
			PreparedStatement stmt = con.prepareStatement(SQL.GET_CHAT_INFO);
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ChatRecord chatRec = new ChatRecord();
				chatRec.setMyName(rs.getString(2));
				chatRec.setFriendName(rs.getString(3));
				chatRec.setTimeStamp(rs.getString(4));
				chatRec.setChatContent(rs.getString(5));
				chatRecords.add(chatRec);
			}
			rs.close();
			
			stmt = con.prepareStatement(SQL.DELETE_CHAT);
			stmt.setString(1, userid);
			stmt.executeUpdate();
			stmt.close();
			
		}catch(SQLException e ){
			e.printStackTrace();
		}
		return chatRecords;
	}
	
	
	//algorithm to compute difference of distance then return recommend
	@Override
	public List<String> getRecommendFriend(String userid) {
		String selfLocation = "";
		String otherLocation = "";
		List<String> apos = new ArrayList<String>();
		try{
			PreparedStatement stmt = con.prepareStatement(SQL.GET_USER_INFO);
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				selfLocation = rs.getString(6);
			}
			rs.close();
			if(selfLocation.trim().equals(""))
				return apos;
			
			List<String> existingFriends = this.getExistingFriend(userid);
			stmt = con.prepareStatement(SQL.GET_USERS);
			ResultSet rs1 = stmt.executeQuery();
			while(rs1.next()){
				if(!rs1.getString(1).equals(userid)){
					if(existingFriends.contains(rs1.getString(1)))
						continue;
					otherLocation = rs1.getString(6);
					if(otherLocation.trim().equals(""))
						continue;
					Account apo = this.checkDistance(selfLocation, otherLocation,rs1.getString(1));
					if(apo.getId() != null)
						apos.add(apo.toString());						
				}
			}
			int recSize = 2;
			if(apos.size() > recSize){
				Long seed = System.currentTimeMillis();
				Random rand = new Random(seed);
				List<String> aposReduce = new ArrayList<String>();
				
				List<Integer> recRandomList = new ArrayList<Integer>();
				for(int i = 0 ; i < recSize; i++){
					int num;
					do{
						num = rand.nextInt(apos.size());
					}while(recRandomList.contains(num));
					recRandomList.add(num);
				}
				
				for(int i = 0 ; i <recRandomList.size(); i++){					
					aposReduce.add(apos.get(recRandomList.get(i)));
				}
				return aposReduce;
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
	public void saveUserInfo(AccountPO account) {
		try (
			PreparedStatement stmt = con.prepareStatement(SQL.INSERT_BASE)) {
			stmt.setString(1, account.getAccount().getId());
			stmt.setString(2, account.getAccount().getName());
			stmt.setString(3, account.getPassword());
			stmt.setString(4, account.getAccount().getAge());
			stmt.setString(5, account.getAccount().getGender());
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
				stmt.setString(1, friend.getReceiverId());
				stmt.setString(2, friend.getSenderId());
				stmt.executeUpdate();
				
				stmt = con.prepareStatement(SQL.INSERT_CONTACT);
				stmt.setString(1, friend.getSenderId());
				stmt.setString(2, friend.getReceiverId());
				stmt.setString(3, "active");
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
	
	@Override
	public void saveChatInfo(ChatRecord chatRec) {
		try (PreparedStatement stmt = con.prepareStatement(SQL.INSERT_CHAT)) {
			stmt.setString(1, chatRec.getMyName());
			stmt.setString(2, chatRec.getFriendName());
			stmt.setString(3, chatRec.getTimeStamp());
			stmt.setString(4, chatRec.getChatContent());
			stmt.executeUpdate();
			stmt.close();			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
	//get account info the user has searched
	@Override
	public Account getSearchUser(String userid) {
		Account a = new Account();
		try {
			PreparedStatement stmt = con.prepareStatement(SQL.GET_USER_INFO);
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				a.setAge(rs.getString(4));
				a.setGender(rs.getString(5));
				a.setId(rs.getString(1));
				a.setName(rs.getString(2));
				a.getHistory().setLast_location(rs.getString(6));
				a.getHistory().setTotalDistance(rs.getString(7));
				a.getHistory().setTotalTime(rs.getString(8));
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return a;
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
	
	private Account checkDistance(String location1, String location2, String userId){
		Account a = new Account();
		double distance = DistanceUtil.getDistance(location1, location2);
		if(distance < 3000){
			try{
				PreparedStatement stmt = con.prepareStatement(SQL.GET_USER_INFO);
				stmt.setString(1, userId);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
					a.setAge(rs.getString(4));
					a.setGender(rs.getString(5));
					a.setId(rs.getString(1));
					a.setName(rs.getString(2));
					a.getHistory().setLast_location(rs.getString(6));
					a.getHistory().setTotalDistance(rs.getString(7));
					a.getHistory().setTotalTime(rs.getString(8));
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		return a;
	}
	
	//get the information of user contacts, divided by status 
	private List<Account> getContacts(String userId, String status){
		List<Account> accounts = new ArrayList<Account>();
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
					Account a = new Account();
					a.setAge(rs1.getString(4));
					a.setGender(rs1.getString(5));
					a.setId(rs1.getString(1));
					a.setName(rs1.getString(2));
					a.getHistory().setLast_location(rs1.getString(6));
					a.getHistory().setTotalDistance(rs1.getString(7));
					a.getHistory().setTotalTime(rs1.getString(8));
					accounts.add(a);
				}
			}
		}catch(SQLException e ){
			e.printStackTrace();
		}
		return accounts;
	}
	
	private List<String> getExistingFriend(String userid) {
		List<String> friendids = new ArrayList<String>();
		try
		{
			PreparedStatement stmt = con.prepareStatement(SQL.GET_FRIENDS);
			stmt.setString(1, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String accountid = rs.getString(3);
				friendids.add(accountid);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return friendids;
	}

}
