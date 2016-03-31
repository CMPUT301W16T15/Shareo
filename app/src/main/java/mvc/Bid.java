package mvc;

import android.util.Log;

import cmput301w16t15.shareo.ShareoApplication;
import io.searchbox.annotations.JestId;
import mvc.Jobs.CallbackInterface;
import mvc.Jobs.CreateBidJob;
import mvc.Jobs.DeleteBidJob;
import mvc.exceptions.NullIDException;

/**
 * Created by A on 2016-02-10.
 */
public class Bid extends JestData {

    private final String bidderID;
    transient private User bidder;
    @JestId
    private String JestID;

    private final String thingID;
    transient private Thing thing;
    private static String TAG ="MVCBID";
    private final int centsPerHour;

    /**
     * <p>
     * Create a new {@link Bid} object.
     * </p>
     * <p>
     * Each bid has a {@link User}, placing the bid, and a {@link Thing}, upon which the bid is
     * placed. When the bid is created, both objects are notified, and are changed accordingly.<br>
     * This means that the {@link User} will properly return this bid with {@link User#getBids()}.
     * </p>
     * @param user The user who place a bid.
     * @param thing The thing being bid on.
     * @param centsPerHour The value of the bid, in cents per hour.
     */
    protected Bid(User user, Thing thing, int centsPerHour) throws NullIDException {
        this.bidder = user;
        this.bidderID = user.getJestID();
        this.thing = thing;
        this.thingID = thing.getJestID();
        this.centsPerHour = centsPerHour;
    }

    public static class Builder {
        private ShareoData data;
        private User user;
        private Thing thing;
        private int centsPerHour;
        private Boolean newThread = true;

        public Builder(ShareoData data, User bidder, Thing thing, int centsPerHour) {
            this.user = bidder;
            this.thing = thing;
            this.centsPerHour = centsPerHour;
            this.data = data;
        }

        public Builder useMainThread()
        {
            newThread = false;
            return this;
        }

        public Bid build() throws NullIDException {

            final Bid bid = new Bid(user, thing, centsPerHour);
            bid.setDataSource(data);
            if (newThread) {
                ShareoApplication.getInstance().getJobManager().addJobInBackground(new CreateBidJob(bid, new CallbackInterface() {
                    @Override
                    public void onSuccess() {
                        try {
                            user.addBid(bid);
                            thing.addBid(bid);

                            user.update();
                            thing.update();
                        } catch (NullIDException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                }));
            } else {
                ShareoData.getInstance().addBid(bid);
                user.addBid(bid);
                thing.addBid(bid);
                data.updateUser(user);
                data.updateGame(thing);
            }
            return bid;
        }
    }

    public class Deleter {
        public boolean newThread = true;

        public void delete() throws NullIDException {

            if (newThread) {
                ShareoApplication.getInstance().getJobManager().addJobInBackground(new DeleteBidJob(Bid.this, new CallbackInterface() {
                    @Override
                    public void onSuccess() {

                            getBidder().removeBidSimple(Bid.this);
                            Log.d(TAG, "Removing Bid from bidder.removeBidSimple");
                            getBidder().update();

                            getThing().removeBidSimple(Bid.this);
                            Log.d(TAG, "Removing Bid from thing.removeBidSimple");
                            getThing().update();

                    }

                    @Override
                    public void onFailure() {

                    }
                }));
            } else {
                ShareoData.getInstance().removeBid(Bid.this);

                getBidder().removeBidSimple(Bid.this);
                getBidder().update();

                getThing().removeBidSimple(Bid.this);
                getThing().update();
            }
        }

        public Deleter useMainThread() {
            newThread = false;
            return this;
        }
    }

    public Thing getThing() {
        if (thing == null) {
            thing = getDataSource().getGame(thingID);
        }
        return thing;
    }
    public User getBidder() {
        if (bidder == null) {
            bidder = getDataSource().getUser(bidderID);
        }
        return bidder;
    }
    public int getBidAmount() { return centsPerHour; }

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
}
