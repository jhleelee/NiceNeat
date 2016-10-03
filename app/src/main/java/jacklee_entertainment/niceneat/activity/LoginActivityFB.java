package jacklee_entertainment.niceneat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.facebook.FacebookException;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jacklee_entertainment.niceneat.App;
import jacklee_entertainment.niceneat.R;

/**
 * Created by user on 2015-05-15.
 */
public class LoginActivityFB extends Activity {

        private static final String TAG = LoginActivityFB.class.getSimpleName();
    public  Firebase mFirebaseRef;
    public AuthData mAuthData;
    public  Map<String, String> usermap ;
    public String userUid;    /* *************************************
 *              GENERAL                *
 ***************************************/
//    public  Firebase mFirebaseRef;
     private ProgressDialog mAuthProgressDialog;
         /* *************************************
         *              FACEBOOK               *
         ***************************************/
        private LoginButton mFacebookLoginButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
            usermap = new HashMap<>();
            /* *************************************
            *              FACEBOOK               *
            ***************************************/
            setContentView(R.layout.activity_login);
            mFacebookLoginButton = (LoginButton) findViewById(R.id.login_with_facebook);
//            mFacebookLoginButton.setBackgroundResource(R.drawable.facebook_signin_btn);
//            mFacebookLoginButton.setText(getResources().getText(R.string.login_with_facebook));
            mFacebookLoginButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            mFacebookLoginButton.setSessionStatusCallback(new Session.StatusCallback(){
                public void call(Session session, SessionState state, Exception exception){
                    onFacebookSessionStateChange(session, state, exception);
                }
            });

            /* *************************************
            *               GENERAL               *
            ***************************************/

