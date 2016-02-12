package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.List;

import thing.Game;
import thing.Thing;
import thing.ThingUnavailableException;
import user.NoGamesFoundException;
import user.User;

/**
 * Created by anonymous on 2/12/2016.
 */
public class BorrowingTest extends ActivityInstrumentationTestCase2 {
    User borrower1;
    User borrower2;
    User owner1;
    User owner2;
    Thing t1;
    Thing t2;
    Thing t3;

    List<Thing> borrowedGames;

    public BorrowingTest() {
        super(MainActivity.class);
    }

    public void initializeTestData() {
        borrower1 = new User("Jack");
        borrower2 = new User("John");
        owner1 = new User("Michael");
        owner2 = new User("Teddy");

        t1 = new Game(Thing.Status.AVAILABLE, "Blank");
        t2 = new Game(Thing.Status.BIDDED, "Blank");
        t3 = new Game(Thing.Status.BORROWED, "Blank");
    }

    public void testViewBorrowedList()
    {
        try
        {
            initializeTestData();

            borrower1.addBorrowedGame(t3);
            assertTrue(borrower1.getBorrowedGames().contains(t3));
        }
        catch (NoGamesFoundException e)
        {
           fail();
        }

        try
        {
            assertFalse(borrower1.getBorrowedGames().contains(t1));
        }
        catch (NoGamesFoundException e)
        {
            fail();
        }

        try
        {
            borrower2.addBorrowedGame(t1);
            borrower2.addBorrowedGame(t2);
            borrower2.addBorrowedGame(t3);

            assertTrue(borrower2.getBorrowedGames().contains(t1));
            assertTrue(borrower2.getBorrowedGames().contains(t2));
            assertTrue(borrower2.getBorrowedGames().contains(t3));
        }

        catch (NoGamesFoundException e)
        {
            fail();
        }
    }

    public void testviewMyGamesBorrowedList() {
        initializeTestData();
        try {
            owner1.addOwnedGame(t1);
            borrower1.addBorrowedGame(t1);
            assertTrue(owner1.getOwnedGames().contains(t1));
            assertTrue(borrower1.getBorrowedGames().contains(t1));
        } catch (NoGamesFoundException e)
        {
            fail();
        }

        try
        {
            assertFalse(owner1.getOwnedGames().contains(t2));
        }
        catch (NoGamesFoundException e)
        {
            fail();
        }

        try
        {
            owner1.addOwnedGame(t2);
            owner1.addOwnedGame(t3);
            borrower1.addBorrowedGame(t2);
            borrower2.addBorrowedGame(t3);

            assertTrue(owner1.getOwnedGames().contains(t2));
            assertTrue(owner2.getOwnedGames().contains(t3));

            assertTrue(borrower1.getBorrowedGames().contains(t2));
            assertTrue(borrower2.getBorrowedGames().contains(t3));
        }
        catch (NoGamesFoundException e)
        {
            fail();
        }
    }



}

