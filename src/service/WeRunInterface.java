package service;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.simple.parser.ParseException;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

public interface WeRunInterface {

	public String newAccount(String accountPOJson) throws IOException, ParseException ;
	public String updateRunningRecord(String historyString) throws IOException, ParseException;
	public String friendRequest(String friendJson) throws IOException, ParseException;
	public String uploadChatRec(String chatJson) throws IOException, ParseException ;
	public void uploadFile(
    		@FormDataParam("file") InputStream uploadedInputStream, 
    		@FormDataParam("senderId") String  imgName,
    		@PathParam("receiverId") String directory,
    		@PathParam("action") String action,
    		@FormDataParam("file") FormDataContentDisposition fileDetail) ;
	public void uploadFileTest(
    		@PathParam("receiverId") String directory,
    		@PathParam("action") String action,
    		 @PathParam("senderId") String imgName,
    		InputStream attachmentInputStream);
	public String getUserInfo(@PathParam("userId") String userId,
			@PathParam("passWord") String password);
	public String getRecommend(@PathParam("userId") String userid);
	public String getSearchedUser(@PathParam("userId") String userid) ;
	public String getChatInfo(@PathParam("userId") String userid);
	public Response getFile(@PathParam("receiverId") String directory) throws IOException ;
}
