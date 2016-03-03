package mvc;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.annotations.JestId;

/**
 * Created by A on 2016-02-10.
 */
public abstract class Thing {
    @JestId
    private String ID;

    private String description;
    private Status status;

    private String owner;
    private String name;
    private Bid acceptedBid;
    private List<Bid> bids;

    public String getID() {
        return ID;
    }


    public enum Status {AVAILABLE, BIDDED, BORROWED}

    public Thing(String gameName, String description, String owner) {
        this(gameName, description, owner, Status.AVAILABLE);
    }

    public Thing(String gameName, String descrption, String owner, Status status) {
        this.status = status;
        this.description = description;
        this.owner = owner;
        this.name = gameName;
        this.bids = new ArrayList<>();
    }

    /**
     * Used to lend a thing. Pass in the username of the person who is borrowing the game.
     * Clear any bids the game had on it.
     * @param acceptedBid Winning bid that was accepted
     */
    public void borrow(Bid acceptedBid)
    {
        this.acceptedBid = acceptedBid;
        this.bids = null;
        this.status = Status.BORROWED;
    }

    /**
     * Used to return a thing.
     */
    public void returnThing()
    {
        this.acceptedBid = null;
        this.status = Status.AVAILABLE;
    }

    public String getOwner() { return owner; }
    public Bid getAcceptedBid() { return acceptedBid; }
    public Status getStatus() { return status; }
    public List<Bid> getBids() { return bids; }
    public static List<Thing> getAllThings() { return null; }
    public static List<Thing> getAllUnborrowedThings(List <Thing> listToFilter) { return null; }
    public List<Thing> getAllBorrowedThings(List <Thing> listToFilter) { return null; }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void addBid(User bidder, int centsPerHour) throws ThingUnavailableException {
        Bid bid = new Bid(bidder.getName(), this.getID(), centsPerHour);
    }

    public void addBid(Bid bid) {
        // protect from duplicates
        if (!bids.contains(bid)) {
            if (bids.size() == 0)
            {
                status = Status.BIDDED;
            }
            bids.add(bid);
        }
    }
}
