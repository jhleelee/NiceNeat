package jacklee_entertainment.niceneat.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rizki on 4/14/15.
 */
// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE",
                               ARG_LAYOUT = "ARG_LAYOUT";

    private int mPage,mLayout;

    public static PageFragment newInstance(int page, int layout) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putInt(ARG_LAYOUT,layout);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mLayout = getArguments().getInt(ARG_LAYOUT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(mLayout, container, false);

        return view;
    }
}
