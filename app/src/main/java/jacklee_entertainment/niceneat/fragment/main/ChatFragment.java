package jacklee_entertainment.niceneat.fragment.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;


import com.fasterxml.jackson.databind.deser.Deserializers;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jacklee_entertainment.niceneat.App;
import jacklee_entertainment.niceneat.R;
import jacklee_entertainment.niceneat.activity.MainActivity;
import jacklee_entertainment.niceneat.activity.MessagingActivity;
import jacklee_entertainment.niceneat.chatroom.ChatMessage;
import jacklee_entertainment.niceneat.chatroom.ChatRoom;
import jacklee_entertainment.niceneat.fragment.dialog.ProgressDialogFragment;
import jacklee_entertainment.niceneat.utils.ContactListEntry;
 import jacklee_entertainment.niceneat.wrap_classes.Friend;


public class ChatFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener  {
    private static final String TAG = "ChatFragment";

    private App app;
    private MainActivity mainActivity;
    private SimpleCursorAdapter simpleCursorAdapter;

    private final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };

    private final static int[] TO_IDS = {
            android.R.id.text1
    };

    long mContactId;
    // The contact's LOOKUP_KEY
    String mContactKey;
    // A content URI for the selected contact
    Uri mContactUri;
    // An adapter that binds the result Cursor to the ListView
//    private ContactlistAdapter mContactsAdapter;
    public Cursor cursor;
    private View view;
    private Button btn_chat_below_chat,btn_chat_below_friendlist;
    private ListView listview_chatrooms;
    private ListView listview_invitebutton_friends_contacts;
    private LinearLayout buttonsPanel;
    private RelativeLayout layout_invite_friends;
    private ImageView imageview_ic_share_white;
    private static final String PROGRESS_DIALOG_TAG = "PROGRESS_DIALOG_TAG";
    private List<ContactListEntry> contactListEntries = new ArrayList<ContactListEntry>();
    private boolean contactListVisible = false;
    private ArrayList<String> arrayListForFriendListUp;
    private ArrayList<Friend> arrayListFriend;
    private ArrayList<String[]> arrayListFrindInfoString;
    private static Pattern phonePattern = Pattern.compile("([1-9]+)000000([0-9]+)");
    int[] to = new int[] {
//            R.id.img_contactlist,
            R.id.tv_contactlist_displayname
    };

    String[] columns = new String[]
            {
               "name"
            };
//    private TextWatcher searchTextWatcher = new TextWatcher() {
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            // ignore
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            // ignore
//        }
//
////        @Override
////        public void afterTextChanged(Editable s) {
////            mContactsAdapter.getFilter().filter(s.toString());
////        }
//    };


    public ChatFragment() {}

    // newInstance constructor for creating fragment with arguments
    public static ChatFragment newInstance(int page, String title) {
        ChatFragment fragmentFirst = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
         Log.d(TAG, "20150527 - onCreateView");
         initUI();


        return view;
    }





    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app = (App)getActivity().getApplicationContext();
        mainActivity = (MainActivity)getActivity();


        // FRIENDS LISTVIEW
        // FRIENDS LISTVIEW
        // FRIENDS LISTVIEW
        // FRIENDS LISTVIEW
        // FRIENDS LISTVIEW







        // FRIENDS LISTVIEW
        // FRIENDS LISTVIEW
        // FRIENDS LISTVIEW
        // FRIENDS LISTVIEW
        // FRIENDS LISTVIEW
        Log.d(TAG, "20150527 - CURSOR GO");
        cursor = mainActivity.dbfriend.rawQuery("SELECT * FROM dbfriend", null);
        Log.d(TAG, "20150527 - CURSOR COLUMN COUNT - "+ String.valueOf(cursor.getColumnCount()));
        Log.d(TAG, "20150527 - CURSOR COUNT - " + String.valueOf(cursor.getCount()));

        // REPLACE INTO CURSOR LOADER
        mainActivity.startManagingCursor(cursor);
        simpleCursorAdapter = new SimpleCursorAdapter(
                mainActivity,
                R.layout.item_listview_chatfragment_contactlist_friend,
                cursor,
                columns,
                to,
                0);
        Log.d(TAG, "20150527 - ADAPTER COUNT - " + simpleCursorAdapter.getCount());


         listview_invitebutton_friends_contacts.setAdapter(simpleCursorAdapter);

