package jacklee_entertainment.niceneat.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


import com.astuetz.PagerSlidingTabStrip;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jacklee_entertainment.niceneat.App;
 import jacklee_entertainment.niceneat.R;
import jacklee_entertainment.niceneat.fragment.main.ChatFragment;
import jacklee_entertainment.niceneat.fragment.main.EventFragment;
import jacklee_entertainment.niceneat.fragment.main.GroupFragment;
import jacklee_entertainment.niceneat.fragment.main.MyProfileFragment;
import jacklee_entertainment.niceneat.fragment.main.PageFragment;
import jacklee_entertainment.niceneat.fragment.main.SlidingTabsBasicFragment;
import jacklee_entertainment.niceneat.TabOption;
import jacklee_entertainment.niceneat.fragment.main.MainTabFragment;
import jacklee_entertainment.niceneat.wrap_classes.Friend_Phone_And_Id;
import jacklee_entertainment.niceneat.wrap_classes.List_Friend_Phone_And_Id;

public class MainActivity extends android.support.v4.app.FragmentActivity {
    private static final String TAG = "MainActivity";

     public  Firebase mFirebaseRef;
    public AuthData mAuthData;
    public  Map<String, String> usermap ;
    public String userUid;

    public  SlidingTabsBasicFragment slidingTabsBasicFragment;
    public MyFragmentPagerAdapter myFragmentPagerAdapter;
    public ViewPager viewPager;
    private FragmentManager fragmentManager;

    private RelativeLayout toolbarLayout;
    public RelativeLayout getToolbarLayout() {
        return this.toolbarLayout;
    }
    public ArrayList<String> arraylist_ContactsPhoneNumFromClient;
    public ArrayList<String> arraylist_ContactsPhoneNumFromClientOriginal;
    public Map<String, String> map_ContactsPhoneNumFromServer;
    public Map<String,Object> mapForUpload;
    public Map<String,Object> mapForUploadAlreadyMember;

    public FriendDBHelper friendDBHelper;
    public SQLiteDatabase dbfriend;
    public SimpleCursorAdapter simpleCursorAdapter;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FIREBASE
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        try {
            mAuthData = mFirebaseRef.getAuth();} catch (Exception e) {mAuthData=null;};
        usermap = new HashMap<String,String>();

        friendDBHelper = new FriendDBHelper(this);
        dbfriend = friendDBHelper.getWritableDatabase();
        Log.d("TAG","111111 mAuthData = "+mAuthData);

        try {if(mAuthData == null){
            startLoginActivityFB();
            finish();
        };} catch (Exception e){
            startLoginActivityFB();
            finish();
        }
        try {
        userUid = mAuthData.getUid();} catch (NullPointerException e){
            startLoginActivityFB();
            finish();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);

        // wrap method onresume
        mapForUpload = new HashMap<String, Object>();
        mapForUploadAlreadyMember = new HashMap<String, Object>();
        arraylist_ContactsPhoneNumFromClient = new ArrayList<>();

        String nationalcode = sharedPreferences.getString("nationalcode", null);
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor_Phone  = contentResolver.query(
                         ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                         null,
                         null, //ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                         null, //new String[]{id},
                         null);
                int typeidx = cursor_Phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                int numidx = cursor_Phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String forLogCursorCount = String.valueOf(cursor_Phone.getCount());
                Log.d(TAG, "111111111111111111111111111111111 Count phone cursor number : " + forLogCursorCount);

