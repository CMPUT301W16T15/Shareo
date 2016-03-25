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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import mvc.AppUserSingleton;
import mvc.Bid;
import mvc.PhotoModel;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;
import mvc.exceptions.NullIDException;

/**
 * DialogFragment: shown when clicking on search result.
 * It allows you to make a bid on an item
 */
public class AddBidFragment extends DialogFragment {
    private static String TAG ="AddBidFragment";
    private Thing mThing;
    private TextView mtextViewBidAmount;
    private TextView mtextViewCurrentTopBid;
    private TextView mtextViewGameOwner;
    private TextView mtextViewGameName;
    private TextView mtextViewDescription;
    private TextView mtextViewNumberPlayers;
    private TextView mtextViewCategory;

    private EditText meditTextMakeOffer;
    private ListView mlistViewBid;

    public AddBidFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_my_bids, null);

        meditTextMakeOffer = (EditText) v.findViewById(R.id.editTextMakeOffer);
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
        final AlertDialog dialog = builder.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        /**
         * This code is modified from here : http://stackoverflow.com/questions/8238952/how-to-disable-enable-dialog-negative-positive-buttons
         * and http://stackoverflow.com/questions/12944559/how-to-multiply-a-bigdecimal-by-an-integer-in-java
         * and http://stackoverflow.com/questions/4134047/java-bigdecimal-round-to-the-nearest-whole-value
         * and http://stackoverflow.com/questions/10070108/can-i-cancel-previous-toast-when-i-want-to-show-an-other-toast
         */
        meditTextMakeOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /**
                 * Make sure that the bid isn't too large to be stored in cents, which is an int.
                 */
                BigDecimal amount = new BigDecimal(meditTextMakeOffer.getText().toString());
                BigDecimal rounded = amount.setScale(0, RoundingMode.HALF_UP);
                Toast toastobject = null;
                /**
                 * They entered an invalid amount --> Their bid is too big.
                 */
                if (rounded.multiply(new BigDecimal(100)).compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0 || rounded.compareTo(new BigDecimal(0)) < 0) {
                    toastobject = Toast.makeText(getActivity(), "Your bid was too large. Please re-enter a valid bid size.", Toast.LENGTH_SHORT);
                    toastobject.show();

                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }

                /**
                 * They entered a valid amount.
                 */
                else {
                    /**
                     * Cancel getting spammed by toasts if you can..
                     * You can only cancel the last toast.
                     * Making an arraylist of toasts doesnt work :(
                     */
                    if (toastobject != null) {
                        toastobject.cancel();


                    }

                    ((AlertDialog) dialog).getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
    }

    private void saveBid() {
        User user = AppUserSingleton.getInstance().getUser();
        try {
            //Integer.parseInt(meditTextBidAmount.getText().toString()
            new Bid.Builder(ShareoData.getInstance(), user, mThing, Integer.parseInt(meditTextMakeOffer.getText().toString())*100).build();
            //new Bid.Builder(ShareoData.getInstance(), user, mThing, .build();
        } catch (NullIDException e) {
            Log.d(TAG, "Failed to build bid.");
        }
    }
    private void setUpText() {
        mtextViewGameOwner.setText(mtextViewGameOwner.getText() +": "+mThing.getOwnerID());
        mtextViewGameName.setText(mtextViewGameName.getText() + ": " + mThing.getName());
        mtextViewDescription.setText(mtextViewDescription.getText() + ": " + mThing.getDescription());
        mtextViewNumberPlayers.setText(mtextViewNumberPlayers.getText() + ": " + mThing.getNumberPlayers());
        mtextViewCategory.setText(mtextViewCategory.getText() + ": " + mThing.getCategory());
        mtextViewCurrentTopBid.setText(mtextViewCurrentTopBid.getText() +": "+String.valueOf(mThing.getTopBidAmount()/100));
    }
}
