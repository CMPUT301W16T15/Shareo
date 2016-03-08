package mvc;


import java.util.ArrayList;
import java.util.List;

import mvc.exceptions.NullIDException;

/**
 * Created by A on 2016-02-10.
 */
public class User extends JestData<ShareoData> {

    private final String username;

    private ArrayList<String> ownedIDs;
    transient private List<Thing> owned;

    private ArrayList<String> bidIDs;
    transient private List<Bid> bids;

    private ArrayList<String> borrowedIDs;
    transient private List<Thing> borrowed;

    public User(String username) {
        this.username = username;
        this.setJestID(username);
        this.ownedIDs = new ArrayList<>();
        this.bidIDs = new ArrayList<>();
        this.borrowedIDs = new ArrayList<>();
    }

    @Override
    public String getJestID() {
        try {
            return super.getJestID();
        } catch (NullIDException e) {
            // This should be impossible, since the Jest ID is the username.
            e.printStackTrace();
        }
        return null;
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
        if (bids == null) {
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
        if (owned == null) {
            owned = getOwnedThings();
        }
        owned.add(thing);
    }

    public boolean removeOwnedThing(Thing thing) {
        // TODO remove owner from thing
        return removeOwnedThingSimple(thing);
    }

    protected boolean removeOwnedThingSimple(Thing thing) {
        if (owned == null) {
            owned = getOwnedThings();
        }
        return owned.remove(thing);
    }

    public void addBorrowedThing(Thing thing){
        // TODO make thing know that it is borrowed.
        addBorrowedThingSimple(thing);
    }

    protected void addBorrowedThingSimple(Thing thing) {
        if (borrowed == null) {
            borrowed = getBorrowedThings();
        }
        borrowed.add(thing);
    }

    public boolean removeBorrowedThing(Thing thing) {
        // TODO make thing know that it is no longer borrowed.
        return removeBorrowedThingSimple(thing);
    }

    protected boolean removeBorrowedThingSimple(Thing thing) {
        if (borrowed == null) {
            borrowed = getBorrowedThings();
        }
        return borrowed.remove(thing);
    }

    public List<Thing> getBorrowedThings() {
        if (borrowed == null) {
            borrowed = new ArrayList<>(borrowedIDs.size());
            for (String ID : borrowedIDs) {
                borrowed.add(getDataSource().getGame(ID));
            }
        }
        return borrowed;
    }

    public List<Thing> getOwnedBiddedThings() {
        if (owned == null) {
            owned = new ArrayList<>(ownedIDs.size());
            for (String ID : ownedIDs) {
                owned.add(getDataSource().getGame(ID));
            }
        }
        // only return those that have bids on them
        List<Thing> ownedWithBids = new ArrayList<>();
        for (Thing t : owned) {
            if (t.getStatus() == Thing.Status.BIDDED) {
                ownedWithBids.add(t);
            }
        }
        return ownedWithBids;
    }

    /*
    * want a way to get owned things with bids on them
    * */

    public List<Thing> getOwnedThings() {
        if (owned == null) {
            owned = new ArrayList<>(ownedIDs.size());
            for (String ID : ownedIDs) {
                owned.add(getDataSource().getGame(ID));
            }
        }
        return owned;
    }

    public void setReturned(Thing thing) {
        // TODO this may be unneccesary with removeBorrowedThing.
    }
}
