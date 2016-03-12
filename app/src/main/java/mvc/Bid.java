package mvc;

import cmput301w16t15.shareo.ShareoApplication;
import mvc.Jobs.CallbackInterface;
import mvc.Jobs.CreateBidJob;
import mvc.exceptions.NullIDException;

/**
 * Created by A on 2016-02-10.
 */
public class Bid extends JestData {

    private final String bidderID;
    transient private User bidder;

    private final String thingID;
    transient private Thing thing;

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

        public Builder(ShareoData data, User bidder, Thing thing, int centsPerHour) {
            this.user = bidder;
            this.thing = thing;
            this.centsPerHour = centsPerHour;
            this.data = data;
        }

        public Bid build() throws NullIDException {

            final Bid bid = new Bid(user, thing, centsPerHour);
            bid.setDataSource(data);
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
            return bid;
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
}
