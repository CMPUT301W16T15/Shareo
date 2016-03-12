package cmput301w16t15.shareo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mvc.AppUserSingleton;
import mvc.Thing;
import mvc.User;


public class BidsFragment extends Fragment implements Observer {
    private User mUser;

    private ListView mList;
    private BidsAdapter mListAdapter;
    private TextView mEmptyMessage;


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
    private void setAdapterBasedOnTab(int position) {
        switch (position) {
            case 0:
                List<Thing> data = mUser.getOwnedBiddedThings();
                mListAdapter = new BidsAdapter(getActivity(), R.layout.available_items, data);
                mList.setAdapter(mListAdapter);

                if (data.size() == 0) {
                    mEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    mEmptyMessage.setVisibility(View.GONE);
                }
                break;
            case 1:
                // TODO: use different data source (will have to make getter in model class)
                data = mUser.getOwnedBiddedThings();
                mListAdapter = new BidsAdapter(getActivity(), R.layout.available_items, data);
                mList.setAdapter(mListAdapter);

                if (data.size() == 0) {
                    mEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    mEmptyMessage.setVisibility(View.GONE);
                }
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // user singleton...used for getting data
        mUser = AppUserSingleton.getInstance().getUser();
        //mUser.addObserver(this);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bids, container, false);
        mList = (ListView) v.findViewById(R.id.listview);
        mEmptyMessage = (TextView) v.findViewById(R.id.empty_notice);

        MultiStateToggleButton button = (MultiStateToggleButton) v.findViewById(R.id.mstb_multi_id);
        button.setElements(R.array.bids_choices, 0);
        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                setAdapterBasedOnTab(position);
            }
        });

        return v;
    }

    @Override
    public void update(Observable observable, Object data) {
        mListAdapter.notifyDataSetChanged();
    }

    private class BidsAdapter extends ArrayAdapter<Thing> {

        private final Context context;
        private final List<Thing> things;

        public BidsAdapter(Context context, int resource, List<Thing> objects) {
            super(context, resource, objects);
            this.context = context;
            this.things = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.available_items, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.available_game_name);
            textView.setText(things.get(position).getName());
            return rowView;
        }
    }
}
