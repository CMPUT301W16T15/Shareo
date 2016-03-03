package mvc;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.searchbox.annotations.JestId;

/**
 * Created by A on 2016-02-10.
 */
public class User extends Observable {
    @JestId
    private String ID;

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

    /**
     * Add the {@link Bid} to the user. This verifies that the bid is not already in the user, and
     * that the bid is made by this user.
     * @param bid
     */
    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public List<Bid> getBids() { return bids; }

    public void addOwnedThing(Thing thing) {
        owned.add(thing);
        setChanged();
        notifyObservers();
    }

    protected void addOwnedThingSimple(Thing thing) {
        owned.add(thing);
    }

    public boolean removeOwnedThing(Thing thing) {
        return owned.remove(thing);
    }

    protected boolean removeOwnedThingSimple(Thing thing) {
        return owned.remove(thing);
    }

    public void addBorrowedThing(Thing thing){
        borrowed.add(thing);
    }

    protected void addBorrowedThingSimple(Thing thing) {
        borrowed.add(thing);
    }

    public boolean removeBorrowedThing(Thing thing) {
        return borrowed.remove(thing);
    }

    protected boolean removeBorrowedThingSimple(Thing thing) {
        return borrowed.remove(thing);
    }

    public List<Thing> getBorrowedThings() {
        return borrowed;
    }

    public List<Thing> getOwnedThings() {
        return owned;
    }

    public void setReturned(Thing thing) {}

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }
}
