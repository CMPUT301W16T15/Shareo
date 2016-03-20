package cmput301w16t15.shareo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import mvc.PhotoModel;
import mvc.Thing;

/**
 * Created by anonymous on 3/19/2016.
 */
public class AddBidFragment extends DialogFragment {
    private Thing mThing;
    private TextView mtextViewBidAmount;
    private TextView mtextViewCurrentTopBid;
    private TextView mtextViewGameOwner;
    private TextView mtextViewGameName;
    private TextView mtextViewDescription;
    private TextView mtextViewNumberPlayers;
    private TextView mtextViewCategory;

    private EditText meditTextBidAmount;
    private ListView mlistViewBid;

    public AddBidFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_my_bids, null);

        mtextViewGameName =  (TextView) v.findViewById(R.id.textViewGameName);
        mtextViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        mtextViewNumberPlayers = (TextView) v.findViewById(R.id.textViewNumberPlayers);
        mtextViewCategory = (TextView) v.findViewById(R.id.textViewCategory);
        mtextViewCurrentTopBid = (TextView) v.findViewById(R.id.textViewCurrentTopBid);
        //mtextViewBidAmount = (TextView) v.findViewById(R.id.textViewBidAmount);
        mtextViewGameOwner = (TextView) v.findViewById(R.id.textViewGameOwner);
        //meditTextBidAmount = (EditText) v.findViewById(R.id.editTextBidAmount);

        mlistViewBid = (ListView) v.findViewById(R.id.listViewBid);
        mThing = (Thing) getArguments().getSerializable("myThing");


        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Bid", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveBid();
                        dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // don't save, just close
                        dismiss();
                    }
                });

        setUpText();
        return builder.create();
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
    }

    private void saveBid() {

    }
    private void setUpText() {
        mtextViewGameOwner.setText(mtextViewGameOwner.getText() +": "+mThing.getOwnerID());
        mtextViewGameName.setText(mtextViewGameName.getText() + ": " + mThing.getName());
        mtextViewDescription.setText(mtextViewDescription.getText() + ": " + mThing.getDescription());
        mtextViewNumberPlayers.setText(mtextViewNumberPlayers.getText() + ": " + mThing.getNumberPlayers());
        mtextViewCategory.setText(mtextViewCategory.getText() + ": " + mThing.getCategory());
    }

}
