package thing;

import java.util.List;

import user.User;

/**
 * Created by A on 2016-02-10.
 */
public abstract class Thing {
    static List<Thing> allThings;

    public enum Status {AVAILABLE, BIDDED, BORROWED}
    public Thing(Status status, String description) {}
    public void setStatus(Status status) { }
    public void setOwner(User user) {}
    public void setBorrower(User user) {}
    public User getOwner() {return null; }
    public User getBorrower() {return null; }
    public Status getStatus() { return null; }
    public List<User> getBidders() { return null; }
    public List<Bid> getBids() { return null; }
    public static List<Thing> getAllThings() { return null; }
    public static List<Thing> getAllUnborrowedThings(List <Thing> listToFilter) { return null; }
    public List<Thing> getAllBorrowedThings(List <Thing> listToFilter) { return null; }
    public void addBid(User bidder, int centsPerHour) throws ThingUnavailableException {}
}
