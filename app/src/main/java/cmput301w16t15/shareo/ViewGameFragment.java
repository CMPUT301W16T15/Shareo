package cmput301w16t15.shareo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import mvc.AppUserSingleton;
import mvc.Bid;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;
import mvc.exceptions.NullIDException;

public class ViewGameFragment extends DialogFragment {
    private static String TAG ="ViewGameFragment";
    private Thing mThing;
    private TextView mtextViewGameOwner;
    private TextView mtextViewGameName;
    private TextView mtextViewDescription;
    private TextView mtextViewNumberPlayers;
    private TextView mtextViewCategory;

    public ViewGameFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.fragment_view_game, null);

        mtextViewGameName =  (TextView) v.findViewById(R.id.textViewGameName);
        mtextViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        mtextViewNumberPlayers = (TextView) v.findViewById(R.id.textViewNumberPlayers);
        mtextViewCategory = (TextView) v.findViewById(R.id.textViewCategory);
        mtextViewGameOwner = (TextView) v.findViewById(R.id.textViewGameOwner);

        mThing = (Thing) getArguments().getSerializable("myThing");


        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

    private void setUpText() {
        mtextViewGameOwner.setText(mtextViewGameOwner.getText() +": "+mThing.getOwnerID());
        mtextViewGameName.setText(mtextViewGameName.getText() + ": " + mThing.getName());
        mtextViewDescription.setText(mtextViewDescription.getText() + ": " + mThing.getDescription());
        mtextViewNumberPlayers.setText(mtextViewNumberPlayers.getText() + ": " + mThing.getNumberPlayers());
        mtextViewCategory.setText(mtextViewCategory.getText() + ": " + mThing.getCategory());
    }
}
