package iammert.com.androidarchitecture.data.remote;

import iammert.com.androidarchitecture.data.remote.model.MoviesResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mertsimsek on 19/05/2017.
 */

public interface MovieDBService {

    @GET("movie/popular")
    Observable<MoviesResponse> loadMovies();

    @GET("movie/popular")
    Observable<MoviesResponse> getPopularMoviesListAtPage(@Query("page") int page);

}
