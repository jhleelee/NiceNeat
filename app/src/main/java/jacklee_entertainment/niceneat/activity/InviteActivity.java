package jacklee_entertainment.niceneat.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.Map;

import jacklee_entertainment.niceneat.App;
import jacklee_entertainment.niceneat.R;
import jacklee_entertainment.niceneat.fragment.main.ChatFragment;

/**
 * Created by user on 2015-05-31.
 */
public class InviteActivity extends Activity {
    private static final String TAG = "InviteActivity";
    private String next_step;
    private ListView listViewSelectFriendsToInvite;
    public Cursor cursor;
    public MainActivity.FriendDBHelper friendDBHelper;
    public SQLiteDatabase dbfriend;
    public SimpleCursorAdapter simpleCursorAdapter;
    public Firebase mFirebaseRef;
    public AuthData mAuthData;
    public Map<String, String> usermap ;
    public String userUid;

     private MainActivity mainActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        setContentView(R.layout.activity_invite);
           //UI
        listViewSelectFriendsToInvite = (ListView)findViewById(R.id.listViewSelectFriendsToInvite);

        cursor = mainActivity.dbfriend.rawQuery("SELECT * FROM dbfriend", null);
        this.startManagingCursor(cursor);

        String[] columns = new String[]
                {
                        "name"
                };
        int[] to = new int[] {
//            R.id.img_contactlist,
                R.id.tv_contactlist_displayname
        };

        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.item_listview_chatfragment_contactlist_friend_checkbox,
                cursor,
                columns,
                to,
                0);

        listViewSelectFriendsToInvite.setAdapter(simpleCursorAdapter);


        // GET NEXTSTEP INFORMATION FROM MAINACTIVITY
        Intent intent = getIntent();
        next_step = intent.getStringExtra("next_step");


        if (next_step.equals("chat")){

        } else if (next_step.equals("event")){

        } else if (next_step.equals("group")){


    }

}}
