package mvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A on 2016-02-10.
 */
public abstract class Thing extends JestData<ShareoData> {

    private String name;
    private String description;
    private Status status;
    private PhotoModel photo;

    private String ownerID;
    private transient User owner;

    private List<String> bidIDs;
    private transient List<Bid> bids;
    private String acceptedBidID;
    private transient Bid acceptedBid;


    public enum Status {AVAILABLE, BIDDED, BORROWED}

    public Thing(String gameName, String description, String owner) {
        this(gameName, description, owner, Status.AVAILABLE);
    }

    public Thing(String gameName, String descrption, String owner, Status status) {
        this.status = status;
        this.description = description;
        this.ownerID = owner;
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

    public User getOwner() {
        if (owner == null) {
            // TODO load user from getDataSource().
        }
        return owner;
    }

    public Bid getAcceptedBid() {
        if (acceptedBid == null) {
            // TODO load bid from getDataSource().
        }
        return acceptedBid;
    }
    public Status getStatus() { return status; }
    public List<Bid> getBids() {
        if (bids == null) {
            // TODO load bids from getDataSource().
        }
        return bids;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void addBid(Bid bid) {
        // protect from duplicates
        if (!bids.contains(bid)) {
            bids.add(bid);
            status = Status.BIDDED;
        }
    }

    public void setPhoto(PhotoModel p)
    {
        this.photo = p;
    }
}
