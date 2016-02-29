package mvc;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Add the {@link Bid} to the user. This verifies that the bid is not already in the user, and
     * that the bid is made by this user.
     * @param bid
     */
    public void addBid(Bid bid) throws BidNotMadeByUserException {
        // make suer this user made the bid.
        if (!bid.getUser().equals(this)) {
            throw new BidNotMadeByUserException();
        }

        // make sure this bid is not already made by the user.
        if (!bids.contains(bid)) {
            bids.add(bid);
        }
    }

    public List<Bid> getBids() { return bids; }

    public void addOwnedThing(Thing thing) {
        thing.setOwnerSimple(this);
        owned.add(thing);
    }

    protected void addOwnedThingSimple(Thing thing) {
        owned.add(thing);
    }

    public boolean removeOwnedThing(Thing thing) {
        thing.setOwnerSimple(null);
        return owned.remove(thing);
    }

    protected boolean removeOwnedThingSimple(Thing thing) {
        return owned.remove(thing);
    }

    public void addBorrowedThing(Thing thing){
        thing.setBorrowerSimple(this);
        borrowed.add(thing);
    }

    protected void addBorrowedThingSimple(Thing thing) {
        borrowed.add(thing);
    }

    public boolean removeBorrowedThing(Thing thing) {
        thing.setBorrowerSimple(null);
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
}
