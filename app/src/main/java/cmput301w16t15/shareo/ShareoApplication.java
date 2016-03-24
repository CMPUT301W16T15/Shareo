package cmput301w16t15.shareo;

import android.app.Application;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

/**
 * Application: a subclass around Application, holding some app specific functionality
 */
public class ShareoApplication extends Application {

    private static ShareoApplication instance;
    private JobManager jobManager;

    public ShareoApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration config = new Configuration.Builder(this).maxConsumerCount(1).build();
        jobManager = new JobManager(this, config);
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public static ShareoApplication getInstance() {
        return instance;
    }
}
