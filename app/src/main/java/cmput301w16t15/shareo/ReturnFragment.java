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

import java.util.List;
import java.util.concurrent.ExecutionException;

import mvc.Bid;
import mvc.PhotoModel;
import mvc.Thing;

/**
 * Created by Bradshaw on 2016-04-02.
 * Used for showing a dialog to set a game to returned
 */
public class ReturnFragment extends DialogFragment {

    private static String TAG ="ReturnGame";

    private Integer mPositionIndex;
    private EditText editTextGameName;
    private EditText editTextDescription;
    private EditText editTextNumberPlayers;
    private EditText editTextCategory;
    private TextView mTextViewAddGame;
    private ImageButton gameImage;

    private PhotoModel gamePhoto;
    private String gameName;
    private String gameDescription;
    private String numberPlayers;
    private String category;
    private Thing mThing;

    private AlertDialog dialog = null;
    private AlertDialog.Builder builder = null;

    private Button mButtonDeleteImage;

    public ReturnFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.fragment_addeditgame, null);
        mPositionIndex = getArguments().getInt("pos");
        mTextViewAddGame = (TextView) v.findViewById(R.id.textViewAddGame);
        editTextGameName = (EditText) v.findViewById(R.id.editTextGameName);
        editTextDescription = (EditText) v.findViewById(R.id.editTextDescription);
        editTextNumberPlayers = (EditText) v.findViewById(R.id.editTextNumberPlayers);
        editTextCategory = (EditText) v.findViewById(R.id.editTextCategory);
        gameImage = (ImageButton) v.findViewById(R.id.gamePicture);
        mThing = (Thing) getArguments().getSerializable("myThing");
        populateFields();
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Return", new DialogInterface.OnClickListener() {
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
        return dialog;
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

    private void populateFields() {
        mTextViewAddGame.setText("Return a Game");
        editTextGameName.setText(mThing.getName());
        editTextDescription.setText(mThing.getDescription());
        editTextCategory.setText(mThing.getCategory());
        editTextNumberPlayers.setText(mThing.getNumberPlayers());

        mTextViewAddGame.setFocusable(false);
        editTextGameName.setFocusable(false);
        editTextDescription.setFocusable(false);
        editTextCategory.setFocusable(false);
        editTextNumberPlayers.setFocusable(false);
        gameImage.setFocusable(false);

        mTextViewAddGame.setClickable(false);
        editTextGameName.setClickable(false);
        editTextDescription.setClickable(false);
        editTextCategory.setClickable(false);
        editTextNumberPlayers.setClickable(false);
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

    private class AcceptBidTask extends AsyncTask<Thing, Void, Void> {
        @Override
        protected Void doInBackground(Thing... thing) {
            thing[0].returnThing();
            return null;
        }
    }
}
