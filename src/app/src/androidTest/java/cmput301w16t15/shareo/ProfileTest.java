package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

import thing.ProfileSet;
import user.User;

/**
 * Created by Larin on 2016/2/11.
 */
public class ProfileTest extends ActivityInstrumentationTestCase2{
    public ProfileTest() {
        super(MainActivity.class);
    }

    public void TestCreateProfile() {
        ArrayList<ProfileSet> ProfileList = new ArrayList<>();
        ProfileSet User = new ProfileSet("JohnSnow","12345@ualberta.ca","7804561234");
        ProfileList.add(User);
        assertEquals("JohnSnow", User.getUserName());
        assertEquals("12345@ualberta.ca", User.getEmail());
        assertEquals("7804561234", User.getPhoneNumber());
    }
    public void TestEditProfile() {
        ArrayList<ProfileSet> ProfileList = new ArrayList<>();
        ProfileSet User = new ProfileSet("JohnSnow","12345@ualberta.ca","7804561234");
        ProfileList.add(User);
        User.setUserName("GentleMan");
        assertEquals("GentleMan", User.getUserName());

    }
    public void TestShowContact() {
        /**
         * cannot be tested so far
         */
    }

}
