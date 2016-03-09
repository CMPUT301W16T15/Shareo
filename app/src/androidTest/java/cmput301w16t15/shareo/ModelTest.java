package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import mvc.Thing;
import mvc.ShareoData;
import mvc.User;
import mvc.UserDoesNotExistException;
import mvc.exceptions.NullIDException;
import mvc.exceptions.UsernameAlreadyExistsException;

/**
 * Created by A on 2016-02-24.
 */
public class ModelTest extends ActivityInstrumentationTestCase2 {

    public ModelTest() {
        super(MainActivity.class);
    }

    public static void populateData() {
        ShareoData data = ShareoData.getInstance();
        User joe = null;
        User sally = null;
        User fred = null;

        data.removeUser("joe");
        data.removeUser("sally");
        data.removeUser("fred");

        try {
            joe = new User.Builder(data, "joe").build();
            sally = new User.Builder(data, "sally").build();
            fred = new User.Builder(data, "fred").build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }

        Thing t1 = null;
        Thing t2 = null;
        Thing t3 = null;
        Thing t4 = null;
        try {
            t1 = new Thing.Builder(data, "joe", "Game1", "One game to rule them all,").build();
            t2 = new Thing.Builder(data, "joe", "Game2", "One game to find them,").build();
            t3 = new Thing.Builder(data, "sally", "Game3", "One game to bring them all,").build();
            t4 = new Thing.Builder(data, "fred", "Game4", "And in the darkness, bind them.").build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(t1.getOwner().equals(joe));
        assertTrue(t2.getOwner().equals(joe));
        assertTrue(t3.getOwner().equals(sally));
        assertTrue(t4.getOwner().equals(fred));

        // TODO add some bids.
    }

    public static void testPopulate() {
        populateData();

        ShareoData data = ShareoData.getInstance();

        User joe = data.getUser("joe");
        User sally = data.getUser("sally");
        User fred = data.getUser("fred");

        assertNotNull(joe);
        assertNotNull(sally);
        assertNotNull(fred);

        assertNotNull(joe.getOwnedThings());
        assertNotNull(sally.getOwnedThings());
        assertNotNull(fred.getOwnedThings());

        assertTrue(joe.getOwnedThings().size() == 2);
        assertTrue(sally.getOwnedThings().size() == 1);
        assertTrue(fred.getOwnedThings().size() == 1);

        // TODO assert that correct things are owned.
    }

    public static void testAddRemoveUser() {
        ShareoData data = ShareoData.getInstance();
        String username = "Test";
        User u1 = null;

        data.removeUser(username);

        try {
            u1 = new User.Builder(data, username).build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }

        User u = data.getUser(username);
        assertTrue(u != null);
        assertTrue(u.equals(u1));
        try {
            data.removeUser(u1);
            assertTrue(data.getUser(username) == null);
        } catch (NullIDException e) {
            fail();
        }
    }

    /*
    public void testSetOwner() {
        ShareoData data = new ShareoData();
        User u1 = new User("Bob");
        User u2 = new User("Susan");
        Thing t1 = new Game("A card game");

        try {
            data.addUser(u1);
            data.addUser(u2);
            data.addThing(t1);
        } catch (UsernameAlreadyExistsException e) {
            fail();
        }

        assertTrue(t1.getOwner() == null);
        assertTrue(!u1.getOwnedThings().contains(t1));
        assertTrue(!u2.getOwnedThings().contains(t1));

        t1.setOwner(u1);
        data.updateThing(t1);
        data.updateUser(u1);
        u1 = data.getUser(u1.getName());
        u2 = data.getUser(u2.getName());
        t1 = data.getThing(t1.getID());
        assertTrue(t1.getOwner().equals(u1));
        assertTrue(u1.getOwnedThings().contains(t1));
        assertTrue(!u2.getOwnedThings().contains(t1));

        t1.setOwner(u2);
        assertTrue(t1.getOwner().equals(u2));
        assertTrue(!u1.getOwnedThings().contains(t1));
        assertTrue(u2.getOwnedThings().contains(t1));

        t1.setOwner(null);
        assertTrue(t1.getOwner() == null);
        assertTrue(!u1.getOwnedThings().contains(t1));
        assertTrue(!u2.getOwnedThings().contains(t1));
    }
    */

    /*
    public void testSetBorrower() {
        User u1 = new User("Bob");
        User u2 = new User("Susan");
        Thing t1 = new Game("A card game");

        assertTrue(t1.getBorrower() == null);
        assertTrue(!u1.getBorrowedThings().contains(t1));
        assertTrue(!u2.getBorrowedThings().contains(t1));

        t1.setBorrower(u1);
        assertTrue(t1.getBorrower().equals(u1));
        assertTrue(u1.getBorrowedThings().contains(t1));
        assertTrue(!u2.getBorrowedThings().contains(t1));

        t1.setBorrower(u2);
        assertTrue(t1.getBorrower().equals(u2));
        assertTrue(!u1.getBorrowedThings().contains(t1));
        assertTrue(u2.getBorrowedThings().contains(t1));

        t1.setBorrower(null);
        assertTrue(t1.getBorrower() == null);
        assertTrue(!u1.getBorrowedThings().contains(t1));
        assertTrue(!u2.getBorrowedThings().contains(t1));
    }
    */

    /*
    public void testAddBid() {
        User u1 = new User("Bob");
        User u2 = new User("Susan");
        Thing t1 = new Game("A card game");

        Bid bid1 = new Bid(u1, t1, 100);
        assertTrue(u1.getBids().contains(bid1));
        assertTrue(!u2.getBids().contains(bid1));
        assertTrue(t1.getBids().contains(bid1));
        assertTrue(t1.getBidders().contains(u1));
        assertTrue(!t1.getBidders().contains(u2));

        Bid bid2 = new Bid(u1, t1, 200);
        assertTrue(u1.getBids().contains(bid1));
        assertTrue(!u2.getBids().contains(bid1));
        assertTrue(t1.getBids().contains(bid1));
        assertTrue(t1.getBidders().contains(u1));
        assertTrue(!t1.getBidders().contains(u2));
        assertTrue(u1.getBids().contains(bid2));
        assertTrue(!u2.getBids().contains(bid2));
        assertTrue(t1.getBids().contains(bid2));
        assertTrue(t1.getBidders().contains(u1));
        assertTrue(!t1.getBidders().contains(u2));

        Bid bid3 = new Bid(u2, t1, 300);
        assertTrue(u1.getBids().contains(bid1));
        assertTrue(!u2.getBids().contains(bid1));
        assertTrue(t1.getBids().contains(bid1));
        assertTrue(t1.getBidders().contains(u1));
        assertTrue(!t1.getBidders().contains(u2));
        assertTrue(u1.getBids().contains(bid2));
        assertTrue(!u2.getBids().contains(bid2));
        assertTrue(t1.getBids().contains(bid2));
        assertTrue(t1.getBidders().contains(u1));
        assertTrue(!t1.getBidders().contains(u2));
        assertTrue(!u1.getBids().contains(bid3));
        assertTrue(u2.getBids().contains(bid3));
        assertTrue(t1.getBids().contains(bid3));
        assertTrue(t1.getBidders().contains(u1));
        assertTrue(t1.getBidders().contains(u2));

    }
    */
}
