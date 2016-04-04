package mvc;

import android.util.Log;

import com.path.android.jobqueue.AsyncAddCallback;

import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
    private Boolean checked = false;

    public enum Status {AVAILABLE, BIDDED, BORROWED}

    protected Thing(String gameName, String description, String category, String numberPlayers) {
        this(gameName, description, category, numberPlayers,Status.AVAILABLE);
    }

    public Thing(String gameName, String description, String category, String numberPlayers,Status status) {
        this.status = status;
        this.description = description;
        this.category = category;
        this.numberPlayers = numberPlayers;
        this.name = gameName;
        this.bidIDs = new ArrayList<>();
    }

    /**
     * Remove a bid that the is on the thing. This does not remove the bid from the server. Also removes
     * the thing from the bid (TODO).
     * @param bid bid to remove.
     * @return true, if the bid is removed.
     */
    public boolean removeBid(Bid bid) {
        //TODO notify bid that it is removed.
        return removeBidSimple(bid);
    }

    /**
     * Remove a bid that is on the Thing. This does not remove the bid from the server. Does not
     * tell the bid to remove the thing.
     * @param bid bid to remove.
     * @return true, if the bid is removed.
     */
    protected boolean removeBidSimple(Bid bid) {
        boolean retVal = false;
        try {
            retVal = bidIDs.remove(bid.getJestID());
            int tempBidAmount = bid.getBidAmount();
            getBids().remove(bid);
            if (tempBidAmount == topBidAmount)
            {
                recalculateMaxBidAmount();
            }
        } catch (NullIDException e) {
            e.printStackTrace();
        }

        if (getBids().isEmpty()) {
            status = Status.AVAILABLE;
        }

        return retVal;
    }

    /**
     * Recalculates the maximum bid. Should be called after adding or removing bids.
     */
    private void recalculateMaxBidAmount()
    {
        topBidAmount = 0;
        for (Bid b : getBids()) {
            if (b.getBidAmount() > topBidAmount)
            {
                topBidAmount = b.getBidAmount();
            }
        }
    }

    /**
     * Used to create a thing, by default synchronously. To stop the creation from being
     * synchronous, see {@link Builder#useMainThread()}. The purpose for this is to allow creation
     * of things to be called from the UI thread.
     */
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
                ShareoApplication.getInstance().getJobManager().addJobInBackground(new CreateGameJob(t,owner));
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

    /**
     * Delete all the objects that depend on a {@link Thing}, specifically, any bids that exist on
     * the object. This is called when deleting the {@link Thing}. Deletes synchronously.
     */
    private void deleteDependants() {
        deleteDependants(true);
    }

    /**
     * Delete all the objects that depend on a {@link Thing}, specifically, any bids that exist on
     * the object. This is called when deleting the {@link Thing}.
     * @param newThread true to delete synchronously, false to use current thread.
     */
    private void deleteDependants(boolean newThread) {
        if (bids == null) {
            Thing.this.getBids();
        }

        // Delete all bids
        for (Bid bid : bids) {
            try {
                if (newThread) {
                    bid.new Deleter().delete();
                } else {
                    bid.new Deleter().useMainThread().delete();
                }
            } catch (NullIDException e) {
                e.printStackTrace();
            }
        }

        if (acceptedBid == null && acceptedBidID != null) {
            Thing.this.getAcceptedBid();
        }

        if (acceptedBid != null) {
            try {
                if (newThread) {
                    acceptedBid.new Deleter().delete();
                } else {
                    acceptedBid.new Deleter().useMainThread().delete();
                }
            } catch (NullIDException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Used to delete a thing, by default synchronously. To stop the deletion from being
     * synchronous, see {@link Deleter#useMainThread()}. The purpose for this is to allow deletion
     * of things to be called from the UI thread.
     */
    public class Deleter {
        private boolean newThread = true;

        public void delete() throws NullIDException {

            if (newThread) {
                ShareoApplication.getInstance().getJobManager().addJobInBackground(new DeleteGameJob(Thing.this, new CallbackInterface() {
                    @Override
                    public void onSuccess() {
                        User gameOwner = getOwner();
                        if (gameOwner != null) {
                            gameOwner.removeOwnedThingSimple(Thing.this);
                            gameOwner.update();
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



    /**
     * Used to lend a thing. Pass in the username of the person who is borrowing the game.
     * Clear any bids the game had on it.
     * @param acceptedBid Winning bid that was accepted
     */
    public void borrow(Bid acceptedBid)
    {
        try {
            getDataSource().updateBid(acceptedBid);
        } catch (NullIDException e) {
            e.printStackTrace();
        }
        Log.d("Thing", "Attempting to borrow game");
        if (getBids().remove(acceptedBid)) {
            try {
                bidIDs.remove(acceptedBid.getJestID());
                this.acceptedBid = acceptedBid;
                this.acceptedBidID = acceptedBid.getJestID();
                Iterator<Bid> iter = getBids().iterator();
                while (iter.hasNext()) {
                    try {
                        Bid b = iter.next();
                        User u = b.getBidderFresh();
                        iter.remove();
                        ShareoData.getInstance().removeBid(b);
                        u.removeBidSimple(b);
                        bidIDs.remove(b.getJestID());
                        getDataSource().updateUser(u);
                        getDataSource().updateGame(b.getThing());
                    } catch (NullIDException e) {
                        e.printStackTrace();
                    }
                }
                User bidder = acceptedBid.getBidderFresh();
                this.status = Status.BORROWED;
                bidder.addBorrowedThingSimple(this);
                bidder.update();
                Log.d("Thing", "Borrowed");
            } catch (NullIDException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("Thing", "Failed to borrow thing, bid not found");
        }
        this.update();
    }


    /**
     * Used to push updates on a things data to the server
     */
    public void update() {
        ShareoApplication.getInstance().getJobManager().addJobInBackground(new UpdateGameJob(this));
        notifyViews();
    }

    /**
     * Used to return a thing, deletes the bid, removes it from the bidder and sets it to available.
     */
    public void returnThing()
    {
        topBidAmount = 0;
        User bidder = this.acceptedBid.getBidder();
        bidder.removeBorrowedThingSimple(this);
        bidder.update();
        try {
            this.acceptedBid.new Deleter().delete();
        } catch (NullIDException e) {
            e.printStackTrace();
        }
        this.acceptedBid = null;
        this.status = Status.AVAILABLE;
    }

    /**
     * Set the name of the thing.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the description of the thing.
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the status of the thing.
     * @param status one of available statuses.
     * @see mvc.Thing.Status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Set the category of the game.
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Set the number of players allowed to play the game.
     * @param numberPlayers
     */
    public void setNumberPlayers(String numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    /**
     * Set the owner of the game, and notify the user that it now owns the game. In order to update
     * the server, one should call {@link Thing#update()} upon completion of changes. The thing
     * should be added to the server before setting the owner of the game, or else this will throw
     * a {@link NullIDException}.
     * @param owner new owner.
     * @throws NullIDException this Thing does not have an ID yet.
     */
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

    /**
     * Set the owner of the game, but do not notify the user that it now owns the game. In order to update
     * the server, one should call {@link Thing#update()} upon completion of changes. The thing
     * should be added to the server before setting the owner of the game, or else this will throw
     * a {@link NullIDException}.
     * @param owner new owner.
     * @throws NullIDException this Thing does not have an ID yet.
     */
    public void setOwnerSimple(User owner) {
        this.ownerID = owner.getJestID();
        this.owner = owner;
    }

    /**
     * Returns the owner of this game, accessing the server if it has not been accessed before.
     * @return the owner of the game.
     */
    public User getOwner() {
        if (owner == null && ownerID != null) {
            User loggedIn = AppUserSingleton.getInstance().getUser();
            if (loggedIn != null && loggedIn.getName().equals(ownerID)) {
                owner = loggedIn;
            }
            if (owner == null) {
                owner = getDataSource().getUser(ownerID);
            }
        }
        return owner;
    }

    /**
     * If this thing is borrowed, return the bid associated with the borrowing.
     * @return the bid associated with the borrowing of the thing.
     */
    public Bid getAcceptedBid() {
        if (acceptedBid == null && acceptedBidID != null) {
            acceptedBid = getDataSource().getBid(acceptedBidID);
        }
        return acceptedBid;
    }

    /**
     * Returns the status of the thing.
     * @see mvc.Thing.Status
     */
    public Status getStatus() { return status; }

    /**
     * Returns all the bids on the object, if the bid is not borrowed.
     * @return list of bids.
     */
    public List<Bid> getBids() {
        if (bids == null) {
            bids = new ArrayList<>();
            for (String ID : bidIDs) {
                bids.add(getDataSource().getBid(ID));
            }
        }
        return bids;
    }

    /**
     * Returns the description of the thing.
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the category of the thing.
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the number of players of the thing.
     * @return number of players
     */
    public String getNumberPlayers() {
        return numberPlayers;
    }

    /**
     * Returns the name of the thing.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the JestID of the owner.
     * @return JestID of the owner.
     * @see JestData
     * @see User
     */
    public String getOwnerID() {
        return this.ownerID;
    }

    /**
     * Returns the picture for the game.
     * @return picture.
     */
    public PhotoModel getPhotoModel() {
        return this.photo;
    }

    /**
     * Add a bid to the game, and update the top bid amount. {@link Thing#update()} should be called
     * to make the changes on the server.
     * @param bid bid to add.
     */
    public void addBid(Bid bid) {
        try {
            if (!bidIDs.contains(bid.getJestID())) {
                bidIDs.add(bid.getJestID());
                checked = false;
                if (bids == null) {
                    bids = getBids();
                }
                bids.add(bid);
                this.status = Status.BIDDED;
                if (bid.getBidAmount() > topBidAmount)
                {
                    topBidAmount = bid.getBidAmount();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the picture of the game.
     * @param p picture of the game.
     */
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

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public void setJestID(String ID) {
        JestID = ID;
    }

    /**
     * Returns the value, in cents, of the top bid on the game.
     * @return cents of the top bid.
     */
    public int getTopBidAmount() {
        return topBidAmount;
    }
}
