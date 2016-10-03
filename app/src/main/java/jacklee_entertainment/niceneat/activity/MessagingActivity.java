package jacklee_entertainment.niceneat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jacklee_entertainment.niceneat.App;
import jacklee_entertainment.niceneat.R;
import jacklee_entertainment.niceneat.chatroom.ChatMessage;

/**
 * Created by user on 2015-05-28.
 */
public class MessagingActivity extends Activity {
    private static final String TAG = "MessagingActivity";

    public  Firebase mFirebaseRef;
    public AuthData mAuthData;
    public  Map<String, String> usermap ;
    public String userUid;

    ListView listview_messages;
    EditText edittext_input_message;
    Button button_send_message;

    String selected_userid;
    String selected_name;
    String selected_num;
    ArrayList<String> arrayList_chatmembers;
    String string_arrayList_chatmembers;
    String string_chatroomId_push;
    ArrayList<String> arrayList_ChatroomIds;
    String string_arrayList_ChatroomIds;
    String alreadyExistingChatroomid;
    String alreadyExistingChatroomMembers;
    private ChatListAdapter chatListAdapter;
    private Firebase ref_chatroomId;
    private Firebase ref_messageId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIREBASE
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        try {
            mAuthData = mFirebaseRef.getAuth();} catch (Exception e) {mAuthData=null;};
        usermap = new HashMap<String,String>();

        // UI
        setContentView(R.layout.activity_messaging);
        listview_messages = (ListView)findViewById(R.id.listview_messages);
        edittext_input_message = (EditText)findViewById(R.id.edittext_input_message);
        button_send_message = (Button)findViewById(R.id.button_send_message);

        // CHECK LOGIN
        if(mAuthData == null){
            startLoginActivityFB();
            finish();
        };
        userUid = mAuthData.getUid();





        // GET FRIEND INFORMATION FROM MAINACTIVITY
        Intent intent = getIntent();
        selected_userid = intent.getStringExtra("selected_userid");
        selected_name = intent.getStringExtra("selected_name");
        selected_num = intent.getStringExtra("selected_num");

        // ME AND FRIEND IDS PUT TO ARRAYLIST, ALIGN, TO_STRING
        arrayList_chatmembers=new ArrayList<String>();
        arrayList_chatmembers.add(userUid);
        arrayList_chatmembers.add(selected_userid);
        Collections.sort(arrayList_chatmembers, String.CASE_INSENSITIVE_ORDER);
        string_arrayList_chatmembers = TextUtils.join(", ", arrayList_chatmembers);
        Log.d("TAG", "20150531 arrayList_chatmembers - " + string_arrayList_chatmembers);

        // CHECK IF CHATROOM ALREADY EXISTS

