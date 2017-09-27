package iammert.com.androidarchitecture.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.util.List;

import iammert.com.androidarchitecture.data.local.entity.MovieEntity;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by mertsimsek on 19/05/2017.
 */
@Dao
public abstract class MovieDao {


    public static final int NOW_PLAYING = 1;
    public static final int POPULAR_MOVIES = 2;

    @Query("SELECT * FROM movies where isPopular='1'")
    public abstract LiveData<List<MovieEntity>> loadPopularMovie();

    @Query("SELECT * FROM movies  where isNowPlaying='1'")
    public abstract LiveData<List<MovieEntity>> loadNowPlayingMovies();

    @Query("SELECT * FROM movies")
    public abstract LiveData<List<MovieEntity>> loadMovies();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void saveMovies(List<MovieEntity> movieEntities);


    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void saveMovie(MovieEntity movieEntitiy);

    @Update(onConflict = OnConflictStrategy.FAIL)
    public abstract void updateMovie(MovieEntity movieEntitiy);

//    @Query("UPDATE movies SET posterPath=:posterPath,adult=:adult,overview=:overview,originalTitle=:originalTitle,title=:title,voteAverage=:voteAverage,backdropPath=:backdropPath,originalLanguage=:originalLanguage,isNowPlaying=:isNowPlaying")
//    public abstract void updateIsNowPlayingMovies(List<MovieEntity> movieEntities);

    /***
     *
     * @param posterPath
     * @param adult
     * @param overview
     * @param originalTitle
     * @param title
     * @param voteCount
     * @param voteAverage
     * @param backdropPath
     * @param originalLanguage
     * @param isNowPlaying
     */
    @Query("UPDATE movies SET posterPath=:posterPath,adult=:adult,overview=:overview,originalTitle=:originalTitle,title=:title,voteCount=:voteCount,voteAverage=:voteAverage,backdropPath=:backdropPath,originalLanguage=:originalLanguage,isNowPlaying=:isNowPlaying,releaseDate=:releaseDate WHERE id=:id")
    public abstract void updateIsNowPlayingMovie(String posterPath, boolean adult, String overview, String originalTitle, String title, int voteCount, double voteAverage, String backdropPath, String originalLanguage, boolean isNowPlaying, String releaseDate, int id);


    /***
     *
     * @param posterPath
     * @param adult
     * @param overview
     * @param originalTitle
     * @param title
     * @param voteCount
     * @param voteAverage
     * @param backdropPath
     * @param originalLanguage
     * @param isPopular
     */
    @Query("UPDATE movies SET posterPath=:posterPath,adult=:adult,overview=:overview,originalTitle=:originalTitle,title=:title,voteCount=:voteCount,voteAverage=:voteAverage,backdropPath=:backdropPath,originalLanguage=:originalLanguage,isPopular=:isPopular,releaseDate=:releaseDate WHERE id=:id")
    public abstract void updateIsPopularMovie(String posterPath, boolean adult, String overview, String originalTitle, String title, int voteCount, double voteAverage, String backdropPath, String originalLanguage, boolean isPopular, String releaseDate, int id);

    @Query("SELECT * FROM movies WHERE id=:id")
    public abstract LiveData<MovieEntity> getMovie(int id);


    @Query("SELECT isPopular FROM movies WHERE id=:id")
    public abstract boolean isMoviePopular(int id);

    @Query("SELECT isNowPlaying FROM movies WHERE id=:id")
    public abstract boolean isMovieNowPlaying(int id);

    @Query("SELECT count(*) FROM movies WHERE id=:id")
    public abstract int isMoviePresentCount(int id);

    @Transaction
    public void insertOrUpdateTransaction(List<MovieEntity> movieEntities, @MovieListType int movieType) {
        for (int i = 0; i < movieEntities.size(); i++) {
            MovieEntity movieEntity = movieEntities.get(i);
            int id = movieEntity.getId();
            switch (movieType) {
                case NOW_PLAYING:
                    if (isMoviePresentCount(id) == 0) {//not Present
                        movieEntity.setNowPlaying(true);
                        saveMovie(movieEntity);
                    } else {
                        //present
                        if (isMoviePopular(id)) {
                            updateIsNowPlayingMovie(
                                    movieEntity.getPosterPath(),
                                    movieEntity.isAdult(),
                                    movieEntity.getOverview(),
                                    movieEntity.getOriginalTitle(),
                                    movieEntity.getTitle(),
                                    movieEntity.getVoteCount(),
                                    movieEntity.getVoteAverage(),
                                    movieEntity.getBackdropPath(),
                                    movieEntity.getOriginalLanguage(),
                                    true,
                                    movieEntity.getReleaseDate(),
                                    id
                            );
                        } else {
                            movieEntity.setNowPlaying(true);
                            updateMovie(movieEntity);
                        }

                    }
                    break;
                case POPULAR_MOVIES:

                    if (isMoviePresentCount(id) == 0) {//not Present
                        movieEntity.setPopular(true);
                        saveMovie(movieEntity);
                    } else {
                        //present
                        if (isMovieNowPlaying(id)) {
                            updateIsPopularMovie(movieEntity.getPosterPath(),
                                    movieEntity.isAdult(),
                                    movieEntity.getOverview(),
                                    movieEntity.getOriginalTitle(),
                                    movieEntity.getTitle(),
                                    movieEntity.getVoteCount(),
                                    movieEntity.getVoteAverage(),
                                    movieEntity.getBackdropPath(),
                                    movieEntity.getOriginalLanguage(),
                                    true,
                                    movieEntity.getReleaseDate(),
                                    id);
                        } else {
                            movieEntity.setPopular(true);
                            updateMovie(movieEntity);
                        }
                    }
                    break;

            }
        }

    }

    @Retention(SOURCE)
    @IntDef({NOW_PLAYING, POPULAR_MOVIES})
    public @interface MovieListType {
    }

}
