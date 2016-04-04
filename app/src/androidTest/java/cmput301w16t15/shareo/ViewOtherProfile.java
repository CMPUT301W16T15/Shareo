package cmput301w16t15.shareo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by Andrew on 2016-04-03.
 */

import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

public class ViewOtherProfile extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public ViewOtherProfile() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testViewProfile() {
        onView(withId(R.id.ownerButton));
    }
}

