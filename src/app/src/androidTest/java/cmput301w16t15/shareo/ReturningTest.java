package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import java.util.List;

import thing.Game;
import thing.Thing;
import user.NoGamesFoundException;
import user.User;

/**
 * Created by anonymous on 2/12/2016.
 */
public class ReturningTest extends ActivityInstrumentationTestCase2 {
    User borrower1;
    User borrower2;
    User owner1;
    User owner2;
    Thing t1;
    Thing t2;
    Thing t3;
    Thing t4;

    List<Thing> borrowedGames;

    public ReturningTest() {
        super(MainActivity.class);
    }
    public void initializeTestData() {
        borrower1 = new User("Jack");
        borrower2 = new User("John");
        owner1 = new User("Michael");
        owner2 = new User("Teddy");

        t1 = new Game("Blank", Thing.Status.AVAILABLE);
        t2 = new Game("Blank", Thing.Status.BIDDED);
        t3 = new Game("Blank", Thing.Status.BORROWED);
        t4 = new Game("Blank", Thing.Status.AVAILABLE);
    }

    public void testreturnToAvailable()
    {
        initializeTestData();
        try
        {
            owner1.addOwnedThing(t1);
            borrower1.addBorrowedThing(t1);
            assertTrue(t1.getStatus() == Thing.Status.BORROWED);
            borrower1.removeBorrowedThing(t1);
            assertFalse(borrower1.getBorrowedThings().contains(t1));
            owner1.setReturned(t1);
            assertTrue(owner1.getOwnedThings().contains(t1));

            assertTrue(t1.getStatus() == Thing.Status.AVAILABLE);
        }
        catch (NoGamesFoundException e)
        {
            fail();
        }

        try
        {
            owner1.addOwnedThing(t3);
            owner1.addOwnedThing(t2);
            borrower1.addBorrowedThing(t3);
            assertTrue(t3.getStatus() == Thing.Status.BORROWED);
            borrower2.addBorrowedThing(t2);
            assertTrue(t2.getStatus() == Thing.Status.BORROWED);
            borrower1.removeBorrowedThing(t3);
            assertFalse(borrower1.getBorrowedThings().contains(t3));
            assertTrue(borrower2.getBorrowedThings().contains(t2));
            borrower2.removeBorrowedThing(t3);
            assertTrue(owner1.getOwnedThings().contains(t3));
            owner1.setReturned(t3);
            owner1.setReturned(t2);
            assertTrue(t3.getStatus() == Thing.Status.AVAILABLE);
            assertTrue(t2.getStatus() == Thing.Status.AVAILABLE);
        }
        catch (NoGamesFoundException e)
        {
            fail();
        }

        try
        {
            owner1.setReturned(t4);
        }
        catch (Exception e)
        {
            fail();
        }
    }

}
