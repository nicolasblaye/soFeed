package fr.infotel.sofeed.bean;

/**
 * Created by nicolas on 26/05/16.
 */
public class Message {
    private String sender;
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
}
