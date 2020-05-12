package edu.url.salle.eric.macia.bubblefy.restapi.service;

import java.util.List;

import edu.url.salle.eric.macia.bubblefy.model.Follow;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.model.UserRegister;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("users/{login}")
    Call<User> getUserById(@Path("login") String login, @Header("Authorization") String token);

    @GET("users")
    Call<List<User>> getAllUsers(@Header("Authorization") String token);

    @POST("register")
    Call<ResponseBody> registerUser(@Body UserRegister user);

    @GET("users/{login}/follow")
    Call<Follow> checkUserFollowed(@Path("login") String login, @Header("Authorization") String token);

    @PUT("users/{login}/follow")
    Call<Follow> followUser(@Path("login") String login, @Header("Authorization") String token);

}
