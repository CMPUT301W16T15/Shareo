package mvc;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cmput301w16t15.shareo.ShareoApplication;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.SearchResult;
import mvc.Jobs.SearchForGamesJob;

/**
 * Created by anonymous on 3/12/2016.
 */
public class SearchController {

    /**
     * searchController can be called to perform searched from ShareoData
     * For example, getGamesByDescription() in ShareoData
     * can be called using a Job to return a result
     */
    public SearchController() {
    }

    public void findGamesByDescription(String description, List<Thing> returnList, Runnable onComplete) {
        ShareoApplication.getInstance().getJobManager().addJobInBackground(new SearchForGamesJob(description, returnList, onComplete, new Handler()));
    }
}
