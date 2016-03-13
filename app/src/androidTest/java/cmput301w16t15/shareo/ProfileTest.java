package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Larin on 2016/2/11.
 */
public class ProfileTest extends ActivityInstrumentationTestCase2{
    public ProfileTest() {
        super(MainActivity.class);
    }

//03.02.01 As a user, I want to edit the contact information in my profile
    public void testEditUser() {
        User testUser = new User("Jack","Jack Snow","123@ualberta.ca","7807091234");

        //Initialize user contact information
        testUser.setName("Jack");
        testUser.setFullName("Jack Snow");
        testUser.setEmailAddress("123@ualberta.ca");
        testUser.setMotto("7807091234");

        //Check to see if that is set to the user
        assertEquals(testUser.getName(), "Jack");
        assertEquals(testUser.getFullName(), "Jack Snow");
        assertEquals(testUser.getEmailAddress(), "123@ualberta.ca");
        assertEquals(testUser.getMotto(), "7807091234");

        //Edit the user information
        testUser.setEmailAddress("shareo@ualberta.ca");
        testUser.setMotto("7807094321");

        //Check to see if the user information is updated
        assertEquals(testUser.getEmailAddress(), "shareo@ualberta.ca");
        assertEquals(testUser.getMotto(), "7807094321");

    }

}
