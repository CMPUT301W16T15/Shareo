package mvc.Jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import mvc.Bid;
import mvc.ShareoData;
import mvc.Thing;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class DeleteGameJob extends Job {
    static final int PRIORITY = 1;
    Thing game;
    CallbackInterface callback;

    public DeleteGameJob(Thing game, CallbackInterface callback) {
        super(new Params(PRIORITY));
        this.game = game;
        this.callback = callback;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public void onRun() throws Throwable {
        ShareoData.getInstance().removeGame(game);
        callback.onSuccess();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {

    }
}
