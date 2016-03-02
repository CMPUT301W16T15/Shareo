package mvc;

/**
 * Created by Bradshaw on 2016-03-01.
 * This singleton is used to keep track of the user that is logged in throughout all points in the
 * application
 */
public class AppUserSingleton {

    User user;

    private static AppUserSingleton ourInstance = new AppUserSingleton();

    public static AppUserSingleton getInstance() {
        return ourInstance;
    }

    private AppUserSingleton() {
    }

    public void logIn(User user) {
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
