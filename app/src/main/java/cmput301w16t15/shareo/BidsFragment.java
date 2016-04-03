package cmput301w16t15.shareo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.List;

import mvc.AppUserSingleton;
import mvc.Bid;
import mvc.Observable;
import mvc.Observer;
import mvc.Thing;
import mvc.User;


/**
 * Fragment: bids main page.
 * offered and bids are the two options using the multistatetoggle button.
 */
public class BidsFragment extends Fragment implements Observer {
    private User mUser;

    private ListView mList;
    private ArrayAdapter mListAdapter;
    private TextView mEmptyMessage;
    private int mPosition = 0;
    private List<?> data;


    public BidsFragment() {
        // Required empty public constructor
    }

    public static BidsFragment newInstance() {
        return new BidsFragment();
    }

    /*
    * The listview renders either the items I am bidding on, or my items other's are bidding on
    * Based on mSelectedMode this function renders the appropriate data in the listivew
    * */
    private void setAdapterBasedOnTab() {
        switch (mPosition) {
            case 0:
                new GetOffersTask().execute();
                break;
            case 1:
                new GetBidsTask().execute();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // user singleton...used for getting data
        mUser = AppUserSingleton.getInstance().getUser();
        mUser.addView(this);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bids, container, false);
        mList = (ListView) v.findViewById(R.id.listview);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (mPosition == 0) {
                    AcceptDeclineBidsFragment adbf = new AcceptDeclineBidsFragment();
                    final Bundle bundle = new Bundle();
                    bundle.putInt("pos", position);
                    Thing clicked = (Thing)data.get(position);
                    bundle.putSerializable("myThing", (Thing) clicked);
                    adbf.setArguments(bundle);
                    adbf.show(getFragmentManager(), "accept_decline");
                } else {
                    ViewBidFragment vgf = new ViewBidFragment();
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable("myThing", (Bid) data.get(position));
                    vgf.setArguments(bundle);
                    vgf.show(getFragmentManager(), "viewGame");
                }
            }
        });

        mEmptyMessage = (TextView) v.findViewById(R.id.empty_notice);

        MultiStateToggleButton button = (MultiStateToggleButton) v.findViewById(R.id.mstb_multi_id);
        button.setElements(R.array.bids_choices, 0);
        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                mPosition = position;
                setAdapterBasedOnTab();
            }
        });

        setAdapterBasedOnTab();

        return v;
    }

    @Override
    public void update(Observable observable) {
        if (getActivity() == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAdapterBasedOnTab();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUser != null) {
            mUser.removeView(this);
        }
    }

    class GetBidsTask extends AsyncTask<String, Void, List<Bid>> {

        @Override
        protected List<Bid> doInBackground(String... params) {
            return mUser.getBids();
        }

        @Override
        protected void onPostExecute(List<Bid> d) {
            data = d;
            mListAdapter = new CustomAdapters.BasicBidAdapter(getActivity(), R.layout.minimal_thing_row, d);
            mList.setAdapter(mListAdapter);

            if (d.size() == 0) {
                mEmptyMessage.setVisibility(View.VISIBLE);
            } else {
                mEmptyMessage.setVisibility(View.GONE);
            }
        }
    }

    class GetOffersTask extends AsyncTask<String, Void, List<Thing>> {

        @Override
        protected List<Thing> doInBackground(String... params) {
            return mUser.getOffers();
        }

        @Override
        protected void onPostExecute(List<Thing> d) {
            data = d;
            mListAdapter = new CustomAdapters.BasicThingAdapter(getActivity(), R.layout.minimal_thing_row, d);
            mList.setAdapter(mListAdapter);

            if (d.size() == 0) {
                mEmptyMessage.setVisibility(View.VISIBLE);
            } else {
                mEmptyMessage.setVisibility(View.GONE);
            }
        }
    }

}
