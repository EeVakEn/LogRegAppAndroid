package com.eevaken.logregapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;


public class SignInActivity extends AppCompatActivity {
    Button signInButton;
    Button regSignInButton;
    String email;
    String password;
    TextView errorMsg;
    String errorStr;
    DBHelper dbHelper;


    // регекс для пароля
    private static final String PASSWORD_PATTERN =
            "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        //создаем дбхелпер
        dbHelper = new DBHelper(this);
        //получаем бд
        final SQLiteDatabase database = dbHelper.getWritableDatabase();


        //кнопка войти
        signInButton = (Button) findViewById(R.id.signinButton);
        //кнопка перхода на активити регмстрации
        regSignInButton = (Button)findViewById(R.id.signinRegButton);


        // событие перехода на другое активити
        regSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        errorMsg = (TextView) findViewById(R.id.signinTextViewErrorMsg);

        //событие входа в аккаунт
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //получаем поля ввода
                EditText emailET = (EditText) findViewById(R.id.signinEditTextEmail);
                EditText passwordET = (EditText) findViewById(R.id.signinEditTextPassword);

                //получаем значения этих полей
                email =emailET.getText().toString();
                password = passwordET.getText().toString();

                //валидация
                if (!validation(email,password))
                    errorMsg.setText(errorStr);
                else{
                    errorMsg.setText("");
                    //проверка соответствия мыла паролю
                    if (authorization(database, email, password)){
                        Intent intent = new Intent(SignInActivity.this, AccountActivity.class);
                        startActivity(intent);
                    }else {
                        Snackbar.make(v, "Fail", Snackbar.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    // авторизация
    private boolean authorization(SQLiteDatabase database, String email, String password){
        //чтение всех записей c такимже мыылом и паролем
        Cursor cursor = database.query(DBHelper.TABLE_USERS, null, "email = ? or username = ? AND password = ?" , new String[]{email, email,password}, null, null, null);
        //если такая имеется переходим на успешное активити
        return cursor.moveToFirst();
    }


    //валидациия
    private boolean validation(String emailStr, String passwordStr) {

        if (emailStr.equals("") ) {
            errorStr = "Email or Username is empty";
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