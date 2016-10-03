package jacklee_entertainment.niceneat.chatroom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaehaklee on 15. 5. 23..
 */
public class ChatRoom {
    private List<ChatMessage> list_ChatMessage = new ArrayList<ChatMessage>();
    private String chat_members;

    // Required default constructor for Firebase serialization / deserialization
    @SuppressWarnings("unused")
    public ChatRoom() {
    }

    public ChatRoom(String chat_members) {
        this.chat_members = chat_members;
    }

    public void addChatMessage(String author, String message) {
        ChatMessage chatMessage = new ChatMessage(author, message);
        list_ChatMessage.add(chatMessage);
    }

    public List<ChatMessage> getList_ChatMessage() {
        return list_ChatMessage;
    }

    public String getChat_members() {
        return chat_members;
    }

}
