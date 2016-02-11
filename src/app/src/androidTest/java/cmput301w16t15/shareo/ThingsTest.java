package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

import thing.Game;
import thing.GameSet;

/**
 * Created by Larin on 2016/2/10.
 */
public class ThingsTest extends ActivityInstrumentationTestCase2{
    public ThingsTest() {
        super(MainActivity.class);
    }
    public void TestAddGame(){
        ArrayList<GameSet> GameList = new ArrayList<GameSet>();
        GameSet G1 = new GameSet("Killer",10.25,"Cardganme","It it a party game");
        GameList.add(G1);
        assertTrue(GameList.getGameName() == "Killer");
        assertTrue(GameList.getPricePerDay() == 10.25 );
        assertTrue(GameList.getKeyWord() == "Cardgame" );
        assertTrue(GameList.getDescription() == "It it a party game" );
    }
    public void TestViewGameList(){

    }
    public void TestViewGame(){

    }
    public void TestEditGame(){
        ArrayList<GameSet> GameList = new ArrayList<GameSet>();
        GameSet G1 = new GameSet("Killer",10.25,"Cardganme","It it a party game");
        GameList.add(G1);
        GameList.setPricePerDay = 8.25;
        assertTrue(GameList.getPricePerDay() == 8.25);

    }
    public void TestDeleteGame(){

    }
}
