package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import mvc.Bid;
import mvc.Thing;
import mvc.exceptions.NullIDException;

/**
 * Created by Larin on 2016/2/10.
 */


public class ThingsTest extends ActivityInstrumentationTestCase2{
    public ThingsTest() {
        super(MainActivity.class);
    }

    //TODO put these tests inline with actual model.
//    public void TestAddGame(){
//        ArrayList<GameSet> GameList = new ArrayList<GameSet>();
//        GameSet G1 = new GameSet("Killer",10.25,"Cardganme","It it a party game");
//        GameList.add(G1);
//        assertTrue(G1.getGameName() == "Killer");
//        assertTrue(G1.getPricePerDay() == 10.25 );
//        assertTrue(G1.getKeyWord() == "Cardgame" );
//        assertTrue(G1.getDescription() == "It it a party game" );
//    }
//    public void TestViewGameList(){
//        /**
//         * cannot be tested so far
//         */
//    }
//    public void TestViewGame(){
//        /**
//         * cannot be tested so far
//         */
//    }
//    public void TestEditGame(){
//        ArrayList<GameSet> GameList = new ArrayList<GameSet>();
//        GameSet G1 = new GameSet("Killer",10.25,"Cardganme","It it a party game");
//        GameList.add(G1);
//        G1.setPricePerDay(8.25);
//        assertTrue(G1.getPricePerDay() == 8.25);
//
//    }
//    public void TestDeleteGame(){
//        ArrayList<GameSet> GameList = new ArrayList<GameSet>();
//        GameSet G1 = new GameSet("Killer",10.25,"Cardganme","It it a party game");
//        GameList.add(G1);
//        GameList.remove(G1);
//        assertTrue(GameList.isEmpty());
//    }

    /**
     * Using game as mock object to test borrowing of a thing
     */
    public void testBorrowThing() {
        String borrower = "John";
        Thing t = null;//new Thing("Catan", "Great Game");
        // TODO use Thing.Builder
        Bid winningBid = null;
        try {
            winningBid = new Bid(borrower, t.getJestID(), 10);
        } catch (NullIDException e) {
            fail();
        }
        t.borrow(winningBid);
        assertEquals(t.getAcceptedBid(), winningBid);
        assertEquals(t.getStatus(), Thing.Status.BORROWED);
        assertNull(t.getBids());
    }

    /**
     * Using game as mock object to test returning of a thing
     */
    public void testReturnThing()
    {
        String borrower = "John";
        Thing t = null;//new Thing("Catan", "Great Game");
        // TODO use Thing.Builder
        Bid winningBid = null;
        try {
            winningBid = new Bid(borrower, t.getJestID(), 10);
        } catch (NullIDException e) {
            fail();
        }
        t.borrow(winningBid);
        t.returnThing();
        assertEquals(t.getStatus(), Thing.Status.AVAILABLE);
        assertNull(t.getAcceptedBid());
        assertNull(t.getBids());
    }
}