                while (cursor_Phone.moveToNext()) {
                    String num = cursor_Phone.getString(numidx);
                    switch (cursor_Phone.getInt(typeidx)) {
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                            num = num.replace(".", "");
                            num = num.replace(" ", "");
                            num = num.replace("-", "");
                            num = num.replace("#", "");
                            num = num.replace("$", "");
                            num = num.replace("[", "");
                            num = num.replace("]", "");
                            num = num.replace("/", "");

                            if(num.startsWith("\\+")){
                                 PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                                try {
                                    Log.d(TAG, "20150602 COUNTRYCODE FILTERED NOT - " + num);
                                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(num, "");
                                    nationalcode = String.valueOf(numberProto.getCountryCode());
                                    // MODIFY CODE BELOW INTO PROGRAMMIC WAY
                                    String plussign_nationalcode = "\\+"+nationalcode;
                                    num = num.replace(plussign_nationalcode, "");
                                    Log.d(TAG, "20150602 COUNTRYCODE FILTERED - " + num);

                                } catch (NumberParseException e) {
                                    System.err.println("NumberParseException was thrown: " + e.toString());
                                }
                            }

                            arraylist_ContactsPhoneNumFromClient.add(nationalcode + "_" + num);
                            Log.d(TAG, "20150602 CLIENT ALL SIZE - " + arraylist_ContactsPhoneNumFromClient.size());
                            HashSet<String> hashSet = new HashSet<>();
                            hashSet.addAll(arraylist_ContactsPhoneNumFromClient);
                            arraylist_ContactsPhoneNumFromClient.clear();
                            arraylist_ContactsPhoneNumFromClient.addAll(hashSet);
                            arraylist_ContactsPhoneNumFromClientOriginal = new ArrayList<String>(arraylist_ContactsPhoneNumFromClient);
                            Log.d(TAG, "20150602 CLIENT WITHOUT DUPLICATION SIZE - " + arraylist_ContactsPhoneNumFromClient.size());
                            break;
                        default:
                            break;
                    }
                }
                cursor_Phone.close();

