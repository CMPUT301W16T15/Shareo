package mvc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import thing.Thing;
import user.User;

/**
 * Created by A on 2016-02-10.
 */
public class ShareoData extends MVCModel {
    private Map<String, User> users;
    private Set<Thing> things;

    public ShareoData() {
        users = new HashMap<>();
        things = new HashSet<>();
    }

    public void addUser(User user) throws UsernameAlreadyExistsException {
        if (users.containsKey(user.getName())) {
            users.put(user.getName(), user);
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }
    public User getUserByName(String name) {
        return users.get(name);
    }

    public void addThing(Thing thing) {
        things.add(thing);
    }

    public List<Thing> getThingsByUser(String username) {
        return users.get(username).getOwnedThings();
    }

    public List<Thing> getThingsByDescrption(String[] keywords) { return null; }
}
