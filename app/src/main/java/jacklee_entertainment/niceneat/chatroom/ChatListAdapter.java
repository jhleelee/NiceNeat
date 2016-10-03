//package jacklee_entertainment.niceneat.chatroom;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.Query;
//import com.firebase.client.ValueEventListener;
//
//import java.text.DateFormat;
//import java.util.Date;
//import java.util.Map;
//
//import jacklee_entertainment.niceneat.App;
//import jacklee_entertainment.niceneat.R;
//import jacklee_entertainment.niceneat.activity.MainActivity;
//import jacklee_entertainment.niceneat.activity.MessagingActivity;
//
//
//public class ChatListAdapter extends jacklee_entertainment.niceneat.chatroom.FirebaseListAdapter<ChatMessage> {
//
//
//    public String userUid;
//    private String string_time;
//    private Long long_time;
//    private App app;
//    private MainActivity mainActivity;
//
//    public ChatListAdapter(Query ref, Activity activity, int layout, String userUid) {
//        super(ref, ChatMessage.class, layout, activity);
//        this.userUid = userUid;
//
//     }
//
//    /**
//     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
//     * when (1)there is a data change, and (2)we are given an instance of a View that corresponds to the layout that we passed
//     * to the constructor, (3)as well as a single <code>Chat</code> instance that represents the current data to bind.
//     *
//     * @param view A view instance corresponding to the layout we passed to the constructor.
//     * @param chatMessage An instance representing the current state of a chat message
//     */
//    @Override
//    protected void populateView(View view, jacklee_entertainment.niceneat.chatroom.ChatMessage chatMessage) {
//        // Map a Chat object to an entry in our listview
//        String author = chatMessage.getAuthor();
//        String message = chatMessage.getMessage();
//
//
//        ref_messageId.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                final Long long_time = (Long)snapshot.getValue();
//                chatMessage.setTime(long_time);
//            }
//
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });
//        Long long_time = chatMessage.getTime();
//        Date date = new Date(long_time*1000);
//        String string_time = DateFormat.getDateInstance().format(date);
//
//
//
//
//
//
//        LinearLayout wrapper_message = (LinearLayout)view.findViewById(R.id.wrapper_message);
//        LinearLayout message_layout_imgview = (LinearLayout)view.findViewById(R.id.message_layout_imgview);
//        ImageView message_imgview = (ImageView)view.findViewById(R.id.message_imgview);
//        View space_message_imgview = (View)view.findViewById(R.id.space_message_imgview);
//        View space_message_right_for_others = (View)view.findViewById(R.id.space_message_right_for_others);
//
//
//        LinearLayout message_layout_textviews = (LinearLayout)view.findViewById(R.id.message_layout_textviews);
//        TextView message_textview_author = (TextView)view.findViewById(R.id.message_textview_author);
//        TextView message_textview_message = (TextView)view.findViewById(R.id.message_textview_message);
//        TextView message_textview_messagedate = (TextView)view.findViewById(R.id.message_textview_messagedate);
//        message_textview_author.setText(author);
//        message_textview_message.setText(message);
//        message_textview_messagedate.setText(string_time);
//
////        final float scale = App.getContext().getResources().getDisplayMetrics().density;
////        int pixels = (int) (dps * scale + 0.5f);
//
//        // If the message was sent by this user, color it differently
//
//
//
//        if (author != null && author.equals(userUid)) {
//            message_imgview.setVisibility(View.GONE);
//            space_message_imgview.setVisibility(View.VISIBLE);
//            space_message_right_for_others.setVisibility(View.GONE);
//            message_layout_textviews.setGravity(Gravity.RIGHT);
//            message_textview_author.setVisibility(View.GONE);
//            message_textview_message.setTextColor(App.getContext().getResources().getColor(R.color.white));
//            message_textview_message.setBackgroundColor((App.getContext().getResources().getColor(R.color.color_base_d)));
//
//        } else {
//
//        }
//        ((TextView) view.findViewById(R.id.message_textview_message)).setText(chatMessage.getMessage());
//    }
//
//    public int getDpFromPixel(Context context,int pixel){
//        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, context.getResources().getDisplayMetrics());
//        return height;
//    }
//
//}
