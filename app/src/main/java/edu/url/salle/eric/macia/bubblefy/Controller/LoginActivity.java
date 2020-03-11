package edu.url.salle.eric.macia.bubblefy.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.url.salle.eric.macia.bubblefy.Model.User;
import edu.url.salle.eric.macia.bubblefy.Model.UserToken;
import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.restapi.callback.UserCallback;

public class LoginActivity extends AppCompatActivity implements UserCallback {

    private final int INCORRECT_CREDENTIALS = 1;

    private Button btnLogin;
    private Button btnSignUp;
    private TextView tvForgotPassword;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.LoginButton);
        btnSignUp = (Button) findViewById(R.id.SignUpButton);
        tvForgotPassword = (TextView) findViewById(R.id.forgotPasswordLink);
        etUsername = (EditText) findViewById(R.id.usernameText);
        etPassword = (EditText) findViewById(R.id.passwordText);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean login = checkLogin(String.valueOf(etUsername.getText()),
                        String.valueOf(etPassword.getText()));
                if (login){
                    loadHomePageActivity();
                }
                else{
                    toast(INCORRECT_CREDENTIALS);
                    resetButtonFields();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSignUpPageActivity();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadForgotPasswordActivity();
            }
        });
    }

    private boolean checkLogin(String username, String password){
        boolean correct = false;
        //To be implemented with API
        return correct;
    }

    private void resetButtonFields(){
        etUsername.setText("");
        etPassword.setText("");
    }

    protected void loadHomePageActivity(){
        /*Intent intent = new Intent(this, null.class);
        startActivity(intent);*/
    }

    protected void loadSignUpPageActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    protected void loadForgotPasswordActivity(){
        Intent intent = new Intent(this, RecoveryEmail.class);
        startActivity(intent);
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

    @Override
    public void onLoginSuccess(UserToken userToken) {

    }

    @Override
    public void onLoginFailure(Throwable throwable) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceiver(User userData) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
