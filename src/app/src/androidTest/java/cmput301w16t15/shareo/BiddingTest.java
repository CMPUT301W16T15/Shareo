package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import thing.Bid;
import thing.Game;
import thing.Thing;
import thing.ThingUnavailableException;
import user.User;

/**
 * Created by A on 2016-02-09.
 */
public class BiddingTest extends ActivityInstrumentationTestCase2 {
    User bidder1;
    User bidder2;
    Thing t1;
    Thing t2;
    Thing t3;

    public BiddingTest() {
        super(MainActivity.class);
    }

    public void initializeTestData() {
        bidder1 = new User("Frank");
        bidder2 = new User("George");

        Thing t1 = new Game(Thing.Status.AVAILABLE, "Blank");
        Thing t2 = new Game(Thing.Status.BIDDED, "Blank");
        Thing t3 = new Game(Thing.Status.BORROWED, "Blank");
    }

    public void testBidOnThing() {
        initializeTestData();

        try {
            t1.addBid(bidder1, 10);
            assertTrue(t1.getStatus() == Thing.Status.BIDDED);
            assertTrue(t1.getBidders().contains(bidder1));
        } catch (ThingUnavailableException e) {
            fail();
        }
        try {
            t2.addBid(bidder1, 20);
            assertTrue(t2.getStatus() == Thing.Status.BIDDED);
            assertTrue(t1.getBidders().contains(bidder1));
        } catch (ThingUnavailableException e) {
            fail();
        }

        try {
            t1.addBid(bidder1, 10);
            fail();
        } catch (ThingUnavailableException e) {
            assertTrue(t1.getStatus() == Thing.Status.BORROWED);
            assertFalse(t1.getBidders().contains(bidder1));
        }
    }

    public void testViewUserBids() {
        initializeTestData();

        try {
            t1.addBid(bidder1, 10);
        } catch (ThingUnavailableException e) {
            fail();
        }

        try {
            t2.addBid(bidder1, 20);
        } catch (ThingUnavailableException e) {
            fail();
        }

        assertTrue(t1.getBidders().contains(bidder1));
        assertTrue(t2.getBidders().contains(bidder1));
        assertTrue(bidder1.getBids().contains(t1));
        assertTrue(bidder1.getBids().contains(t2));
    }

    public void testViewMyThingBids() {
        initializeTestData();

        try {
            t1.addBid(bidder1, 10);
        } catch (ThingUnavailableException e) {
            fail();
        }

        try {
            t2.addBid(bidder1, 20);
            t2.addBid(bidder2, 30);
        } catch (ThingUnavailableException e) {
            fail();
        }

        try {
            t3.addBid(bidder2, 20);
        } catch (ThingUnavailableException e) {
            fail();
        }

        List<Thing> bidThings = new ArrayList<>();
        for (Bid bid : bidder1.getBids()) {
            bidThings.add(bid.getThing());
        }

        assertTrue(bidThings.contains(t1));
        assertTrue(bidThings.contains(t2));
        assertFalse(bidThings.contains(t3));
    }
}
