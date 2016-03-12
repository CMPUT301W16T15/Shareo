package cmput301w16t15.shareo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import mvc.AppUserSingleton;
import mvc.User;

public class ProfileFragment extends Fragment {
    private EditText mEditTextFullName;
    private EditText mEditTextUserName;
    private EditText mEditTextEmail;
    private EditText mEditTextMotto;

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
        mEditTextFullName= (EditText) v.findViewById(R.id.editTextFullName);
        mEditTextUserName = (EditText) v.findViewById(R.id.editTextUserName);
        mEditTextEmail = (EditText) v.findViewById(R.id.editTextEmail);
        mEditTextMotto = (EditText) v.findViewById(R.id.editTextMotto);

        setAttributes();
        setHints();
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

    private void setHints()
    {
        mEditTextUserName.setHint(userName);
        mEditTextFullName.setHint(fullName);
        mEditTextEmail.setHint(emailAddress);
        mEditTextMotto.setHint(motto);
    }
}


