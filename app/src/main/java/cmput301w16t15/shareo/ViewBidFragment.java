package cmput301w16t15.shareo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import mvc.Bid;
import mvc.Thing;

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

        // find the highest bid
        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    mBids = mThing.getBids();
                }
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int max = mBids.get(0).getBidAmount();
        for (Bid b : mBids) {
            max = Math.max(max, b.getBidAmount());
        }

        mMaxBid.setText(mMaxBid.getText() + ": " + max);
        mMyBid.setText(mMyBid.getText() + ": " + mBid.getBidAmount());
        mtextViewGameOwner.setText(mtextViewGameOwner.getText() +": "+mThing.getOwnerID());
        mtextViewGameName.setText(mtextViewGameName.getText() + ": " + mThing.getName());
        mtextViewDescription.setText(mtextViewDescription.getText() + ": " + mThing.getDescription());
        mtextViewNumberPlayers.setText(mtextViewNumberPlayers.getText() + ": " + mThing.getNumberPlayers());
        mtextViewCategory.setText(mtextViewCategory.getText() + ": " + mThing.getCategory());

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
    }

}
