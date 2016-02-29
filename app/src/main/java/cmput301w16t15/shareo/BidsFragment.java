package cmput301w16t15.shareo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class BidsFragment extends Fragment {
    private static final String TAG = "TAGBidsFragment";
    private Selected mSelectedMode;

    private Button button_selling;
    private Button button_bidding;

    public enum Selected {
        STUFF_IAMBIDDING, OTHERS_BIDDING
    }

    public BidsFragment() {
        // Required empty public constructor
    }

    public static BidsFragment newInstance() {
        return new BidsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bids, container, false);

        button_selling = (Button) v.findViewById(R.id.button_selling);
        button_bidding= (Button) v.findViewById(R.id.button_bidding);

        /**
         * TODO: Probably change selling bids and mybidds to fragments.
         * Do something with selected mode?
         * This is just a starting point.
         */
        button_selling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Bids Selling Clicked on");
                mSelectedMode = Selected.OTHERS_BIDDING;
                Intent sellingIntent = new Intent(getActivity(), SellingBidsActivity.class);
                BidsFragment.this.startActivity(sellingIntent);

            }
        });

        button_bidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Bids Buying Clicked on");
                mSelectedMode = Selected.STUFF_IAMBIDDING;
                Intent buyingIntent = new Intent(getActivity(), MyBidsActivity.class);
                BidsFragment.this.startActivity(buyingIntent);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
