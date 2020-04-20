package edu.url.salle.eric.macia.bubblefy.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.UserManager;
import edu.url.salle.eric.macia.bubblefy.utils.PreferenceUtils;
import edu.url.salle.eric.macia.bubblefy.utils.Session;

public class LoginActivity extends AppCompatActivity implements UserCallback {

    private final int INCORRECT_CREDENTIALS = 1;

    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        checkSavedData();
    }

    //VIEW
    private void initViews() {
        btnLogin = (Button) findViewById(R.id.LoginButton);
        etUsername = (EditText) findViewById(R.id.usernameText);
        etPassword = (EditText) findViewById(R.id.passwordText);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(String.valueOf(etUsername.getText()),
                        String.valueOf(etPassword.getText()));
            }
        });
    }

    private void resetButtonFields(){
        etUsername.setText("");
        etPassword.setText("");
    }

    private void toast(int toastCode){
        String message = "Error";

        if (toastCode == INCORRECT_CREDENTIALS){
            message = "Incorrect credentials";
        }

        Toast toast = null;
        toast =  Toast.makeText(LoginActivity.this,
                message,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    //LOCAL CREDENTIALS
    private void checkSavedData(){
        if (checkExistingPreferences()) {
            etUsername.setText(PreferenceUtils.getUser(this));
            etPassword.setText(PreferenceUtils.getPassword(this));
        }
    }

    private boolean checkExistingPreferences() {
        return PreferenceUtils.getUser(this) != null
                && PreferenceUtils.getPassword(this) != null;
    }

    //USER MANAGER
    private void doLogin(String username, String password){
        UserManager.getInstance(getApplicationContext())
                .loginAttempt(username, password, LoginActivity.this);
    }

    //USER CALLBACK
    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getApplicationContext())
                .setUserToken(userToken);
        Intent intent = new Intent(this, MainActivity.class);
        PreferenceUtils.saveUser(this, etUsername.getText().toString());
        PreferenceUtils.savePassword(this, etPassword.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        toast(INCORRECT_CREDENTIALS);
        resetButtonFields();
    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}

