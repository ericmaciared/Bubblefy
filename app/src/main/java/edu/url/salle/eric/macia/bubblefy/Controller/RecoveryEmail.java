package edu.url.salle.eric.macia.bubblefy.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.url.salle.eric.macia.bubblefy.R;

public class RecoveryEmail extends AppCompatActivity {

    private Button sendKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_email);

        sendKey = (Button) findViewById(R.id.sendKeyButton);
        sendKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecoverKeyActivity();
            }
        });
    }

    protected void loadRecoverKeyActivity(){
        Intent intent = new Intent(this, RecoverKey.class);
        startActivity(intent);
    }
}
