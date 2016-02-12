package user;

import java.util.List;

import thing.Bid;
import thing.Game;
import thing.Thing;

/**
 * Created by A on 2016-02-10.
 */
public class User {
    public User(String username) {}

    public String getName() { return null; }
    public List<Bid> getBids() { return null; }
    public void addOwnedGame(Thing game) {}
    public void addBorrowedGame(Thing game){ }
    public List<Thing> getBorrowedGames() { return null; }
    public List<Thing> getOwnedGames() { return null; }
    public void addThing(Thing thing) {}
    public List<Thing> getThings() { return null; }
}
