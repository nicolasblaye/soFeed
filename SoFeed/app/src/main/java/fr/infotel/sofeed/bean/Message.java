package fr.infotel.sofeed.bean;

/**
 * Created by nicolas on 26/05/16.
 */
public class Message {
    private String sender;
    private String chatRoom;
    private String message;

    public void setSender(String sender){
        this.sender = sender;
    }
    public String getSender(){
        return this.sender;
    }

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setChatRoom(String chatRoom){this.chatRoom = chatRoom;}
    public String getChatRoom(){
        return this.chatRoom;
    }
}
