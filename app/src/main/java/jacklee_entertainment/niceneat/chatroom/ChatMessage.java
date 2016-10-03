package jacklee_entertainment.niceneat.chatroom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.Map;

/**
 * Created by jaehaklee on 15. 5. 23..
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {

    private String message;
    private String author;
    private Long time;

    // Required default constructor for Firebase serialization / deserialization
    @SuppressWarnings("unused")
    public ChatMessage() {
    }

    public ChatMessage(String author, String message) {
        this.author = author;
        this.message = message;
//        this.time = ServerValue.TIMESTAMP;
       }

    public String getAuthor() {
        return author;
    }
    public String getMessage() {
        return message;
    }
    public Long getTime() {
        return time;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setAuthor(String author){
        this.author= author;
    }
    public void setTime(Long time){
        this.time= time;
    }
}
