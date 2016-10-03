package jacklee_entertainment.niceneat;

import android.app.Application;
import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {


//    public static final String USER_ID_TWITTER = "2446056205";
//    public static final String USER_ID_LINKED_IN = "WQlagxgbbw";
    private static Context appContext;

    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        appContext = getApplicationContext();

     }


    public static Context getContext() {
        return appContext;
    }
}
