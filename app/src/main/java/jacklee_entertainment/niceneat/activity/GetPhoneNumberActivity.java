package jacklee_entertainment.niceneat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import jacklee_entertainment.niceneat.App;
import jacklee_entertainment.niceneat.R;

/**
 * Created by user on 2015-05-15.
 */
public class GetPhoneNumberActivity extends Activity{

    /* *************************************
*              GENERAL                *
***************************************/
    private ProgressDialog mAuthProgressDialog;

    public  Firebase mFirebaseRef;
    public AuthData mAuthData;
    public  Map<String, String> usermap ;
    public String userUid;

    Toolbar toolbar;
    EditText edittext_nationalcode,edittext_phonenumber;
    EditText        edittext_verifycode;
    Button button_verifycode;
    TextView     textview_countryname;
    TextView     textview_timer;
    String countryCodeInText;
    String countryDialCode;
    String verifycode_out;
    String verifycode_comein;
    String nationalcode;
    String countryName;
    String phonenumber;
    String phonenumber_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_getphonenumber);
        // FIREBASE
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        try {
            mAuthData = mFirebaseRef.getAuth();} catch (Exception e) {mAuthData=null;};
        usermap = new HashMap<String,String>();

//                app.mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
//        mAuthProgressDialog.show();



                edittext_nationalcode = (EditText)findViewById(R.id.edittext_nationalcode);
        edittext_phonenumber = (EditText)findViewById(R.id.edittext_phonenumber);
        textview_timer = (TextView)findViewById(R.id.textview_timer);
        textview_countryname = (TextView)findViewById(R.id.textview_countryname);

        edittext_verifycode = (EditText)findViewById(R.id.edittext_verifycode);
        edittext_verifycode.setMaxLines(1);

        button_verifycode = (Button)findViewById(R.id.button_verifycode);

        edittext_nationalcode.setMaxLines(1);
        edittext_nationalcode.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        edittext_nationalcode.setImeOptions(EditorInfo.IME_ACTION_NEXT | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(4);
        edittext_nationalcode.setFilters(inputFilters);
        countryDialCode = getCountryDialCode();









        edittext_nationalcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    String[] rl = getApplication().getResources().getStringArray(R.array.CountryCodes);
                    for (int i = 0; i < rl.length; i++) {
                        String[] g = rl[i].split(",");
                        if (g[0].trim().equals(s.toString())) {
                            countryCodeInText = g[1];
                            break;
                        }
                    }
                    Locale locale = new Locale("", countryCodeInText);
                    countryName = locale.getDisplayCountry();
                    textview_countryname.setText(countryName);
                } catch (Exception e) {
                    textview_countryname.setText("Input Country Code ...");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });




        edittext_nationalcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    edittext_phonenumber.requestFocus();
                    return true;
                }
                return false;
            }
        });

        edittext_nationalcode.setText(countryDialCode);






         edittext_phonenumber.setMaxLines(1);
        edittext_phonenumber.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        edittext_phonenumber.setImeOptions(EditorInfo.IME_ACTION_NEXT | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        button_verifycode.setOnClickListener(button_verifycode_sendsms_OnClickListener);
    }


    protected void onResume() {
        super.onResume();
        registerReceiver(incomingSMSBR, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(incomingSMSBR);
    }

    BroadcastReceiver incomingSMSBR = new BroadcastReceiver(){

        public void onReceive(Context context, Intent intent){
            final Bundle bundle = intent.getExtras();
            try {
                if(bundle != null){
                    final Object[] pdusObj = (Object[])bundle.get("pdus");
                    for (int i = 0; i<pdusObj.length; i++){
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        phonenumber_sms = smsMessage.getDisplayOriginatingAddress();
                        String body_sms = smsMessage.getDisplayMessageBody();
                        String verifycode_comein = body_sms.trim().replace("Niceneat Code ","");
                        if ( verifycode_out.equals(verifycode_comein)&&(phonenumber.equals(phonenumber_sms)) ){
                            // ** upload to firebase **
                            nationalcode=edittext_nationalcode.getText().toString();
                            String nationalcode_n_phone = nationalcode +"_"+ phonenumber_sms;
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(mAuthData.getUid(),nationalcode_n_phone);
                             (mFirebaseRef).child("users_phone").setValue(map);
                            // ** save to phone **
                            savePreferences("nationalcode_n_phone", nationalcode_n_phone);
                            savePreferences("nationalcode", nationalcode);

                            // ** go **
                            startMainAcitivity();
                            finish();
                        } else {

                        }
//
                    }
                }


            } catch (Exception e){
                Log.e("SmsReceiver", "Exception smsReceiver" + e);
            }

        }

        public String removeCharExceptNumber(String str) {
            return str.replaceAll("^[0-9]", "");

        }

    };



        public String getCountryDialCode(){
            String CountryID="";
            String countryDialCode="";
            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            //getNetworkCountryIso
            CountryID= manager.getSimCountryIso().toUpperCase();
            String[] rl= this.getResources().getStringArray(R.array.CountryCodes);
            for(int i=0 ;i<rl.length;i++){
                String[] g =rl[i].split(",");
                if(g[1].trim().equals(CountryID.trim())){
                    countryDialCode=g[0];
                    break;
            }
        }
        return countryDialCode;
    }



    public View.OnClickListener button_verifycode_sendsms_OnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            phonenumber = "";
            phonenumber = edittext_phonenumber.getText().toString();

            verifycode_out = "";
            Random random = new Random();
            verifycode_out = String.valueOf(random.nextInt(99998 - 10001) + 10000);
            String message = getResources().getString(R.string.button_verifycode) +" "+ verifycode_out;
            SmsManager smsManager = SmsManager.getDefault();
            try {
                smsManager.sendTextMessage(phonenumber, null, message, null, null);
            } catch (Exception e){

            }

//            button_verifycode.setClickable(false);
//            CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    textview_timer.setText("00:" + millisUntilFinished/1000);
//                }
//
//                @Override
//                public void onFinish() {
//
//                }
//            };

        }
    };



    private View.OnClickListener button_verifycode_compare_OnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            phonenumber = "";
            verifycode_comein = null;


            if(verifycode_out==verifycode_comein){

                // save phone num
           // save preferences
                startMainAcitivity();
                finish();
            } else {

            }

        }
    };

    private void savePreferences(String key, String value){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // if key does not exists
        editor.putString(key, value);
        editor.commit();
     }


    private void startMainAcitivity(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

//    private void registerSmsReciever() {
//        incomingSMS = new IncomingSMS();
//        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//        intentFilter.setPriority(2147483647);
//        this.registerReceiver(incomingSMS, intentFilter);
//    }

//    private class IncomingSMS extends BroadcastReceiver {
//
//        final SmsManager smsManagerForReceive = SmsManager.getDefault();
//        String phonenumber_sms;
//        Context context;
//
//
//        public void onReceive(Context context, Intent intent){
//            final Bundle bundle = intent.getExtras();
//            context.unregisterReceiver(this);
//            try {
//
//                if(bundle != null){
//                    final Object[] pdusObj = (Object[])bundle.get("pdus");
//                    for (int i = 0; i<pdusObj.length; i++){
//                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
//                        phonenumber_sms = smsMessage.getDisplayOriginatingAddress();
//                        String body_sms = smsMessage.getDisplayMessageBody();
//
//                            verifycode_comein = body_sms.replace("Niceneat Code ","");
//                            edittext_verifycode.setText(verifycode_comein);
//                            if (verifycode_out == verifycode_comein) {
//                                // register telephone
//                                // save preferences
//                                startMainAcitivity();
//                                finish();
//                            } else  {
//
//                        }
//
//                    }
//                }
//
//
//            } catch (Exception e){
//                Log.e("SmsReceiver", "Exception smsReceiver" + e);
//            }
//
//        }
//
//        public String removeCharExceptNumber(String str) {
//            return str.replaceAll("^[0-9]", "");
//
//        }
//    }




}
