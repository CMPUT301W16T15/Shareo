package cmput301w16t15.shareo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class HomeFragment extends Fragment {
    private static final String TAG = "TAGHome";

    private Button mButtonAvailable;
    private Button mButtonIamBorrowing;
    private Button mButtonOthersBorrowing;

    private ImageView createGameView;
    // probably save when leaving fragment
    private Selected mSelectedMode;

    public enum Selected {
        AVAILABLE, IAM_BORROWING, OTHERS_BORROWING
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSelectedMode = Selected.AVAILABLE;

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mButtonAvailable = (Button) v.findViewById(R.id.button_available);
        mButtonIamBorrowing = (Button) v.findViewById(R.id.button_iam_borrowing);
        mButtonOthersBorrowing = (Button) v.findViewById(R.id.button_others_borrowing);

        createGameView = (ImageView) v.findViewById(R.id.createGameView);

        mButtonAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMode = Selected.AVAILABLE;

                // filter things by avail
            }
        });

        mButtonIamBorrowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMode = Selected.IAM_BORROWING;

                // filter things by i am borrowing
            }
        });

        mButtonOthersBorrowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedMode = Selected.OTHERS_BORROWING;

                // filter things by others borrowing
            }
        });

        createGameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cancel Button Clicked");
                /* From here : http://developer.android.com/guide/components/fragments.html#Transactions */
                Fragment newFragment = new AddEditGameFragment();
                // consider using Java coding conventions (upper first char class names!!!)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.home_fragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        return v;
    }
}
