package mvc.Jobs;

import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

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

    @Override
    public void onRun() throws Throwable {
        ShareoData.getInstance().addGame(thing);
        owner.addThingID(thing.getJestID());
        ShareoData.getInstance().updateUser(owner);
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {
        owner.addIDLessThing(thing);
    }
}
