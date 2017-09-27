package iammert.com.androidarchitecture.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ajinkyabadve on 26/9/17.
 */

public abstract class NetworkPageBoundResource<ResultType, RequestType extends ResultPage> {
    protected final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    protected int currentPage = getNextPageIndex() - 1;

    @MainThread
    NetworkPageBoundResource() {
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> result.setValue(Resource.success(newData)));
            }
        });
    }


    @MainThread
    private void saveResultAndReInit(List<RequestType> response) {
        if (response == null) {
            return;
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < response.size(); i++) {
                    saveCallResult(response.get(i));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                result.addSource(loadFromDb(), newData -> result.setValue(Resource.success(newData)));
            }
        }.execute();
    }

    @WorkerThread
    protected abstract void saveCallResult(@android.support.annotation.NonNull RequestType item);

    @MainThread
    protected boolean shouldFetch(@Nullable ResultType data) {
        return true;
    }

    @android.support.annotation.NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();


    @MainThread
    protected void onFetchFailed() {
    }

    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }

    protected abstract boolean takeUntil(RequestType movie);

    @MainThread
    protected abstract int getNextPageIndex();

    @MainThread
    protected abstract void saveNextPageIndex(int pageIndexToSave);

    protected abstract int getPageLimit();


    protected abstract Observable<RequestType> getPagedObservable(int nextPageToFetch);

    protected void fetchFromNetwork(LiveData<ResultType> dbSource) {
//        super.fetchFromNetwork(dbSource);
        result.addSource(dbSource, newData -> result.setValue(Resource.loading(newData)));
        createPagedCall(dbSource);

    }

    private void createPagedCall(LiveData<ResultType> dbSource) {

        Observable.range(1, 20)//for pagination
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                // Get each page in order.
                .concatMap(new Function<Integer, Observable<RequestType>>() {
                    @Override
                    public Observable<RequestType> apply(@NonNull Integer integer) throws Exception {
                        return getPagedObservable(integer).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

//                        return moviesAPI.getPopularMovieAtPage(integer).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

                    }
                }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                result.removeSource(dbSource);

            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                onFetchFailed();
                result.removeSource(dbSource);
                result.addSource(dbSource, newData -> result.setValue(Resource.error(throwable.getMessage(), newData)));
            }
        })
                // Take every result up to and including the one where the next page index is null.
//                .takeUntil(new Func1<Movie, Boolean>() {
//                    @Override
//                    public Boolean call(Movie personsList) {
//                        return personsList.getPage() == 15;
//                    }
//                })
                .takeUntil(new Predicate<RequestType>() {
                    @Override
                    public boolean test(@NonNull RequestType movie) throws Exception {
                        return takeUntil(movie);
                    }
                })
                // Add each output to a list builder. I'm using Guava's ImmutableList, but you could
                // just as easily use a regular ArrayList and avoid having to map afterwards. I just
                // personally prefer outputting an immutable data structure, and using the builder
                // is natural for this.
                //
                // Also, if you wanted to have the observable stream the full output at each page,
                // you could use collect instead of reduce. Note it has a slightly different syntax.
                .reduce(new ArrayList<RequestType>(), new BiFunction<ArrayList<RequestType>, RequestType, ArrayList<RequestType>>() {
                    @Override
                    public ArrayList<RequestType> apply(ArrayList<RequestType> resultTypes, RequestType resultType) throws Exception {
                        resultTypes.add(resultType);
                        return resultTypes;
                    }
                })
                // Convert list builder to one List<ResponseObject> of all the things.
                .map(new Function<ArrayList<RequestType>, CombineClass>() {
                    @Override
                    public CombineClass apply(@NonNull ArrayList<RequestType> movies) throws Exception {
                        return new CombineClass<RequestType>(movies);
                    }
                }).subscribe(new BiConsumer<CombineClass, Throwable>() {
            @Override
            public void accept(CombineClass testObject, Throwable throwable) throws Exception {
//                        Log.d(TAG, "accept() called with: testObject = [" + testObject + "], throwable = [" + throwable + "]");
                if (testObject == null) {
                    return;
                }
                saveResultAndReInit(testObject.getList());
//                onNextPageLoad(testObject);

            }
        });
    }

}
