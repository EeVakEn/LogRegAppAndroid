package com.eevaken.logregapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends Activity {
    Button regButton;
    EditText name;
    EditText surname;
    EditText email;
    EditText username;
    EditText password;
    TextView errorMsg;
    String errorStr;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN =
            "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regButton = (Button) findViewById(R.id.regRegistrationButton);

        errorMsg = (TextView) findViewById(R.id.regTextViewErrorMsg);
        View.OnClickListener oclBtn;
        oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = (EditText) findViewById(R.id.regEditTextName);
                surname = (EditText) findViewById(R.id.regEditTextSurname);
                email = (EditText) findViewById(R.id.regEditTextEmail);
                username = (EditText) findViewById(R.id.regEditTextUsername);
                password = (EditText) findViewById(R.id.regEditTextPassword);

                if (!validation(name.getText().toString(), surname.getText().toString(),
                        email.getText().toString(), username.getText().toString(),
                        password.getText().toString()))
                    errorMsg.setText(errorStr);
                else
                    errorMsg.setText("");
            }
        };

        regButton.setOnClickListener(oclBtn);
    }

    private boolean validation(String nameStr, String surnameStr,
                              String emailStr, String usernameSrt, String passwordStr) {
        if (nameStr.equals("")) {
            errorStr = "Empty name";
            return false;
        }
        if (surnameStr.equals("")) {
            errorStr = "Empty surname";
            return false;
        }
        if (emailStr.equals("") || !emailStr.matches(EMAIL_PATTERN)) {
            errorStr = "Email is invalid";
            return false;
        }
        if (usernameSrt.equals("")) {
            errorStr ="Empty username";
            return false;
        }
        System.out.println(passwordStr);
        if (passwordStr.equals("") || !passwordStr.matches(PASSWORD_PATTERN)) {
            errorStr = "Password is invalid. Check:\n" +
                    " At least 8 characters\n" +
                    "Contains at least one digit\n" +
                    "Contains at least one lower alpha char \n" +
                    "and one upper alpha char";
            return false;
        }
        return true;
    }
}