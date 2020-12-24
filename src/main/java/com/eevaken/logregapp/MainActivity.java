package com.eevaken.logregapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;


import java.util.regex.Pattern;

public class MainActivity extends Activity {
    Button regButton;
    Button regSignInButton;
    String name;
    String surname;
    String email;
    String username;
    String password;
    TextView errorMsg;
    String errorStr;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN =
            "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";

    DBHelper dbHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //подключаем  дб хелпер
        dbHelper = new DBHelper(this);
        //получаем бд
        final SQLiteDatabase database = dbHelper.getWritableDatabase();


        // кнопка зарегистрироваться
        regButton = (Button) findViewById(R.id.regRegistrationButton);
        // кнопка перехода между активити регистрации и входа
        regSignInButton = (Button) findViewById(R.id.regSigninButton);
        // текстовое поля вывода ошибок валидации
        errorMsg = (TextView) findViewById(R.id.regTextViewErrorMsg);

        // переменная для перехода к др активити
        final Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        // событие перехода к активити входа
        regSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });


        // событие нажптия на кнопку зарегистрироваться
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //берем данные из полей ввода
                EditText nameET = findViewById(R.id.regEditTextName);
                EditText surnameET = findViewById(R.id.regEditTextSurname);
                EditText emailET = findViewById(R.id.regEditTextEmail);
                EditText usernameET = findViewById(R.id.regEditTextUsername);
                EditText passwordET = findViewById(R.id.regEditTextPassword);

                name = nameET.getText().toString();
                surname = surnameET.getText().toString();
                email = emailET.getText().toString();
                username = usernameET.getText().toString();
                password = passwordET.getText().toString();


                // валидация
                if (!validation(name, surname, email, username, password))
                    errorMsg.setText(errorStr);
                else {
                    //валидация пройдена!!!
                    // ошибка чистится
                    errorMsg.setText("");

                    //проверка на такое же мыло в бд
                    if (!emailRepeat(database, email)) {
                        registration(database, name, surname, email, username, password);
                        Snackbar.make(v, "Success", Snackbar.LENGTH_SHORT).show();
                    } else
                        errorMsg.setText("Email is used");

                }

            }
        });
    }


    // занесение пользователя в бд
    private void registration(SQLiteDatabase database, String name, String surname, String email, String username, String password) {
        // объект для добавления новых строк в таблицу
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_SURNAME, surname);
        contentValues.put(DBHelper.KEY_EMAIL, email);
        contentValues.put(DBHelper.KEY_USERNAME, username);
        contentValues.put(DBHelper.KEY_PASSWORD, password);

        //добавляем в бд запись о пользователе
        database.insert(DBHelper.TABLE_USERS, null, contentValues);
    }

    // проверка на повтор мыла
    private boolean emailRepeat(SQLiteDatabase database, String email) {
        //чтение всех записей c такимже мыылом
        Cursor cursor = database.query(DBHelper.TABLE_USERS, null, "email = ?" , new String[]{email}, null, null, null);
        //если такие имеются возвращаем 1
        return cursor.moveToFirst();
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
            errorStr = "Empty username";
            return false;
        }
        System.out.println(passwordStr);
        if (passwordStr.equals("") || !passwordStr.matches(PASSWORD_PATTERN)) {
            errorStr = "Password is invalid. Check:\n" +
                    "  At least 8 characters\n" +
                    "Contains at least one digit\n" +
                    "Contains at least one lower alpha char \n" +
                    "and one upper alpha char";
            return false;
        }
        return true;
    }
}