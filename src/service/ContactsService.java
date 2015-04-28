package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import dbutil.DaoImpl;
import model.AccountPO;
import model.Account;
import model.ChatRecord;
import model.FriendRequest;
import model.HistoryRecord;


@Path("/werun")
public class ContactsService {

	//save the user base information into database when register.
	@POST
	@Path("/register")
	@Produces(MediaType.TEXT_HTML)
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String newAccount(String accountPOJson) throws IOException, ParseException {
		JSONParser parser=new JSONParser();
		JSONObject objs =(JSONObject)parser.parse(accountPOJson);
		AccountPO accountpo = new AccountPO();
		JSONObject accountJson  = (JSONObject) objs.get("basicAccount");
		accountpo.getAccount().setId((String) accountJson.get("name"));
		accountpo.getAccount().setAge((String) accountJson.get("age"));
		accountpo.getAccount().setGender((String) accountJson.get("gender"));
		accountpo.setPassword((String) objs.get("password"));
		accountpo.getAccount().setName("");
		
		DaoImpl daoimpl = new DaoImpl();
		List<String> existings = daoimpl.getExistingUserid();
		if(!accountpo.getAccount().getId().equals("") && !existings.contains(accountpo.getAccount().getId())){
			daoimpl.saveUserInfo(accountpo);
			daoimpl.closeConnection();
			return "success";
		}
		else{
			daoimpl.closeConnection();
			return "failed";
		}
			
	}
	
	//update the running record of users into database.
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
			daoimpl.closeConnection();
			return "success";
		}
		else{
			daoimpl.closeConnection();
			return "failed";
		}
			
	}
	
	//manage the CONTACT_INFO table based on the action in the object FriendRequest
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
		List<String> existings = daoimpl.getExistingUserid();
		if(existings.contains(friend.getReceiverId()) && existings.contains(friend.getSenderId())){
			daoimpl.savefriendFequests(friend);
			daoimpl.closeConnection();
			return "success";	
		}
		else{
			daoimpl.closeConnection();
			return "fail";
		}
	}
	
	//manage the CHAT_INFO table, act like a buffer table.
		@POST
		@Path("/chatup")
		@Produces(MediaType.TEXT_HTML)
		@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public String uploadChatRec(String chatJson) throws IOException, ParseException {
			JSONParser parser=new JSONParser();
			JSONObject objs =(JSONObject)parser.parse(chatJson);
				ChatRecord chatRec = new ChatRecord();
				chatRec.setMyName((String) objs.get("myName"));
				chatRec.setFriendName((String) objs.get("friendName"));
				chatRec.setTimeStamp((String) objs.get("timeStamp"));
				chatRec.setChatContent((String) objs.get("chatContent"));			
				DaoImpl daoimpl = new DaoImpl();
				daoimpl.saveChatInfo(chatRec);
				daoimpl.closeConnection();
				return "success";		
		}
	
	//upload an image from client to server, save it to the correct directory with correct name.
	//or delete the image after it is downloaded.
	@POST
    @Path("upload/{receiverId}/{action}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadFile(
    		@FormDataParam("file") InputStream uploadedInputStream, 
    		@FormDataParam("senderId") String  imgName,
    		@PathParam("receiverId") String directory,
    		@PathParam("action") String action,
    		@FormDataParam("file") FormDataContentDisposition fileDetail)  {
        OutputStream os = null;
        
        try {
        	File file =new File("/Users/JiateLi/Desktop/"+directory);  
            //if there is no such directory, create the directory
            if  (!file .exists()  && !file .isDirectory())    
            	file .mkdir(); 
           
        	String path = "/Users/JiateLi/Desktop/" + directory + "/" + imgName +".png";
            File fileUploaded = new File(path);
            if(action.equals("delete")){
            	fileUploaded.delete();
            	return;
            }
            os = new FileOutputStream(fileUploaded);
            byte[] b = new byte[2048];
            int length;
            while ((length = uploadedInputStream.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException ex) {
            
        } finally {
            try {
            	if(!action.equals("delete"))
            		os.close();
            } catch (IOException ex) {
                
            }
        }
	}
	
	
	//when the user login, return an AccountPO to the client including all the information
	@GET
	@Path("/login/{userId}/{passWord}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String getUserInfo(@PathParam("userId") String userId,
			@PathParam("passWord") String password) {
		DaoImpl daoimpl = new DaoImpl();
		AccountPO user = new AccountPO();
		user = daoimpl.getUserInfo(userId, password);
		daoimpl.closeConnection();
		return user.toString();
	}
	
	//get recommended friends with userid based on location
	@GET
	@Path("/recommend/{userId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String getRecommend(@PathParam("userId") String userid) {
		List<String> recommends = new ArrayList<String>();
		DaoImpl daoimpl = new DaoImpl();
		
		recommends = daoimpl.getRecommendFriend(userid);
		daoimpl.closeConnection();
		return recommends.toString();
	}
	
	//search user based on user id
	@GET
	@Path("/search/{userId}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String getSearchedUser(@PathParam("userId") String userid) {
		Account searched = new Account();
		DaoImpl daoimpl = new DaoImpl();
		searched = daoimpl.getSearchUser(userid);
		daoimpl.closeConnection();
		return searched.toString();
	}
	
	//get recommended friends with userid based on location
		@GET
		@Path("/getChat/{userId}")
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public String getChatInfo(@PathParam("userId") String userid) {
			List<ChatRecord> chatRecords = new ArrayList<ChatRecord>();
			List<String> chatRecordsString = new ArrayList<String>();
			DaoImpl daoimpl = new DaoImpl();
			chatRecords = daoimpl.getAndDeleteChatRecord(userid);
			for(int i= 0 ; i < chatRecords.size(); i++){
				chatRecordsString.add(chatRecords.get(i).toString());
			}
			daoimpl.closeConnection();
			return chatRecordsString.toString();
		}
	
	
	//get ONE image based on the receiver id.
	@GET
	@Path("/getImage/{receiverId}")
	@Produces("image/png")
	public Response getFile(@PathParam("receiverId") String directory) throws IOException {
		File fileToDownload = null;
		File file=new File("/Users/JiateLi/Desktop/"+directory);
		String test[];
		test=file.list();
		if(test!=null && test.length != 0){
			fileToDownload = new File("/Users/JiateLi/Desktop/" +directory+"/"+ test[0]);
			ResponseBuilder response = Response.ok((Object) fileToDownload);
			response.header("imgFrom",
					test[0]);
			Response resp = response.build();
			return resp;
		}
		else{
			ResponseBuilder response = Response.noContent();
			return response.build();
		}
	}

}
