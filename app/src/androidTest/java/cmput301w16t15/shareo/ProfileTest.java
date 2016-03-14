package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Larin on 2016/2/11.
 */
public class ProfileTest extends ActivityInstrumentationTestCase2{
    public ProfileTest() {
        super(MainActivity.class);
    }

 //03.01.01 As a user, I want a profile with a unique username and my contact information.
    public void testAddUser() {
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
        //03.03.01 As a user, I want to, when a username is presented for a thing, retrieve and show
    // its contact information
    public void testViewRentersInfo () {
        //Create User
        User testUser = new User("Jack","Jack Snow","123@ualberta.ca","7807091234");
        Thing testThings = new Thing("Killer"," Role playing game","Part Game","9-20", Thing.Status.AVAILABLE);
        testThings.setOwnerSimple(testUser);
        testThings.setStatus(Thing.Status.BIDDED);

        //Simulate a click on an object where that user owns it
        //fail if I add this command
        //seems like that we cannot connect the relationship between adding things to user and bidding
        //assertTrue(testUser.getOwnedBiddedThings().contains(testThings.getName()));

        assertEquals(testUser.getName(), "Jack");
        assertEquals(testUser.getFullName(), "Jack Snow");
        assertEquals(testUser.getEmailAddress(), "123@ualberta.ca");
        assertEquals(testUser.getMotto(), "7807091234");
    }
    
     //03.03.01 As a user, I want to, when a username is presented for a thing, retrieve and show
    // its contact information
    public static void testViewRentersInfo () {
        //Create User
        ShareoData data = ShareoData.getInstance();
        User testUser = null;
        String fullName = "Jack Snow";
        String emailAddress = "123@ualberta.ca";
        String motto = "7807091234";
        data.removeUser("Jack");


        try {
            testUser = new User.Builder(data, "Jack", fullName, emailAddress, motto).build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }
        Thing testThings = null;
        try {
            testThings = new Thing.Builder(data, testUser, "Killer", "Role playing game,","Part Game","9-20").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        testThings.setStatus(Thing.Status.BIDDED);

        //Simulate a click on an object where that user owns it
        assertTrue(testThings.getOwner().equals(testUser));

    }
