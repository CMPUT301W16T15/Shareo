package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by A on 2016-02-09.
 */
public class BiddingTest extends ActivityInstrumentationTestCase2 {
    public BiddingTest() {
        super(MainActivity.class);
    }

    public void testBidOnThing() {
        User bidder = new User("Frank");
        Thing t1 = new Thing(Thing.AVAILABLE);
        Thing t2 = new Thing(Thing.BIDDED);
        Thing t3 = new Thing(Thing.BORROWED);

        try {
            t1.placeBid(bidder, 0.1);
            assertTrue(t1.getStatus() == Thing.BIDDED);
            assertTrue(t1.getBidders().contains(bidder));
        } catch (ThingUnavailableException e) {
            fail();
        }
        try {
            t2.placeBid(bidder, 0.2);
            assertTrue(t2.getStatus() == Thing.BIDDED);
            assertTrue(t1.getBidders().contains(bidder));
        } catch (ThingUnavailableException e) {
            fail();
        }

        try {
            t1.placeBid(bidder, 0.1);
            fail();
        } catch (ThingUnavailableException e) {
            assertTrue(t1.getStatus() == Thing.BORROWED);
            assertFalse(t1.getBidders().contains(bidder));
        }
    }
}