        alreadyExistingChatroomid ="";
        alreadyExistingChatroomMembers="";
        mFirebaseRef.child("users_chatrooms").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Iterable<DataSnapshot> children_datasnapshots = snapshot.getChildren();
                Iterator iterator = children_datasnapshots.iterator();
                while (iterator.hasNext()) {
                    String key_chatroomid__value_chatmembers = iterator.next().toString();
                    // EXAMPLE : DataSnapshot { key = facebook:1573463126267040, value = 82n01026237641 }
                    String[] parts = key_chatroomid__value_chatmembers.split(", value = ");
                    String key_chatroomid = parts[0].replace("DataSnapshot { key = ", "");
                    String value_chatmembers = parts[1].replace(" }", "");
                    key_chatroomid.replaceAll("\\s", "");
                    value_chatmembers.replaceAll("\\s", "");

                    Log.d(TAG, "20150531 key_chatroomid - '" + key_chatroomid + "'");
                    Log.d(TAG, "20150531 value_chatmembers - '" + value_chatmembers + "'");

                    if (string_arrayList_chatmembers.equals(value_chatmembers)) {
                        Log.d(TAG, "20150531 FOUND A EXISTING CHATROOM..");

                        alreadyExistingChatroomid = key_chatroomid;
                        alreadyExistingChatroomMembers = value_chatmembers;
                        break;
                    } else {

                    }
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        // CHATROOM CREATE FIREBASE PUSH
        Log.d(TAG, "20150531 alreadyExistingChatroomid : "+alreadyExistingChatroomid );
        Log.d(TAG, "20150531 alreadyExistingChatroomMembers : "+alreadyExistingChatroomMembers );

        if (alreadyExistingChatroomid.length()==0) {
            // ADD NEW CHATROOM
            Log.d(TAG, "20150531 CREATING A NEW CHATROOM...");
            Map<String, String> map_chatroom = new HashMap<String, String>();
            map_chatroom.put("chat_members", string_arrayList_chatmembers);

            ref_chatroomId = mFirebaseRef.child("chatrooms").push();
            ref_chatroomId.setValue(map_chatroom);

            string_chatroomId_push = ref_chatroomId.getKey();
            Log.d(TAG, "20150531 CHATROOM CREATED NEWLY : " +string_chatroomId_push);

            // ADD TO PERSONAL USER LIST
            Map<String, String> map_userschatrooms = new HashMap<String, String>();
            map_userschatrooms.put(string_chatroomId_push, string_arrayList_chatmembers);
            mFirebaseRef.child("users_chatrooms").child(userUid).child("messages").setValue(map_userschatrooms);


        } else {
            Log.d(TAG, "20150531 USE EXISTING CHATROOM...");
            ref_chatroomId = mFirebaseRef.child("chatrooms").child(alreadyExistingChatroomid);
        }




            // chat send
        edittext_input_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });
        button_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }



    public void onStart() {
        super.onStart();
//         Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
//         Tell our list adapter that we only want 50 messages at a time
        chatListAdapter = new ChatListAdapter(ref_chatroomId.child("messages").limit(50), this, R.layout.message, userUid);
        listview_messages.setAdapter(chatListAdapter);
        chatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listview_messages.setSelection(chatListAdapter.getCount() - 1);
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
         chatListAdapter.cleanup();
    }



    public void startLoginActivityFB(){
        Intent intent= new Intent(this, LoginActivityFB.class);
        startActivity(intent);
        finish();
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.edittext_input_message);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            final ChatMessage chatMessage = new ChatMessage( userUid,input);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            ref_messageId = ref_chatroomId.child("messages").push();
            ref_messageId.setValue(chatMessage);

            inputText.setText("");
        }
    }




    public class ChatListAdapter extends jacklee_entertainment.niceneat.chatroom.FirebaseListAdapter<ChatMessage> {


        public String userUid;
        private String string_time;
        private Long long_time;
        private App app;
        private MainActivity mainActivity;

        public ChatListAdapter(Query ref, Activity activity, int layout, String userUid) {
            super(ref, ChatMessage.class, layout, activity);
            this.userUid = userUid;

        }

        /**
         * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
         * when (1)there is a data change, and (2)we are given an instance of a View that corresponds to the layout that we passed
         * to the constructor, (3)as well as a single <code>Chat</code> instance that represents the current data to bind.
         *
         * @param view A view instance corresponding to the layout we passed to the constructor.
         * @param chatMessage An instance representing the current state of a chat message
         */
        @Override
        protected void populateView(View view, jacklee_entertainment.niceneat.chatroom.ChatMessage chatMessage) {
            // Map a Chat object to an entry in our listview
            String author = chatMessage.getAuthor();
            String message = chatMessage.getMessage();

//            ref_messageId.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    string_time =  snapshot.getValue().toString();
////                    Long long_time =  (Long)snapshot.getValue();
//
//                }
//
//
//                @Override
//                public void onCancelled(FirebaseError firebaseError) {
//                    System.out.println("The read failed: " + firebaseError.getMessage());
//                }
//            });
//             Date date = new Date(long_time*1000);
//                String string_time = DateFormat.getDateInstance().format(date);






            LinearLayout wrapper_message = (LinearLayout)view.findViewById(R.id.wrapper_message);
            LinearLayout message_layout_imgview = (LinearLayout)view.findViewById(R.id.message_layout_imgview);
            ImageView message_imgview = (ImageView)view.findViewById(R.id.message_imgview);
            View space_message_imgview = (View)view.findViewById(R.id.space_message_imgview);
            View space_message_right_for_others = (View)view.findViewById(R.id.space_message_right_for_others);


            LinearLayout message_layout_textviews = (LinearLayout)view.findViewById(R.id.message_layout_textviews);
            TextView message_textview_author = (TextView)view.findViewById(R.id.message_textview_author);
            TextView message_textview_message = (TextView)view.findViewById(R.id.message_textview_message);

            TextView message_textview_messagedate = (TextView)view.findViewById(R.id.message_textview_messagedate);
            message_textview_author.setText(author);
            message_textview_message.setText(message);
            message_textview_message.setBackgroundResource(R.drawable.tags_rounded_corners);

//            message_textview_messagedate.setText(string_time);

//        final float scale = App.getContext().getResources().getDisplayMetrics().density;
//        int pixels = (int) (dps * scale + 0.5f);

            // If the message was sent by this user, color it differently



            if (author != null && author.equals(userUid)) {
                message_imgview.setVisibility(View.GONE);
                space_message_imgview.setVisibility(View.VISIBLE);
                space_message_right_for_others.setVisibility(View.GONE);
                message_layout_textviews.setGravity(Gravity.RIGHT);
                message_textview_author.setVisibility(View.GONE);
                message_textview_message.setTextColor(App.getContext().getResources().getColor(R.color.white));
                message_textview_message.setBackgroundColor((App.getContext().getResources().getColor(R.color.color_base_d)));

            } else {

            }
            ((TextView) view.findViewById(R.id.message_textview_message)).setText(chatMessage.getMessage());
        }

        public int getDpFromPixel(Context context,int pixel){
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, context.getResources().getDisplayMetrics());
            return height;
        }

    }

}
