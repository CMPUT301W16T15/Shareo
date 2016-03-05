package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.util.Log;

import io.searchbox.core.Index;
import mvc.Bid;
import mvc.Game;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;
import mvc.UsernameAlreadyExistsException;

/**
 * Created by A on 2016-02-24.
 */
public class ModelTest extends ActivityInstrumentationTestCase2 {

    public ModelTest() {
        super(MainActivity.class);
    }

    public static void testAddRemoveUser() {
        ShareoData data = new ShareoData();
        String username = "Test";
        User u1 = new User(username);

        data.removeUser(u1);
        try {
            data.addUser(u1);
        } catch (UsernameAlreadyExistsException e) {
            fail("Test username already in data");
    }
        User u = data.getUser(username);
        assertTrue(u != null);
        Log.println(Log.DEBUG, "ModelTest", "Username: " + u.getName());
        Log.println(Log.DEBUG, "ModelTest", "ID: " + u.getID());
        Log.println(Log.DEBUG, "ModelTest", "Username: " + u1.getName());
        Log.println(Log.DEBUG, "ModelTest", "ID: " + u1.getID());
        assertTrue(u.equals(u1));

        data.removeUser(u1);
        assertTrue(data.getUser(username) == null);
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
