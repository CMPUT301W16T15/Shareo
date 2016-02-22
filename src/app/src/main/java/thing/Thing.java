package thing;

import java.util.ArrayList;
import java.util.List;

import user.User;

/**
 * Created by A on 2016-02-10.
 */
public abstract class Thing {
    String description;
    Status status;

    User owner;
    User borrower;
    List<User> bidders;
    List<Bid> bids;


    public enum Status {AVAILABLE, BIDDED, BORROWED}

    public Thing(String description) {
        this(description, Status.AVAILABLE);
    }

    public Thing(String description, Status status) {
        this.status = status;
        this.description = description;

        this.owner = null;
        this.borrower = null;
        this.bidders = new ArrayList<>();
        this.bids = new ArrayList<>();
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public void setBorrower(User user) {
        this.borrower = user;
    }

    public User getOwner() { return owner; }
    public User getBorrower() {return borrower; }
    public Status getStatus() { return status; }
    public List<User> getBidders() { return bidders; }
    public List<Bid> getBids() { return bids; }
    public static List<Thing> getAllThings() { return null; }
    public static List<Thing> getAllUnborrowedThings(List <Thing> listToFilter) { return null; }
    public List<Thing> getAllBorrowedThings(List <Thing> listToFilter) { return null; }

    public void addBid(User bidder, int centsPerHour) throws ThingUnavailableException {
        Bid bid = new Bid(bidder, this, centsPerHour);
        bids.add(bid);
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }
}
