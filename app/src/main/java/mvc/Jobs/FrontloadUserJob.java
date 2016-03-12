package mvc.Jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import java.util.List;

import mvc.AppUserSingleton;
import mvc.ShareoData;
import mvc.Thing;
import mvc.User;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class FrontloadUserJob extends Job {

    static final int PRIORITY = 1;
    String username;
    CallbackInterface callback;

    public FrontloadUserJob(String username, CallbackInterface callback) {
        super(new Params(PRIORITY));
        this.username = username;
        this.callback = callback;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public void onRun() throws Throwable {
        ShareoData data = ShareoData.getInstance();
        User user = data.getUser(username);
        if (user == null)
        {
            throw new Exception();
        }
        user.getBids();
        user.getBorrowedThings();
        List<Thing> things = user.getOwnedThings();
        for (Thing t : things) {
            t.getBids();
            if (t.getStatus() == Thing.Status.BORROWED) {
                t.getAcceptedBid();
            }
        }
        AppUserSingleton.getInstance().setUser(user);
        callback.onSuccess();
    }

    @Override
    protected void onCancel() {
        callback.onFailure();
    }

    @Override
    public void onAdded() {

    }
}
