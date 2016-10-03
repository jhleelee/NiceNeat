//package jacklee_entertainment.niceneat.utils;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.telephony.SmsManager;
//import android.telephony.SmsMessage;
//import android.util.Log;
//
///**
// * Created by user on 2015-05-15.
// */
//public class IncomingSMS extends BroadcastReceiver{
//
//    final SmsManager smsManagerForReceive = SmsManager.getDefault();
//    String phonenumber_sms;
//    String verifycode_comein;
//
//    public void onReceive(Context context, Intent intent){
//        final Bundle bundle = intent.getExtras();
//
//        try {
//
//            if(bundle != null){
//                final Object[] pdusObj = (Object[])bundle.get("pdus");
//                for (int i = 0; i<pdusObj.length; i++){
//                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
//                    phonenumber_sms = smsMessage.getDisplayOriginatingAddress();
//                    String body_sms = smsMessage.getDisplayMessageBody();
//                    verifycode_comein = removeCharExceptNumber(body_sms);
//
//                }
//            }
//
//
//        } catch (Exception e){
//            Log.e("SmsReceiver", "Exception smsReceiver" + e);
//        }
//
//    }
//
//    public static String removeCharExceptNumber(String str) {
//        return str.replaceAll("^[0-9]", "");
//
//    }
//}