        // WORKS
        mFirebaseRef.child("users_contacts").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Iterable<DataSnapshot> children_datasnapshots = snapshot.getChildren();
                Iterator<DataSnapshot> iterator = children_datasnapshots.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().getKey();
                    if (arraylist_ContactsPhoneNumFromClient.contains(key)) {
                        arraylist_ContactsPhoneNumFromClient.remove(key);
                        Log.d(TAG, "111111 SERVER P_NUM IS INCLUDED IN CLIENT P_NUM - " + key);
                        Log.d(TAG, "20150525 arraylist_ContactsPhoneNumFromClient SIZE - " + arraylist_ContactsPhoneNumFromClient.size());
                        Log.d(TAG, "20150525 arraylist_ContactsPhoneNumFromClientOriginal SIZE - " + arraylist_ContactsPhoneNumFromClientOriginal.size());

                    } else {
                        Log.d(TAG, "111111 SERVER P_NUM IS NOT INCLUDED IN CLIENT P_NUM - " + key);
                    }
                }


                if (arraylist_ContactsPhoneNumFromClient.size() > 0) {
                    Log.d(TAG, "111111 arraylist_ContactsPhoneNumFromClient CONTAINS AT LEAST ONE");
                    Log.d(TAG, "111111 AFTER REMOVED SIZE - " + arraylist_ContactsPhoneNumFromClient.size());
                    for (int i = 0; i < arraylist_ContactsPhoneNumFromClient.size(); i++) {
                        String key_forUpload = arraylist_ContactsPhoneNumFromClient.get(i);
                        mapForUpload.put(key_forUpload, "not_yet");
                        Log.d(TAG, "111111 PUT TO MAP TO UPLOAD - " + key_forUpload);
                    }

                    mFirebaseRef.child("users_contacts").child(userUid).updateChildren(mapForUpload);
                    Log.d(TAG, "222222 UPLOADED SIZE - " + mapForUpload.size());

                } else {
                    Log.d(TAG, "222222 NOTHING UPLOADED");
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        //  CHANGE INTO CHILDEVENTLISTENER - WORKS
//        app.mFirebaseRef.child("users_phone").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                Iterable<DataSnapshot> children_datasnapshots = snapshot.getChildren();
//                Iterator iterator = children_datasnapshots.iterator();
//                while (iterator.hasNext()) {
//
//                    String key_value = (String) iterator.next().toString();
//                    Log.d(TAG, "20150525 APP CUSTOMER ALREADY - " + key_value);
//                    // EXAMPLE : DataSnapshot { key = facebook:1573463126267040, value = 82n01026237641 }
//                    String[] parts = key_value.split(", value = ");
//                    String key = parts[0].replace("DataSnapshot { key = ", "");
//                    String value = parts[1].replace(" }", "");
//                    value.trim();
//                    Log.d(TAG, "20150525 APP CUSTOMER ALREADY KEY - '" + key+"'");
//                    Log.d(TAG, "20150525 APP CUSTOMER ALREADY VALUE - '" + value+"'");
//
//                    if (arraylist_ContactsPhoneNumFromClientOriginal.contains(value)) {
//                        mapForUploadAlreadyMember.put(key, value);
//                    }
//                }
//                app.mFirebaseRef.child("users_contacts").child(userUid).updateChildren(mapForUploadAlreadyMember);
//                Log.d(TAG, "20150525 APP CUSTOMER ALREADY UPLOADED SIZE - " + mapForUploadAlreadyMember.size());
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });
//
//
        // ALREADY APP CUSTOMER - ID, PHONE -> USERS FRIENDLIST
        mFirebaseRef.child("users_phone").addChildEventListener(new ChildEventListener() {  // child(mainActivity.userUid).

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                String key__id = dataSnapshot.getKey();
                Log.d(TAG, "20150526 KEY(ID_ALREADY MEM) - " + key__id);
                String value__phone = dataSnapshot.getValue().toString();
                Log.d(TAG, "20150526 VALUE(PHONE_ALREADY MEM) - " + value__phone);
                if (arraylist_ContactsPhoneNumFromClientOriginal.contains(value__phone)) {
                    mapForUploadAlreadyMember.put(value__phone, key__id);
                }
                mFirebaseRef.child("users_contacts").child(userUid).updateChildren(mapForUploadAlreadyMember);
                Log.d(TAG, "20150525 APP CUSTOMER ALREADY UPLOADED SIZE - " + mapForUploadAlreadyMember.size());
                // UPLOAD TO DB
                // RENEW FRIEND LIST
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                // UPLOAD TO DB
                // RENEW FRIEND LIST
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // SHOULD BE HANDLED
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
                // No-op
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });


        mFirebaseRef.child("users_contacts").child(userUid).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                String key__num = dataSnapshot.getKey();
                Log.d(TAG, "20150526 KEY PHONE (USERCONTACT) - " + key__num);
                String value__id = dataSnapshot.getValue().toString();
                Log.d(TAG, "20150526 VALUE ID_EMPTY (USERCONTACT) - " + value__id);

                if (!value__id.equals("not_yet")) {
                    if (verifyIfFriendIdExistInDB(value__id)) {
                        Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET  - KEY(ID) - EXISTS IN DB - " + value__id);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("num", key__num);
                        dbfriend.update("dbfriend", contentValues, "userid = '" + value__id + "'", null);

                    } else {
                        Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET  - KEY(ID) - NOT EXISTS IN DB - " + value__id);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("userid", value__id);
                        contentValues.put("num", key__num);
                        dbfriend.insert("dbfriend", null, contentValues);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                String key__num = dataSnapshot.getKey();
                Log.d(TAG, "20150526 KEY PHONE (USERCONTACT) - " + key__num);
                String value__id = dataSnapshot.getValue().toString();
                Log.d(TAG, "20150526 VALUE ID_EMPTY (USERCONTACT) - " + value__id);

                if (!value__id.equals("not_yet")) {
                    if (verifyIfFriendIdExistInDB(value__id)) {
                        Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET  - KEY(ID) - EXISTS IN DB - " + value__id);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("num", key__num);
                        dbfriend.update("dbfriend", contentValues, "userid = '" + value__id + "'", null);

                    } else {
                        Log.d(TAG, "20150525 APP CUSTOMER FOR CHAT TARGET  - KEY(ID) - NOT EXISTS IN DB - " + value__id);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("userid", value__id);
                        contentValues.put("num", key__num);
                        dbfriend.insert("dbfriend", null, contentValues);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // SHOULD BE HANDLED
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
                // No-op
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });


        mFirebaseRef.child("users").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                String id = dataSnapshot.getKey();
                Map<String, Object> userdatamap = (Map<String, Object>) dataSnapshot.getValue();
                String name = (String) userdatamap.get("displayName");
                Log.d(TAG, "20150526 USER ID - " + id);
                Log.d(TAG, "20150526 USER ID - " + name);

                if (verifyIfFriendIdExistInDB(id)) {
                    Log.d(TAG, "20150526 USER ID EXISTS IN DB - " + id);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name);
                    dbfriend.update("dbfriend", contentValues, "userid = '" + id + "'", null);

                } else {
                    Log.d(TAG, "20150526 USER ID NOT EXISTS IN DB - " + id);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("userid", id);
                    contentValues.put("num", name);
                    dbfriend.insert("dbfriend", null, contentValues);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                String id = dataSnapshot.getKey();
                Map<String, Object> userdatamap = (Map<String, Object>) dataSnapshot.getValue();
                String name = (String) userdatamap.get("displayName");
                Log.d(TAG, "20150526 USER ID - " + id);
                Log.d(TAG, "20150526 USER ID - " + name);

                if (verifyIfFriendIdExistInDB(id)) {
                    Log.d(TAG, "20150526 USER ID EXISTS IN DB - " + id);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", name);
                    dbfriend.update("dbfriend", contentValues, "userid = '" + id + "'", null);

                } else {
                    Log.d(TAG, "20150526 USER ID NOT EXISTS IN DB - " + id);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("userid", id);
                    contentValues.put("num", name);
                    dbfriend.insert("dbfriend", null, contentValues);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // SHOULD BE HANDLED
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
                // No-op
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });



        setContentView(R.layout.activity_main);
         ActionButton actionButton = (ActionButton) findViewById(R.id.action_button);
         actionButton.show();

//        ChatFragment chatFragment = new ChatFragment();
//        EventFragment eventFragment  = new EventFragment();
//        GroupFragment groupFragment = new GroupFragment();
//        MyProfileFragment myProfileFragment = new MyProfileFragment();
//        List<Fragment> mList = new ArrayList<>();
//        mList.add(chatFragment);
//        mList.add(eventFragment);
//        mList.add(groupFragment);
//        mList.add(myProfileFragment);
//        fragmentManager = getSupportFragmentManager();

            if (savedInstanceState == null) {
                int[] icon = {
                        R.drawable.tab_1,
                        R.drawable.tab_2,
                        R.drawable.tab_3,
                        R.drawable.tab_4
                };
                int pageLayout[] = new int[]{
                        R.layout.fragment_chat,
                        R.layout.fragment_event,
                        R.layout.fragment_group,
                        R.layout.fragment_myprofile
                };

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                slidingTabsBasicFragment = new SlidingTabsBasicFragment();
                // Tab options
                TabOption option = new TabOption();
                option.setPageCount(4);
                option.setIconHeader(true);
                option.setIcon(icon);
                option.setPageLayout(pageLayout);
                option.setPageFragment(new MainTabFragment());

                // Put them in bundle and render it to sliding basic fragment
                Bundle b = new Bundle();
                b.putParcelable("taboption", option);
                slidingTabsBasicFragment.setArguments(b);

                transaction.replace(R.id.sample_content_fragment, slidingTabsBasicFragment);
                transaction.commit();


            }
            toolbarLayout = (RelativeLayout) findViewById(R.id.toolbar_layout);


    }

    public void startLoginActivityFB(){
        Intent intent= new Intent(this, LoginActivityFB.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.sample_content_fragment);
//        fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class FriendDBHelper extends SQLiteOpenHelper {
        public FriendDBHelper(Context context){
            super(context, "dbfriend.db",null,1);
        }
        public void onCreate(SQLiteDatabase db){
            db.execSQL("CREATE TABLE IF NOT EXISTS dbfriend ( _id INTEGER PRIMARY KEY AUTOINCREMENT, userid TEXT , name TEXT, num TEXT, image TEXT);");
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS dbfriend");
            onCreate(db);
        }

    }


    public boolean verifyIfFriendIdExistInDB(String id) {
        Cursor c = dbfriend.rawQuery("SELECT 1 FROM "+"dbfriend"+" WHERE "+"userid"+"=?", new String[] {id});
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;
        private List<Fragment> fragmentList;
        private List<String> titleList;
        private List<Integer> iconList;

        public MyFragmentPagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {  //, List<Fragment> list
            super(fragmentManager);
//            fragmentList = list;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return fragmentList.size();
        }

//        public Fragment getItem(int arg0) {
//            return fragmentList.get(arg0);
//        }

        // Returns the fragment to display for that page
         public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ChatFragment.newInstance(0, "Page # 4");

                case 1: // Fragment # 0 - This will show FirstFragment different title
//                    return Main_fragment_event.newInstance(1, "Page # 2");
                case 2: // Fragment # 1 - This will show SecondFragment
//                    return Main_fragment_group.newInstance(2, "Page # 3");
                case 3: // Fragment # 1 - This will show SecondFragment
                    return MyProfileFragment.newInstance(3, "Page # 4");
                default:
                    return null;
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        public int getIconResId(int index) {
            if (iconList != null)
                return iconList.get(index);
            return 0;
        }
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}
