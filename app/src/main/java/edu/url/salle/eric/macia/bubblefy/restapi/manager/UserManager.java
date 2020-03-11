package edu.url.salle.eric.macia.bubblefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import edu.url.salle.eric.macia.bubblefy.Model.UserLogin;
import edu.url.salle.eric.macia.bubblefy.Model.UserToken;
import edu.url.salle.eric.macia.bubblefy.Utils.Constants;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.service.UserService;
import edu.url.salle.eric.macia.bubblefy.restapi.service.UserTokenService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserManager {

    private static final String TAG = "UserManager";

    private static UserManager sUserManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private UserService mService;
    private UserTokenService mTokenService;

    public static UserManager getInstance(Context context){
        if (sUserManager == null){
            sUserManager = new UserManager(context);
        }
        return sUserManager;
    }

    public UserManager(Context cntxt) {
        mContext = cntxt;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(UserService.class);
        mTokenService = mRetrofit.create(UserTokenService.class);
    }

    /***************************   LOGIN   ***************************/
    public synchronized void loginAttempt (String username, String password,
                                           final UserCallback userCallback){
        Call<UserToken> call = mTokenService.loginUser(new UserLogin(username, password,
                true));

        call.enqueue(new Callback<UserToken>() {
            //Case where server provides response
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                int code = response.code(); //HTTP status code
                UserToken userToken = response.body(); //HTTP status explanation

                if (response.isSuccessful()){
                    //We get a token for the user
                    userCallback.onLoginSuccess(userToken);
                } else {
                    Log.d(TAG, "Error: " + code);
                    try{
                        userCallback.onLoginFailure(new Throwable("ERROR "+ code + ", " +
                                response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Case where server is not responding
            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                userCallback.onFailure(t);
            }
        });
    }

}
