package iammert.com.androidarchitecture.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import iammert.com.androidarchitecture.ui.NowPlayingFragment.NowPlaying;
import iammert.com.androidarchitecture.ui.PopularFragment.Popular;
import iammert.com.androidarchitecture.ui.main.MovieListFragment;

/**
 * Created by mertsimsek on 30/05/2017.
 */
@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract MovieListFragment contributeMovieListFragment();

    @ContributesAndroidInjector
    abstract Popular contributePopularMovieListFragment();

    @ContributesAndroidInjector
    abstract NowPlaying contributeNowMovieListFragment();

}
