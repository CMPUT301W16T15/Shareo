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

    private User owner;
    private User borrower;
    private List<User> bidders;
    private List<Bid> bids;

    public String getID() {
        return ID;
    }


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

    /**
     * Set the owner of the Thing, and modify the {@link User} that previously owned the thing.
     * The new user will be modified to own this Thing, and this Thing will now know its new owner.
     * @param user new owner.
     */
    public void setOwner(User user) {
        // remove from old owner, if not null
        if (this.owner != null) {
            owner.removeOwnedThingSimple(this);
        }

        // add to new owner, if not null
        this.owner = user;
        if (this.owner != null) {
            owner.addOwnedThingSimple(this);
        }
    }

    /**
     * Simply set the owner of the Thing. Nothing is done to change the state of the {@link User}
     * that now claims to use this object. This is primarily intended for use with
     * {@link User#addOwnedThing(Thing)}
     * @param user new owner.
     */
    protected void setOwnerSimple(User user) {
        this.owner = user;
    }

    /**
     * Set the borrower of the Thing, and modify the {@link User} that previously was borrowing the
     * thing. The new borrower will be modified to be borrowing this Thing, and this Thing will now
     * know its new borrower.
     * @param user new borrower.
     */
    public void setBorrower(User user) {
        // remove old borrower, if not null
        if (borrower != null) {
            borrower.removeBorrowedThingSimple(this);
        }

        // add new borrower, if not null
        borrower = user;
        if (borrower != null) {
            borrower.addBorrowedThingSimple(this);
        }
    }

    /**
     * Simply set the owner of the Thing. Nothing is done to change the state of the {@link User}
     * that now claims to use this object. This is primarily intended for use with
     * {@link User#addBorrowedThing(Thing)}
     * @param user new owner.
     */
    protected void setBorrowerSimple(User user) {
        this.borrower = user;
    }

    public User getOwner() { return owner; }
    public User getBorrower() { return borrower; }
    public Status getStatus() { return status; }
    public List<User> getBidders() { return bidders; }
    public List<Bid> getBids() { return bids; }
    public static List<Thing> getAllThings() { return null; }
    public static List<Thing> getAllUnborrowedThings(List <Thing> listToFilter) { return null; }
    public List<Thing> getAllBorrowedThings(List <Thing> listToFilter) { return null; }

    public void addBid(User bidder, int centsPerHour) throws ThingUnavailableException {
        Bid bid = new Bid(bidder, this, centsPerHour);
    }

    public void addBid(Bid bid) throws BidNotMadeForThingException {
        // make sure that the bid is for this thing
        if (!bid.getThing().equals(this)) {
            throw new BidNotMadeForThingException();
        }

        // protect from duplicates
        if (!bids.contains(bid)) {
            bids.add(bid);
        }
    }
}
