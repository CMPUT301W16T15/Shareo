package cmput301w16t15.shareo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mvc.ShareoData;
import mvc.Thing;

public class SearchFragment extends Fragment {
    private ListView mListView;
    private SearchView mSearchView;
    private SearchAdapter mListAdapter;

    private Handler mHandler;
    private Runnable mRunnable;

    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    // TODO: save search query and past results in localstorage
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        mListView = (ListView) v.findViewById(R.id.listview);
        // empty results at first
        mListAdapter = new SearchAdapter(this.getContext(), R.layout.available_items, new ArrayList<Thing>());
        mListView.setAdapter(mListAdapter);

        mHandler = new Handler();

        mSearchView = (SearchView) v.findViewById(R.id.searchbox);
        mSearchView.setIconified(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                if (mRunnable != null) {
                    mHandler.removeCallbacks(mRunnable);
                }
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        new SearchTask().execute(query);
                    }
                };
                mHandler.postDelayed(mRunnable, 500);

                return false;
            }
        });

        return v;
    }

    class SearchTask extends AsyncTask<String, Void, List<Thing>> {

        @Override
        protected List<Thing> doInBackground(String... params) {
            return ShareoData.getInstance().getGamesByDescription(params[0]);
        }

        @Override
        protected void onPostExecute(List<Thing> res) {
            Log.v("TAG", "size: " + res.size());
            mListAdapter = new SearchAdapter(getActivity(), R.layout.available_items, res);
            mListView.setAdapter(mListAdapter);
            mListAdapter.notifyDataSetChanged();
        }
    }

    private class SearchAdapter extends ArrayAdapter<Thing> {

        private final Context context;
        private final List<Thing> things;

        public SearchAdapter(Context context, int resource, List<Thing> objects) {
            super(context, resource, objects);
            this.context = context;
            this.things = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.available_items, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.available_game_name);
            textView.setText(things.get(position).getName());
            return rowView;
        }
    }
}
