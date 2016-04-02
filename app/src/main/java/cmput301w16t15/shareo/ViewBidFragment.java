package cmput301w16t15.shareo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mvc.Bid;
import mvc.ShareoData;
import mvc.Thing;
import mvc.exceptions.NullIDException;

/**
 * DialogFragment: when click on bid from bids tab, will show bid and game information
 * */
public class ViewBidFragment extends DialogFragment {
    private static String TAG ="ViewBidFragment";
    private Bid mBid;
    private Thing mThing;
    private List<Bid> mBids;
    private TextView mtextViewGameOwner;
    private TextView mtextViewGameName;
    private TextView mtextViewDescription;
    private TextView mtextViewNumberPlayers;
    private TextView mtextViewCategory;
    private TextView mMaxBid;
    private TextView mMyBid;
    private Button mOwnerButton;

    public ViewBidFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.fragment_view_bid, null);

        mtextViewGameName =  (TextView) v.findViewById(R.id.textViewGameName);
        mtextViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        mtextViewNumberPlayers = (TextView) v.findViewById(R.id.textViewNumberPlayers);
        mtextViewCategory = (TextView) v.findViewById(R.id.textViewCategory);
        mtextViewGameOwner = (TextView) v.findViewById(R.id.textViewGameOwner);
        mMaxBid = (TextView) v.findViewById(R.id.textViewHighestBid);
        mMyBid = (TextView) v.findViewById(R.id.textViewMyBid);
        mOwnerButton = (Button) v.findViewById(R.id.ownerButton);

        mOwnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileInfo d = new UserProfileInfo();
                Bundle bundle = new Bundle();

                bundle.putString("userId", mThing.getOwnerID());
                d.setArguments(bundle);
                d.show(getActivity().getFragmentManager(), "user_profile");
            }
        });

        mBid = (Bid) getArguments().getSerializable("myThing");

        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    mThing = mBid.getThing();
                }
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // TODO: getTopBidAmount this doesn't update properly
        mMaxBid.setText(mMaxBid.getText() + ": $" + (double) mThing.getTopBidAmount()/100);
        mMyBid.setText(mMyBid.getText() + ": $" + (double) mBid.getBidAmount()/100);
        mOwnerButton.setText(mThing.getOwnerID());
        mtextViewGameName.setText(mtextViewGameName.getText() + ": " + mThing.getName());
        mtextViewDescription.setText(mtextViewDescription.getText() + ": " + mThing.getDescription());
        mtextViewNumberPlayers.setText(mtextViewNumberPlayers.getText() + ": " + mThing.getNumberPlayers());
        mtextViewCategory.setText(mtextViewCategory.getText() + ": " + mThing.getCategory());

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();

                    }
                })
                .setNegativeButton("Remove Bid", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeBid();
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
    }

    private void removeBid()
    {
        try
        {
            mBid.new Deleter().delete();
        }
        catch (NullIDException e)
        {
            Log.d(TAG, "Delete Bid Failed!");
        }
    }

}
