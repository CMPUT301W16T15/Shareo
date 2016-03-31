package mvc.Jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import mvc.AppUserSingleton;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class UpdateGameJob extends Job {

    static final int PRIORITY = 1;
    Thing thing;

    public UpdateGameJob(Thing thing) {
        super(new Params(PRIORITY).requireNetwork().persist());
        this.thing = thing;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public void onRun() throws Throwable {
        ShareoData.getInstance().updateGame(thing);
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {

    }
}
