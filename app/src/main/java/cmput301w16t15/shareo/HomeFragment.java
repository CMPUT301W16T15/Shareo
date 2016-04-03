package cmput301w16t15.shareo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.util.List;

import mvc.AppUserSingleton;
import mvc.Observable;
import mvc.Observer;
import mvc.Thing;
import mvc.User;
import mvc.exceptions.NullIDException;

/*
* Fragment: Main screen showing available items, borrowing items, and lent items
* Each of these choices is selectable by hitting the option on the multi-state toggle button
* This page is also where games are added and edited
* */
public class HomeFragment extends Fragment implements Observer {
    private FloatingActionButton mFab;
    private User mUser;
    private int mPosition = 0;
    private List<Thing> data;

    private ListView mList;
    private CustomAdapters.ThingWithStatusAdapter mListAdapter;
    private TextView mEmptyMessage;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUser != null) {
            mUser.removeView(this);
        }
    }

    private void setAdapterBasedOnTab() {
        switch (mPosition) {
            case 0:
                data = mUser.getAvailableThings();
                mListAdapter = new CustomAdapters.ThingWithStatusAdapter(getActivity(), R.layout.minimal_thing_row, data);
                mList.setAdapter(mListAdapter);
                mFab.show();
                if (data.size() == 0) {
                    mEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    mEmptyMessage.setVisibility(View.GONE);
                }
                break;
            case 1:
                data = mUser.getBorrowedThings();
                mListAdapter = new CustomAdapters.ThingWithStatusAdapter(getActivity(), R.layout.minimal_thing_row, data);
                mList.setAdapter(mListAdapter);
                mFab.hide();
                if (data.size() == 0) {
                    mEmptyMessage.setVisibility(View.VISIBLE);
                } else {
                    mEmptyMessage.setVisibility(View.GONE);
                }
                break;
            case 2:
                data = mUser.getLentThings();
                mListAdapter = new CustomAdapters.ThingWithStatusAdapter(getActivity(), R.layout.minimal_thing_row, data);
                mList.setAdapter(mListAdapter);
                mFab.hide();
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
        mListAdapter = new CustomAdapters.ThingWithStatusAdapter(this.getContext(), R.layout.minimal_thing_row, mUser.getOwnedThings());
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
                Thing thing = data.get(position);
                if (mPosition == 0) { // available
                    if (thing.getBids().isEmpty()) {
                        showEditGameDialog(thing, position);
                    } else {
                        showAcceptDeclineDialog(thing, position);
                    }
                } else if (mPosition == 1) { // borrowed thing...view game info and profile...of maybe just profile
                    showGameOwnerInfo(thing);
                } else { // lent, so will have zero gets
                    showReturnGameDialog(thing, position);
                }
            }
        });

        setAdapterBasedOnTab();

        return v;
    }

    private void showGameOwnerInfo(Thing thing) {
        UserProfileInfo d = new UserProfileInfo();
        Bundle bundle = new Bundle();

        bundle.putString("userId", thing.getOwnerID());
        d.setArguments(bundle);
        d.show(getActivity().getFragmentManager(), "user_profile");
    }

    private void showAcceptDeclineDialog(Thing thing, int position) {
        AcceptDeclineBidsFragment adbf = new AcceptDeclineBidsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putSerializable("myThing", thing);
        adbf.setArguments(bundle);
        adbf.show(getFragmentManager(), "accept_decline");
    }

    private void showEditGameDialog(Thing thing, int position) {
        try {
            thing.getJestID();
            AddGameFragment agf = new AddGameFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pos", position);
            bundle.putSerializable("myThing", data.get(position));
            agf.setArguments(bundle);
            agf.show(getFragmentManager(), "edit");
        } catch (NullIDException e) {
            Toast z = Toast.makeText(getActivity(), "Go online before editing this game.", Toast.LENGTH_SHORT);
            z.show();
        }
    }

    private void showReturnGameDialog(Thing thing, int position) {
        ReturnFragment returnFragment = new ReturnFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putSerializable("myThing", data.get(position));
        returnFragment.setArguments(bundle);
        returnFragment.show(getFragmentManager(), "return");
    }

    @Override
    public void update(Observable model) {
        if (getActivity() == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAdapterBasedOnTab();
                mListAdapter.notifyDataSetChanged();
            }
        });
    }
}