//        cursor.close();

        // CHANGE INTO CHILD CHANGED LISTENER - MAINACTIVITY
//        app.mFirebaseRef.child("users_contacts").child(mainActivity.userUid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                Iterable<DataSnapshot> children_datasnapshots = snapshot.getChildren(); // DELETE IF CHILD LISTENER
//                Iterator iterator = children_datasnapshots.iterator(); // DELETE IF CHILD LISTENER
//
//                while (iterator.hasNext()) {
//
//                    String key_value = (String) iterator.next().toString();
//                    Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET - " + key_value);
//                    // EXAMPLE : DataSnapshot { key = facebook:1573463126267040, value = 82n01026237641 }
//                    String[] parts = key_value.split(", value = ");
//                    String key = parts[0].replace("DataSnapshot { key = ", "");
//                    String value = parts[1].replace(" }", "");
//                    Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET  - KEY(ID) - " + key);
//                    Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET  - VALUE(PHONE) - " + value);
//
//                    if (!value.equals("not_yet")) {
//                        if(mainActivity.verifyIfFriendIdExistInDB(key)){
//                            Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET  - KEY(ID) - EXISTS IN DB - " + key);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("num", value);
//                            mainActivity.frienddb.update("frienddb",contentValues,"id = '"+key+"'",null);
//
//                        } else {
//                            Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET  - KEY(ID) - NOT EXISTS IN DB - " + key);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put("id",key);
//                            contentValues.put("num",value);
//                            mainActivity.frienddb.insert("frienddb", null, contentValues);
//                        }
//                    }
//                }
//            }
//
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });

        // GET NAME AND PHOTO AND UPDATE TO DB - MAINACTIVITY
        // USE CHILD LISTENER
//        app.mFirebaseRef.child("users").addChildEventListener(new ChildEventListener() {  // child(mainActivity.userUid).
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
//                String id = dataSnapshot.getKey();
//                Map<String,Object> userdatamap = (Map<String,Object>)dataSnapshot.getValue();
//                String name = (String)userdatamap.get("displayName");
//                Log.d(TAG, "20150526 USER ID - " + id);
//                Log.d(TAG, "20150526 USER ID - " + name);
//
//                if(mainActivity.verifyIfFriendIdExistInDB(id)){
//                    Log.d(TAG, "20150526 USER ID EXISTS IN DB - " + id);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("name", name);
//                    mainActivity.frienddb.update("frienddb",contentValues,"id = '"+id+"'",null);
//
//                } else {
//                    Log.d(TAG, "20150526 USER ID NOT EXISTS IN DB - " + id);
//                    ContentValues contentValues = new ContentValues();
//                    contentValues.put("id",id);
//                    contentValues.put("num",name);
//                    mainActivity.frienddb.insert("frienddb", null, contentValues);
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
//                // No-op
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                // SHOULD BE HANDLED
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
//                // No-op
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                // No-op
//            }
//        });

 //        listview_invitebutton_friends_contacts = (ListView) getActivity().findViewById(R.id.listview_invitebutton_friends_contacts);
//         mContactsAdapter  = new ContactlistAdapter(getActivity(), R.layout.item_listview_chatfragment_contactlist_friend,
//                contactListEntries);

