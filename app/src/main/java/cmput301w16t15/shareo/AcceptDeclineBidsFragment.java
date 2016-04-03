package cmput301w16t15.shareo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mvc.AppUserSingleton;
import mvc.Bid;
import mvc.Observable;
import mvc.Observer;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;

public class AcceptDeclineBidsFragment extends DialogFragment implements Observer {

    private static String TAG ="AcceptDeclineBidsFragment";
    private List<Bid> bidList;
    private Thing thing;
    User mUser;

    private ListView mListView;
    private CustomAdapters.AcceptDeclineBidAdapter mListAdapter;

    public AcceptDeclineBidsFragment() {
        // Required empty public constructor
    }
    public static AcceptDeclineBidsFragment newInstance() {
        return new AcceptDeclineBidsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_selling_bids, container, false);
        mUser = AppUserSingleton.getInstance().getUser();
        mUser.addView(this);
        thing = (Thing) getArguments().getSerializable("myThing");
        bidList = new GetBidsTask().doInBackground();

        this.getDialog().setTitle(thing.getName());

        //TextView title = (TextView) v.findViewById((R.id.ItemSelling));
        //title.setText(thing.getName());

        TextView description = (TextView) v.findViewById(R.id.textViewDescription);
        description.setText(thing.getDescription());

        TextView category = (TextView) v.findViewById(R.id.textViewCategory);
        category.setText(thing.getCategory());

        mListView = (ListView) v.findViewById(R.id.listViewBid);
        mListAdapter = new CustomAdapters.AcceptDeclineBidAdapter(this.getContext(), R.layout.bid_accept_decline_row, bidList);
        mListView.setAdapter(mListAdapter);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUser != null) {
            mUser.removeView(this);
        }
    }

    @Override
    public void update(Observable observable) {
        if (getActivity() == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListAdapter.notifyDataSetChanged();
            }
        });
    }

    private class GetBidsTask extends AsyncTask<Void, Void, List<Bid>> {
        @Override
        protected List<Bid> doInBackground(Void... params) {
            return thing.getBids();
        }
    }
}
