package mvc.Jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import mvc.Bid;
import mvc.ShareoData;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class DeleteBidJob extends Job {
    static final int PRIORITY = 1;
    Bid bid;
    CallbackInterface callback;

    public DeleteBidJob(Bid bid, CallbackInterface callback) {
        super(new Params(PRIORITY));
        this.bid = bid;
        this.callback = callback;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public void onRun() throws Throwable {
        ShareoData.getInstance().removeBid(bid);
        callback.onSuccess();
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {

    }
}
