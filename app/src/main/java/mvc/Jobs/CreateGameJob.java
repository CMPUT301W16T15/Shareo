package mvc.Jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import mvc.ShareoData;
import mvc.Thing;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class CreateGameJob extends Job {
    static final int PRIORITY = 1;
    Thing thing;
    CallbackInterface callback;

    public CreateGameJob(Thing thing, CallbackInterface callback) {
        super(new Params(PRIORITY).requireNetwork());
        this.thing = thing;
        this.callback = callback;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.RETRY;
    }

    @Override
    public void onRun() throws Throwable {
        ShareoData.getInstance().addGame(thing);
        callback.onSuccess();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {

    }
}
