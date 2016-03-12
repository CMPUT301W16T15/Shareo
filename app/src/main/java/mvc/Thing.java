package mvc;

import com.path.android.jobqueue.AsyncAddCallback;

import java.util.ArrayList;
import java.util.List;

import cmput301w16t15.shareo.ShareoApplication;
import mvc.Jobs.CallbackInterface;
import mvc.Jobs.CreateGameJob;
import mvc.Jobs.UpdateGameJob;
import mvc.exceptions.NullIDException;

/**
 * Created by A on 2016-02-10.
 */
public class Thing extends JestData {

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
        private User owner;
        private String name;
        private String description;
        private PhotoModel p;

        public Builder(ShareoData data, User owner, String name, String description) {
            this.data = data;
            this.owner = owner;
            this.name = name;
            this.description = description;

            this.p = null;
        }

        public Builder setPhoto(PhotoModel p) {
            this.p = p;
            return this;
        }

        public Thing build() throws UserDoesNotExistException {

            final Thing t = new Thing(name, description);
            t.ownerID = owner.getJestID();
            t.setDataSource(data);
            t.setPhoto(p);
            ShareoApplication.getInstance().getJobManager().addJobInBackground(new CreateGameJob(t, new CallbackInterface() {
                @Override
                public void onSuccess() {
                    try {
                        owner.addOwnedThing(t);
                        owner.update();
                    } catch (NullIDException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {

                }
            }));
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
        this.bidIDs = new ArrayList<>();
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
     * Used to push updates on a things data to the server
     */
    public void update() {
        notifyViews();
        ShareoApplication.getInstance().getJobManager().addJobInBackground(new UpdateGameJob(this));
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
