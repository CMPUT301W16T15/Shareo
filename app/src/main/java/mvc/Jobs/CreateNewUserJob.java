package mvc.Jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import mvc.AppUserSingleton;
import mvc.ShareoData;
import mvc.User;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class CreateNewUserJob extends Job {

    static final int PRIORITY = 1;
    String username;
    String emailAddress;
    String motto;
    String fullName;
    CallbackInterface callback;

    public CreateNewUserJob(String username, String fullName, String emailAddress, String motto, CallbackInterface callback) {
        super(new Params(PRIORITY));
        this.username = username;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.motto = motto;
        this.callback = callback;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public void onRun() throws Throwable {
        User user = new User.Builder(ShareoData.getInstance(), username, fullName, emailAddress, motto).build();
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