//        View v = getActivity().getLayoutInflater().inflate(R.layout.header_listview_chatfragment_contactlist, null);
//        layout_invite_friends = (RelativeLayout)v.findViewById(R.id.layout_invite_friends);
//        imageview_ic_share_white.setColorFilter(getActivity().getResources().getColor(R.color.color_base_e), android.graphics.PorterDuff.Mode.MULTIPLY);
//        layout_invite_friends.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, getActivity().getString(R.string.invite_text));
//                getActivity().startActivityForResult(Intent.createChooser(intent, getActivity().getString(R.string.invite_title)), 500);
//            }
//        });

//        listview_invitebutton_friends_contacts.addHeaderView(v);
//        listview_invitebutton_friends_contacts.setAdapter(mContactsAdapter);
        listview_invitebutton_friends_contacts.setOnItemClickListener(this);
//        listview_invitebutton_friends_contacts.setVisibility(View.GONE);

    }

//
//    private void addLocalContacts(ArrayList<String> phoneNumbers) {
//        Cursor managedCursor = getActivity().getContentResolver()
//                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        new String[]{ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//                                ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
//        int phoneNumberIdx = managedCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//        if (managedCursor.getCount() > 0) {
//            while (managedCursor.moveToNext()) {
//                String name = managedCursor
//                        .getString(managedCursor
//                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                String phone = managedCursor.getString(phoneNumberIdx);
//
//                if (phone == null || !phoneNumbers.contains(phone)) {
//                    ContactListEntry contactListEntry = new ContactListEntry();
//                    contactListEntry.setEntryType(ContactListEntry.EntryType.CONTACT_ENTRY);
//                    contactListEntry.setDisplayName(name);
//
//                    if (phone != null) {
//                        contactListEntry.setPhone(phone);
//                    }
//
//                    contactListEntries.add(contactListEntry);
//                }
//            }
//        }
//        managedCursor.close();
//        hideProgress();
//        mContactsAdapter.notifyDataSetChanged();
//    }

//    private void loadUsersIntoList() {
//
//        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
//        final QBUser signedInUser = DataHolder.getDataHolder().getSignInQbUser();
//
//        QBCustomObjects.getObjects("friends_list", requestBuilder, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
//            @Override
//            public void onSuccess(ArrayList<QBCustomObject> customObjects, Bundle params) {
//                contactListEntries.clear();
//                mContactsAdapter.setFiltered(contactListEntries);
//
//                QBCustomObject ourObject = null;
//                for (QBCustomObject obj: customObjects) {
//                    if (obj.getUserId().equals(signedInUser.getId())) {
//                        ourObject = obj;
//                        break;
//                    }
//                }
//
//                if (ourObject == null) {
//                    addLocalContacts(new ArrayList<String>());
//                    return;
//                }
//
//                ArrayList<String> phoneNumbers = (ArrayList<String>) ourObject.getFields().get("friend_ntnlcode_and_phonenumber");
//
//                QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
//                pagedRequestBuilder.setPage(1);
//                pagedRequestBuilder.setPerPage(100);
//
//                QBUsers.getUsersByPhoneNumbers(phoneNumbers, pagedRequestBuilder, new QBEntityCallbackImpl<ArrayList<QBUser>>() {
//                    @Override
//                    public void onSuccess(ArrayList<QBUser> users, Bundle params) {
//                        ArrayList<String> phoneNumbers = new ArrayList<String>();
//                        for (QBUser qbUser : users) {
//                            ContactListEntry contactListEntry = new ContactListEntry();
//                            contactListEntry.setEntryType(ContactListEntry.EntryType.FRIEND_ENTRY);
//                            contactListEntry.setDisplayName(qbUser.getFullName());
//                            contactListEntry.setLastRequestAt(qbUser.getLastRequestAt());
//
//                            if (qbUser.getPhone() != null) {
//                                Matcher matcher = phonePattern.matcher(qbUser.getPhone());
//                                if (matcher.matches()) {
//                                    contactListEntry.setPhone(matcher.group(1));
//                                    phoneNumbers.add(contactListEntry.getPhone());
//                                    contactListEntry.setNationalCode(matcher.group(2));
//                                }
//                            }
//
//                            contactListEntries.add(contactListEntry);
//                        }
//
//                        addLocalContacts(phoneNumbers);
//                    }
//
//                    @Override
//                    public void onError(List<String> errors) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onError(List<String> errors) {
//
//            }
//        });
//    }

    public void showContactList() {
        syncContactsPhoneNumFromClientAndServer();
        listview_invitebutton_friends_contacts.setVisibility(View.VISIBLE);

        if (getActivity() instanceof MainActivity &&
                ((MainActivity)getActivity()).slidingTabsBasicFragment != null) {
            MainActivity mainActivity = ((MainActivity)getActivity());
            if (mainActivity.getToolbarLayout() != null) {
                ImageView icon = (ImageView) mainActivity.getToolbarLayout().findViewById(R.id.iv_ic);
                TextView toolbarText = (TextView) mainActivity.getToolbarLayout().findViewById(R.id.iv_text);
                final ImageButton searchButton = (ImageButton) mainActivity.getToolbarLayout().findViewById(R.id.iv_img);
                final EditText searchField = (EditText) mainActivity.getToolbarLayout().findViewById(R.id.iv_field);

                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (searchButton.getVisibility() == View.VISIBLE) {
                            getActivity().onBackPressed();
                        }
                    }
                });

