package edu.url.salle.eric.macia.bubblefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import edu.url.salle.eric.macia.bubblefy.model.Follow;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.model.UserLogin;
import edu.url.salle.eric.macia.bubblefy.model.UserRegister;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.service.UserService;
import edu.url.salle.eric.macia.bubblefy.restapi.service.UserTokenService;
import edu.url.salle.eric.macia.bubblefy.utils.Constants;
import edu.url.salle.eric.macia.bubblefy.utils.Session;

public class UserManager {

    private static final String TAG = "UserManager";

    private static UserManager sUserManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private UserService mService;
    private UserTokenService mTokenService;

    public static UserManager getInstance(Context context) {
        if (sUserManager == null) {
            sUserManager = new UserManager(context);
        }
        return sUserManager;
    }

    private UserManager(Context cntxt) {
        mContext = cntxt;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(UserService.class);
        mTokenService = mRetrofit.create(UserTokenService.class);
    }


    /********************   LOGIN    ********************/
    public synchronized void loginAttempt (String username, String password,
                                           final UserCallback userCallback) {

        Call<UserToken> call = mTokenService.loginUser(new UserLogin(username, password,
                true));

        call.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                int code = response.code();
                UserToken userToken = response.body();

                if (response.isSuccessful()) {
                    userCallback.onLoginSuccess(userToken);
                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        userCallback.onLoginFailure(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                userCallback.onFailure(t);
            }
        });
    }

    /********************   USER INFO    ********************/
    public synchronized void getUserData (String login, final UserCallback userCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<User> call = mService.getUserById(login, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    userCallback.onUserInfoReceived(response.body());
                } else {
                    Log.d(TAG, "Error NOT SUCCESSFUL: " + response.body());
                    try {
                        userCallback.onFailure(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                userCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    /********************   REGISTRATION    ********************/
    public synchronized void registerAttempt (String email, String username, String password,
                                              final UserCallback userCallback) {

        Call<ResponseBody> call = mService.registerUser(new UserRegister(email, username, password));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    userCallback.onRegisterSuccess();
                } else {
                    try {
                        userCallback.onRegisterFailure(new Throwable("ERROR " + code + ", " +
                                response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                userCallback.onFailure(t);
            }
        });
    }

    /********************   FOLLOW / CHECK FOLLOW    ****************/
    public synchronized void checkUserFollowed(String login, final UserCallback userCallback){
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Follow> call = mService.checkUserFollowed(login, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Follow>() {
            @Override
            public void onResponse(Call<Follow> call, Response<Follow> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    userCallback.onCheckFollowReceived(response.body());
                } else {
                    Log.d(TAG, "Error NOT SUCCESSFUL: " + response.toString());
                    try {
                        userCallback.onFailure(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Follow> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                userCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));

            }
        });
    }

    /********************   FOLLOW / CHECK FOLLOW    ****************/
    public synchronized void followUser(String login, final UserCallback userCallback){
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<Follow> call = mService.followUser(login, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<Follow>() {
            @Override
            public void onResponse(Call<Follow> call, Response<Follow> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    userCallback.onUserFollowed(response.body());
                } else {
                    Log.d(TAG, "Error NOT SUCCESSFUL: " + response.toString());
                    try {
                        userCallback.onFailure(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Follow> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                userCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));

            }
        });
    }

    /********************   GETTERS / SETTERS    ********************/


}