            mAuthProgressDialog = new ProgressDialog(this);
            mAuthProgressDialog.setTitle("Loading");
            mAuthProgressDialog.setMessage("Authenticating with Firebase...");
            mAuthProgressDialog.setCancelable(false);
            mAuthProgressDialog.show();

        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
            mFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    mAuthProgressDialog.hide();
                    setAuthenticatedUser(authData);
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }

        private void onFacebookSessionStateChange(Session session, SessionState state, Exception exception){
            if (state.isOpened()) {
                mAuthProgressDialog.show();
                mFirebaseRef.authWithOAuthToken("facebook", session.getAccessToken(), new Firebase.AuthResultHandler(){
                    public void onAuthenticated(AuthData authData){
                    mAuthData = authData;
                    usermap.put("provider", authData.getProvider());
                    if(authData.getProviderData().containsKey("id")){
                        usermap.put("provider_id", (authData).getProviderData().get("id").toString());
                    }
                    if(authData.getProviderData().containsKey("displayName")){
                        usermap.put("displayName", (authData).getProviderData().get("displayName").toString());
                    }
                    (((mFirebaseRef).child("users")).child((authData.getUid()))).setValue(usermap);

                        SharedPreferences sharedPreferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
                    final String sharedPreferences_nationalcode_n_phone = sharedPreferences.getString("nationalcode_n_phone",null);

                    if (sharedPreferences_nationalcode_n_phone == null) {
                        startGetPhoneNumberActivity();
                    }

                    mFirebaseRef.child("users_phone").child(authData.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                startGetPhoneNumberActivity();

                            } else if (snapshot.exists()) {
                                String snapshotString = snapshot.getValue().toString();
                                if (!snapshotString.equals(sharedPreferences_nationalcode_n_phone)) {
                                    startGetPhoneNumberActivity();
                                } else if(snapshotString.equals(sharedPreferences_nationalcode_n_phone)) {
                                    startMainAcitivity();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError error) {
                        }

                    });

                    // else startMainActivity();

                    }

                    public void onAuthenticationError(FirebaseError firebaseError){

                    }
                });

                ;
            }else if(state.isClosed()) {
                if(mAuthData !=null && mAuthData.getProvider().equals("facebook")){
                    mFirebaseRef.unauth();
                    setAuthenticatedUser(null);
                }
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */

            if (mAuthData != null) {
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_logout) {
                logout();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        /**
         * Unauthenticate from Firebase and from providers where necessary.
         */
        private void logout() {
            if (mAuthData != null) {
                 mFirebaseRef.unauth();
                 setAuthenticatedUser(null);
            }
        }

        private void authWithFirebase(final String provider, Map<String, String> options) {

            if (options.containsKey("error")) {
                showErrorDialog(options.get("error"));
            } else {
                mAuthProgressDialog.show();
                if (provider.equals("twitter")) {
                    // if the provider is twitter, we pust pass in additional options, so use the options endpoint
                    mFirebaseRef.authWithOAuthToken(provider, options, new AuthResultHandler(provider));
                } else {
                    // if the provider is not twitter, we just need to pass in the oauth_token
                    mFirebaseRef.authWithOAuthToken(provider, options.get("oauth_token"), new AuthResultHandler(provider));
                }
            }
        }

        /**
         * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
         */
        private void setAuthenticatedUser(AuthData authData) {
            if (authData != null) {

             /* show a provider specific status text */
                String name = null;
                if (authData.getProvider().equals("facebook")
                        || authData.getProvider().equals("google")
                        || authData.getProvider().equals("twitter")) {
                    name = (String) authData.getProviderData().get("displayName");
                } else if (authData.getProvider().equals("anonymous")
                        || authData.getProvider().equals("password")) {
                    name = authData.getUid();
                } else {
                    Log.e(TAG, "Invalid provider: " + authData.getProvider());
                }
                if (name != null) {
//                mLoggedInStatusTextView.setText("Logged in as " + name + " (" + authData.getProvider() + ")");
                }



                mAuthData = authData;

                usermap.put("provider",authData.getProvider());
                if(authData.getProviderData().containsKey("id")){
                    usermap.put("provider_id", (authData).getProviderData().get("id").toString());
                }
                if(authData.getProviderData().containsKey("displayName")){
                    usermap.put("displayName", (authData).getProviderData().get("displayName").toString());
                }
                (((mFirebaseRef).child("users")).child((authData.getUid()))).setValue(usermap);

                SharedPreferences sharedPreferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
                final String sharedPreferences_nationalcode_n_phone = sharedPreferences.getString("nationalcode_n_phone",null);

                if (sharedPreferences_nationalcode_n_phone == null) {
                    startGetPhoneNumberActivity();
                }

                mFirebaseRef.child("users_phone").child(authData.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            startGetPhoneNumberActivity();

                        } else if (snapshot.exists()) {
                            String snapshotString = snapshot.getValue().toString();
                            if (!snapshotString.equals(sharedPreferences_nationalcode_n_phone)) {
                                startGetPhoneNumberActivity();
                            } else if(snapshotString.equals(sharedPreferences_nationalcode_n_phone)) {
                                startMainAcitivity();
                            }
                        }

                    }

                    @Override
    public void onCancelled(FirebaseError error) {
    }

});
        startMainAcitivity();
        } else {
            /* No authenticated user show all the login buttons */
        mAuthData = null;
        mFacebookLoginButton.setVisibility(View.VISIBLE);
        }

        /* invalidate options menu to hide/show the logout button */
//        supportInvalidateOptionsMenu();
        }

        /**
         * Show errors to users
         */
        private void showErrorDialog(String message) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        private class AuthResultHandler implements Firebase.AuthResultHandler {

            private final String provider;

            public AuthResultHandler(String provider) {
                this.provider = provider;
            }

            @Override
            public void onAuthenticated(AuthData authData) {
                mAuthProgressDialog.hide();
                Log.i(TAG, provider + " auth successful");
                setAuthenticatedUser(authData);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                mAuthProgressDialog.hide();
                showErrorDialog(firebaseError.toString());
            }
        }

    /* ************************************
     *             FACEBOOK               *
     **************************************
     */
    /* Handle any changes to the Facebook session */
//    private void onFacebookSessionStateChange(Session session, SessionState state, Exception exception) {
//        if (state.isOpened()) {
//            mAuthProgressDialog.show();
//            mFirebaseRef.authWithOAuthToken("facebook", session.getAccessToken(), new AuthResultHandler("facebook"));
//        } else if (state.isClosed()) {
//            /* Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout */
//            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
//                mFirebaseRef.unauth();
//                setAuthenticatedUser(null);
//            }
//        }
//    }

    public void startMainAcitivity(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void startGetPhoneNumberActivity(){
        Intent intent=new Intent(this,GetPhoneNumberActivity.class);
        startActivity(intent);
        finish();
    }
    }
