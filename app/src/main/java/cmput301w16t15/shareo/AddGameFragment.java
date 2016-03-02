package cmput301w16t15.shareo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import mvc.AppUserSingleton;
import mvc.Game;

/**
 * Created by Andrew on 2016-02-28.
 */
public class AddGameFragment extends DialogFragment {

    private EditText editTextGameName;
    private EditText editTextDescription;
    private EditText editTextRate;
    private EditText editTextNumberPlayers;
    private EditText editTextCategory;

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
        Game game = new Game(gameName, gameDescription);
        AppUserSingleton.getInstance().getUser().addOwnedThing(game);
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


}
