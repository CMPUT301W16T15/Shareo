package mvc;

import java.util.List;

import thing.Thing;
import user.User;

/**
 * Created by A on 2016-02-10.
 */
public class ShareoData extends MVCModel {
    public ShareoData() {}

    public User getUserByName(String name) { return null; }
    public List<Thing> getThingsByUser(User user) { return null; }
    public List<Thing> getThingsByDescrption(String[] keywords) { return null; }
}
