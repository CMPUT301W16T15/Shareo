package cmput301w16t15.shareo;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;

import mvc.AppUserSingleton;
import mvc.ShareoData;
import mvc.User;


public class LoginTest extends ActivityInstrumentationTestCase2 {
    Instrumentation instrumentation;
    Activity loginActivity;

    String name1 = "david sanderson";
    String username1 = "david";
    String email1 = "dave@gmail.com";
    String motto1 = "seize the day";

    String name2 = "kent sanderson";
    String username2 = "kent";
    String email2 = "kent@gmail.com";
    String motto2 = "seize the day";

    public LoginTest() {
        super(LoginActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
        loginActivity = getActivity();
    }

    /**
     * Taken from here http://stackoverflow.com/questions/10491526/how-can-i-test-the-result-of-a-button-click-that-changes-the-activitys-view-asy/10491965#10491965
     * @param username
     */
    private void signUp(String name, String username, String email, String motto)
    {

        EditText editTextFullName = (EditText) loginActivity.findViewById(R.id.editTextFullName);
        EditText editTextUserNameSignup = (EditText) loginActivity.findViewById(R.id.editTextUserNameSignup);
        EditText editTextEmail = (EditText) loginActivity.findViewById(R.id.editTextEmail);
        EditText editTextMotto = (EditText) loginActivity.findViewById(R.id.editTextMotto);

        assertNotNull(loginActivity.findViewById(R.id.buttonSignup));
        editTextFullName.setText(name);
        editTextMotto.setText(motto);
        editTextEmail.setText(email);
        editTextUserNameSignup.setText(username);

        final Button button = (Button) loginActivity.findViewById(R.id.buttonSignup);
        loginActivity.runOnUiThread(new Runnable() {
            public void run() {
                button.performClick();
            }
        });

        // assume AsyncTask will be finished in 6 seconds.
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Taken from here http://stackoverflow.com/questions/10491526/how-can-i-test-the-result-of-a-button-click-that-changes-the-activitys-view-asy/10491965#10491965
     * @param username
     */
    private void login(String username) {
        EditText usernameLogin = (EditText) loginActivity.findViewById(R.id.editTextUserNameLogin);
        assertNotNull(loginActivity.findViewById(R.id.buttonLogin));
        usernameLogin.setText(username);

        final Button button = (Button) loginActivity.findViewById(R.id.buttonLogin);
        loginActivity.runOnUiThread(new Runnable() {
            public void run() {
                button.performClick();
            }
        });

        // assume AsyncTask will be finished in 6 seconds.
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @UiThreadTest
    public void testLogin(String username){
        login(username);
        assertEquals(username, AppUserSingleton.getInstance().getUser().getJestID());
    }

    @UiThreadTest
    public void testSignup() {
        try {
            signUp(name1, username1, email1, motto1);
            assertEquals(username1, AppUserSingleton.getInstance().getUser().getJestID());
            assertEquals(motto1, AppUserSingleton.getInstance().getUser().getMotto());
            assertEquals(email1, AppUserSingleton.getInstance().getUser().getEmailAddress());
            assertEquals(name1, AppUserSingleton.getInstance().getUser().getFullName());

            User dave = ShareoData.getInstance().getUser(username1);
            dave.new Deleter().useMainThread().delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThreadTest
    public void testSignupAndLogin() {
        try {
            signUp(name2, username2, email2, motto2);
            assertEquals(username2, AppUserSingleton.getInstance().getUser().getJestID());
            assertEquals(motto2, AppUserSingleton.getInstance().getUser().getMotto());
            assertEquals(email2, AppUserSingleton.getInstance().getUser().getEmailAddress());
            assertEquals(name2, AppUserSingleton.getInstance().getUser().getFullName());

            testLogin(username2);
            User user = ShareoData.getInstance().getUser(username2);
            user.new Deleter().useMainThread().delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThreadTest
    public void testKnownLogin() {
        String username = "sally";
        login(username);
        assertEquals(username, AppUserSingleton.getInstance().getUser().getJestID());
    }

    @UiThreadTest
    public void testInvalidLogin() {
        String invalidUsername = "invlaidusername191919191";
        login(invalidUsername);
        try
        {
            assertEquals(invalidUsername, AppUserSingleton.getInstance().getUser().getJestID());
        }
        catch (NullPointerException e)
        {
            fail();
        }
    }
}