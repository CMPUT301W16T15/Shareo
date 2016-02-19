package user;

import java.util.List;

import thing.Bid;
import thing.Game;
import thing.Thing;
import thing.ThingUnavailableException;

/**
 * Created by A on 2016-02-10.
 */
public class User {
    public User(String username) {}

    public String getName() { return null; }
    public List<Bid> getBids() { return null; }
    public void addOwnedGame(Thing game) {}
    public void addBorrowedGame(Thing game){ }
    public void removeBorrowedGame(Thing game){ }
    public List<Thing> getBorrowedGames() throws NoGamesFoundException { return null; }
    public List<Thing> getOwnedGames () throws NoGamesFoundException { return null; }
    public void addThing(Thing thing) {}
    public void setReturned(Thing thing) {}
    public List<Thing> getThings() { return null; }
}
