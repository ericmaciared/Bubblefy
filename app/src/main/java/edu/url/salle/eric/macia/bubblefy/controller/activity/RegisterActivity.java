package edu.url.salle.eric.macia.bubblefy.controller.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import edu.url.salle.eric.macia.bubblefy.model.Follow;
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
    private EditText etREmail;
    private EditText etLogin;
    private EditText etPassword;
    private EditText etRPassword;
    private CheckBox cbBox;
    private Button btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = (EditText) findViewById(R.id.emailText);
        etLogin = (EditText) findViewById(R.id.usernameText);
        etPassword = (EditText) findViewById(R.id.passwordText);
        etREmail = (EditText) findViewById(R.id.repeatEmailText);
        etRPassword = (EditText) findViewById(R.id.repeatPasswordText);
        cbBox = (CheckBox) findViewById(R.id.checkbox);

        btnRegister = (Button) findViewById(R.id.btnSignUp);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                String rPassword = etRPassword.getText().toString();
                String rEmail = etREmail.getText().toString();
                if (checkData(email, rEmail, password, rPassword)){
                    Session.getInstance(getApplicationContext()).setUserRegister(
                            new UserRegister(email, login, password));
                    UserManager.getInstance(getApplicationContext()).registerAttempt(email,
                            login, password, RegisterActivity.this);
                }
            }
        });
    }

    private boolean checkData(String email, String rEmail, String password, String rPassword) {
        boolean valid = true;
        if (!email.equals(rEmail)) {
            valid = false;
            Toast.makeText(getApplicationContext(), "Email doesn't match",
                    Toast.LENGTH_LONG).show();
        } else if (!password.equals(rPassword)){
            valid = false;
            Toast.makeText(getApplicationContext(), "Password doesn't match",
                    Toast.LENGTH_LONG).show();
        } else if (!cbBox.isChecked()){
            valid = false;
            Toast.makeText(getApplicationContext(), "Terms and Conditions not accepted",
                    Toast.LENGTH_LONG).show();
        }
        resetFields();
        return valid;
    }

    private void resetFields() {
        etEmail.setText("");
        etLogin.setText("");
        etPassword.setText("");
        etREmail.setText("");
        etRPassword.setText("");
        return;
    }

    private void doLogin(String username, String password){
        UserManager.getInstance(getApplicationContext())
                .loginAttempt(username, password, RegisterActivity.this);
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {
        Session.getInstance(getApplicationContext())
                .setUserToken(userToken);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
    public void onCheckFollowReceived(Follow follow) {

    }

    @Override
    public void onUserFollowed(Follow follow) {

    }


    @Override
    public void onFailure(Throwable throwable) {

    }
}
