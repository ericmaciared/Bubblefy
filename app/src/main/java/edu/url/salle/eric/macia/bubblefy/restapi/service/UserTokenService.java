package edu.url.salle.eric.macia.bubblefy.restapi.service;

import edu.url.salle.eric.macia.bubblefy.model.UserLogin;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserTokenService {

    @POST("authenticate")
    Call<UserToken> loginUser(@Body UserLogin login);
}
