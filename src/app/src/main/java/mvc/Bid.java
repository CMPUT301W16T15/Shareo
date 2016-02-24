package mvc;

/**
 * Created by A on 2016-02-10.
 */
public class Bid {
    private final User bidder;
    private final Thing biddee;
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
     * @param user The bidder.
     * @param thing The thing being bid on.
     * @param centsPerHour The value of the bid, in cents per hour.
     */
    public Bid(User user, Thing thing, int centsPerHour) {
        this.bidder = user;
        this.biddee = thing;
        this.centsPerHour = centsPerHour;

        try {
            user.addBid(this);
        } catch (BidNotMadeByUserException e) {
            e.printStackTrace();
        }

        try {
            thing.addBid(this);
        } catch (BidNotMadeForThingException e) {
            e.printStackTrace();
        }
    }

    public Thing getThing() { return biddee; }
    public User getUser() { return bidder; }
    public int getBidAmount() { return centsPerHour; }
}
