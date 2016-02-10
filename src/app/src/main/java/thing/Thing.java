package thing;

import java.util.List;

import user.User;

/**
 * Created by A on 2016-02-10.
 */
public abstract class Thing {
    public enum Status {AVAILABLE, BIDDED, BORROWED}

    public Thing(Status status) {}

    public Object getStatus() { return null; }
    public List<User> getBidders() { return null; }
    public List<Bid> getBids() { return null; }

    public void addBid(User bidder, int centsPerHour) throws ThingUnavailableException {}
}
