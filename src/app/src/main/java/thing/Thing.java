package thing;

import java.util.List;

/**
 * Created by A on 2016-02-10.
 */
public abstract class Thing {
    public Thing(Status status) {}

    public Object getStatus() { return null; }

    public enum Status {AVAILABLE, BIDDED, BORROWED}

    public void addBid() throws ThingNotAvailableException {}
    public List<Bid> getBids() { return null; }
}