//                searchField.addTextChangedListener(searchTextWatcher);

                searchButton.setVisibility(View.VISIBLE);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        searchField.setVisibility(View.VISIBLE);
                        if(searchField.requestFocus()) {
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });
                toolbarText.setText("");
                icon.setImageResource(R.drawable.ic_action_next_item);
                icon.setScaleX(-1);
            }
        }
//        loadUsersIntoList();
        contactListVisible = true;
    }

    public void hideContactList() {
        listview_invitebutton_friends_contacts.setVisibility(View.GONE);
        if (getActivity() instanceof MainActivity &&
                ((MainActivity)getActivity()).slidingTabsBasicFragment != null) {
            MainActivity mainActivity = ((MainActivity)getActivity());

            if (mainActivity.getToolbarLayout() != null) {
                ImageView icon = (ImageView) mainActivity.getToolbarLayout().findViewById(R.id.iv_ic);
                TextView toolbarText = (TextView) mainActivity.getToolbarLayout().findViewById(R.id.iv_text);
                ImageButton searchButton = (ImageButton) mainActivity.getToolbarLayout().findViewById(R.id.iv_img);
                final EditText searchField = (EditText) mainActivity.getToolbarLayout().findViewById(R.id.iv_field);

                searchButton.setVisibility(View.GONE);
                searchField.setVisibility(View.GONE);
                toolbarText.setText("Nice n' neat");
                icon.setImageResource(R.drawable.ic_actionbar);
                icon.setScaleX(1);
            }
        }
        contactListVisible = false;
    }
//
//    public boolean isContactListVisible() {
//        return this.contactListVisible;
//    }

    private void initUI() {
        btn_chat_below_chat = (Button) view.findViewById(R.id.btn_chat_below_chat);
        btn_chat_below_friendlist = (Button) view.findViewById(R.id.btn_chat_below_friendlist);
        buttonsPanel  = (LinearLayout) view.findViewById(R.id.btn_panel_contactlist);
        listview_invitebutton_friends_contacts = (ListView)view.findViewById(R.id.listview_invitebutton_friends_contacts);
        listview_chatrooms = (ListView)view.findViewById(R.id.listview_chatrooms);

        btn_chat_below_chat.setOnClickListener(this);
        btn_chat_below_friendlist.setOnClickListener(this);
    }

    protected void showProgress(String text) {
        ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(text);
        progressDialogFragment.setTargetFragment(this, 0);
        progressDialogFragment.setCancelable(false);
        progressDialogFragment.show(getFragmentManager(), PROGRESS_DIALOG_TAG);
    }

    protected void hideProgress() {
        Fragment fragment = getFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);

        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_chat_below_chat:
                hideContactList();

                break;

            case R.id.btn_chat_below_friendlist:
                showContactList();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        cursor.moveToPosition(position);
        String selected_userid = cursor.getString(cursor.getColumnIndex("userid"));
        String selected_name = cursor.getString(cursor.getColumnIndex("name"));
        String selected_num = cursor.getString(cursor.getColumnIndex("num"));
        Intent intent = new Intent(mainActivity, MessagingActivity.class);
        intent.putExtra("selected_userid",selected_userid );
        intent.putExtra("selected_name",selected_name );
        intent.putExtra("selected_num",selected_num );
        mainActivity.startActivity(intent);
