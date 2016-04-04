package cmput301w16t15.shareo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchUITest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);

    @Before
    public void login() {
        // login
        onView(withId(R.id.editTextUserNameLogin)).perform(typeText("sally"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        try {
            Thread.sleep(8000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void searchText() {
        onView(withId(R.id.sliding_tabs)).perform(swipeRight()); // go to profile page
        onView(withId(R.id.sliding_tabs)).perform(swipeRight()); // go to bids page
        closeSoftKeyboard();
        onView(withId(R.id.sliding_tabs)).perform(swipeRight()); // go to bids page
        closeSoftKeyboard();

        onView(withId(R.id.listview)).check(matches(isDisplayed())); // confirm its shown

    }

}