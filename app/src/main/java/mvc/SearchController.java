package mvc;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.SearchResult;

/**
 * Created by anonymous on 3/12/2016.
 */
public class SearchController {
    private static final String TAG = "SearchController";
    private static JestDroidClient jestClient;
    private final String searchType;
    private static final String ELASTIC_INDEX = "shareo";
    private static final String ELASTIC_USER_TYPE = "user";
    private static final String ELASTIC_GAME_TYPE = "game";
    private String[] keywords;

    /**
     * searchController can be called from ShareoData to perform searches.
     * For example, getGamesByDescription() in ShareoData
     * can call searchController to get a search result.
     *
     * The jestClient, searchType, and keywords would all be passed into getGamesByDescription.
     * @param searchType
     * @param jestClient
     * @param keywords
     */
    public SearchController(String searchType, JestDroidClient jestClient, String[] keywords)
    {
        this.searchType = searchType;
        this.jestClient = jestClient;
        this.keywords = keywords;

        switch (searchType)
        {
            case ELASTIC_GAME_TYPE:
                Log.d(TAG,"Passed in Elastic Game Type Search");
                // AddGamesTask addGamesTask= new AddGamesTask();
                break;
            case ELASTIC_USER_TYPE:
                Log.d(TAG,"Passed in Elastic User Type Search");
                break;
            default:
                Log.d(TAG,"Other Search");
                break;

        }

    }
    public static void verifyConfig() {
        if (jestClient == null) {
            Log.d(TAG, "jestClient is null");
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            jestClient = (JestDroidClient) factory.getObject();
        }
    }

    public static class AddGamesTask extends AsyncTask<Thing,Void,Void> {
        @Override
        protected Void doInBackground(Thing... games) {
            verifyConfig();

            for (Thing game : games) {
                Index index = new Index.Builder(game).index(ELASTIC_INDEX).type(ELASTIC_GAME_TYPE).build();

                try {
                    DocumentResult result = jestClient.execute(index);
                    if (result.isSucceeded()) {
                        game.setJestID(result.getId());
                    } else {
                        Log.d(TAG, "Our insert of game failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static class AddUsersTask extends AsyncTask<User,Void,Void> {
        @Override
        protected Void doInBackground(User... users) {
            verifyConfig();

            for (User user : users) {
                Index index = new Index.Builder(user).index(ELASTIC_INDEX).type(ELASTIC_USER_TYPE).build();

                try {
                    DocumentResult result = jestClient.execute(index);
                    if (result.isSucceeded()) {
                        user.setJestID(result.getId());
                    } else {
                        Log.d(TAG, "Our insert of user failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    public class GetGamesTask extends AsyncTask<String, Void, ArrayList<Thing>> {
        @Override
        protected ArrayList<Thing> doInBackground(String... stringToSearch) {
            verifyConfig();

            ArrayList<Thing> gamesFromSearch = new ArrayList<Thing>();

            String mySearchString;
            if(stringToSearch[0] != "") {
                mySearchString = "{\"query\":{\"match\":{\"message\":\""+stringToSearch[0]+"\"}}}";
            } else {
                mySearchString = "{\"from\":0,\"size\":1000}";
            }
            //Index post = new Index.Builder(o).index(index).type(type).build();
            io.searchbox.core.Search search = new io.searchbox.core.Search.Builder(mySearchString).addIndex(ELASTIC_INDEX)
                    .addType(ELASTIC_GAME_TYPE).build();

            try {
                SearchResult result = jestClient.execute(search);
                if (result.isSucceeded()) {
                    List<Thing> gamesReturned = result.getSourceAsObjectList(Thing.class);
                    gamesFromSearch.addAll(gamesReturned);
                } else {
                    Log.d(TAG, "Searching for games unsuccessful.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return gamesFromSearch;
        }
    }

    public class GetUsersTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... stringToSearch) {
            verifyConfig();

            ArrayList<User> usersFromSearch = new ArrayList<User>();

            String mySearchString;
            if(stringToSearch[0] != "") {
                 mySearchString = "{\"query\":{\"match\":{\"message\":\""+stringToSearch[0]+"\"}}}";
            } else {
                mySearchString = "{\"from\":0,\"size\":1000}";
            }
            //Index post = new Index.Builder(o).index(index).type(type).build();
            io.searchbox.core.Search search = new io.searchbox.core.Search.Builder(mySearchString).addIndex(ELASTIC_INDEX)
                    .addType(ELASTIC_USER_TYPE).build();

            try {
                SearchResult result = jestClient.execute(search);
                if (result.isSucceeded()) {
                    List<User> usersReturned = result.getSourceAsObjectList(User.class);
                    usersFromSearch.addAll(usersReturned);
                }
                else
                {
                    Log.d(TAG,"Searching for users failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return usersFromSearch;
        }
    }
}
