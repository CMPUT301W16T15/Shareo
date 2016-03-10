package mvc;

import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import mvc.exceptions.NullIDException;
import mvc.exceptions.UsernameAlreadyExistsException;

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

    private static final String SERVER_URL = "http://cmput301.softwareprocess.es:8080";
    private static final String ELASTIC_INDEX = "shareo";
    private static final String ELASTIC_USER_TYPE = "user";
    private static final String ELASTIC_GAME_TYPE = "game";
    private static final String ELASTIC_BID_TYPE = "bid";
    private static JestDroidClient jestClient;
    static {createJestClient();}

    private static ShareoData instance;

    public static ShareoData getInstance() {
        if (instance == null) {
            instance = new ShareoData();
        }
        return instance;
    }

    protected ShareoData() {

    }

    /**
     * Add a new {@link User} to the database. If a user already exists with the same name,
     * the call will fail. This only adds a user to the database, meaning any things that are
     * owned by it must also be added, separately, using {@link #addGame(Thing)}. The same goes for
     * any bids placed by the user, using {@link #addBid(Bid)}. The user's id will be its username.
     * @param user User to add to the database.
     * @throws UsernameAlreadyExistsException A user with the same name already exists.
     * @see #removeUser(User)
     * @see #updateUser(User)
     * @see #getUser(String)
     * @see #addGame(Thing)
     * @see #addBid(Bid)
     */
    public void addUser(User user) throws UsernameAlreadyExistsException {
        if (getUser(user.getName()) != null) {
            throw new UsernameAlreadyExistsException();
        }

        addByObject(ELASTIC_INDEX, ELASTIC_USER_TYPE, user.getJestID(), user);
    }

    /**
     * Add a new {@link Thing} to the database. This only adds the thing to the database, meaning
     * that the owner, borrower, and bids must be added separately.
     * @param thing Thing to add to the database.
     * @see #removeGame(Thing)
     * @see #updateGame(Thing)
     * @see #getGame(String)
     * @see #getGamesByDescrption(String[])
     * @see #addUser(User)
     * @see #addBid(Bid)
     */
    public void addGame(Thing thing) {
        addByObject(ELASTIC_INDEX, ELASTIC_GAME_TYPE, thing);
    }

    /**
     * Add a new {@link Bid} to the database. This only adds the bid to the database, meaning
     * that the bidder and biddee must be added separately.
     * @param bid Bid to add to the database.
     * @see #removeBid(Bid)
     * @see #addUser(User)
     * @see #addGame(Thing)
     */
    public void addBid(Bid bid) {
        addByObject(ELASTIC_INDEX, ELASTIC_BID_TYPE, bid);
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
        return getByID(ELASTIC_INDEX, ELASTIC_USER_TYPE, name, User.class);
    }

    public Thing getGame(String ID) {
        return getByID(ELASTIC_INDEX, ELASTIC_GAME_TYPE, ID, Thing.class);
    }

    public Bid getBid(String ID) {
        return getByID(ELASTIC_INDEX, ELASTIC_BID_TYPE, ID, Bid.class);
    }

    /**
     * Get a list of things that match a set of keywords from the database.
     * TODO expand on search specifics.
     * @param keywords list of keywords to match.
     * @return A list of things that have matching words in their descrption.
     */
    public List<Thing> getGamesByDescrption(String[] keywords) {
        // TODO this will use elastic search on server.
        return null;
    }

    /**
     * Update a user that is already in the database, by using an updated {@link User} object. The
     * user with a matching name will be updated.
     * @param user User, matching the name of the user to be updated.
     * @throws NullIDException The user has no JestID.
     * @see #addUser(User)
     * @see #removeUser(User)
     * @see #getUser(String)
     */
    public void updateUser(User user) throws NullIDException {
        updateByObject(ELASTIC_INDEX, ELASTIC_USER_TYPE, user);

        notifyViews();
    }

    /**
     * Update a thing that is already in the database, by using an updated {@link Thing} object.
     * The thing with a matching id will be updated.
     * @param thing Thing, matching the id of the thing to be updated.
     * @see #addGame(Thing)
     * @see #removeGame(Thing)
     * @see #getGame(String)
     * @see #getGamesByDescrption(String[])
     */
    public void updateGame(Thing thing) throws NullIDException {
        updateByObject(ELASTIC_INDEX, ELASTIC_GAME_TYPE, thing);

        notifyViews();
    }

    public void updateBid(Bid bid) throws NullIDException{
        updateByObject(ELASTIC_INDEX, ELASTIC_BID_TYPE, bid);

        notifyViews();
    }

    /**
     * Remove an exising {@link User} from the database. This only removes a user from the database,
     * meaning any things that are owned by it must also be removed, if desired. It would be
     * quite undesireable to leave {@link Thing}s and {@link Bid}s with dangling references.
     * @param user User to remove from the database.
     * @throws NullIDException The user has no JestID
     * @see #addUser(User)
     * @see #updateUser(User)
     * @see #getUser(String)
     * @see #removeGame(Thing)
     * @see #removeBid(Bid)
     */
    public void removeUser(User user) throws NullIDException {
        removeByObject(ELASTIC_INDEX, ELASTIC_USER_TYPE, user);
    }

    public void removeUser(String ID) {
        removeByID(ELASTIC_INDEX, ELASTIC_USER_TYPE, ID);
    }

    /**
     * Remove an exising {@link Thing} from the database. This only removes a thing from the database,
     * meaning any owners and borrowers should also be removed, if desired. It would be
     * quite undesireable to leave {@link Bid}s with dangling references.
     * @param thing Thing to remove from the database.
     * @see #addGame(Thing)
     * @see #updateGame(Thing) )
     * @see #getGamesByDescrption(String[])
     * @see #removeUser(User)
     * @see #removeBid(Bid)
     */
    public void removeGame(Thing thing) throws NullIDException {
        removeByObject(ELASTIC_INDEX, ELASTIC_GAME_TYPE, thing);
    }

    /**
     * Remove an exising {@link Bid} from the database. This only removes a thing from the database,
     * meaning any owners and borrowers should also be removed, if desired.
     * @param bid Bid to remove from the database.
     * @see #addBid(Bid)
     * @see #getGamesByDescrption(String[])
     * @see #removeUser(User)
     * @see #removeGame(Thing)
     */
    public void removeBid(Bid bid) throws NullIDException {
        removeByObject(ELASTIC_INDEX, ELASTIC_BID_TYPE, bid);
    }

    private static void createJestClient() {
        if (jestClient == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(SERVER_URL);
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            jestClient = (JestDroidClient) factory.getObject();
        }
    }

    protected void addByObject(String index, String type, JestData<ShareoData> o) {
        Index post = new Index.Builder(o).index(index).type(type).build();

        try {
            DocumentResult result = jestClient.execute(post);
            if (result.isSucceeded()) {
                o.setJestID(result.getId());
            } else {
                // TODO what if we fail?
                Log.e("TODO", "Unable to save " + o.getClass().getName() + " to server.");
            }
        } catch (IOException e) {
            // TODO do something clever, like offline behavior.
            e.printStackTrace();
        }
    }

    protected void addByObject(String index, String type, String ID, JestData<ShareoData> o) {
        Index post = new Index.Builder(o).index(index).type(type).id(ID).build();

        try {
            DocumentResult result = jestClient.execute(post);
            if (result.isSucceeded()) {
                o.setJestID(result.getId());
            } else {
                // TODO what if we fail?
                Log.e("TODO", "Unable to save " + o.getClass().getName() + " to server.");
            }
        } catch (IOException e) {
            // TODO do something clever, like offline behavior.
            e.printStackTrace();
        }
    }

    protected <T extends JestData> T getByID(String index, String type, String ID, Class<T> classType) {
        Get get = new Get.Builder(index, ID).type(type).build();

        try {
            JestResult result = jestClient.execute(get);
            if (result.isSucceeded()) {
                T t = result.getSourceAsObject(classType);
                t.setJestID(ID);
                return t;
            } else {
                // TODO what if we fail?
                Log.e("TODO", "Unable to get " + classType.getName() + " with ID=" + ID);
                return null;
            }
        } catch (IOException e) {
            // TODO what if we fail?
            e.printStackTrace();
        }

        return null;
    }

    protected void updateByObject(String index, String type, JestData<ShareoData> o) throws NullIDException {
        Index put = new Index.Builder(o).index(index).type(type).id(o.getJestID()).build();

        try {
            DocumentResult result = jestClient.execute(put);
            if (result.isSucceeded()) {

            } else {
                // TODO what if failed?
                // TODO seems unable to update.
                Log.e("TODO", "Failed to update " + o.getClass().getName()  + ". Response Code: " + result.getResponseCode());
                Log.e("TODO", "Failed update: " + put.getURI());
            }
        } catch (IOException e) {
            // TODO what to do if failed?
            e.printStackTrace();
        }
    }

    protected void removeByObject(String index, String type, JestData<ShareoData> o) throws NullIDException {
        removeByID(index, type, o.getJestID());
    }

    protected void removeByID(String index, String type, String ID) {
        Delete delete = new Delete.Builder(ID).index(index).type(type).build();

        try {
            DocumentResult result = jestClient.execute(delete);
            if (result.isSucceeded()) {

            } else {
                // TODO what if failed?
                Log.e("TODO", "Failed to delete ID: " + ID);
            }
        } catch (IOException e) {
            // TODO what to do if failed?
            e.printStackTrace();
        }
    }
}
