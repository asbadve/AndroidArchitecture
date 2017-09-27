package iammert.com.androidarchitecture.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import iammert.com.androidarchitecture.ui.NowPlayingFragment.NowPlaying;
import iammert.com.androidarchitecture.ui.PopularFragment.Popular;

/**
 * Created by mertsimsek on 20/05/2017.
 */

public class MoviesPagerAdapter extends FragmentStatePagerAdapter {

    private static final String[] titles = new String[]{"Popular", "Now", "Comedy"};

    public MoviesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return Popular.newInstance(false);
            case 1:
                return NowPlaying.newInstance(false);
//            case 2:
//                return MovieListFragment.newInstance(false);

        }
        return MovieListFragment.newInstance(true);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
