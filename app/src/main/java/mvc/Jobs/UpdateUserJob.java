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
public class UpdateUserJob extends Job {
    static final int PRIORITY = 1;
    User user;

    public UpdateUserJob(User user) {
        super(new Params(PRIORITY).requireNetwork());
        this.user = user;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public void onRun() throws Throwable {
        ShareoData.getInstance().updateUser(user);
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {

    }
}
