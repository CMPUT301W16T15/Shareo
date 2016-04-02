package mvc.Jobs;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import java.util.List;

import cmput301w16t15.shareo.ShareoApplication;
import mvc.AppUserSingleton;
import mvc.Observable;
import mvc.Observer;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class CreateGameJob extends Job {
    static final int PRIORITY = 1;
    Thing thing;
    User owner;

    public CreateGameJob(Thing thing, User owner) {
        super(new Params(PRIORITY).persist().requireNetwork());
        this.thing = thing;
        this.owner = owner;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.RETRY;
    }

    /**
     * Add new game to a user, note the pointer to the user WILL BE DIFFERENT than the one passed in
     * the pointer to the thing will also be different (due to data persistence).
     * @throws Throwable
     */
    @Override
    public void onRun() throws Throwable {
        User updatedUser = ShareoData.getInstance().getUser(owner.getName());
        ShareoData.getInstance().addGame(thing);
        updatedUser.addThingID(thing.getJestID());
        ShareoData.getInstance().updateUser(updatedUser);

        //Update the currently logged in user with proper game info
        User loggedIn = AppUserSingleton.getInstance().getUser();
        if (loggedIn != null && loggedIn.getName().equals(owner.getName())) {
            loggedIn.removeIDLessThings();
            loggedIn.addOwnedThing(thing);
        }
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {
        owner.addIDLessThing(thing);
    }
}
