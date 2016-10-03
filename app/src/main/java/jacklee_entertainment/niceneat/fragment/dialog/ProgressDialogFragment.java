package jacklee_entertainment.niceneat.fragment.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;



public class ProgressDialogFragment extends DialogFragment {

    private static final String PARAM_MESSAGE = "ProgressDialogFragment.PARAM_MESSAGE";

    public static ProgressDialogFragment newInstance(String message) {

        Bundle args = new Bundle();
        args.putString(PARAM_MESSAGE, message);

        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.setArguments(args);
        return progressDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Bundle args = getArguments();

        final String paramMessage = args.getString(PARAM_MESSAGE);

        Dialog dialog = ProgressDialog.show(getActivity(), "Executing request", paramMessage);
        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

        super.onCancel(dialog);

        Fragment fragment = getTargetFragment();

//        // stop all requests on cancel
//        if (fragment != null && fragment instanceof SocialLoginFragment) {
//            ((SocialLoginFragment) fragment).onRequestCancel();
//        }
    }
}
