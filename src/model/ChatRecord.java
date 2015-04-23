package model;

import com.google.gson.Gson;

/**
 * Created by Jackyliz on 4/10/15.
 */
public class ChatRecord {
    private String myName;
    private String friendName;
    private String timeStamp;
    private String chatContent;

    public String getMyName() {
        return myName;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }
    
    @Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
