package cmput301w16t15.shareo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import mvc.AppUserSingleton;
import mvc.Thing;
import mvc.PhotoModel;
import mvc.ShareoData;
import mvc.User;
import mvc.UserDoesNotExistException;
import mvc.exceptions.NullIDException;

/**
 * DialogFragment: shown when click floating action button (plus) from home page.
 * Also used for editing game when position parameter passed in.
 */
public class AddGameFragment extends DialogFragment {
    private static String TAG ="AddEditGame";
    private Integer mPositionIndex;
    private List<Thing> myGames;

    private final int CHOOSE_PICTURE = 1;
    private EditText editTextGameName;
    private EditText editTextDescription;
    //private EditText editTextRate;
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

    public AddGameFragment() {

    }


    private void saveAllText()
    {
        gameName = editTextGameName.getText().toString();
        gameDescription = editTextDescription.getText().toString();
        //gameRate = editTextRate.getText().toString();
        numberPlayers = editTextNumberPlayers.getText().toString();
        //int numPlay = Integer.parseInt(numberPlayers);
        category = editTextCategory.getText().toString();
        User user = AppUserSingleton.getInstance().getUser();

        /**
         * If mPositionIndex == -1, then we are in Add Game mode, so call .build() in Thing.
         */
        if (mPositionIndex == -1) {
            try {
                new Thing.Builder(ShareoData.getInstance(), user, gameName, gameDescription, category, numberPlayers).setPhoto(gamePhoto).build();
            } catch (UserDoesNotExistException e) {
                // TODO catch error in a meaningful way.
                e.printStackTrace();
            }
        }

        /**
         * We are in edit game mode so don't call the same method, call .edit() in Thing.
         */

        else {
            mThing.setNumberPlayers(numberPlayers);
            mThing.setDescription(gameDescription);
            mThing.setName(gameName);
            mThing.setCategory(category);
            mThing.setPhoto(gamePhoto);

            mThing.update();
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.fragment_addeditgame, null);

        mPositionIndex = getArguments().getInt("pos");

        mButtonDeleteImage = (Button) v.findViewById(R.id.buttonDeleteImage);
        mButtonDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImageClicked();
            }
        });
        mTextViewAddGame = (TextView) v.findViewById(R.id.textViewAddGame);
        editTextGameName = (EditText) v.findViewById(R.id.editTextGameName);
        editTextDescription = (EditText) v.findViewById(R.id.editTextDescription);
        //editTextRate = (EditText) v.findViewById(R.id.editTextRate);
        editTextNumberPlayers = (EditText) v.findViewById(R.id.editTextNumberPlayers);
        editTextCategory = (EditText) v.findViewById(R.id.editTextCategory);
        gameImage = (ImageButton) v.findViewById(R.id.gamePicture);
        gameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClicked(v);
            }
        });

        if (mPositionIndex != -1) {
            mThing = (Thing) getArguments().getSerializable("myThing");
            populateFields();

            // Build the dialog and set up the button click handlers
            builder = new AlertDialog.Builder(getActivity());
            builder.setView(v)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveAllText();
                            dismiss();

                        }
                    })

                    .setNeutralButton("Delete Game", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            buttonClicked();
                            dismiss();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // don't save, just close
                            dismiss();
                        }
                    });
        }

        else
        {
            // Build the dialog and set up the button click handlers
            builder = new AlertDialog.Builder(getActivity());
            builder.setView(v)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveAllText();
                            dismiss();

                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // don't save, just close
                            dismiss();
                        }
                    });
        }



        dialog = builder.show();
        return dialog;
    }

    public void imageClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Choose Picture"), CHOOSE_PICTURE);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_PICTURE) {
                Uri imageUri = data.getData();
                try {
                    gamePhoto = new PhotoModel(MediaStore.Images.Media
                            .getBitmap(getActivity().getContentResolver(), imageUri));
                    gameImage.setImageBitmap(gamePhoto.getPhoto());
                    mButtonDeleteImage.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void populateFields() {
        mTextViewAddGame.setText("Edit a Game");
        editTextGameName.setText(mThing.getName());
        editTextDescription.setText(mThing.getDescription());
        editTextCategory.setText(mThing.getCategory());
        editTextNumberPlayers.setText(mThing.getNumberPlayers());

        // photo is optional field
        if (mThing.getPhotoModel() != null) {
            gameImage.setImageBitmap(mThing.getPhotoModel().getPhoto());
            mButtonDeleteImage.setVisibility(View.VISIBLE);
            gamePhoto = mThing.getPhotoModel();
        }
        else
        {
            gamePhoto = null;
        }
    }

    private void buttonClicked()
    {
        try
        {
            mThing.new Deleter().delete();
        }

        catch(NullIDException e)
        {
            Log.d(TAG, "Failed to delete a thing from listView");
        }

    }

    private void deleteImageClicked()
    {
        if (gamePhoto != null)
        {
            mButtonDeleteImage.setVisibility(View.GONE);
            gameImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image_placeholder));
            gamePhoto = null;
        }

    }
}
