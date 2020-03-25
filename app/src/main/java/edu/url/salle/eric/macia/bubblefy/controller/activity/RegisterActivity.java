package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.url.salle.eric.macia.bubblefy.model.User;
import edu.url.salle.eric.macia.bubblefy.model.UserRegister;
import edu.url.salle.eric.macia.bubblefy.model.UserToken;
import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.utils.Session;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;
import edu.url.salle.eric.macia.bubblefy.restapi.manager.UserManager;

public class RegisterActivity extends AppCompatActivity
        implements UserCallback{

    private EditText etEmail;
    private EditText etLogin;
    private EditText etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = (EditText) findViewById(R.id.emailText);
        etLogin = (EditText) findViewById(R.id.usernameText);
        etPassword = (EditText) findViewById(R.id.passwordText);

        btnRegister = (Button) findViewById(R.id.proceedSignUpButton);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                Session.getInstance(getApplicationContext()).setUserRegister(
                        new UserRegister(email, login, password));
                UserManager.getInstance(getApplicationContext()).registerAttempt(email,
                        login, password, RegisterActivity.this);
            }
        });
    }

    private void doLogin(String username, String password){
        UserManager.getInstance(getApplicationContext())
                .loginAttempt(username, password, RegisterActivity.this);
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getApplicationContext())
                .setUserToken(userToken);
        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(Throwable throwable) {
        Session.getInstance(getApplicationContext())
                .setUserRegister(null);
    }

    @Override
    public void onRegisterSuccess() {
        UserRegister userData = Session.getInstance(getApplicationContext()).getUserRegister();
        doLogin(userData.getLogin(), userData.getPassword());
    }

    @Override
    public void onRegisterFailure(Throwable throwable) {
        Session.getInstance(getApplicationContext())
                .setUserRegister(null);
        Toast.makeText(getApplicationContext(), "Register failed " + throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
