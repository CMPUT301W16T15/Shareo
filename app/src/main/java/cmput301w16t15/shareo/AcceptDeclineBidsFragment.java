package cmput301w16t15.shareo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

import mvc.AppUserSingleton;
import mvc.Bid;
import mvc.Observable;
import mvc.Observer;
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.activity_selling_bids, null);

        mUser = AppUserSingleton.getInstance().getUser();
        mUser.addView(this);
        thing = (Thing) getArguments().getSerializable("myThing");
        bidList = new GetBidsTask().doInBackground();

        TextView description = (TextView) v.findViewById(R.id.textViewDescription);
        description.setText(thing.getDescription());

        TextView category = (TextView) v.findViewById(R.id.textViewCategory);
        category.setText(thing.getCategory());

        mListView = (ListView) v.findViewById(R.id.listViewBid);
        mListAdapter = new CustomAdapters.AcceptDeclineBidAdapter(this.getContext(), R.layout.bid_accept_decline_row, bidList);
        mListView.setAdapter(mListAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(thing.getName());
        builder.setView(v)
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();

                    }
                });
        AlertDialog dialog = builder.show();

        return dialog;
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
                if (bidList.size() == 0) {
                    dismiss();
                }
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
