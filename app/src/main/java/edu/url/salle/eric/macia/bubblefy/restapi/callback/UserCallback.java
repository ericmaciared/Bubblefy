package edu.url.salle.eric.macia.bubblefy.restapi.callback;

import edu.url.salle.eric.macia.bubblefy.Model.User;
import edu.url.salle.eric.macia.bubblefy.Model.UserToken;

public interface UserCallback extends FailureCallback{
    void onLoginSuccess(UserToken userToken);
    void onLoginFailure(Throwable throwable);
    void onRegisterSuccess();
    void onRegisterFailure(Throwable throwable);
    void onUserInfoReceiver(User userData);
}
