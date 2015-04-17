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

import dbutil.DaoImpl;
import model.AccountDAO;
import model.AccountPO;
import model.FriendRequest;
import model.HistoryRecord;


@Path("/werun")
public class ContactsService {

	@POST
	@Path("/register")
	@Produces(MediaType.TEXT_HTML)
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String newAccount(AccountDAO contact) throws IOException {
		DaoImpl daoimpl = new DaoImpl();
		List<String> existings = daoimpl.getExistingUserid();
		if(!contact.getId().trim() .equals("") && !existings.contains(contact.getId().trim())){
			daoimpl.saveUserInfo(contact);
			return "success";
		}
		else
			return "failed";
	}
	
	@GET
	@Path("/login/{userId}/{passWord}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public AccountDAO getUserInfo(@PathParam("userId") String userId,
			@PathParam("passWord") String password) {
		DaoImpl daoimpl = new DaoImpl();
		AccountDAO user = new AccountDAO();
		user = daoimpl.getUserInfo(userId, password);
		return user;
	}
	
	@POST
	@Path("/record")
	@Produces(MediaType.TEXT_HTML)
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String updateRunningRecord(HistoryRecord history) throws IOException {
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
	public String friendRequest(FriendRequest friend) throws IOException {
		DaoImpl daoimpl = new DaoImpl();
		daoimpl.savefriendFequests(friend);
		return "success";		
	}
	
	@GET
	@Path("/recommend/{userId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<AccountPO> getRecommend(@PathParam("userId") String userid) {
		List<AccountPO> recommends = new ArrayList<AccountPO>();
		DaoImpl daoimpl = new DaoImpl();
		recommends = daoimpl.getRecommendFriend(userid);
		return recommends;
	}
	
	//search user service
	@GET
	@Path("/search/{userId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public AccountPO getSearchedUser(@PathParam("userId") String userid) {
		AccountPO searched = new AccountPO();
		DaoImpl daoimpl = new DaoImpl();
		searched = daoimpl.getSearchUser(userid);
		return searched;
	}
	

	
}
