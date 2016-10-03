//package jacklee_entertainment.niceneat.fragment.login;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//
//
//
//import java.util.List;
//
// import jacklee_entertainment.niceneat.R;
//import jacklee_entertainment.niceneat.activity.MainActivity;
//import jacklee_entertainment.niceneat.fragment.dialog.AlertDialogFragment;
//import jacklee_entertainment.niceneat.fragment.dialog.ProgressDialogFragment;
//
//public class SignInFragment extends Fragment implements View.OnClickListener {
//
//    private static final String PROGRESS_DIALOG_TAG = "PROGRESS_DIALOG_TAG";
//
//    private EditText loginEditText;
//    private EditText passwordEditText;
//    private Button signInButton;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_signin, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        loginEditText = (EditText) view.findViewById(R.id.login_edittext);
//        passwordEditText = (EditText) view.findViewById(R.id.password_edittext);
//        signInButton = (Button) view.findViewById(R.id.sign_in_button);
//        signInButton.setOnClickListener(this);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceBundle) {
//        super.onCreate(savedInstanceBundle);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.sign_in_button:
//                showProgress("Signin...");
//
//                QBUser qbUser = new QBUser(loginEditText.getText().toString(), passwordEditText.getText().toString());
//                QBUsers.signIn(qbUser, new QBEntityCallbackImpl<QBUser>() {
//                    @Override
//                    public void onSuccess(QBUser qbUser, Bundle bundle) {
//                        hideProgress();
//                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
//                        DataHolder.getDataHolder().setSignInUsingSocial(false);
//
//                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString("username", loginEditText.getText().toString());
//                        editor.putString("password", passwordEditText.getText().toString());
//                        editor.commit();
//
//                        startMainAcitivity();
//                    }
//
//                    @Override
//                    public void onError(List<String> errors) {
//                        hideProgress();
//                        AlertDialogFragment.newInstance("Error", errors.toString()).show(getFragmentManager(), null);
//                    }
//                });
//                break;
//        }
//    }
//
//    public void startMainAcitivity(){
//        Intent intent=new Intent(getActivity(), MainActivity.class);
//        startActivity(intent);
//        getActivity().finish();
//    }
//
//    protected void showProgress(String text) {
//        ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(text);
//        progressDialogFragment.setTargetFragment(this, 0);
//        progressDialogFragment.setCancelable(false);
//        progressDialogFragment.show(getFragmentManager(), PROGRESS_DIALOG_TAG);
//    }
//
//    protected void hideProgress() {
//        Fragment fragment = getFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);
//
//        if (fragment != null) {
//            getFragmentManager().beginTransaction().remove(fragment).commit();
//        }
//    }
//}