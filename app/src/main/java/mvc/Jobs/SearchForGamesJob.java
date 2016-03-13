package mvc.Jobs;

import android.os.Handler;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import java.util.List;

import mvc.ShareoData;
import mvc.Thing;

/**
 * Created by Bradshaw on 2016-03-13.
 */
public class SearchForGamesJob extends Job {
    static final int PRIORITY = 1;
    List<Thing> things;
    String description;
    Handler handler;
    Runnable onComplete;

    public SearchForGamesJob(String description, List<Thing> returnList, Runnable onComplete, Handler h) {
        super(new Params(PRIORITY));
        this.description = description;
        this.things = things;
        this.onComplete = onComplete;
        this.handler = handler;
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    @Override
    public void onRun() throws Throwable {
        things = ShareoData.getInstance().getGamesByDescription(description);
        handler.post(onComplete);
    }

    @Override
    protected void onCancel() {

    }

    @Override
    public void onAdded() {

    }
}
