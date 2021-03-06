package cmput301w16t15.shareo;

import static android.support.test.espresso.Espresso.onData;
import android.support.test.espresso.DataInteraction;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.anything;


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
public class PlaceBidUITest {

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
    public void placeBid() {
        onView(withId(R.id.sliding_tabs)).perform(swipeRight()); // go to profile page
        onView(withId(R.id.sliding_tabs)).perform(swipeRight()); // go to bids page
        onView(withId(R.id.sliding_tabs)).perform(swipeRight()); // go to search page

        onView(withId(R.id.searchbox)).perform(typeText("game"));

        try {
            Thread.sleep(3000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        onData(anything()).inAdapterView(withId(R.id.listview)).atPosition(0).perform(click());

        onView(withId(R.id.editTextMakeOffer)).perform(typeText("10"), closeSoftKeyboard());

    }

}