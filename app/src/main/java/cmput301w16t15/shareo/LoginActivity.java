package cmput301w16t15.shareo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mvc.AppUserSingleton;
import mvc.Jobs.CallbackInterface;

/**
 * Activity: You can login or signup as a new user from this activity.
 * For logging in, we don't use passwords -- only the relevant username
 * */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String fullName;
    private String userNameSignup;
    private String email;
    private String motto;
    private String userNameLogin;

    private Button buttonSignup;
    private Button buttonLogin;

    private EditText editTextFullName;
    private EditText editTextUserNameSignup;
    private EditText editTextEmail;
    private EditText editTextMotto;
    private EditText editTextUserNameLogin;

    private static final String TAG = "TAGLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AppUserSingleton.getInstance().getUser() != null) {
            AppUserSingleton.getInstance().logOut();
        }
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

	/**
	 * Wire up the edit texts used for signing up or logging in.
	 */
        editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        editTextUserNameSignup = (EditText) findViewById(R.id.editTextUserNameSignup);
        editTextUserNameLogin = (EditText) findViewById(R.id.editTextUserNameLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextMotto = (EditText) findViewById(R.id.editTextMotto);
        editTextUserNameLogin = (EditText) findViewById(R.id.editTextUserNameLogin);

        buttonSignup.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG + "Cycle", "Called the onResume method for " + TAG);
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onPause() {
        Log.d(TAG + "Cycle", "Called the onPause method for " + TAG);
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }

    @Override
    protected void onStop() {
        Log.d(TAG + "Cycle", "Called the onStop method for " + TAG);
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG + "Cycle", "Called the onDestroy method for " + TAG);
        super.onDestroy();
        // The activity is about to be destroyed.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        Log.d(TAG + "Cycle", "Entering onSaveInstanceInstance");


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);

    }

    /**
     * Sign up or login user depending on button clicked. Notify user if login/create fail.
     * @param v
     */
    public void onClick(View v) {
        final Intent mainIntent = new Intent(this, MainActivity.class);
        switch (v.getId()) {
	   /**
	    * They clicked on button signup, so parse the signup of a new user.
	    */
            case R.id.buttonSignup:
                Log.d(TAG, "Clicked Button Signup");
		/**
	 	 * Store the fields userNameSignup, fullName, email, and motto based upon what the user entered.
	         */
                parseSignUp();
		/**
	 	 * Only if the email is valid(i.e. they entered in blah@blah.com), do we parse the signup.
	 	 */
                if (isValidEmail(email))
                {
		   /**
		    * Create a new user by calling the relevant backend call after collecting the fields inputted.
	 	    */
                    AppUserSingleton.getInstance().createUser(userNameSignup, fullName, email, motto, new CallbackInterface() {
                        @Override
                        public void onSuccess() {
                            startActivity(mainIntent);
                        }

                        @Override
                        public void onFailure() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast z = Toast.makeText(LoginActivity.this, "User already exists. Please choose a different username.", Toast.LENGTH_SHORT);
                                    z.show();
                                }
                            });
                        }
                    });
                }

                else
                {
                    Log.d(TAG,"Invalid Email");
                    Toast z = Toast.makeText(this, "Invalid Email Inputted", Toast.LENGTH_SHORT);
                    z.show();
                }

                break;

	    /**
	     * They clicked the login button, so get their username and log them in if the ID exists.
	     */
            case R.id.buttonLogin:
                Log.d(TAG, "Clicked Button Login");
		/**
	 	 * Store the relevant field userNameLogin based upon what the user entered.
	 	 */
                parseLogin();
                AppUserSingleton.getInstance().logIn(userNameLogin, new CallbackInterface() {
                    @Override
                    public void onSuccess() {
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onFailure() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast z = Toast.makeText(LoginActivity.this, "No user exists. Please check spelling and try again.", Toast.LENGTH_SHORT);
                                z.show();
                            }
                        });
                    }
                });
                break;
        }
    }

    /**
     * Store the relevant fields needed to parse signing up a new user.
     */
    private void parseSignUp() {
        fullName = editTextFullName.getText().toString();
        userNameSignup = editTextUserNameSignup.getText().toString();
        email = editTextEmail.getText().toString();
        motto = editTextMotto.getText().toString();
    }

    /**
     * Store the relevant fields needed to parse logging in a user.
     */
    private void parseLogin() {
        userNameLogin = editTextUserNameLogin.getText().toString();
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
