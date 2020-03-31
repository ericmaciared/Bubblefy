package edu.url.salle.eric.macia.bubblefy.restapi.service;

import java.util.List;

import edu.url.salle.eric.macia.bubblefy.model.Search;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchService {

    @GET("search")
    Call<Search> getSearchResult(@Query("keyword") String keyword, @Header("Authorization") String token);
}
