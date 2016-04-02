package mvc;


import android.util.Log;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import cmput301w16t15.shareo.ShareoApplication;
import io.searchbox.annotations.JestId;
import mvc.Jobs.CallbackInterface;
import mvc.Jobs.DeleteUserJob;
import mvc.Jobs.UpdateUserJob;
import mvc.exceptions.NullIDException;
import mvc.exceptions.UsernameAlreadyExistsException;

/**
 * Created by A on 2016-02-10.
 */
public class User extends JestData {

    private String username;
    private String fullName;
    private String emailAddress;
    private String motto;
    @JestId
    private String JestID;

    private ArrayList<String> ownedIDs;
    transient private List<Thing> owned;

    private ArrayList<String> bidIDs;
    transient private List<Bid> bids;

    private ArrayList<String> borrowedIDs;
    transient private List<Thing> borrowed;

    public User(String username, String fullName, String emailAddress, String motto) {
        this.username = username;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.motto = motto;

        this.setJestID(username);
        this.ownedIDs = new ArrayList<>();
        this.bidIDs = new ArrayList<>();
        this.borrowedIDs = new ArrayList<>();
    }

    public boolean removeBid(Bid bid) {
        // TODO notify bid that it is removed.
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

        notifyViews();

        return retVal;
    }

    public static class Builder {
        private String username;
        private String fullName;
        private String emailAddress;
        private String motto;
        private ShareoData source;

        /**
         * Set the data for creating a User
         * @param dataSource
         * @param username
         * @param fullName
         * @param emailAddress
         * @param motto
         */
        public Builder(ShareoData dataSource, String username, String fullName, String emailAddress, String motto) {
            this.username = username;
            this.fullName = fullName;
            this.emailAddress = emailAddress;
            this.motto = motto;
            this.source = dataSource;
        }

        /**
         * Actually create the user, beware create the user synchronous and uses a network call
         * @return
         * @throws UsernameAlreadyExistsException
         */
        public User build() throws UsernameAlreadyExistsException{
            User user = new User(username, fullName, emailAddress, motto);
            source.addUser(user);
            return user;
        }

    }

    private void deleteDependants(boolean newThread) {
        // Deleted all owned things
        if (owned == null) {
            User.this.getOwnedThings();
        }

        for (Thing game : owned) {
            try {
                if (newThread) {
                    game.new Deleter().delete();
                } else {
                    game.new Deleter().useMainThread().delete();
                }
            } catch (NullIDException e) {
                e.printStackTrace();
            }
        }

        // Delete all bids
        if (bids == null) {
            User.this.getBids();
        }

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
    }

    private void deleteDependants() {
        deleteDependants(true);
    }

    /**
     * Delete a user and all its owned games
     */
    public class Deleter {
        private boolean newThread = true;

        public void delete() {

            if (newThread) {
                ShareoApplication.getInstance().getJobManager().addJobInBackground(new DeleteUserJob(User.this, new CallbackInterface() {
                    @Override
                    public void onSuccess() {
                        deleteDependants();
                    }

                    @Override
                    public void onFailure() {

                    }
                }));
            } else {
                try {
                    getDataSource().removeUser(User.this);
                    deleteDependants(false);
                } catch (NullIDException e) {
                    e.printStackTrace();
                }
            }
        }

        public Deleter useMainThread() {
            newThread = false;
            return this;
        }
    }

    public String getName() { return username; }
    public String getFullName() { return fullName; }
    public String getEmailAddress() { return emailAddress; }
    public String getMotto() { return motto; }

    public void setName(String username) {
        this.username = username;
        notifyViews();
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
        notifyViews();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        notifyViews();
    }

    public void setMotto(String motto) {
        this.motto = motto;
        notifyViews();
    }


    /**
     * Add the {@link Bid} to the user. This verifies that the bid is not already in the user, and
     * that the bid is made by this user. The bids are transient; the only thing stored
     * consistently is a list of the IDs of the bids. This means that the bids should be have an ID
     * before adding this item to the user.
     * @param bid
     * @throws NullIDException The bid has no ID.
     */
    public void addBid(Bid bid) throws NullIDException {
        if (!bidIDs.contains(bid.getJestID())) {
            bidIDs.add(bid.getJestID());
            if (bids == null) {
                bids = getBids();
            }
            bids.add(bid);
        }
        notifyViews();
    }

