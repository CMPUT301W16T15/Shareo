package mvc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * This class is intended as an interaction between a database and an application. The database
 * stores {@link User}s, {@link Thing}s, and {@link Bid}s. In order to keep the database in order,
 * some care must be taken when adding data. For example, when adding a User, all the Things that
 * it owns will not be added automatically.
 * </p>
 * <p>
 * Modification of Things and Users can occur outside this object, but to update the database with
 * the changes, the update methods of this object must be used.
 * </p>
 * <p>
 * One can also remove objects from the database, in a very similar manner to updating them.
 * </p>
 */
public class ShareoData extends MVCModel {
    private Map<String, User> users;
    private Set<Thing> things;
    private Set<Bid> bids;

    public ShareoData() {
        users = new HashMap<>();
        things = new HashSet<>();
        bids = new HashSet<>();
    }

    /**
     * Add a new {@link User} to the database. If a user already exists with the same name,
     * the call will fail. This only adds a user to the database, meaning any things that are
     * owned by it must also be added, separately, using {@link #addThing(Thing)}. The same goes for
     * any bids placed by the user, using {@link #addBid(Bid)}.
     * @param user User to add to the database.
     * @throws UsernameAlreadyExistsException A user with the same name already exists.
     * @see #removeUser(User)
     * @see #updateUser(User)
     * @see #getUser(String)
     * @see #addThing(Thing)
     * @see #addBid(Bid)
     */
    public void addUser(User user) throws UsernameAlreadyExistsException {
        // TODO this will add to the server instead.
        if (users.containsKey(user.getName())) {
            users.put(user.getName(), user);
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }

    /**
     * Add a new {@link Thing} to the database. This only adds the thing to the database, meaning
     * that the owner, borrower, and bids must be added separately.
     * @param thing Thing to add to the database.
     * @see #removeThing(Thing)
     * @see #updateThing(Thing)
     * @see #getThing(String)
     * @see #getThingsByDescrption(String[])
     * @see #addUser(User)
     * @see #addBid(Bid)
     */
    public void addThing(Thing thing) {
        // TODO this will add to server instead.
        things.add(thing);
    }

    /**
     * Add a new {@link Bid} to the database. This only adds the bid to the database, meaning
     * that the bidder and biddee must be added separately.
     * @param bid Bid to add to the database.
     * @see #removeBid(Bid)
     * @see #addUser(User)
     * @see #addThing(Thing)
     */
    public void addBid(Bid bid) {
        // TODO this will add to server instead.
        bids.add(bid);
    }

    /**
     * Get the {@link User} with the specified username. Returns null if this user does not exist.
     * @param name username of the user.
     * @return The user object.
     * @see #addUser(User)
     * @see #removeUser(User)
     * @see #updateUser(User)
     */
    public User getUser(String name) {
        // TODO this will query the server instead.
        return users.get(name);
    }

    public Thing getThing(String ID) {
        // TODO this will query the server, and get a thing by its ID
        return null;
    }

    /**
     * Get a list of things that match a set of keywords from the database.
     * TODO expand on search specifics.
     * @param keywords list of keywords to match.
     * @return A list of things that have matching words in their descrption.
     */
    public List<Thing> getThingsByDescrption(String[] keywords) {
        // TODO this will use elastic search on server.
        return null;
    }

    /**
     * Update a user that is already in the database, by using an updated {@link User} object. The
     * user with a matching name will be updated.
     * @param user User, matching the name of the user to be updated.
     * @see #addUser(User)
     * @see #removeUser(User)
     * @see #getUser(String)
     */
    public void updateUser(User user) {
        //TODO the user will need to be updated on server.
    }

    /**
     * Update a thing that is already in the database, by using an updated {@link Thing} object.
     * The thing with a matching id will be updated.
     * @param thing Thing, matching the id of the thing to be updated.
     * @see #addThing(Thing)
     * @see #removeThing(Thing)
     * @see #getThing(String)
     * @see #getThingsByDescrption(String[])
     */
    public void updateThing(Thing thing) {
        //TODO the thing will need to be updated on server.
    }

    /**
     * Remove an exising {@link User} from the database. This only removes a user from the database,
     * meaning any things that are owned by it must also be removed, if desired. It would be
     * quite undesireable to leave {@link Thing}s and {@link Bid}s with dangling references.
     * @param user User to remove from the database.
     * @see #addUser(User)
     * @see #updateUser(User)
     * @see #getUser(String)
     * @see #removeThing(Thing)
     * @see #removeBid(Bid)
     */
    public void removeUser(User user) {
        // TODO implement removal of user
    }

    /**
     * Remove an exising {@link Thing} from the database. This only removes a thing from the database,
     * meaning any owners and borrowers should also be removed, if desired. It would be
     * quite undesireable to leave  {@link Bid}s with dangling references.
     * @param thing Thing to remove from the database.
     * @see #addThing(Thing)
     * @see #updateThing(Thing))
     * @see #getThingsByDescrption(String[])
     * @see #removeUser(User)
     * @see #removeBid(Bid)
     */
    public void removeThing(Thing thing) {
        // TODO implement removal of thing
    }

    /**
     * Remove an exising {@link Bid} from the database. This only removes a thing from the database,
     * meaning any owners and borrowers should also be removed, if desired.
     * @param bid Bid to remove from the database.
     * @see #addBid(Bid)
     * @see #getThingsByDescrption(String[])
     * @see #removeUser(User)
     * @see #removeThing(Thing)
     */
    public void removeBid(Bid bid) {
        //TODO implement removal of bid
    }
}