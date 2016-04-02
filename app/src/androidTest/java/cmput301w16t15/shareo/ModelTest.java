package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import java.util.List;

import mvc.Bid;
import mvc.Thing;
import mvc.ShareoData;
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

        User joe = null;
        User sally = null;
        User fred = null;
        User dan = null;

        try {
            joe = new User.Builder(data, "joe", "Joe Smith", "joe455@shaw.ca", "We all live in a yellow submarine").build();
            sally = new User.Builder(data, "sally", "Sally Winchester", "sallywin@gmail.com", "Carpe diem").build();
            fred = new User.Builder(data, "fred", "Fred Tanner", "ftanner@hotmail.com", "Don't skip leg day").build();
            dan = new User.Builder(data, "dan", "Daniel Simons", "daniel.simons@gmail.com", "Bah-dun-dun-ch").build();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
            fail();
        }

        Thing tj_1 = null;
        Thing tj_2 = null;
        Thing tj_3 = null;
        Thing ts_1 = null;
        Thing ts_2 = null;
        Thing tf_1 = null;
        Thing tf_2 = null;
        try {
            tj_1 = new Thing.Builder(data, joe, "Monopoly",
                    "The Fast-Dealing Property Trading Game.",
                    "Board, Competitive","4", null).useMainThread().build();
            tj_2 = new Thing.Builder(data, joe, "The Game of Life",
                    "Get a job, get married, have children and retire, all in a game's work!",
                    "Board, Competitive","6", null).useMainThread().build();
            tj_3 = new Thing.Builder(data, joe, "Settlers of Catan",
                    "Embark on a quest to settle the fair isle of Catan! Guide your brave settlers to victory by using clever trading and development.",
                    "Board, Competitive", "6", null).useMainThread().build();
            ts_1 = new Thing.Builder(data, sally, "Hanabi",
                    "A cooperative game in which players try to create the perfect fireworks show by placing the cards on the table in the right order.",
                    "Card, Cooperative, Logic","5", null).useMainThread().build();
            ts_2 = new Thing.Builder(data, sally, "Space Alert",
                    "Players become crew members of a small spaceship scanning dangerous sectors of galaxy. The missions last just 10 real-time minutes (hyperspace jump, sector scan, hyperspace jump back) and the only task the players have is to protect their ship.",
                    "Board, Cooperative, Space", "5", null).useMainThread().build();
            tf_1 = new Thing.Builder(data, fred, "Blokus",
                    "Try to fit as many of your pieces on the board as you can, while your opponents to the same.",
                    "Board, Competitive, Strategy","4", null).useMainThread().build();
            tf_2 = new Thing.Builder(data, fred, "Exploding Kittens",
                    "A kitty-powered version of Russian Roulette",
                    "Card, Competitive", "5", null).useMainThread().build();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(tj_1.getOwner().equals(joe));
        assertTrue(tj_2.getOwner().equals(joe));
        assertTrue(ts_1.getOwner().equals(sally));
        assertTrue(tf_1.getOwner().equals(fred));
        assertTrue(tf_2.getOwner().equals(fred));
        assertTrue(ts_2.getOwner().equals(sally));
        assertTrue(tj_3.getOwner().equals(joe));

        // TODO add some bids.
        Bid b1_1 = null;
        Bid b1_2 = null;
        Bid b1_3 = null;
        Bid b2_1 = null;
        Bid b3_1 = null;
        Bid b4_1 = null;
        try {
            b1_1 = new Bid.Builder(data, sally, tj_1, 1000).useMainThread().build();
            b1_2 = new Bid.Builder(data, fred, tj_1, 1001).useMainThread().build();
            b1_3 = new Bid.Builder(data, dan, tj_1, 800).useMainThread().build();
            b2_1 = new Bid.Builder(data, fred, tj_2, 700).useMainThread().build();
            b3_1 = new Bid.Builder(data, joe, ts_1, 1500).useMainThread().build();
            b4_1 = new Bid.Builder(data, sally, tf_1, 1699).useMainThread().build();
        } catch (NullIDException e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(b1_1.getBidder().equals(sally));
        assertTrue(b1_2.getBidder().equals(fred));
        assertTrue(b1_3.getBidder().equals(dan));
        assertTrue(b2_1.getBidder().equals(fred));
        assertTrue(b3_1.getBidder().equals(joe));
        assertTrue(b4_1.getBidder().equals(sally));

        assertTrue(b1_1.getThing().equals(tj_1));
        assertTrue(b1_2.getThing().equals(tj_1));
        assertTrue(b1_3.getThing().equals(tj_1));
        assertTrue(b2_1.getThing().equals(tj_2));
        assertTrue(b3_1.getThing().equals(ts_1));
        assertTrue(b4_1.getThing().equals(tf_1));

        assertTrue(b1_1.getBidAmount() == 1000);
        assertTrue(b1_2.getBidAmount() == 1001);
        assertTrue(b1_3.getBidAmount() == 800);
        assertTrue(b2_1.getBidAmount() == 700);
        assertTrue(b3_1.getBidAmount() == 1500);
        assertTrue(b4_1.getBidAmount() == 1699);

        assertTrue(sally.getBids().contains(b1_1));
        assertTrue(fred.getBids().contains(b1_2));
        assertTrue(dan.getBids().contains(b1_3));
        assertTrue(fred.getBids().contains(b2_1));
        assertTrue(joe.getBids().contains(b3_1));
        assertTrue(sally.getBids().contains(b4_1));
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
        List<Thing> results = data.getGamesByField("description","one game all");
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
}
