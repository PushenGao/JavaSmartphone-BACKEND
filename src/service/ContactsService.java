package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dbutil.DaoImpl;
import model.AccountPO;
import model.Account;
import model.FriendRequest;
import model.HistoryRecord;


@Path("/werun")
public class ContactsService {

	@POST
	@Path("/register")
	@Produces(MediaType.TEXT_HTML)
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String newAccount(String accountPOJson) throws IOException, ParseException {
		JSONParser parser=new JSONParser();
		JSONObject objs =(JSONObject)parser.parse(accountPOJson);
		AccountPO accountpo = new AccountPO();
		JSONObject accountJson  = (JSONObject) objs.get("account");
		accountpo.getAccount().setId((String) accountJson.get("id"));
		accountpo.getAccount().setAge((String) accountJson.get("age"));
		accountpo.getAccount().setGender((String) accountJson.get("gender"));
		accountpo.setPassword((String) objs.get("password"));
		accountpo.getAccount().setName("");
		
		DaoImpl daoimpl = new DaoImpl();
		List<String> existings = daoimpl.getExistingUserid();
		if(!accountpo.getAccount().getId().equals("") && !existings.contains(accountpo.getAccount().getId())){
			daoimpl.saveUserInfo(accountpo);
			return "success";
		}
		else
			return "failed";
	}
	
	@GET
	@Path("/login/{userId}/{passWord}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String getUserInfo(@PathParam("userId") String userId,
			@PathParam("passWord") String password) {
		DaoImpl daoimpl = new DaoImpl();
		AccountPO user = new AccountPO();
		user = daoimpl.getUserInfo(userId, password);
		return user.toString();
	}
	
	@POST
	@Path("/record")
	@Produces(MediaType.TEXT_HTML)
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String updateRunningRecord(String historyString) throws IOException, ParseException {
				
		JSONParser parser=new JSONParser();
		JSONObject objs =(JSONObject)parser.parse(historyString);
		HistoryRecord history = new HistoryRecord();
		history.setUserId((String) objs.get("userId"));
		history.setTotalDistance((String) objs.get("totalDistance"));
		history.setTotalTime((String) objs.get("totalTime"));
		history.setLast_location((String) objs.get("lastLocation"));
		
		DaoImpl daoimpl = new DaoImpl();
		if(!history.getUserId().trim() .equals("")){
			daoimpl.saveRunningRecord(history);
			return "success";
		}
		else
			return "failed";
	}
	
	@POST
	@Path("/friquest")
	@Produces(MediaType.TEXT_HTML)
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String friendRequest(String friendJson) throws IOException, ParseException {
		JSONParser parser=new JSONParser();
		JSONObject objs =(JSONObject)parser.parse(friendJson);
		FriendRequest friend = new FriendRequest();
		friend.setAction((String) objs.get("action"));
		friend.setSenderId((String) objs.get("senderId"));
		friend.setReceiverId((String) objs.get("receiverId"));
		
		DaoImpl daoimpl = new DaoImpl();
		daoimpl.savefriendFequests(friend);
		return "success";		
	}
	
	@GET
	@Path("/recommend/{userId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String getRecommend(@PathParam("userId") String userid) {
		List<String> recommends = new ArrayList<String>();
		DaoImpl daoimpl = new DaoImpl();
		recommends = daoimpl.getRecommendFriend(userid);
		return recommends.toString();
	}
	
	//search user service
	@GET
	@Path("/search/{userId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String getSearchedUser(@PathParam("userId") String userid) {
		Account searched = new Account();
		DaoImpl daoimpl = new DaoImpl();
		searched = daoimpl.getSearchUser(userid);
		return searched.toString();
	}
	

	
}
