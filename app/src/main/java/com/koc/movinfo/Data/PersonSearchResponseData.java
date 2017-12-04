package com.koc.movinfo.Data;

/**
 * Created by MochammadIqbal on 14/11/2017.
 */

public class PersonSearchResponseData {
    private int page = 0;
    private int total_results = 0;
    private int total_pages = 0;
    private Integer[] actorIds;

    public PersonSearchResponseData(int page, int total_results, int total_pages, Integer[] actorIds) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.actorIds = actorIds;
    }

    public Integer[] getActorIds() {
        return actorIds;
    }
}
