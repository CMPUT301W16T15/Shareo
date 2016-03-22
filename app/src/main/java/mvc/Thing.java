package mvc;

import com.path.android.jobqueue.AsyncAddCallback;

import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cmput301w16t15.shareo.ShareoApplication;
import io.searchbox.annotations.JestId;
import mvc.Jobs.CallbackInterface;
import mvc.Jobs.CreateGameJob;
import mvc.Jobs.DeleteGameJob;
import mvc.Jobs.UpdateGameJob;
import mvc.exceptions.NullIDException;

/**
 * Created by A on 2016-02-10.
 */
public class Thing extends JestData {

    private int topBidAmount = 0;
    private String name;
    private String description;
    private Status status;
    private String category;
    private String numberPlayers;
    private PhotoModel photo;
    @JestId
    private String JestID;

    private String ownerID;
    private transient User owner;

    private ArrayList<String> bidIDs;
    private transient List<Bid> bids;
    private String acceptedBidID;
    private transient Bid acceptedBid;

    public boolean removeBid(Bid bid) {
        //TODO notify bid that it is removed.
        return removeBidSimple(bid);
    }

    protected boolean removeBidSimple(Bid bid) {
        boolean retVal = false;

        try {
            retVal = bidIDs.remove(bid.getJestID());
            if (bids != null) {
                bids.remove(bid);
            }
        } catch (NullIDException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static class Builder {
        private ShareoData data;
        private User owner;
        private String name;
        private String description;
        private String category;
        private String numberPlayers;
        private PhotoModel p;
        private Boolean newThread;

        public Builder(ShareoData data, User owner, String name, String description, String category, String numberPlayers) {
            this.data = data;
            this.owner = owner;
            this.name = name;
            this.category = category;
            this.numberPlayers = numberPlayers;
            this.description = description;
            newThread = true;
            this.p = null;
        }

        public Builder setPhoto(PhotoModel p) {
            this.p = p;
            return this;
        }

        public Builder useMainThread() {
            newThread = false;
            return this;
        }

        public Thing build() throws UserDoesNotExistException {

            final Thing t = new Thing(name, description, category, numberPlayers);
            t.ownerID = owner.getJestID();
            t.setDataSource(data);
            t.setPhoto(p);
            if (newThread) {
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
            } else {
                data.addGame(t);
                try {
                    owner.addOwnedThingSimple(t);
                    owner.update();
                } catch (NullIDException e) {
                    e.printStackTrace();
                }
            }
            return t;
        }

    }

    private void deleteDependants() {
        deleteDependants(true);
    }

    private void deleteDependants(boolean newThread) {
        if (bids == null) {
            Thing.this.getBids();
        }

        // Delete all bids
        for (Bid bid : bids) {
            try {
                bid.new Deleter().useMainThread().delete();
            } catch (NullIDException e) {
                e.printStackTrace();
            }
        }

        if (acceptedBid == null && acceptedBidID != null) {
            Thing.this.getAcceptedBid();
        }

        if (acceptedBid != null) {
            try {
                acceptedBid.new Deleter().useMainThread().delete();
            } catch (NullIDException e) {
                e.printStackTrace();
            }
        }
    }

    public class Deleter {
        private boolean newThread = true;

        public void delete() throws NullIDException {

            if (newThread) {
                ShareoApplication.getInstance().getJobManager().addJobInBackground(new DeleteGameJob(Thing.this, new CallbackInterface() {
                    @Override
                    public void onSuccess() {
                        if (owner == null && ownerID != null) {
                            Thing.this.getOwner();
                        }

                        if (owner != null) {
                            owner.removeOwnedThingSimple(Thing.this);
                        }

                        deleteDependants();
                    }

                    @Override
                    public void onFailure() {

                    }
                }));
            } else {
                getDataSource().removeGame(Thing.this);

                if (owner == null && ownerID != null) {
                    Thing.this.getOwner();
                }

                if (owner != null) {
                    owner.removeOwnedThingSimple(Thing.this);
                }

                deleteDependants(false);
            }
        }

        public Deleter useMainThread() {
            newThread = false;
            return this;
        }
    }



    public enum Status {AVAILABLE, BIDDED, BORROWED}

    protected Thing(String gameName, String description, String category, String numberPlayers) {
        this(gameName, description, category, numberPlayers, Status.AVAILABLE);
    }

    public Thing(String gameName, String description, String category, String numberPlayers, Status status) {
        this.status = status;
        this.description = description;
        this.category = category;
        this.numberPlayers = numberPlayers;
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
        ShareoApplication.getInstance().getJobManager().addJobInBackground(new UpdateGameJob(this));
        notifyViews();
    }

    /**
     * Used to return a thing.
     */
    public void returnThing()
    {
        this.acceptedBid = null;
        this.status = Status.AVAILABLE;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNumberPlayers(String numberPlayers) {
        this.numberPlayers = numberPlayers;
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
    public String getCategory() {
        return category;
    }
    public String getNumberPlayers() {
        return numberPlayers;
    }
    public String getName() {
        return name;
    }
    public String getOwnerID() {
        return this.ownerID;
    }
    public PhotoModel getPhoto() {
        return this.photo;
    }

    public void addBid(Bid bid) {
        try {
            if (!bidIDs.contains(bid.getJestID())) {
                bidIDs.add(bid.getJestID());
                if (bids == null) {
                    bids = getBids();
                }
                bids.add(bid);
                if (bid.getBidAmount() > topBidAmount)
                {
                    topBidAmount = bid.getBidAmount();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPhoto(PhotoModel p)
    {
        this.photo = p;
    }

    /**
     * Returns the JestID associated with this object. Never returns null, but will instead throw
     * an exception.
     * @return The JestID.
     * @throws NullIDException No JestID has been set, or it has been set to null.
     */
    @Override
    public String getJestID() throws NullIDException {
        if (JestID == null) {
            throw new NullIDException();
        }
        return JestID;
    }

    @Override
    public void setJestID(String ID) {
        JestID = ID;
    }

    public int getTopBidAmount() {
        return topBidAmount;
    }

    public void setTopBidAmount(int topBidAmount) {
        this.topBidAmount = topBidAmount;
    }
}
