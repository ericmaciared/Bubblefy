package edu.url.salle.eric.macia.bubblefy.restapi.manager;

import android.content.Context;

import edu.url.salle.eric.macia.bubblefy.restapi.service.UserService;
import edu.url.salle.eric.macia.bubblefy.restapi.service.UserTokenService;
import retrofit2.Retrofit;

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

    public UserManager(Context context) {
    }

}
