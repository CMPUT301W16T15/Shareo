package cmput301w16t15.shareo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

/**
 * Created by Andrew on 2016-02-27.
 */
public class ShareoFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Home", "Profile", "Bids", "Search" };
    private Context context;

    public ShareoFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /*
    * TODO: other screens (profile, bids, search), this is where we create their instance!
    * */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return HomeFragment.newInstance();
        } else if (position == 1) {
            return ProfileFragment.newInstance();
        } else if (position == 2) {
            return BidsFragment.newInstance();
        } else if (position == 3) {
            return SearchFragment.newInstance();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
