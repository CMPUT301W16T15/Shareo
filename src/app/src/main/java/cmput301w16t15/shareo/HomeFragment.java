package cmput301w16t15.shareo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {
    private Button mButtonAvailable;
    private Button mButtonIamBorrowing;
    private Button mButtonOthersBorrowing;

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

        return v;
    }
}
