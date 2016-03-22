package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import java.util.List;

import mvc.Bid;
import mvc.Thing;
import mvc.ShareoData;
import mvc.ThingDoesNotExistException;
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
        User joe = data.getUser("joe");
        User sally = data.getUser("sally");
        User fred = data.getUser("fred");

        if (joe != null) joe.new Deleter().useMainThread().delete();
        if (sally != null) sally.new Deleter().useMainThread().delete();
        if (fred != null) fred.new Deleter().useMainThread().delete();

        joe = data.getUser("joe");
        sally = data.getUser("sally");
        fred = data.getUser("fred");

        assertNull(joe);
        assertNull(sally);
        assertNull(fred);

        String fullName = "my full name";
        String emailAddress = "my email address";
        String motto = "my motto";

        try {
            joe = new User.Builder(data, "joe", fullName, emailAddress, motto).build();
            sally = new User.Builder(data, "sally", fullName, emailAddress, motto).build();
            fred = new User.Builder(data, "fred", fullName, emailAddress, motto).build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }

        Thing t1 = null;
        Thing t2 = null;
        Thing t3 = null;
        Thing t4 = null;
        try {
            t1 = new Thing.Builder(data, joe, "Game1", "One game to rule them all,","GameCategory","2").useMainThread().build();
            t2 = new Thing.Builder(data, joe, "Game2", "One game to find them,","GameCategory","2").useMainThread().build();
            t3 = new Thing.Builder(data, sally, "Game3", "One game to bring them all,","GameCategory","2").useMainThread().build();
            t4 = new Thing.Builder(data, fred, "Game4", "And in the darkness, bind them.","GameCategory","2").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(t1.getOwner().equals(joe));
        assertTrue(t2.getOwner().equals(joe));
        assertTrue(t3.getOwner().equals(sally));
        assertTrue(t4.getOwner().equals(fred));

        // TODO add some bids.
        Bid b1 = null;
        Bid b2 = null;
        Bid b3 = null;
        Bid b4 = null;
        Bid b5 = null;
        try {
            b1 = new Bid.Builder(data, joe, t3, 100).useMainThread().build();
            b2 = new Bid.Builder(data, sally, t1, 200).useMainThread().build();
            b3 = new Bid.Builder(data, sally, t4, 300).useMainThread().build();
            b4 = new Bid.Builder(data, fred, t1, 400).useMainThread().build();
            b5 = new Bid.Builder(data, fred, t2, 500).useMainThread().build();
        } catch (NullIDException e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(b1.getBidder().equals(joe));
        assertTrue(b2.getBidder().equals(sally));
        assertTrue(b3.getBidder().equals(sally));
        assertTrue(b4.getBidder().equals(fred));
        assertTrue(b5.getBidder().equals(fred));

        assertTrue(b1.getThing().equals(t3));
        assertTrue(b2.getThing().equals(t1));
        assertTrue(b3.getThing().equals(t4));
        assertTrue(b4.getThing().equals(t1));
        assertTrue(b5.getThing().equals(t2));

        assertTrue(b1.getBidAmount() == 100);
        assertTrue(b2.getBidAmount() == 200);
        assertTrue(b3.getBidAmount() == 300);
        assertTrue(b4.getBidAmount() == 400);
        assertTrue(b5.getBidAmount() == 500);

        assertTrue(joe.getBids().contains(b1));
        assertTrue(sally.getBids().contains(b2));
        assertTrue(sally.getBids().contains(b3));
        assertTrue(fred.getBids().contains(b4));
        assertTrue(fred.getBids().contains(b5));

        try {
            b1.new Deleter().delete();

            assertFalse(joe.getBids().contains(b1));
            assertNull(data.getBid(b1.getJestID()));
        } catch (NullIDException e) {
            fail();
        }

        sally.new Deleter().delete();

        try {
            assertNull(data.getUser(sally.getName()));
            assertNull(data.getGame(t3.getJestID()));
            assertNull(data.getBid(b2.getJestID()));
            assertNull(data.getBid(b3.getJestID()));
        } catch (NullIDException e) {
            fail();
        }
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

    public static void testSearch() {
        ShareoData data = ShareoData.getInstance();
        List<Thing> results = data.getGamesByDescription("one game all");
        for (Thing t : results) {
            assertTrue(t.getDescription().toLowerCase().contains("one"));
            assertTrue(t.getDescription().toLowerCase().contains("game"));
            assertTrue(t.getDescription().toLowerCase().contains("all"));
        }
    }

    public static void testAddRemoveUser() {
        ShareoData data = ShareoData.getInstance();
        String username = "Test";
        User u1 = null;

        String fullName = "my full name";
        String emailAddress = "my email address";
        String motto = "my motto";

        data.removeUser(username);

        try {
            u1 = new User.Builder(data, username, fullName, emailAddress, motto).build();
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
