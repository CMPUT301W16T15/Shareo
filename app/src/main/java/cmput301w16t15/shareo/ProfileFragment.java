package cmput301w16t15.shareo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mvc.AppUserSingleton;
import mvc.Jobs.CallbackInterface;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;
import mvc.UserDoesNotExistException;
import mvc.exceptions.NullIDException;

/**
 * Fragment: the main profile fragment allowing user to see information and edit
 * */
public class ProfileFragment extends Fragment {
    private static String TAG ="ProfileFragment";
    private Button mButtonSaveEdits;
    private EditText mEditTextFullName;
    private EditText mEditTextUserName;
    private EditText mEditTextEmail;
    private EditText mEditTextMotto;
    private TextView mTextCreateProfile;

    private User myUser;
    private String fullName;
    private String userName;
    private String emailAddress;
    private String motto;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mTextCreateProfile = (TextView) v.findViewById(R.id.textCreateProfile);

        mEditTextFullName= (EditText) v.findViewById(R.id.editTextFullName);
        mEditTextEmail = (EditText) v.findViewById(R.id.editTextEmail);
        mEditTextMotto = (EditText) v.findViewById(R.id.editTextMotto);

        mButtonSaveEdits = (Button) v.findViewById(R.id.buttonSaveEdits);
        mButtonSaveEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });

        setAttributes();
        setProfileText();
        setText();
        return v;
    }

    private void setAttributes()
    {
        myUser = AppUserSingleton.getInstance().getUser();
        userName = myUser.getName();
        fullName = myUser.getFullName();
        emailAddress = myUser.getEmailAddress();
        motto = myUser.getMotto();
    }

    private void setProfileText()
    {
        mTextCreateProfile.setText(userName+"'s Profile");
    }

    private void setText()
    {
        //mEditTextUserName.setHint(userName);
        mEditTextFullName.setText(fullName);
        mEditTextEmail.setText(emailAddress);
        mEditTextMotto.setText(motto);
    }

    private void saveUserClassChanges()
    {
        myUser.setName(userName);
        myUser.setFullName(fullName);
        myUser.setEmailAddress(emailAddress);
        myUser.setMotto(motto);

        myUser.update();
    }

    private void saveAllText()
    {
        fullName = mEditTextFullName.getText().toString();
        //userName = mEditTextUserName.getText().toString();
        emailAddress = mEditTextEmail.getText().toString();
        motto = mEditTextMotto.getText().toString();

        if (isValidEmail(emailAddress))
        {
            Log.d(TAG, "Save Changes to Profile");
            Toast z = Toast.makeText(getActivity(), "Profile Saved Successfully", Toast.LENGTH_SHORT);
            z.show();
            saveUserClassChanges();
        }

        else
        {
            Log.d(TAG, "Invalid Email");
            Toast z = Toast.makeText(getActivity(), "Invalid Email Inputted", Toast.LENGTH_SHORT);
            z.show();
        }


    }
    private void buttonClicked(View v)
    {
        saveAllText();
        setAttributes();

    }

    /***
     * Taken from here:
     * http://stackoverflow.com/questions/9355899/android-email-edittext-validation
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}


