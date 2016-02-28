package cmput301w16t15.shareo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

public class SearchFragment extends Fragment {
    private ListView listViewSearch;
    private SearchView searchView;
    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        listViewSearch = (ListView) v.findViewById(R.id.listViewSearch);
        searchView = (SearchView) v.findViewById(R.id.searchViewSearch);
        return v;
    }
}
