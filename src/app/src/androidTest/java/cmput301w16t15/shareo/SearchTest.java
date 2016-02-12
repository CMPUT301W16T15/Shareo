package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import thing.Bid;
import thing.Game;
import thing.Search;
import thing.Thing;
import user.User;

/**
 * Created by anonymous on 2/12/2016.
 */
public class SearchTest extends ActivityInstrumentationTestCase2 {
    User borrower1;
    User borrower2;
    User bidder1;
    User bidder2;
    User owner1;
    User owner2;
    Thing t1;
    Thing t2;
    Thing t3;
    Thing t4;
    Search s1;
    Bid b1;
    Bid b2;

    public SearchTest() {
        super(MainActivity.class);
    }

    public void initializeTestData() {
        borrower1 = new User("Jack");
        borrower2 = new User("John");
        owner1 = new User("Michael");
        owner2 = new User("Teddy");
        bidder1 = new User("Joey");
        bidder2 = new User("Johnny");

        t1 = new Game(Thing.Status.AVAILABLE, "These words will become keywords automatically.");
        t2 = new Game(Thing.Status.BIDDED, "These are all keywords");
        t3 = new Game(Thing.Status.BORROWED, "Board Game");
        t4 = new Game(Thing.Status.AVAILABLE, "Card Game");

        b1 = new Bid(borrower1, t1, 20);
        b2 = new Bid(borrower2, t4,  30);

    }

    public void testsearchForUnborrowedGames() {
        initializeTestData();
        try {
            owner1.addOwnedGame((t1));
            owner1.addOwnedGame((t4));
            s1 = new Search(Thing.getAllUnborrowedThings(Thing.getAllThings()));
            assertTrue(s1.filterByKeyword("automatically").contains(t1));
            assertTrue(s1.filterByKeyword("automatically").contains(t4));
            assertFalse(s1.filterByKeyword("Mcdonalds").contains(t1));
            assertFalse(s1.filterByKeyword("Apple").contains(t4));

            //Filtering by game name and game type will be added later..
        } catch (Exception e) {
            fail();
        }

        try
        {
            owner1.addOwnedGame(t3);
            owner1.addOwnedGame(t2);
            s1 = new Search(Thing.getAllUnborrowedThings(Thing.getAllThings()));
            assertTrue(s1.getCurrentThings().size() == 0);
        }
        catch (Exception e)
        {
            fail();
        }

        try
        {
            owner1.addOwnedGame((t1));
            owner1.addOwnedGame((t4));
            s1 = new Search(Thing.getAllUnborrowedThings(Thing.getAllThings()));
            assertTrue(s1.getCurrentThings().contains(t1));
            assertTrue(s1.getCurrentThings().contains(t4));
            assertFalse(s1.getCurrentThings().contains(t2));
        }

        catch (Exception e)
        {
            fail();
        }
    }

    public void testsearchForBiddedGames()
    {
        initializeTestData();
        try
        {
            owner1.addOwnedGame(t1);
            owner1.addOwnedGame(t2);
            owner1.addOwnedGame(t3);
            owner1.addOwnedGame(t4);

            t1.addBid(bidder1, 10);
            s1 = new Search();
            s1.setCurrentBids(owner1.getBids());
            assertTrue(s1.bidFilterByDescription("automatically").contains(b1));
            assertFalse(s1.bidFilterByDescription("automatically").contains(b2));

        }
        catch (Exception e)
        {
            fail();
        }

        try
        {
            owner1.addOwnedGame((t1));
            owner1.addOwnedGame((t4));
            s1 = new Search();
            s1.setCurrentBids(owner1.getBids());
            assertTrue(s1.getCurrentBids().contains(b1));
            assertTrue(s1.getCurrentBids().contains(b2));
        }

        catch (Exception e)
        {
            fail();
        }


    }

    public void testsearchForBorrowedGames()
    {
        initializeTestData();
        try
        {
            borrower1.addBorrowedGame((t1));
            s1 = new Search(borrower1.getBorrowedGames());
            assertTrue(s1.filterByKeyword("automatically").contains(t1));
        }

        catch(Exception e)
        {
            fail();
        }
    }

}
