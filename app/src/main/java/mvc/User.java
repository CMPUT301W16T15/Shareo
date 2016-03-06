package mvc;


import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.searchbox.annotations.JestId;
import mvc.exceptions.NullIDException;
import mvc.exceptions.ThingUnavailableException;

/**
 * Created by A on 2016-02-10.
 */
public class User extends JestData<ShareoData> {

    private final String username;

    private List<String> ownedIDs;
    transient private List<Thing> owned;

    private List<String> bidIDs;
    transient private List<Bid> bids;

    private List<String> borrowedIDs;
    transient private List<Thing> borrowed;

    public User(String username) {
        this.username = username;
        this.owned = new ArrayList<>();
        this.bids = new ArrayList<>();
        this.borrowed = new ArrayList<>();
    }

    public String getName() { return username; }

    /**
     * Add the {@link Bid} to the user. This verifies that the bid is not already in the user, and
     * that the bid is made by this user. The bids are transient; the only thing stored
     * consistently is a list of the IDs of the bids. This means that the bids should be have an ID
     * before adding this item to the user.
     * @param bid
     * @throws NullIDException The bid has no ID.
     */
    public void addBid(Bid bid) throws NullIDException {
        bidIDs.add(bid.getJestID());
        bids.add(bid);
    }

    public List<Bid> getBids() {
        if (bids.size() != bidIDs.size()) {
            // TODO load bids from getDataSource().
        }
        return bids;
    }

    public void addOwnedThing(Thing thing) throws NullIDException {
        // TODO change thing owner to this user.
        addOwnedThingSimple(thing);
    }

    protected void addOwnedThingSimple(Thing thing) throws NullIDException {
        ownedIDs.add(thing.getJestID());
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
}
