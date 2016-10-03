//package jacklee_entertainment.niceneat.activity;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.google.android.gms.common.*;
//import com.google.android.gms.common.GooglePlayServicesClient.*;
//import com.google.android.gms.plus.PlusClient;
//
//public class MyLoginActivity extends Activity implements View.OnClickListener,
//        ConnectionCallbacks, OnConnectionFailedListener {
//    private static final String TAG = "ExampleActivity";
//    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
//
//    private ProgressDialog mConnectionProgressDialog;
//    private PlusClient mPlusClient;
//    private ConnectionResult mConnectionResult;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPlusClient = new PlusClient.Builder(this, this, this)
//                .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
//                .build();
 //        mConnectionProgressDialog = new ProgressDialog(this);
//        mConnectionProgressDialog.setMessage("Signing in...");
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mPlusClient.connect();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mPlusClient.disconnect();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        if (result.hasResolution()) {
//            try {
//                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
//            } catch (IntentSender.SendIntentException e) {
//                mPlusClient.connect();
//            }
//        }
 //        mConnectionResult = result;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
//        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
//            mConnectionResult = null;
//            mPlusClient.connect();
//        }
//    }
//
//    @Override
//    public void onConnected() {
//        String accountName = mPlusClient.getAccountName();
//        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onDisconnected() {
//        Log.d(TAG, "disconnected");
//    }
//}