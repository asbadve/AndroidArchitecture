package iammert.com.androidarchitecture.ui.NowPlayingFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import iammert.com.androidarchitecture.R;
import iammert.com.androidarchitecture.data.local.entity.MovieEntity;
import iammert.com.androidarchitecture.databinding.FragmentMovieListBinding;
import iammert.com.androidarchitecture.ui.detail.MovieDetailActivity;
import iammert.com.androidarchitecture.ui.main.MovieListAdapter;
import iammert.com.androidarchitecture.ui.main.MovieListCallback;

/**
 * Created by mertsimsek on 19/05/2017.
 */

public class NowPlaying extends Fragment implements MovieListCallback {

    @Inject
    NowMovieListViewModel movieListViewModel;

    FragmentMovieListBinding binding;

    public static NowPlaying newInstance(boolean isNowPlaying) {
        Bundle args = new Bundle();
        args.putBoolean("isNowPlaying", isNowPlaying);
        NowPlaying fragment = new NowPlaying();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieListBinding.inflate(inflater, container, false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.recyclerView.setAdapter(new MovieListAdapter(this));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movieListViewModel
                .getNowPLayingMovies()
                .observe(this, listResource -> binding.setResource(listResource));
    }

    @Override
    public void onMovieClicked(MovieEntity movieEntity, View sharedView) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), sharedView, getString(R.string.shared_image));
        startActivity(MovieDetailActivity.newIntent(getActivity(), movieEntity.getId()), options.toBundle());
    }
}
