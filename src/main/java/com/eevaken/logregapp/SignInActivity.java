package com.eevaken.logregapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eevaken.logregapp.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    Button signInButton;
    Button regSignInButton;
    EditText email;
    EditText password;
    TextView errorMsg;
    String errorStr;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN =
            "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        signInButton = (Button) findViewById(R.id.signinButton);
        regSignInButton = (Button)findViewById(R.id.signinRegButton);
        View.OnClickListener oclR = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        regSignInButton.setOnClickListener(oclR);

        errorMsg = (TextView) findViewById(R.id.signinTextViewErrorMsg);
        View.OnClickListener oclBtn;
        oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = (EditText) findViewById(R.id.signinEditTextEmail);
                password = (EditText) findViewById(R.id.signinEditTextPassword);

                if (!validation(email.getText().toString(),
                        password.getText().toString()))
                    errorMsg.setText(errorStr);
                else{
                    errorMsg.setText("");

                    auth.signInWithEmailAndPassword(email.getText().toString(),
                            password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    startActivity(new Intent(SignInActivity.this, AccountActivity.class));
                                    return;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(findViewById(R.id.signin_element),"SignIn error. "+e.getMessage(),Snackbar.LENGTH_SHORT);
                            return;
                        }
                    });

                }

            }
        };
        signInButton.setOnClickListener(oclBtn);
    }

    private boolean validation(String emailStr, String passwordStr) {

        if (emailStr.equals("") || !emailStr.matches(EMAIL_PATTERN)) {
            errorStr = "Email is invalid";
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