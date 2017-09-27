package iammert.com.androidarchitecture.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import iammert.com.androidarchitecture.data.local.dao.MovieDao;
import iammert.com.androidarchitecture.data.local.entity.MovieEntity;

/**
 * Created by mertsimsek on 19/05/2017.
 */
@Database(entities = {MovieEntity.class}, version = 4, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();
}
