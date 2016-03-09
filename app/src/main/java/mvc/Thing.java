package mvc;

import java.util.ArrayList;
import java.util.List;

import mvc.exceptions.NullIDException;

/**
 * Created by A on 2016-02-10.
 */
public class Thing extends JestData<ShareoData> {

    private String name;
    private String description;
    private Status status;
    private PhotoModel photo;

    private String ownerID;
    private transient User owner;

    private ArrayList<String> bidIDs;
    private transient List<Bid> bids;
    private String acceptedBidID;
    private transient Bid acceptedBid;

    public static class Builder {
        private ShareoData data;
        private String ownerName;
        private String name;
        private String description;
        private PhotoModel p;

        public Builder(ShareoData data, String ownerName, String name, String description) {
            this.data = data;
            this.ownerName = ownerName;
            this.name = name;
            this.description = description;

            this.p = null;
        }

        public Builder setPhoto(PhotoModel p) {
            this.p = p;
            return this;
        }

        public Thing build() throws UserDoesNotExistException {
            User u = data.getUser(ownerName);

            if (u == null) {
                throw new UserDoesNotExistException();
            }

            Thing t = new Thing(name, description);
            t.ownerID = ownerName;
            t.setDataSource(data);
            t.setPhoto(p);

            data.addGame(t);

            try {
                u.addOwnedThing(t);
                data.updateUser(u);
            } catch (NullIDException e) {
                e.printStackTrace();
            }

            return t;
        }
    }


    public enum Status {AVAILABLE, BIDDED, BORROWED}

    protected Thing(String gameName, String description) {
        this(gameName, description, Status.AVAILABLE);
    }

    public Thing(String gameName, String description, Status status) {
        this.status = status;
        this.description = description;
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

    public void setOwner(User owner) throws NullIDException {
        if (this.owner == null) {
            getOwner();
        }

        if (this.owner != null) {
            owner.removeOwnedThing(this);
        }

        if (owner != null) {
            owner.addOwnedThingSimple(this);
            setOwnerSimple(owner);
        }
    }

    public void setOwnerSimple(User owner) {
        this.ownerID = owner.getJestID();
        this.owner = owner;
    }

    public User getOwner() {
        if (owner == null && ownerID != null) {
            owner = getDataSource().getUser(ownerID);
        }
        return owner;
    }

    public Bid getAcceptedBid() {
        if (acceptedBid == null) {
            acceptedBid = getDataSource().getBid(acceptedBidID);
        }
        return acceptedBid;
    }
    public Status getStatus() { return status; }
    public List<Bid> getBids() {
        if (bids == null) {
            bids = new ArrayList<>();
            for (String ID : bidIDs) {
                bids.add(getDataSource().getBid(ID));
            }
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
        if (bids == null) {
            bids = getBids();
        }
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
