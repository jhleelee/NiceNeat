//package jacklee_entertainment.niceneat.activity;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AlertDialog;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.firebase.client.AuthData;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.google.android.gms.auth.GoogleAuthException;
//import com.google.android.gms.auth.GoogleAuthUtil;
//import com.google.android.gms.auth.UserRecoverableAuthException;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.Scopes;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.plus.Plus;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import jacklee_entertainment.niceneat.App;
// import jacklee_entertainment.niceneat.R;
//import jacklee_entertainment.niceneat.fragment.dialog.AlertDialogFragment;
//import jacklee_entertainment.niceneat.fragment.dialog.ProgressDialogFragment;
//
//
//public class LoginActivity extends Activity implements
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener {
//
//    LinearLayout ly_loginbuttons, ly_signincustom, ly_signupcustom;
//    EditText  edittext_emailid_signin, edittext_emailpw_signin, edittext_emailid_signup, edittext_emailpw_signup;
//    Button         btn_begin_signup_email, btn_email_signin, btn_forgot_emailpw_signin, btn_verifyemail;
//    private static final String TAG = MainActivity.class.getSimpleName();
//
//    private String login;
//    private boolean startup;
//
//    /* *************************************
// *              GENERAL                *
// ***************************************/
//    /* TextView that is used to display information about the logged in user */
////    private TextView mLoggedInStatusTextView;
//
//    /* A dialog that is presented until the Firebase authentication finished. */
//    private ProgressDialog mAuthProgressDialog;
//
//    /* A reference to the Firebase */
//    private Firebase mFirebaseRef;
//
//    /* Data from the authenticated user */
//    private AuthData mAuthData;
//
//    /* *************************************
//     *              FACEBOOK               *
//     ***************************************/
//    /* The login button for Facebook */
//    private LoginButton mFacebookLoginButton;
//    CallbackManager callbackManager;
//
//    /* *************************************
//     *              GOOGLE                 *
//     ***************************************/
//    /* Request code used to invoke sign in user interactions for Google+ */
//    public static final int RC_GOOGLE_LOGIN = 1;
//
//    /* Client used to interact with Google APIs. */
//    private GoogleApiClient mGoogleApiClient;
//
//    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
//    private boolean mGoogleIntentInProgress;
//
//    /* Track whether the sign-in button has been clicked so that we know to resolve all issues preventing sign-in
//     * without waiting. */
//    private boolean mGoogleLoginClicked;
//
//    /* Store the connection result from onConnectionFailed callbacks so that we can resolve them when the user clicks
//     * sign-in. */
//    private ConnectionResult mGoogleConnectionResult;
//
//    /* The login button for Google */
//    private SignInButton mGoogleLoginButton;
//
//    /* *************************************
//     *              TWITTER                *
//     ***************************************/
//    public static final int RC_TWITTER_LOGIN = 2;
//
//    private Button mTwitterLoginButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//                /* *************************************
//         *              FACEBOOK               *
//         ***************************************/
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//
//        setContentView(R.layout.activity_login);
//
//
//
//        mFacebookLoginButton = (LoginButton) findViewById(R.id.login_with_facebook);
//        mFacebookLoginButton.setReadPermissions("user_friends");
//
//        // Other app specific specialization
//
//         // Callback registration
//        mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
//
//        /* *************************************
//         *               GOOGLE                *
//         ***************************************/
//        /* Load the Google login button */
//        mGoogleLoginButton = (SignInButton) findViewById(R.id.login_with_google);
//        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mGoogleLoginClicked = true;
//                if (!mGoogleApiClient.isConnecting()) {
//                    if (mGoogleConnectionResult != null) {
//                        resolveSignInError();
//                    } else if (mGoogleApiClient.isConnected()) {
//                        getGoogleOAuthTokenAndLogin();
//                    } else {
//                    /* connect API now */
//                        Log.d(TAG, "Trying to connect to Google API");
//                        mGoogleApiClient.connect();
//                    }
//                }
//            }
//        });
//        /* Setup the Google API object to allow Google+ logins */
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API)
//                .addScope(Plus.SCOPE_PLUS_LOGIN)
//                .build();
//
//        /* *************************************
//         *                TWITTER              *
//         ***************************************/
//        mTwitterLoginButton = (Button) findViewById(R.id.login_with_twitter);
//        mTwitterLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginWithTwitter();
//            }
//        });
//
//
//        startup = true;
//
//        ly_loginbuttons= (LinearLayout)findViewById(R.id.ly_loginbuttons);
//        ly_signincustom= (LinearLayout)findViewById(R.id.ly_signincustom);
//        ly_signupcustom= (LinearLayout)findViewById(R.id.ly_signupcustom);
//        edittext_emailid_signin = (EditText)findViewById(R.id.edittext_emailid_signin);
//        edittext_emailpw_signin = (EditText)findViewById(R.id.edittext_emailpw_signin);
//        edittext_emailid_signup = (EditText)findViewById(R.id.edittext_emailid_signup);
//        edittext_emailpw_signup = (EditText)findViewById(R.id.edittext_emailpw_signup);
//        btn_forgot_emailpw_signin = (Button)findViewById(R.id.btn_forgot_emailpw_signin);
//        btn_verifyemail = (Button)findViewById(R.id.btn_verifyemail);
//        btn_email_signin = (Button)findViewById(R.id.btn_email_signin);
//
//
//                /* *************************************
//         *               GENERAL               *
//         ***************************************/
////        mLoggedInStatusTextView = (TextView) findViewById(R.id.login_status);
//
//        /* Create the Firebase ref that is used for all authentication with Firebase */
//        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
//
//        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
//        mAuthProgressDialog = new ProgressDialog(this);
//        mAuthProgressDialog.setTitle("Loading");
//        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
//        mAuthProgressDialog.setCancelable(false);
//        mAuthProgressDialog.show();
//
//        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
//         * user and hide hide any login buttons */
//        mFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(AuthData authData) {
//                mAuthProgressDialog.hide();
//                setAuthenticatedUser(authData);
//            }
//        });
//    }
//
//    /**
//     * This method fires when any startActivityForResult finishes. The requestCode maps to
//     * the value passed into startActivityForResult.
//     */
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Map<String, String> options = new HashMap<String, String>();
//        if (requestCode == RC_GOOGLE_LOGIN) {
//            /* This was a request by the Google API */
//            if (resultCode != RESULT_OK) {
//                mGoogleLoginClicked = false;
//            }
//            mGoogleIntentInProgress = false;
//            if (!mGoogleApiClient.isConnecting()) {
//                mGoogleApiClient.connect();
//            }
//        } else if (requestCode == RC_TWITTER_LOGIN) {
//            options.put("oauth_token", data.getStringExtra("oauth_token"));
//            options.put("oauth_token_secret", data.getStringExtra("oauth_token_secret"));
//            options.put("user_id", data.getStringExtra("user_id"));
//            authWithFirebase("twitter", options);
//        } else {
//            /* Otherwise, it's probably the request by the Facebook login button, keep track of the session */
//             callbackManager.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//
//
//
//    public void startMainAcitivity(){
//        Intent intent=new Intent(this,MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//
//
//
//
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        /* If a user is currently authenticated, display a logout menu */
//        if (this.mAuthData != null) {
//            getMenuInflater().inflate(R.menu.main, menu);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_logout) {
//            logout();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    /**
//     * Unauthenticate from Firebase and from providers where necessary.
//     */
//    private void logout() {
//        if (this.mAuthData != null) {
//            /* logout of Firebase */
//            mFirebaseRef.unauth();
//            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into
//             * Facebook/Google+ after logging out of Firebase. */
////            if (this.mAuthData.getProvider().equals("facebook")) {
////                /* Logout from Facebook */
////                Session session = Session.getActiveSession();
////                if (session != null) {
////                    if (!session.isClosed()) {
////                        session.closeAndClearTokenInformation();
////                    }
////                } else {
////                    session = new Session(getApplicationContext());
////                    Session.setActiveSession(session);
////                    session.closeAndClearTokenInformation();
////                }
////            } else
//            if (this.mAuthData.getProvider().equals("google")) {
//                /* Logout from Google+ */
//                if (mGoogleApiClient.isConnected()) {
//                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                    mGoogleApiClient.disconnect();
//                }
//            }
//            /* Update authenticated user and show login buttons */
//            setAuthenticatedUser(null);
//        }
//    }
//
//    /**
//     * This method will attempt to authenticate a user to firebase given an oauth_token (and other
//     * necessary parameters depending on the provider)
//     */
//    private void authWithFirebase(final String provider, Map<String, String> options) {
//        if (options.containsKey("error")) {
//            showErrorDialog(options.get("error"));
//        } else {
//            mAuthProgressDialog.show();
//            if (provider.equals("twitter")) {
//                // if the provider is twitter, we pust pass in additional options, so use the options endpoint
//                mFirebaseRef.authWithOAuthToken(provider, options, new AuthResultHandler(provider));
//            } else {
//                // if the provider is not twitter, we just need to pass in the oauth_token
//                mFirebaseRef.authWithOAuthToken(provider, options.get("oauth_token"), new AuthResultHandler(provider));
//            }
//        }
//    }
//
//    /**
//     * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
//     */
//    private void setAuthenticatedUser(AuthData authData) {
//        if (authData != null) {
//            /* Hide all the login buttons */
//            mFacebookLoginButton.setVisibility(View.GONE);
//            mGoogleLoginButton.setVisibility(View.GONE);
//            mTwitterLoginButton.setVisibility(View.GONE);
// //            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
//            /* show a provider specific status text */
//            String name = null;
//            if (authData.getProvider().equals("facebook")
//                    || authData.getProvider().equals("google")
//                    || authData.getProvider().equals("twitter")) {
//                name = (String) authData.getProviderData().get("displayName");
//            } else if (authData.getProvider().equals("anonymous")
//                    || authData.getProvider().equals("password")) {
//                name = authData.getUid();
//            } else {
//                Log.e(TAG, "Invalid provider: " + authData.getProvider());
//            }
//            if (name != null) {
////                mLoggedInStatusTextView.setText("Logged in as " + name + " (" + authData.getProvider() + ")");
//            }
//        } else {
//            /* No authenticated user show all the login buttons */
//            mFacebookLoginButton.setVisibility(View.VISIBLE);
//            mGoogleLoginButton.setVisibility(View.VISIBLE);
//            mTwitterLoginButton.setVisibility(View.VISIBLE);
////             mLoggedInStatusTextView.setVisibility(View.GONE);
//        }
//        this.mAuthData = authData;
//        /* invalidate options menu to hide/show the logout button */
////        supportInvalidateOptionsMenu();
//    }
//
//    /**
//     * Show errors to users
//     */
//    private void showErrorDialog(String message) {
//        new AlertDialog.Builder(this)
//                .setTitle("Error")
//                .setMessage(message)
//                .setPositiveButton(android.R.string.ok, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//    }
//
//    /**
//     * Utility class for authentication results
//     */
//    private class AuthResultHandler implements Firebase.AuthResultHandler {
//
//        private final String provider;
//
//        public AuthResultHandler(String provider) {
//            this.provider = provider;
//        }
//
//        @Override
//        public void onAuthenticated(AuthData authData) {
//            mAuthProgressDialog.hide();
//            Log.i(TAG, provider + " auth successful");
//            setAuthenticatedUser(authData);
//        }
//
//        @Override
//        public void onAuthenticationError(FirebaseError firebaseError) {
//            mAuthProgressDialog.hide();
//            showErrorDialog(firebaseError.toString());
//        }
//    }
//
//    /* ************************************
//     *             FACEBOOK               *
//     **************************************
//     */
//    /* Handle any changes to the Facebook session */
////    private void onFacebookSessionStateChange(Session session, SessionState state, Exception exception) {
////        if (state.isOpened()) {
////            mAuthProgressDialog.show();
////            mFirebaseRef.authWithOAuthToken("facebook", session.getAccessToken(), new AuthResultHandler("facebook"));
////        } else if (state.isClosed()) {
////            /* Logged out of Facebook and currently authenticated with Firebase using Facebook, so do a logout */
////            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")) {
////                mFirebaseRef.unauth();
////                setAuthenticatedUser(null);
////            }
////        }
////    }
//
//
//    /* ************************************
//     *              GOOGLE                *
//     **************************************
//     */
//    /* A helper method to resolve the current ConnectionResult error. */
//    private void resolveSignInError() {
//        if (mGoogleConnectionResult.hasResolution()) {
//            try {
//                mGoogleIntentInProgress = true;
//                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
//            } catch (IntentSender.SendIntentException e) {
//                // The intent was canceled before it was sent.  Return to the default
//                // state and attempt to connect to get an updated ConnectionResult.
//                mGoogleIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
//    }
//
//    private void getGoogleOAuthTokenAndLogin() {
//        mAuthProgressDialog.show();
//        /* Get OAuth token in Background */
//        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
//            String errorMessage = null;
//
//            @Override
//            protected String doInBackground(Void... params) {
//                String token = null;
//
//                try {
//                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
//                    token = GoogleAuthUtil.getToken(LoginActivity.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
//                } catch (IOException transientEx) {
//                    /* Network or server error */
//                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
//                    errorMessage = "Network error: " + transientEx.getMessage();
//                } catch (UserRecoverableAuthException e) {
//                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
//                    /* We probably need to ask for permissions, so start the intent if there is none pending */
//                    if (!mGoogleIntentInProgress) {
//                        mGoogleIntentInProgress = true;
//                        Intent recover = e.getIntent();
//                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
//                    }
//                } catch (GoogleAuthException authEx) {
//                    /* The call is not ever expected to succeed assuming you have already verified that
//                     * Google Play services is installed. */
//                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
//                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
//                }
//                return token;
//            }
//
//            @Override
//            protected void onPostExecute(String token) {
//                mGoogleLoginClicked = false;
//                if (token != null) {
//                    /* Successfully got OAuth token, now login with Google */
//                    mFirebaseRef.authWithOAuthToken("google", token, new AuthResultHandler("google"));
//                } else if (errorMessage != null) {
//                    mAuthProgressDialog.hide();
//                    showErrorDialog(errorMessage);
//                }
//            }
//        };
//        task.execute();
//    }
//
//    @Override
//    public void onConnected(final Bundle bundle) {
//        /* Connected with Google API, use this to authenticate with Firebase */
//        getGoogleOAuthTokenAndLogin();
//    }
//
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        if (!mGoogleIntentInProgress) {
//            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
//            mGoogleConnectionResult = result;
//
//            if (mGoogleLoginClicked) {
//                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
//                 * or they cancel. */
//                resolveSignInError();
//            } else {
//                Log.e(TAG, result.toString());
//            }
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        // ignore
//    }
//
//    /* ************************************
//     *               TWITTER              *
//     **************************************
//     */
//    private void loginWithTwitter() {
//        startActivityForResult(new Intent(this, TwitterOAuthActivity.class), RC_TWITTER_LOGIN);
//    }
//
//
//}
