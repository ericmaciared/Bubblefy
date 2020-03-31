package edu.url.salle.eric.macia.bubblefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.util.List;

import edu.url.salle.eric.macia.bubblefy.model.Search;
import edu.url.salle.eric.macia.bubblefy.model.Track;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.SearchCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.TrackCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.service.SearchService;
import edu.url.salle.eric.macia.bubblefy.restapi.service.TrackService;
import edu.url.salle.eric.macia.bubblefy.utils.Constants;
import edu.url.salle.eric.macia.bubblefy.utils.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchManager {
    private static final String TAG = "SearchManager";
    private Context mContext;
    private static SearchManager sSearchManager;
    private Retrofit mRetrofit;
    private SearchService mSearchService;


    public static SearchManager getInstance (Context context) {
        if (sSearchManager == null) {
            sSearchManager = new SearchManager(context);
        }

        return sSearchManager;
    }

    public SearchManager(Context context) {
        mContext = context;

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mSearchService = mRetrofit.create(SearchService.class);
    }

    public synchronized void getSearchResult(String keyword, final SearchCallback searchCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Search> call = mSearchService.getSearchResult(keyword, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    searchCallback.onSearchReceived(response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    searchCallback.onNoSearch(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                searchCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }
}
