package cmput301w16t15.shareo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class HomeFragment extends Fragment implements Observer {
    private FloatingActionButton mFab;
    private User mUser;

    private ListView mList;
    private HomeAdapter mListAdapter;
    private TextView mEmptyMessage;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private void setAdapterBasedOnTab(int position) {
        switch (position) {
            case 0:
                // TODO: avail games data source
                List<Thing> data = mUser.getOwnedBiddedThings();
                mListAdapter = new HomeAdapter(getActivity(), R.layout.available_items, data);
                mList.setAdapter(mListAdapter);

                if (data.size() == 0) {
                    mEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    mEmptyMessage.setVisibility(View.GONE);
                }
                break;
            case 1:
                // TODO: borrowing games data source
                data = mUser.getOwnedBiddedThings();
                mListAdapter = new HomeAdapter(getActivity(), R.layout.available_items, data);
                mList.setAdapter(mListAdapter);

                if (data.size() == 0) {
                    mEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    mEmptyMessage.setVisibility(View.GONE);
                }
                break;
            case 2:
                // TODO: lending games data source
                data = mUser.getOwnedBiddedThings();
                mListAdapter = new HomeAdapter(getActivity(), R.layout.available_items, data);
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
        mUser.addObserver(this);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mList = (ListView) v.findViewById(R.id.listview);
        mListAdapter = new HomeAdapter(this.getContext(), R.layout.available_items, mUser.getOwnedThings());
        mList.setAdapter(mListAdapter);
        mEmptyMessage = (TextView) v.findViewById(R.id.empty_notice);

        MultiStateToggleButton button = (MultiStateToggleButton) v.findViewById(R.id.mstb_multi_id);
        button.setElements(R.array.home_choices, 0);
        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                setAdapterBasedOnTab(position);
            }
        });

        mFab = (FloatingActionButton) v.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGameFragment agf = new AddGameFragment();
                agf.show(getFragmentManager(), "new game");
            }
        });

        return v;
    }

    @Override
    public void update(Observable observable, Object data) {
        mListAdapter.notifyDataSetChanged();
    }

    private class HomeAdapter extends ArrayAdapter<Thing> {

        private final Context context;
        private final List<Thing> things;

        public HomeAdapter(Context context, int resource, List<Thing> objects) {
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
