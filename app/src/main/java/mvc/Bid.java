package mvc;

import io.searchbox.annotations.JestId;

/**
 * Created by A on 2016-02-10.
 */
public class Bid extends JestData<ShareoData> {

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
     * @param username The user who place a bid.
     * @param thingID The thing being bid on.
     * @param centsPerHour The value of the bid, in cents per hour.
     */
    public Bid(String username, String thingID, int centsPerHour) {
        this.bidderID = username;
        this.thingID = thingID;
        this.centsPerHour = centsPerHour;
    }

    public Thing getThing() {
        if (thing == null) {
            // TODO read thing from getDataSource().
        }
        return thing;
    }
    public User getBidder() {
        if (bidder == null) {
            // TODO read bidder from getDataSource().
        }
        return bidder;
    }
    public int getBidAmount() { return centsPerHour; }
}
