package jacklee_entertainment.niceneat.fragment.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jacklee_entertainment.niceneat.App;
 import jacklee_entertainment.niceneat.R;
import jacklee_entertainment.niceneat.activity.LoginActivityFB;
import jacklee_entertainment.niceneat.activity.MainActivity;
import jacklee_entertainment.niceneat.fragment.dialog.AlertDialogFragment;
import jacklee_entertainment.niceneat.fragment.dialog.ProgressDialogFragment;

/**
 * Created by user on 2015-04-10.
 */
public class MyProfileFragment extends Fragment implements   ImageChooserListener{
    private MainActivity mainActivity;

    // FIELD - PHOTO BUTTON
    private ImageChooserManager imageChooserManager;
    private String filePath;
    private int chooserType;
    private ProgressBar pbar;

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
    private static final String PROGRESS_DIALOG_TAG = "PROGRESS_DIALOG_TAG";

    private LinearLayout linearLayout;
    private TextView emailTextView;
    private TextView fullnameTextView;
    private ImageButton imageButton;
    private Button logoutButton;
    private View view;
    private App app;
     private ChosenImage chosenImage;
    private int PICK_IMAGE_REQUEST = 1;

    public MyProfileFragment() {}

    // newInstance constructor for creating fragment with arguments
    public static MyProfileFragment newInstance(int page, String title) {
        MyProfileFragment fragmentFirst = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myprofile, container, false);
//         signInQbUser = DataHolder.getDataHolder().getSignInQbUser();

//        signInQbUser = App.signedQBUser;
        initUI();
//        showProfile();
        return view;
    }


    // PHOTO PICKER
    // PHOTO PICKER
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        mainActivity = (MainActivity)getActivity();

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == mainActivity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), uri);
                imageButton = (ImageButton) mainActivity.findViewById(R.id.imageButton);
                imageButton.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



//    private void showProfile() {
//        if(DataHolder.getDataHolder().checkSignInUsingSocial()){
//
//        }
//        else{
//            try {
//                if (!TextUtils.isEmpty(signInQbUser.getFullName()))
//                    fillField(fullnameTextView, signInQbUser.getFullName());
//                else
//                    fillField(fullnameTextView, signInQbUser.getLogin());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private void showSocialProfile() {
//        Picasso.with(getActivity()).load(mSocialPerson.avatarURL).into(imageButton);
//        fillField(fullnameTextView, mSocialPerson.name);
//    }

    protected void fillField(TextView textView, String value) {
        if (!TextUtils.isEmpty(value)) {
            textView.setText(value);
        }
    }

     public void onClick(View view){
        switch (view.getId()) {
            case R.id.logout_button:
                showProgress("Logout...");
                logout();
                redirectLoginActivityFB();
         }
    }

    private void logout() {
        if (mainActivity.mAuthData != null) {
            mainActivity.mFirebaseRef.unauth();
//                 setAuthenticatedUser(null);
        }
    }
    protected void redirectLoginActivityFB() {
        Intent intent = new Intent(getActivity(), LoginActivityFB.class);
        startActivity(intent);
        getActivity().finish();
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

    protected void handleError(String text) {
        AlertDialogFragment.newInstance("Error", text).show(getFragmentManager(), null);
    }

//    private class RequestSocialPersonCompleteListener implements OnRequestSocialPersonCompleteListener {
//        @Override
//        public void onRequestSocialPersonSuccess(int socialNetworkID, SocialPerson socialPerson) {
//            hideProgress();
//            Picasso.with(getActivity()).load(socialPerson.avatarURL).into(imageButton);
//            fillField(fullnameTextView, socialPerson.name);
//        }
//
//        @Override
//        public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
//            hideProgress();
//            handleError(errorMessage);
//        }
//    }





    @Override
    public void onError(final String reason) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pbar.setVisibility(View.GONE);
//                Toast.makeText(getActivity().this, reason,
//                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void chooseImage() {
        Intent intent = new Intent();
         intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }

    public void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType,
                "myfolder", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
//                pbar.setVisibility(View.GONE);
                                            if (image != null) {
                                                imageButton.setImageURI(Uri.parse(new File(image
                                                        .getFileThumbnail()).toString()));

                                            }
                                        }
                                    }
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        super.onSaveInstanceState(outState);
    }

     public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }

            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
        }
//        getActivity().onRestoreInstanceState(savedInstanceState);
    }




    private void initUI() {
        fullnameTextView = (TextView) view.findViewById(R.id.fullname_textview);
        emailTextView = (TextView) view.findViewById(R.id.email_textview);
        imageButton  = (ImageButton) view.findViewById(R.id.imageButton);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        logoutButton = (Button) view.findViewById(R.id.logout_button);

    }
}
