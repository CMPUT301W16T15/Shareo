package cmput301w16t15.shareo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginUITest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Test
    public void loginTest() {
        // user name and login
        onView(withId(R.id.editTextUserNameLogin)).perform(typeText("sally"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
    }
}

