package cmput301w16t15.shareo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import mvc.AppUserSingleton;
import mvc.Bid;
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
    private TextView mtextViewGameName;
    private TextView mtextViewDescription;
    private TextView mtextViewNumberPlayers;
    private TextView mtextViewCategory;
    private TextView mtextViewPickUpLocation;
    private ImageView mimageViewGame;
    private Button mOwnerButton;

    private AlertDialog dialog = null;
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
        //mtextViewPickUpLocation= (TextView)v.findViewById(R.id.editTextPickUpLocation);
        mtextViewCurrentTopBid = (TextView) v.findViewById(R.id.textViewCurrentTopBid);
        //mtextViewBidAmount = (TextView) v.findViewById(R.id.textViewBidAmount);
        mOwnerButton = (Button) v.findViewById(R.id.ownerButton);
        mimageViewGame = (ImageView) v.findViewById(R.id.gamePicture);
        //meditTextBidAmount = (EditText) v.findViewById(R.id.editTextBidAmount);

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
        dialog = builder.show();
        /**
         * Set the bid button to be disabled by default
         * so you can't enter in an empty bid which
         * crashes the app.
         */
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        /**
         * This code is modified from here : http://stackoverflow.com/questions/8238952/how-to-disable-enable-dialog-negative-positive-buttons
         * and http://stackoverflow.com/questions/12944559/how-to-multiply-a-bigdecimal-by-an-integer-in-java
         * and http://stackoverflow.com/questions/4134047/java-bigdecimal-round-to-the-nearest-whole-value
         * and http://stackoverflow.com/questions/10070108/can-i-cancel-previous-toast-when-i-want-to-show-an-other-toast
         * and http://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
         */
        meditTextMakeOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

	/**
	 *  This is called when someone has finished entering a bid.
	 */
            @Override
            public void afterTextChanged(Editable arg0) {
                /**
                 * Added from here : http://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
                 */
                String str = meditTextMakeOffer.getText().toString();
                if (str.isEmpty()) return;
		/**
		 * Limit their bid to 9 places before decimal and 2 places after decimal.
	 	 */
                String str2 = PerfectDecimal(str, 9, 2);

                if (!str2.equals(str)) {
                    meditTextMakeOffer.setText(str2);
                    int pos = meditTextMakeOffer.getText().length();
                    meditTextMakeOffer.setSelection(pos);
                }

                /**
                 * Make sure that the bid isn't too large to be stored in cents, which is an int.
                 */
                validateBidField();

                }

        });

        return dialog;
    }

    /**
     * Taken from here :
     * http://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
     * This helper function is used to limit the input of their bid to an appropriate amount.
     */
    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal;
            }else if(t == '.') {
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }return rFinal;
    }

    /**
     * Try to save the bid by calling the backend Builder function to build it.
     * Dollars to cents taken from here :
     * http://stackoverflow.com/questions/2406881/regarding-java-string-dollar-to-cents-conversion/2408469#2408469
     */
    private void saveBid() {
        User user = AppUserSingleton.getInstance().getUser();
        BigDecimal dollars = new BigDecimal(meditTextMakeOffer.getText().toString());
        if(dollars.scale()>2)
        {
            throw new IllegalArgumentException();
        }
        int cents = dollars.multiply(new BigDecimal(100)).intValue();
        try {
            //Integer.parseInt(meditTextBidAmount.getText().toString()
            new Bid.Builder(ShareoData.getInstance(), user, mThing, cents).build();
            //new Bid.Builder(ShareoData.getInstance(), user, mThing, .build();
        } catch (NullIDException e) {
            Log.d(TAG, "Failed to build bid.");
        }
    }

   /**
    * Set up the text of the fragment by setting every attribute of the bid known to the Thing class
    * The only non-trivial field set is the bid amount, which undergoes a conversion from cents to dollars.
    */
    private void setUpText() {
        mOwnerButton.setText(mThing.getOwnerID());
        mtextViewGameName.setText(mtextViewGameName.getText() + ": " + mThing.getName());
        mtextViewDescription.setText(mtextViewDescription.getText() + ": " + mThing.getDescription());
        mtextViewNumberPlayers.setText(mtextViewNumberPlayers.getText() + ": " + mThing.getNumberPlayers());
        mtextViewCategory.setText(mtextViewCategory.getText() + ": " + mThing.getCategory());
        mtextViewPickUpLocation.setText(mtextViewPickUpLocation.getText()+ ": " + mThing.getPickUpLocation());
        mtextViewCurrentTopBid.setText(mtextViewCurrentTopBid.getText() + ": $" + String.valueOf((double) mThing.getTopBidAmount() / 100));
        if (mThing.getPhotoModel() != null) {
            mimageViewGame.setImageBitmap(mThing.getPhotoModel().getPhoto());
        }
    }

    /**
     * Validate the bid field by ensuring that the amount entered isn't too large to enter
     * in an integer format (cents). It does this by undergoing some operations by first storing the bid in BigDecimal
     * and then comparing the amount entered to be less than the maximum integer value.
     */
    private void validateBidField()
    {
        BigDecimal amount;
        try {
            amount = new BigDecimal(meditTextMakeOffer.getText().toString());
            BigDecimal rounded = amount.setScale(0, RoundingMode.HALF_UP);
            Toast toastobject = null;
            /**
             * They entered an invalid amount --> Their bid is too big.
             */
            if (rounded.multiply(new BigDecimal(100)).compareTo(new BigDecimal(Integer.MAX_VALUE)) > 0) {
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
        } catch (NumberFormatException e) {
            amount = new BigDecimal(0);
        }


    }
}
