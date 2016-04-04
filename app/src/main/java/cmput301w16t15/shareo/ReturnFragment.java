package cmput301w16t15.shareo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ExecutionException;

import mvc.Bid;
import mvc.PhotoModel;
import mvc.Thing;


/**
 * Created by Bradshaw on 2016-04-03.
 */
public class ReturnFragment extends DialogFragment {

    private static String TAG ="Borrowing";

    private Integer mPositionIndex;
    private TextView textViewGameName;
    private TextView textViewDescription;
    private TextView textViewNumberPlayers;
    private TextView textViewCategory;
    private TextView textViewGameOwner;
    private TextView mTextViewAddGame;
    private ImageButton gameImage;
    private String mPersonLending;

    private PhotoModel gamePhoto;
    private Thing mThing;

    private AlertDialog dialog = null;
    private AlertDialog.Builder builder = null;

    private Button mButtonDeleteImage;

    public ReturnFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.fragment_lent_game, null);
        mPositionIndex = getArguments().getInt("pos");
        mTextViewAddGame = (TextView) v.findViewById(R.id.textViewAddGame);
        textViewGameName = (TextView) v.findViewById(R.id.textViewGameName);
        textViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        textViewNumberPlayers = (TextView) v.findViewById(R.id.textViewNumberPlayers);
        textViewCategory = (TextView) v.findViewById(R.id.textViewCategory);
        textViewGameOwner = (TextView) v.findViewById(R.id.textViewOwner);
        gameImage = (ImageButton) v.findViewById(R.id.gamePicture);
        mThing = (Thing) getArguments().getSerializable("myThing");
        populateFields();
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Return Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        returnGame();
                        dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        dialog = builder.show();
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialog;
    }

    private void populateFields() {
        new PersonLendingTask().execute(mThing);
        mTextViewAddGame.setText("Lent Game");
        textViewGameName.setText(textViewGameName.getText() + ": " + mThing.getName());
        textViewDescription.setText(textViewDescription.getText() + ": " + mThing.getDescription());
        textViewCategory.setText(textViewCategory.getText() + ": " + mThing.getCategory());
        textViewNumberPlayers.setText(textViewNumberPlayers.getText() + ": " + mThing.getNumberPlayers());

        gameImage.setClickable(false);

        /**
         * If they have a photo --> Make it visible, else leave it in GONE state.
         */
        // photo is optional field
        if (mThing.getPhotoModel() != null) {
            gameImage.setImageBitmap(mThing.getPhotoModel().getPhoto());
            gamePhoto = mThing.getPhotoModel();
        }
        else
        {
            gamePhoto = null;
        }
    }

    private void returnGame() {
        try {
            new AcceptBidTask().execute(mThing).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class AcceptBidTask extends AsyncTask<Thing, Void, Void> {
        @Override
        protected Void doInBackground(Thing... thing) {
            thing[0].returnThing();
            return null;
        }
    }

    private class PersonLendingTask extends AsyncTask<Thing, Void, String> {
        @Override
        protected String doInBackground(Thing... thing) {
            return thing[0].getAcceptedBid().getBidder().getJestID();
        }
        @Override
        public void onPostExecute(final String person) {
            textViewGameOwner.setText(textViewGameOwner.getText() + ": " + person);
            textViewGameOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserProfileInfo d = new UserProfileInfo();
                    Bundle bundle = new Bundle();

                    bundle.putString("userId", person);
                    d.setArguments(bundle);
                    d.show(getActivity().getFragmentManager(), "user_profile");
                }
            });
        }
    }

}