    /**
     * Gets a bid and preloads the things the bids contain
     * @return a list of bids with preloading things
     */
    public List<Bid> getBids() {
        if (bids == null) {
            bids = new ArrayList<>(bidIDs.size());
            for (String ID : bidIDs) {
                Bid b = getDataSource().getBid(ID);
                b.getThing(); // must preload the thing, kind of sketch code, what at this point we'll go with it
                bids.add(b);
            }
        }
        return bids;
    }

    /**
     * Adds an owned thing to the user, sets the owner of the thing, and then pushes the data to the server
     * @param thing
     * @throws NullIDException
     */
    public void addOwnedThing(Thing thing) throws NullIDException {
        thing.setOwnerSimple(this);
        addOwnedThingSimple(thing);
    }

    protected void addOwnedThingSimple(Thing thing) throws NullIDException {
        ownedIDs.add(thing.getJestID());
        if (owned == null) {
            owned = getOwnedThings();
        }
        owned.add(thing);
        notifyViews();
    }

    public void addIDLessThing(Thing thing) {
        if (owned == null) {
            owned = getOwnedThings();
        }
        owned.add(thing);
        notifyViews();
    }

    public void addThingID(String ID) {
        ownedIDs.add(ID);
    }

    public boolean removeOwnedThing(Thing thing) {
        if (removeOwnedThingSimple(thing)) {
            thing.setOwnerSimple(null);
            return true;
        }
        return false;
    }

    protected boolean removeOwnedThingSimple(Thing thing) {
        boolean retVal = false;

        try {
            retVal = ownedIDs.remove(thing.getJestID());
            if (owned != null) {
                owned.remove(thing);
            }
        } catch (NullIDException e) {
            e.printStackTrace();
        }

        notifyViews();
        return retVal;
    }

    public void addBorrowedThing(Thing thing){
        // TODO make thing know that it is borrowed.
        addBorrowedThingSimple(thing);
    }

    protected void addBorrowedThingSimple(Thing thing) {
        try {
            borrowedIDs.add(thing.getJestID());
            if (borrowed != null) {
                borrowed.add(thing);
            }
            notifyViews();
        } catch (NullIDException e) {
            e.printStackTrace();
        }
    }

    public boolean removeBorrowedThing(Thing thing) {
        // TODO make thing know that it is no longer borrowed.
        return removeBorrowedThingSimple(thing);
    }

    protected boolean removeBorrowedThingSimple(Thing thing) {
        boolean retVal = false;

        try {
            retVal = borrowedIDs.remove(thing.getJestID());
            if (borrowed != null) {
                borrowed.remove(thing);
            }
        } catch (NullIDException e) {
            e.printStackTrace();
        }

        notifyViews();

        return retVal;
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

    public List<Thing> getOffers() {
        getOwnedThings();
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

    /**
     * Get all avaiable game (games that arnt being lent out)
     */
    public List<Thing> getAvailableThings()
    {
        getOwnedThings();
        // only return those that have bids on them
        List<Thing> available = new ArrayList<>();
        for (Thing t : owned) {
            if (t.getStatus() == Thing.Status.BIDDED || t.getStatus() == Thing.Status.AVAILABLE) {
                available.add(t);
            }
        }
        return available;
    }

    public List<Thing> getLentThings() {
        getOwnedThings();
        // only return those that have bids on them
        List<Thing> available = new ArrayList<>();
        for (Thing t : owned) {
            if (t.getStatus() == Thing.Status.BORROWED) {
                available.add(t);
            }
        }
        return available;
    }

    /**
     *
     */
    public void update() {
        notifyViews();
        ShareoApplication.getInstance().getJobManager().addJobInBackground(new UpdateUserJob(this));
    }

    public void setReturned(Thing thing) {
        // TODO this may be unneccesary with removeBorrowedThing.
    }



    /**
     * Returns the JestID associated with this object. Never returns null, but will instead throw
     * an exception.
     * @return The JestID.
     * @throws NullIDException No JestID has been set, or it has been set to null.
     */
    @Override
    public String getJestID() {
        if (JestID == null) {
            Log.e("Impossible?", "This should be impossible");
            return null;
        }
        return JestID;
    }

    public void setJestID(String ID) {
        if (ID != null) {
            JestID = ID;
        }
    }
}
