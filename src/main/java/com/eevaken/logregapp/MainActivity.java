package com.eevaken.logregapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    Button regButton;
    EditText name;
    EditText surname;
    EditText email;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regButton = (Button) findViewById(R.id.regRegistrationButton);
        name = (EditText) findViewById(R.id.regEditTextName);
        surname =(EditText) findViewById(R.id.regEditTextSurname);
        email = (EditText) findViewById(R.id.regEditTextEmail);
        username = (EditText) findViewById(R.id.regEditTextUsername);
        password = (EditText) findViewById(R.id.regEditTextPassword);

        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    username.setText("ХУЙ");
                    name.setText("ХУЙ");
                    surname.setText("ПИЗДА");
            }
        };

        regButton.setOnClickListener(oclBtn);
    }
}