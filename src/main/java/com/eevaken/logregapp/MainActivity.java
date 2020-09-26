package com.eevaken.logregapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import java.util.regex.Pattern;

public class MainActivity extends Activity {
    Button regButton;
    Button regSignInButton;
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

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        regButton = (Button) findViewById(R.id.regRegistrationButton);
        regSignInButton = (Button)findViewById(R.id.regSigninButton);
        errorMsg = (TextView) findViewById(R.id.regTextViewErrorMsg);
        final Intent intent = new Intent(MainActivity.this, SignInActivity.class);

        View.OnClickListener oclSI = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        };
        regSignInButton.setOnClickListener(oclSI);

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
                else{
                    errorMsg.setText("");




                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    User user = new User();
                                    user.setName(name.getText().toString());
                                    user.setSurname(surname.getText().toString());
                                    user.setEmail(email.getText().toString());
                                    user.setUsername(username.getText().toString());
                                    user.setPassword(password.getText().toString());

                                    users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Snackbar.make(findViewById(R.id.reg_element),"Success", Snackbar.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                    return;
                                                }
                                            });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(findViewById(R.id.reg_element),"Fail. " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            return;
                        }
                    });

                }

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