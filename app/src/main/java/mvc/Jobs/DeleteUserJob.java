package mvc.Jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import mvc.ShareoData;
import mvc.Thing;
import mvc.User;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class DeleteUserJob extends Job {
    static final int PRIORITY = 1;
    User user;
    CallbackInterface callback;

    public DeleteUserJob(User user, CallbackInterface callback) {
        super(new Params(PRIORITY));
        this.user = user;
        this.callback = callback;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public void onRun() throws Throwable {
        ShareoData.getInstance().removeUser(user);
        callback.onSuccess();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {

    }
}
