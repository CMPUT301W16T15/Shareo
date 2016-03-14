package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import mvc.Bid;
import mvc.Thing;
import mvc.exceptions.NullIDException;

/**
 * Created by Larin on 2016/2/10.
 */


public class ThingsTest extends ActivityInstrumentationTestCase2 {
    public ThingsTest() {
        super(MainActivity.class);
    }


    //01.01.01 As an owner, I want to add a card game in my inventory, each denoted with a clear, suitable description.
    public void testAddGame() {
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
            testThings = new Thing.Builder(data, testUser, "Killer", "Role playing game,", "Part Game", "9-20").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
            //Simulate a click on an object where that user owns it
            assertTrue(testThings.getOwner().equals(testUser));
        }
    }

    //01.02.01 and 01.03.01 As an owner, I want to view a list of all my inventory, and their descriptions and statuses.
    public void ViewGameListTest() {
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
        Thing testThings1 = null;
        Thing testThings2 = null;
        try {
            testThings1 = new Thing.Builder(data, testUser, "Killer1", "Role playing game1,", "Part Game1", "9-201").useMainThread().build();
            testThings2 = new Thing.Builder(data, testUser, "Killer2", "Role playing game2,", "Part Game2", "9-202").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        for (int i = 0; i < testUser.getAvailableThings().size(); i++) {
            assertEquals(Thing.Status.AVAILABLE, testUser.getAvailableThings().get(i).getStatus());
        }
        assertEquals("Role playing game1", testUser.getAvailableThings().get(0).getDescription());
        assertEquals("Role playing game2", testUser.getAvailableThings().get(0).getDescription());
    }

    //01.04.01 As an owner, I want to modify a card game in my inventory.

    public void ViewGameTest() {
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
        Thing testThings1 = null;
        Thing testThings2 = null;
        try {
            testThings1 = new Thing.Builder(data, testUser, "Killer1", "Role playing game1,", "Part Game1", "9-201").useMainThread().build();
            testThings2 = new Thing.Builder(data, testUser, "Killer2", "Role playing game2,", "Part Game2", "9-202").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals("Role playing game1", testUser.getAvailableThings().get(0).getDescription());
        assertEquals(Thing.Status.AVAILABLE, testUser.getAvailableThings().get(0).getStatus());
    }

    //01.04.01 As an owner, I want to modify a card game in my inventory.

    public void EditGameTest() {
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
        Thing testThings1 = null;
        try {
            testThings1 = new Thing.Builder(data, testUser, "Killer1", "Role playing game1,", "Part Game1", "9-201").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        testUser.getAvailableThings().get(0).setDescription("Role play");
        testUser.getAvailableThings().get(0).setStatus(Thing.Status.BIDDED);
        assertEquals("Role play", testUser.getAvailableThings().get(0).getDescription());
        assertEquals(Thing.Status.BIDDED, testUser.getAvailableThings().get(0).getStatus());
    }

    //01.05.01 As an owner, I want to delete a card game in my inventory.

    public void DeleteGameTest() {
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
        Thing testThings1 = null;
        Thing testThings2 = null;
        try {
            testThings1 = new Thing.Builder(data, testUser, "Killer1", "Role playing game1,", "Part Game1", "9-201").useMainThread().build();
            testThings1 = new Thing.Builder(data, testUser, "Killer2", "Role playing game2,", "Part Game2", "9-202").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        testUser.removeOwnedThing(testThings1);
        assertEquals("Role playing game2", testUser.getAvailableThings().get(0).getDescription());
        assertEquals("Killer2", testUser.getAvailableThings().get(0).getName());
    }

    //02.01.01 As an owner or borrower, I want a thing to have a status of one of: available, bidded, or borrowed.
    public void testStatus() {
        //Create User
        User testUser = new User("Jack", "Jack Snow", "123@ualberta.ca", "7807091234");
        Thing testThings = new Thing("Killer", " Role playing game", "Part Game", "9-20", Thing.Status.AVAILABLE);
        testThings.setOwnerSimple(testUser);
        testThings.setStatus(Thing.Status.BIDDED);

        assertEquals(testThings.getStatus(), Thing.Status.BIDDED);
    }
}

    /**
     * Using game as mock object to test borrowing of a thing
     */
   /* public void testBorrowThing() {
        String borrower = "John";
        Thing t = null;//new Thing("Catan", "Great Game");
        // TODO use Thing.Builder
        Bid winningBid = null;
        //try {
            //winningBid = new Bid(borrower, t.getJestID(), 10);
            // TODO use Bid.Builder
        //} catch (NullIDException e) {
        //    fail();
        //}
        t.borrow(winningBid);
        assertEquals(t.getAcceptedBid(), winningBid);
        assertEquals(t.getStatus(), Thing.Status.BORROWED);
        assertNull(t.getBids());
    }
*/
    /**
     * Using game as mock object to test returning of a thing
     */
   /* public void testReturnThing()
    {
        String borrower = "John";
        Thing t = null;//new Thing("Catan", "Great Game");
        // TODO use Thing.Builder
        Bid winningBid = null;
//        try {
//            winningBid = new Bid(borrower, t.getJestID(), 10);
//            // TODO use Bid.Builder
//        } catch (NullIDException e) {
//            fail();
//        }
        t.borrow(winningBid);
        t.returnThing();
        assertEquals(t.getStatus(), Thing.Status.AVAILABLE);
        assertNull(t.getAcceptedBid());
        assertNull(t.getBids());
    }
}*/
