package mvc;

import com.path.android.jobqueue.JobManager;

import cmput301w16t15.shareo.ShareoApplication;
import mvc.Jobs.CallbackInterface;
import mvc.Jobs.CreateNewUserJob;
import mvc.Jobs.FrontloadUserJob;

/**
 * Created by Bradshaw on 2016-03-01.
 * This singleton is used to keep track of the user that is logged in throughout all points in the
 * application
 */
public class AppUserSingleton {

    User user;
    JobManager jobManager;

    private static AppUserSingleton ourInstance = new AppUserSingleton();

    public static AppUserSingleton getInstance() {
        return ourInstance;
    }

    private AppUserSingleton() {
        jobManager = ShareoApplication.getInstance().getJobManager();
    }

    public void logIn(String username, CallbackInterface callback) {
        jobManager.addJob(new FrontloadUserJob(username, callback));
    }

    public void createUser(String username, String fullName, String emailAddress, String motto, CallbackInterface callback) {
        jobManager.addJobInBackground(new CreateNewUserJob(username, fullName, emailAddress, motto, callback));
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void logOut()
    {
        this.user = null;
    }

    public User getUser()
    {
        return this.user;
    }


}
