package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import org.apache.commons.lang3.ObjectUtils;

import mvc.Game;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;
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

        User joe = new User("joe");
        User sally = new User("sally");
        User fred = new User("fred");

        try {
            data.removeUser(joe);
            data.removeUser(sally);
            data.removeUser(fred);
        } catch (NullIDException e) {
            // should not be possible.
            e.printStackTrace();
        }

        try {
            data.addUser(joe);
            data.addUser(sally);
            data.addUser(fred);
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        }

        Game t1 = new Game("Game1", "One game to rule them all,");
        Game t2 = new Game("Game2", "One game to find them,");
        Game t3 = new Game("Game3", "One game to bring them all,");
        Game t4 = new Game("Game4", "And in the darkness, bind them.");

        data.addGame(t1);
        data.addGame(t2);
        data.addGame(t3);
        data.addGame(t4);

        try {
            joe.addOwnedThing(t1);
            joe.addOwnedThing(t2);
            sally.addOwnedThing(t3);
            fred.addOwnedThing(t4);

            data.updateUser(joe);
            data.updateUser(sally);
            data.updateUser(fred);
        } catch (NullIDException e) {
            e.printStackTrace();
        }

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
        User u1 = new User(username);

        try {
            data.removeUser(u1);
        } catch (NullIDException e) {
            fail();
        }
        try {
            data.addUser(u1);
        } catch (UsernameAlreadyExistsException e) {
            fail("Test username already in data");
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
