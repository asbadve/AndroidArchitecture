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

    @Inject
    public MovieListViewModel(MovieRepository movieRepository) {
//        popularMovies = movieRepository.loadPopularMovies();
        popularMovies = movieRepository.loadPopularMoviesByPage();
    }

    LiveData<Resource<List<MovieEntity>>> getPopularMovies() {
        return popularMovies;
    }
}
