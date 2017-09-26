package iammert.com.androidarchitecture.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import iammert.com.androidarchitecture.data.local.dao.MovieDao;
import iammert.com.androidarchitecture.data.local.entity.MovieEntity;
import iammert.com.androidarchitecture.data.remote.MovieDBService;
import iammert.com.androidarchitecture.data.remote.model.MoviesResponse;
import io.reactivex.Observable;

/**
 * Created by mertsimsek on 19/05/2017.
 */

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();
    private final MovieDao movieDao;
    private final MovieDBService movieDBService;

    @Inject
    public MovieRepository(MovieDao movieDao, MovieDBService movieDBService) {
        this.movieDao = movieDao;
        this.movieDBService = movieDBService;
    }

    public LiveData<Resource<List<MovieEntity>>> loadPopularMoviesByPage() {
        return new NetworkPageBoundResource<List<MovieEntity>, MoviesResponse>() {


            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                currentPage = item.getPage();
                movieDao.saveMovies(item.getResults());
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                return movieDao.loadMovies();
            }


            @Override
            protected boolean takeUntil(MoviesResponse movie) {
                return movie.takeUntil();
            }

            @Override
            protected int getNextPageIndex() {
                return currentPage + 1;
            }

            @Override
            protected void saveNextPageIndex(int pageIndexToSave) {
                //save to sf
            }

            @Override
            protected int getPageLimit() {
                return 15;
            }

            @Override
            protected void onNextPageLoad(CombineClass<MoviesResponse> testObject) {
                if (testObject != null) {
                    List<MoviesResponse> list = testObject.getList();
                    Log.d(TAG, "onNextPageLoad() called with: testObject = [" + testObject + "]");
                }
            }


            @Override
            protected Observable<MoviesResponse> getPagedObservable(int nextPageToFetch) {
                return movieDBService.getPopularMoviesListAtPage(nextPageToFetch);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<MovieEntity>>> loadPopularMovies() {
        return new NetworkBoundResource<List<MovieEntity>, MoviesResponse>() {

            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                movieDao.saveMovies(item.getResults());
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                return movieDao.loadMovies();
            }

            @NonNull
            @Override
            protected Observable<MoviesResponse> createCall() {
                return movieDBService.loadMovies();
            }


        }.getAsLiveData();
    }

    public LiveData<MovieEntity> getMovie(int id) {
        return movieDao.getMovie(id);
    }
}
