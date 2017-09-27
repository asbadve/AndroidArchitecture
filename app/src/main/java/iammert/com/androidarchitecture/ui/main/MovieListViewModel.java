package iammert.com.androidarchitecture.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import iammert.com.androidarchitecture.data.MovieRepository;
import iammert.com.androidarchitecture.data.Resource;
import iammert.com.androidarchitecture.data.local.entity.MovieEntity;

/**
 * Created by mertsimsek on 19/05/2017.
 */

public class MovieListViewModel extends ViewModel {
    private final LiveData<Resource<List<MovieEntity>>> popularMovies;
    private boolean isNowPlaying;

    @Inject
    public MovieListViewModel(MovieRepository movieRepository) {
//        popularMovies = movieRepository.loadPopularMovies();
        if (isNowPlaying) {
            popularMovies = movieRepository.loadPopularMoviesByPage();
        } else {
            popularMovies = movieRepository.loadNowPlayingMoviesByPage();
        }
    }

    LiveData<Resource<List<MovieEntity>>> getPopularMovies() {
        return popularMovies;
    }

    LiveData<Resource<List<MovieEntity>>> getNowPlayingMovies() {
        return popularMovies;
    }


    public void setMovieType(boolean isNowPlaying) {
        this.isNowPlaying = isNowPlaying;
    }
}