//        ContactListEntry entry = mContactsAdapter.getFiltered().get(i-1);
//
//        if (entry.getEntryType() == ContactListEntry.EntryType.CONTACT_ENTRY &&
//                entry.getPhone() != null) {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", entry.getPhone(), null));
//            intent.putExtra("sms_body", getActivity().getString(R.string.invite_text));
//            getActivity().startActivityForResult(intent, 500);
//        }
    }






    public void syncContactsPhoneNumFromClientAndServer(){
//        app.mFirebaseRef.child("users_contacts").child(mainActivity.userUid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                Iterable<DataSnapshot> children_datasnapshots = snapshot.getChildren();
//                Iterator<DataSnapshot> iterator = children_datasnapshots.iterator();
//                while (iterator.hasNext()) {
//                    String key = iterator.next().getKey();
//                    if (arraylist_ContactsPhoneNumFromClient.contains(key)) {
//                        arraylist_ContactsPhoneNumFromClient.remove(key);
//                        Log.d(TAG, "111111 SERVER P_NUM IS INCLUDED IN CLIENT P_NUM - " + key);
//                        Log.d(TAG, "20150525 arraylist_ContactsPhoneNumFromClient SIZE - " + arraylist_ContactsPhoneNumFromClient.size());
//                        Log.d(TAG, "20150525 arraylist_ContactsPhoneNumFromClientOriginal SIZE - " + arraylist_ContactsPhoneNumFromClientOriginal.size());
//
//                    } else {
//                        Log.d(TAG, "111111 SERVER P_NUM IS NOT INCLUDED IN CLIENT P_NUM - " + key);
//                    }
//                }
//
//
//                if (arraylist_ContactsPhoneNumFromClient.size() > 0) {
//                    Log.d(TAG, "111111 arraylist_ContactsPhoneNumFromClient CONTAINS AT LEAST ONE");
//                    Log.d(TAG, "111111 AFTER REMOVED SIZE - " + arraylist_ContactsPhoneNumFromClient.size());
//                    for (int i = 0; i < arraylist_ContactsPhoneNumFromClient.size(); i++) {
//                        String key_forUpload = arraylist_ContactsPhoneNumFromClient.get(i);
//                        mapForUpload.put(key_forUpload, "not_yet");
//                        Log.d(TAG, "111111 PUT TO MAP TO UPLOAD - " + key_forUpload);
//                    }
//
//                    app.mFirebaseRef.child("users_contacts").child(userUid).updateChildren(mapForUpload);
//                    Log.d(TAG, "222222 UPLOADED SIZE - " + mapForUpload.size());
//
//                } else {
//                    Log.d(TAG, "222222 NOTHING UPLOADED");
//                }
//            }
//
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });


}

    public class ChatRoomListViewAdapter extends BaseAdapter {


        private String chatmembers;
        private String lasttime;
        private String lastmessage;

        public ChatRoomListViewAdapter(Query ref, Activity activity, int layout, String userUid) {
            super(ref, Cha.class, layout, activity);
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

        protected void getView(View view, jacklee_entertainment.niceneat.chatroom.ChatMessage chatMessage) {
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