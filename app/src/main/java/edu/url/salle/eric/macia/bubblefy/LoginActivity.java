package edu.url.salle.eric.macia.bubblefy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadHomePageActivity();
            }
        });

        signupButton = (Button) findViewById(R.id.SignUpButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSignUpPageActivity();
            }
        });

        forgotPassword = (TextView) findViewById(R.id.forgotPasswordLink);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadForgotPasswordActivity();
            }
        });
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

}
