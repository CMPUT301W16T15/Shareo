package cmput301w16t15.shareo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;

import mvc.AppUserSingleton;
import mvc.Game;
import mvc.PhotoModel;
import mvc.ShareoData;
import mvc.User;

/**
 * Created by Andrew on 2016-02-28.
 */
public class AddGameFragment extends DialogFragment {

    private final int CHOOSE_PICTURE = 1;
    private EditText editTextGameName;
    private EditText editTextDescription;
    private EditText editTextRate;
    private EditText editTextNumberPlayers;
    private EditText editTextCategory;
    private ImageButton gameImage;

    private PhotoModel gamePhoto;
    private String gameName;
    private String gameDescription;
    private String gameRate;
    private String numberPlayers;
    private String category;

    public AddGameFragment() {

    }

    private void saveAllText()
    {
        gameName = editTextGameName.getText().toString();
        gameDescription = editTextDescription.getText().toString();
        gameRate = editTextRate.getText().toString();
        numberPlayers = editTextNumberPlayers.getText().toString();
        category = editTextCategory.getText().toString();
        User user = AppUserSingleton.getInstance().getUser();
        Game game = new Game(gameName, gameDescription);
        if (gamePhoto != null)
        {
            game.setPhoto(gamePhoto);
        }
        ShareoData.getInstance().addGame(game);
        try {
            user.addOwnedThing(game);
        } catch (mvc.exceptions.NullIDException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.fragment_addeditgame, null);

        editTextGameName = (EditText) v.findViewById(R.id.editTextGameName);
        editTextDescription = (EditText) v.findViewById(R.id.editTextDescription);
        editTextRate = (EditText) v.findViewById(R.id.editTextRate);
        editTextNumberPlayers = (EditText) v.findViewById(R.id.editTextNumberPlayers);
        editTextCategory = (EditText) v.findViewById(R.id.editTextCategory);
        gameImage = (ImageButton) v.findViewById(R.id.gamePicture);
        gameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageClicked(v);
            }
        });

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Game Entry")
                .setView(v)
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
        return builder.create();
    }

    public void imageClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Choose Picture"), CHOOSE_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_PICTURE) {
                Uri imageUri = data.getData();
                try {
                    gamePhoto = new PhotoModel(MediaStore.Images.Media
                            .getBitmap(getActivity().getContentResolver(), imageUri));
                    gameImage.setImageBitmap(gamePhoto.getPhoto());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
