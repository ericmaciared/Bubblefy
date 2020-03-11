package edu.url.salle.eric.macia.bubblefy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
prova
public class LoginActivity extends AppCompatActivity {

    private final int INCORRECT_CREDENTIALS = 1;

    private Button loginButton;
    private Button signupButton;
    private TextView forgotPassword;
    private EditText usernameText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.LoginButton);
        signupButton = (Button) findViewById(R.id.SignUpButton);
        forgotPassword = (TextView) findViewById(R.id.forgotPasswordLink);
        usernameText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean login = checkLogin(String.valueOf(usernameText.getText()),
                        String.valueOf(passwordText.getText()));
                if (login){
                    loadHomePageActivity();
                }
                else{
                    toast(INCORRECT_CREDENTIALS);
                    resetButtonFields();
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSignUpPageActivity();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
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
        usernameText.setText("");
        passwordText.setText("");
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

}
