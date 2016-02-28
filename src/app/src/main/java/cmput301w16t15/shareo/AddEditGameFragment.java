package cmput301w16t15.shareo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static cmput301w16t15.shareo.R.layout.fragment_addeditgame;

public class AddEditGameFragment extends Fragment {


    private EditText editTextGameName;
    private EditText editTextDescription;
    private EditText editTextRate;
    private EditText editTextNumberPlayers;
    private EditText editTextCategory;

    private Button buttonSave;
    private Button buttonCancel;

    private String gameName;
    private String gameDescription;
    private String gameRate;
    private String numberPlayers;
    private String category;

    private static final String TAG = "TAGAddGameFragment";
    public AddEditGameFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(fragment_addeditgame, container, false);

        /* There was a fragment over fragment bug from a transition of HomeFragment ->AddEditGame Fragment
           It was fixed here http://stackoverflow.com/questions/18309815/fragments-displayed-over-each-other
         */
        if (container != null) {
            container.removeAllViews();
        }

        editTextGameName = (EditText) v.findViewById(R.id.editTextGameName);
        editTextDescription = (EditText) v.findViewById(R.id.editTextDescription);
        editTextRate = (EditText) v.findViewById(R.id.editTextRate);
        editTextNumberPlayers = (EditText) v.findViewById(R.id.editTextNumberPlayers);
        editTextCategory = (EditText) v.findViewById(R.id.editTextCategory);

        buttonSave = (Button) v.findViewById(R.id.buttonSave);
        buttonCancel = (Button) v.findViewById(R.id.buttonCancel);

        /**
         * Todo : do stuff when save button is clicked
         */
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Save Button Clicked");
                saveAllText();

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cancel Button Clicked");
                removeCurrentFragment();
                /* Call MainActivity or Fragment ?
                Intent intent = new Intent(getActivity(), MainActivity.class);
                AddEditGameFragment.this.startActivity(intent);
                */
            }
        });


        return v;
    }

    @Override
    public void onResume() {

        super.onResume();
        /* As seen here --> Handle Back Button in Fragment:
         http://stackoverflow.com/questions/18755550/fragment-pressing-back-button */
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.d(TAG, "Back Button Clicked");
                    removeCurrentFragment();
                    return true;

                }

                return false;
            }
        });
    }

    private void saveAllText()
    {
        gameName = editTextGameName.getText().toString();
        gameDescription = editTextDescription.getText().toString();
        gameRate = editTextRate.getText().toString();
        numberPlayers = editTextNumberPlayers.getText().toString();
        category = editTextCategory.getText().toString();

    }

    public void removeCurrentFragment()
    {
        /* From here : http://developer.android.com/guide/components/fragments.html#Transactions */
        Fragment newFragment = new HomeFragment();
        // consider using Java coding conventions (upper first char class names!!!)
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.AddEditGameFragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
