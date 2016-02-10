package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import thing.Game;
import thing.Thing;
import user.User;

/**
 * Created by A on 2016-02-09.
 */
public class BiddingTest extends ActivityInstrumentationTestCase2 {
    public BiddingTest() {
        super(MainActivity.class);
    }

    public void testBidOnThing() {
        User bidder = new User("Frank");
        Thing t1 = new Game(Thing.Status.AVAILABLE);
        Thing t2 = new Game(Thing.Status.BIDDED);
        Thing t3 = new Game(Thing.Status.BORROWED);

        try {
            t1.addBid(bidder, 0.1);
            assertTrue(t1.getStatus() == Thing.BIDDED);
            assertTrue(t1.getBidders().contains(bidder));
        } catch (ThingUnavailableException e) {
            fail();
        }
        try {
            t2.addBid(bidder, 0.2);
            assertTrue(t2.getStatus() == Thing.BIDDED);
            assertTrue(t1.getBidders().contains(bidder));
        } catch (ThingUnavailableException e) {
            fail();
        }

        try {
            t1.addBid(bidder, 0.1);
            fail();
        } catch (ThingUnavailableException e) {
            assertTrue(t1.getStatus() == Thing.BORROWED);
            assertFalse(t1.getBidders().contains(bidder));
        }
    }

    public void testViewUserBids() {
        User bidder = new User("Frank");

        Thing t1 = new Game(Thing.AVAILABLE);
        Thing t2 = new Game(Thing.BIDDED);

        t1.addBid(bidder, 0.1);
        t2.addBid(bidder, 0.2);

        assertTrue(t1.getBidders().contains(bidder));
        assertTrue(t2.getBidders().contains(bidder));
        assertTrue(bidder.getCurrentBids().contains(t1));
        assertTrue(bidder.getCurrentBids().contains(t2));
    }
}
