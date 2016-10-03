//package jacklee_entertainment.niceneat.utils;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.provider.ContactsContract;
//
//import com.firebase.client.AuthData;
//import com.firebase.client.ChildEventListener;
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//import jacklee_entertainment.niceneat.App;
//
///**
// * Created by user on 2015-05-08.
// */
//public class ContactsPhoneNumUpdater {
//
//
//
//    public List<String> getFriendsPhoneNumFromServer (Firebase firebaseref, AuthData authData) {
//        List<String> friendsPhoneNumFromServer = new ArrayList<String>();
//        firebaseref.child("users_contacts").child(authData.getUid()).addChildEventListener(new ChildEventListener(){
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
//                Map<String, Object> newFriendsPhoneNumFromServer = (Map<String, Object>) dataSnapshot.getValue();
//            }
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
//                Map<String, Object> newFriendsPhoneNumFromServer = (Map<String, Object>) dataSnapshot.getValue();
//            }
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Map<String, Object> newFriendsPhoneNumFromServer = (Map<String, Object>) dataSnapshot.getValue();
//            }
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildKey) {
//                Map<String, Object> newFriendsPhoneNumFromServer = (Map<String, Object>) dataSnapshot.getValue();
//            }
//            public void onCancelled(FirebaseError firebaseError) {
//            }
//        }) ;
//
//
//
//
//
//
//
//
//
//
//
//
//
//        return friendsPhoneNumFromServer;
//
//    }
//
//    public void saveFriendsPhoneNumAfterCheckDuplication(List<String> ContactsPhoneNumFromClient, List<String> FriendsPhoneNumFromServer , Context context){
//
//
//    Map<String, String> map = new HashMap<String, String>();
//
//        for(i=0; i<list.length(); i++) {
//            map.put(app.mAuthData.getUid(),nationalcode_n_phone);}
//
//    (app.mFirebaseRef).child("users_phone").setValue(map);
//    }
//
//}
