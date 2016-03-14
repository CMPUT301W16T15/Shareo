package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import mvc.Bid;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;
import mvc.UserDoesNotExistException;
import mvc.exceptions.NullIDException;
import mvc.exceptions.UsernameAlreadyExistsException;

/**
 * Created by A on 2016-02-09.
 */
public class BiddingTest extends ActivityInstrumentationTestCase2 {
    Bid bid1;
    Bid bid2;
    User bidder1;
    User bidder2;
    Thing t1;
    Thing t2;
    Thing t3;

    String fullName = "my full name";
    String emailAddress = "my email address";
    String motto = "my motto";

    public BiddingTest() {
        super(MainActivity.class);
    }
/*
    public void initializeTestData() {
        bidder1 = new User("Frank", fullName, emailAddress, motto);
        bidder2 = new User("George", fullName, emailAddress, motto);

        String description = "NODESC";

        Thing t1 = new Thing("Blank", description, Thing.Status.AVAILABLE);
        Thing t2 = new Thing("Blank", description, Thing.Status.BIDDED);
        Thing t3 = new Thing("Blank", description, Thing.Status.BORROWED);

        bid1 = new Bid(bidder1, t1, 20);


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
    */
    
    //05.01.01 As a borrower, I want to bid for an available thing, with a monetary rate
    public void BiddingTest() {
        //Create User
        ShareoData data = ShareoData.getInstance();
        User testUser1 = null;
        User testUser2 = null;

        try {
            testUser1 = new User.Builder(data, "Jack", "Jack Snow", "123@ualberta.ca", "7807091234").build();
            testUser2 = new User.Builder(data, "Rob", "Rob Snow", "321@ualberta.ca", "7807094321").build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }
        Bid testBidder1 = null;
        Thing testThings=null;
        try {
            testThings = new Thing.Builder(data, testUser1, "Killer", "Role playing game,", "Part Game", "9-20").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        try {
            testBidder1 = new Bid.Builder(data, testUser2, testThings, 20).useMainThread().build();
        } catch (NullIDException e) {
            e.printStackTrace();
            fail();
        }
        testThings.setStatus(Thing.Status.BIDDED);
        assertTrue(testBidder1.getBidder().equals(testUser2));
        assertTrue(testBidder1.getThing().equals(testThings));
        assertTrue(testBidder1.getBidAmount() == 20);
        assertTrue(testUser1.getBids().contains(testBidder1));
    }
    //05.02.01 | I want to view a list of things I have bidded on that are pending, each thing with its description, owner username, and my bid.
    public void ViewUserBidsTest() {
        //Create User
        ShareoData data = ShareoData.getInstance();
        User testUser1 = null;
        User testUser2 = null;
        User testUser3 = null;

        try {
            testUser1 = new User.Builder(data, "Jack", "Jack Snow", "123@ualberta.ca", "7807091234").build();
            testUser2 = new User.Builder(data, "Rob", "Rob Snow", "321@ualberta.ca", "7807094321").build();
            testUser3 = new User.Builder(data, "Sarah", "Sarah Snow", "3214@ualberta.ca", "7807094322").build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }
        Bid testBidder1 = null;
        Bid testBidder2 = null;
        Thing testThings1=null;
        Thing testThings2=null;
        try {
            testThings1 = new Thing.Builder(data, testUser1, "Killer", "Role playing game,", "Part Game", "9-20").useMainThread().build();
            testThings2 = new Thing.Builder(data, testUser1, "Killer2", "Role playing game2,", "Part Game2", "9-202").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        try {
            testBidder1 = new Bid.Builder(data, testUser2, testThings1, 20).useMainThread().build();
            testBidder2 = new Bid.Builder(data, testUser3, testThings2, 50).useMainThread().build();
        } catch (NullIDException e) {
            e.printStackTrace();
            fail();
        }
        testThings1.setStatus(Thing.Status.BIDDED);
        testThings2.setStatus(Thing.Status.BIDDED);

        assertTrue(testThings1.getStatus() == Thing.Status.BIDDED);
        assertTrue(testThings2.getStatus() == Thing.Status.BIDDED);
        assertEquals(testThings1.getDescription(), "Role playing game");
        assertEquals(testThings2.getDescription(), "Role playing game2");
        assertTrue(testBidder1.getBidAmount() == 20);
        assertTrue(testBidder2.getBidAmount() == 50);
        assertTrue(testBidder1.getBidder()==testUser2);
        assertTrue(testBidder2.getBidder()==testUser2);
    }

    //05.04.01 As an owner, I want to view a list of my things with bids.
    public void testViewUserBids() {
        ShareoData data = ShareoData.getInstance();
        User testUser1 = null;
        User testUser2 = null;
        User testUser3 = null;

        try {
            testUser1 = new User.Builder(data, "Jack", "Jack Snow", "123@ualberta.ca", "7807091234").build();
            testUser2 = new User.Builder(data, "Rob", "Rob Snow", "321@ualberta.ca", "7807094321").build();
            testUser3 = new User.Builder(data, "Sarah", "Sarah Snow", "3214@ualberta.ca", "7807094322").build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }
        Bid testBidder1 = null;
        Bid testBidder2 = null;
        Thing testThings1=null;
        Thing testThings2=null;
        try {
            testThings1 = new Thing.Builder(data, testUser1, "Killer", "Role playing game,", "Part Game", "9-20").useMainThread().build();
            testThings2 = new Thing.Builder(data, testUser1, "Killer2", "Role playing game2,", "Part Game2", "9-202").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        try {
            testBidder1 = new Bid.Builder(data, testUser2, testThings1, 20).useMainThread().build();
            testBidder2 = new Bid.Builder(data, testUser3, testThings2, 50).useMainThread().build();
        } catch (NullIDException e) {
            e.printStackTrace();
            fail();
        }
        testThings1.setStatus(Thing.Status.BIDDED);
        testThings2.setStatus(Thing.Status.BIDDED);

        assertTrue(testThings1.getStatus() == Thing.Status.BIDDED);
        assertTrue(testThings2.getStatus() == Thing.Status.BIDDED);
        assertTrue(testBidder1.getBidAmount() == 20);
        assertTrue(testBidder2.getBidAmount() == 50);
        assertTrue(testBidder1.getBidder()==testUser2);
        assertTrue(testBidder2.getBidder()==testUser3);

        assertEquals(testBidder1.getBidder(), testUser2);
        assertEquals(testBidder2.getBidder(), testUser3);
        assertTrue(testThings1.getBids()== testBidder1);
        assertTrue(testThings2.getBids() == testBidder2);
    }
    //05.05.01 As an owner, I want to view the bids on one of my things.
   /* public void testViewMyThingBids() {
        //Create User
        ShareoData data = ShareoData.getInstance();
        User testUser1 = null;
        User testUser2 = null;
        User testUser3 = null;

        try {
            testUser1 = new User.Builder(data, "Jack", "Jack Snow", "123@ualberta.ca", "7807091234").build();
            testUser2 = new User.Builder(data, "Rob", "Rob Snow", "321@ualberta.ca", "7807094321").build();
            testUser3 = new User.Builder(data, "Sarah", "Sarah Snow", "3214@ualberta.ca", "7807094322").build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }
        Bid testBidder1 = null;
        Bid testBidder2 = null;
        Thing testThings1=null;
        try {
            testThings1 = new Thing.Builder(data, testUser1, "Killer", "Role playing game,", "Part Game", "9-20").useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }
        try {
            testBidder1 = new Bid.Builder(data, testUser2, testThings1, 20).useMainThread().build();
            testBidder2 = new Bid.Builder(data, testUser3, testThings1, 50).useMainThread().build();
        } catch (NullIDException e) {
            e.printStackTrace();
            fail();
        }

        List<Thing> bidThings = new ArrayList<>();
        for (Bid bid : testThings1.getBids()) {
            bidThings.add(bid.getThing());
        }

        assertTrue(bidThings.contains(testThings1));
    }*/
}
