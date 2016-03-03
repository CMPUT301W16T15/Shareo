package cmput301w16t15.shareo;

import android.content.Context;
import android.os.Bundle;
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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import mvc.AppUserSingleton;
import mvc.Thing;
import mvc.User;

public class HomeFragment extends Fragment implements Observer {
    private static final String TAG = "TAGHome";

    private Button mButtonAvailable;
    private Button mButtonIamBorrowing;
    private Button mButtonOthersBorrowing;
    private AvailableGamesAdapter availableGamesAdapter;
    private ListView availableGames;
    private ImageView createGameView;
    private User user;
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
        user = AppUserSingleton.getInstance().getUser();
        user.addObserver(this);
        //Used to create the list of available games and connect listeners
        availableGames = (ListView)v.findViewById(R.id.available_list);
        availableGamesAdapter = new AvailableGamesAdapter(this.getContext(), R.layout.available_items, user.getOwnedThings());
        availableGames.setAdapter(availableGamesAdapter);
        mButtonAvailable = (Button) v.findViewById(R.id.button_available);
        mButtonIamBorrowing = (Button) v.findViewById(R.id.button_iam_borrowing);
        mButtonOthersBorrowing = (Button) v.findViewById(R.id.button_others_borrowing);

        createGameView = (ImageView) v.findViewById(R.id.createGameView);

        mButtonAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked Available Button");
                mSelectedMode = Selected.AVAILABLE;

                // filter things by avail
            }
        });

        mButtonIamBorrowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked Borrowing Button");
                mSelectedMode = Selected.IAM_BORROWING;

                // filter things by i am borrowing
            }
        });

        mButtonOthersBorrowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked Lending Button");
                mSelectedMode = Selected.OTHERS_BORROWING;

                // filter things by others borrowing
            }
        });

        createGameView.setOnClickListener(new View.OnClickListener() {
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
        availableGamesAdapter.notifyDataSetChanged();
    }



    private class AvailableGamesAdapter extends ArrayAdapter<Thing> {

        private final Context context;
        private final List<Thing> things;

        public AvailableGamesAdapter(Context context, int resource, List<Thing> objects) {
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
