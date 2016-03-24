package cmput301w16t15.shareo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

/**
 * Created by Bradshaw on 2016-03-11.
 */
public class ShareoApplication extends Application {

    private static ShareoApplication instance;
    private JobManager jobManager;

    public ShareoApplication() {
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
