package user;

import java.util.ArrayList;
import java.util.List;

import thing.Bid;
import thing.Thing;

/**
 * Created by A on 2016-02-10.
 */
public class User {
    private final String username;

    private List<Thing> owned;
    private List<Bid> bids;
    private List<Thing> borrowed;

    public User(String username) {
        this.username = username;

        this.owned = new ArrayList<>();
        this.bids = new ArrayList<>();
        this.borrowed = new ArrayList<>();
    }

    public String getName() { return username; }
    public List<Bid> getBids() { return bids; }
    public void addOwnedThing(Thing thing) {
        owned.add(thing);
    }

    public void addBorrowedThing(Thing thing){
        borrowed.add(thing);
    }

    public void removeBorrowedThing(Thing thing){
        borrowed.remove(thing);
    }

    public List<Thing> getBorrowedThings() {
        return borrowed;
    }

    public List<Thing> getOwnedThings() {
        return owned;
    }

    public void setReturned(Thing thing) {}

    public boolean removeOwnedThing(Thing thing) {
        return owned.remove(thing);
    }
}
