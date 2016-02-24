package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import java.util.List;

import mvc.Game;
import mvc.Thing;
import mvc.User;

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

        t1 = new Game("Blank", Thing.Status.AVAILABLE);
        t2 = new Game("Blank", Thing.Status.BIDDED);
        t3 = new Game("Blank", Thing.Status.BORROWED);
    }

    public void testViewBorrowedList()
    {
        initializeTestData();

        borrower1.addBorrowedThing(t3);
        assertTrue(borrower1.getBorrowedThings().contains(t3));

        assertFalse(borrower1.getBorrowedThings().contains(t1));

        borrower2.addBorrowedThing(t1);
        borrower2.addBorrowedThing(t2);
        borrower2.addBorrowedThing(t3);

        assertTrue(borrower2.getBorrowedThings().contains(t1));
        assertTrue(borrower2.getBorrowedThings().contains(t2));
        assertTrue(borrower2.getBorrowedThings().contains(t3));
    }

    public void testviewMyGamesBorrowedList() {
        initializeTestData();
        owner1.addOwnedThing(t1);
        borrower1.addBorrowedThing(t1);
        assertTrue(owner1.getOwnedThings().contains(t1));
        assertTrue(borrower1.getBorrowedThings().contains(t1));

        assertFalse(owner1.getOwnedThings().contains(t2));

        owner1.addOwnedThing(t2);
        owner1.addOwnedThing(t3);
        borrower1.addBorrowedThing(t2);
        borrower2.addBorrowedThing(t3);

        assertTrue(owner1.getOwnedThings().contains(t2));
        assertTrue(owner2.getOwnedThings().contains(t3));

        assertTrue(borrower1.getBorrowedThings().contains(t2));
        assertTrue(borrower2.getBorrowedThings().contains(t3));
    }



}

