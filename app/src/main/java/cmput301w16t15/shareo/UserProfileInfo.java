package cmput301w16t15.shareo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import mvc.ShareoData;
import mvc.User;

/**
 * Created by Andrew on 2016-03-31.
 */
public class UserProfileInfo extends DialogFragment {
    TextView username;
    TextView fullname;
    TextView email;
    TextView motto;

    public UserProfileInfo() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.dialog_user_profile, null);

        username = (TextView) v.findViewById(R.id.username);
        fullname = (TextView) v.findViewById(R.id.fullName);
        email = (TextView) v.findViewById(R.id.email);
        motto = (TextView) v.findViewById(R.id.motto);

        String userId = getArguments().getString("userId");
        new GetUserTask().execute(userId);

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(userId + "'s Contact Information");
        builder.setView(v)
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();

                    }
                });
        AlertDialog dialog = builder.show();

        return dialog;
    }

    private class GetUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... params) {
            return ShareoData.getInstance().getUser(params[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            username.setText(username.getText() + user.getName());
            fullname.setText(fullname.getText() + user.getFullName());
            email.setText(email.getText() + user.getEmailAddress());
            motto.setText(motto.getText() + user.getMotto());
        }
    }
}


