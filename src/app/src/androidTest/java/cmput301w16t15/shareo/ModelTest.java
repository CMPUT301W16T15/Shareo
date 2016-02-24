package cmput301w16t15.shareo;

import android.test.ActivityInstrumentationTestCase2;

import mvc.Game;
import mvc.Thing;
import mvc.User;

/**
 * Created by A on 2016-02-24.
 */
public class ModelTest extends ActivityInstrumentationTestCase2 {
    public ModelTest() {
        super(MainActivity.class);
    }

    public void testAddOwner() {
        User u1 = new User("Bob");
        User u2 = new User("Susan");
        Thing t1 = new Game("A card game");

        assertTrue(t1.getOwner() == null);
        assertTrue(!u1.getOwnedThings().contains(t1));
        assertTrue(!u2.getOwnedThings().contains(t1));

        t1.setOwner(u1);
        assertTrue(t1.getOwner().equals(u1));
        assertTrue(u1.getOwnedThings().contains(t1));
        assertTrue(!u2.getOwnedThings().contains(t1));

        t1.setOwner(u2);
        assertTrue(t1.getOwner().equals(u2));
        assertTrue(!u1.getOwnedThings().contains(t1));
        assertTrue(u2.getOwnedThings().contains(t1));

        t1.setOwner(null);
        assertTrue(t1.getOwner() == null);
        assertTrue(!u1.getOwnedThings().contains(t1));
        assertTrue(!u2.getOwnedThings().contains(t1));

    }
}
