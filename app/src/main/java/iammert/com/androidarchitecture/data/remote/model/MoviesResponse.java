package iammert.com.androidarchitecture.data.remote.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import iammert.com.androidarchitecture.data.ResultPage;
import iammert.com.androidarchitecture.data.local.entity.MovieEntity;
import iammert.com.androidarchitecture.data.remote.ApiConstants;

/**
 * Created by mertsimsek on 19/05/2017.
 */

public class MoviesResponse extends ResultPage {
    private List<MovieEntity> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    @SerializedName("page")
    @Expose
    private int page;

    public List<MovieEntity> getResults() {
        return results;
    }

    public void setResults(List<MovieEntity> results) {
        this.results = results;
    }

    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
    private int totalResults;

    @Override
    public boolean takeUntil() {
        return getPage() == ApiConstants.PAGE_LIMIT;
    }
}
