package cmput301w16t15.shareo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import mvc.MVCModel;
import mvc.MVCView;
import mvc.Thing;
import mvc.User;

public class HomeFragment extends Fragment implements MVCView {
    private FloatingActionButton mFab;
    private User mUser;
    private int mPosition;

    private ListView mList;
    private ThingAdapters.BasicThingAdapter mListAdapter;
    private TextView mEmptyMessage;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private void setAdapterBasedOnTab() {
        switch (mPosition) {
            case 0:
                // TODO: avail games data source
                List<Thing> data = mUser.getAvailableThings();
                mListAdapter = new ThingAdapters.BasicThingAdapter(getActivity(), R.layout.detailed_thing_row, data);
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
                mListAdapter = new ThingAdapters.BasicThingAdapter(getActivity(), R.layout.detailed_thing_row, data);
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
                mListAdapter = new ThingAdapters.BasicThingAdapter(getActivity(), R.layout.detailed_thing_row, data);
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
        mUser.addView(this);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mList = (ListView) v.findViewById(R.id.listview);
        mListAdapter = new ThingAdapters.BasicThingAdapter(this.getContext(), R.layout.detailed_thing_row, mUser.getOwnedThings());
        mList.setAdapter(mListAdapter);
        mEmptyMessage = (TextView) v.findViewById(R.id.empty_notice);

        MultiStateToggleButton button = (MultiStateToggleButton) v.findViewById(R.id.mstb_multi_id);
        button.setElements(R.array.home_choices, 0);
        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                mPosition = position;
                setAdapterBasedOnTab();
            }
        });

        mFab = (FloatingActionButton) v.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGameFragment agf = new AddGameFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", -1);
                agf.setArguments(bundle);
                agf.show(getFragmentManager(), "new game");
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddGameFragment agf = new AddGameFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                agf.setArguments(bundle);
                agf.show(getFragmentManager(), "edit");
            }
        });

        setAdapterBasedOnTab();

        return v;
    }

    @Override
    public void updateView(MVCModel model) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAdapterBasedOnTab();
                mListAdapter.notifyDataSetChanged();
            }
        });
    }
}
