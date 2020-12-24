package com.eevaken.logregapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class AccountActivity extends AppCompatActivity {
    EditText inputPass;
    EditText inputName;
    EditText inputSurname;
    String currentId;
    DBHelper dbHelper;
    Button changePassBtn;
    Button changeNameBtn;
    Button changeSurnameBtn;
    Button delAccBtn;
    TextView userNameTV;
    TextView nameTV;
    TextView surnameTV;
    Button checkCoursesBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        final Bundle b = getIntent().getExtras();
        currentId = b.getString("id");
        dbHelper = new DBHelper(this);

        dataOut();

        checkCoursesBtn = (Button) findViewById(R.id.checkCoursesButton);

        checkCoursesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, CourseActivity.class));
            }
        });




        //смена пароля
        changePassBtn = (Button) findViewById(R.id.changePasswordButton);
        inputPass = new EditText(this);

        AlertDialog.Builder changePassAlert = new AlertDialog.Builder(this)
                .setTitle("Change password")
                .setMessage("Input new password: ")
                .setView(inputPass)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPass = inputPass.getText().toString();
                        dbHelper.changePass(currentId, newPass);
                        Toast.makeText(AccountActivity.this, "Password changed", Toast.LENGTH_SHORT).show();

                    }
                });

        final AlertDialog adPass = changePassAlert.create();
        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adPass.show();
            }
        });

        //cмена имени

        changeNameBtn = (Button) findViewById(R.id.editNameButton);
        inputName = new EditText(this);

        AlertDialog.Builder changeNameAlert = new AlertDialog.Builder(this)
                .setTitle("Change name")
                .setMessage("Input new name: ")
                .setView(inputName)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = inputName.getText().toString();
                        dbHelper.changeName(currentId, newName);
                        Toast.makeText(AccountActivity.this, "Name changed", Toast.LENGTH_SHORT).show();
                        dataOut();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog adName = changeNameAlert.create();

        changeNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adName.show();
            }
        });

        //смена фамилии

        changeSurnameBtn = (Button) findViewById(R.id.editSurnameButton);
        inputSurname = new EditText(this);

        AlertDialog.Builder changeSurnameAlert = new AlertDialog.Builder(this)
                .setTitle("Change surname")
                .setMessage("Input new surname: ")
                .setView(inputSurname)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newSurname = inputSurname.getText().toString();
                        dbHelper.changeSurname(currentId, newSurname);
                        Toast.makeText(AccountActivity.this, "Surname changed", Toast.LENGTH_SHORT).show();
                        dataOut();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog adSurname = changeSurnameAlert.create();
        changeSurnameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adSurname.show();
            }
        });

        // удаление аккаунта

        delAccBtn = (Button) findViewById(R.id.deleteButton);
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete account")
                .setMessage("Are u sure?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes, delete acc", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.delAcc(currentId);
                        startActivity(new Intent(AccountActivity.this, MainActivity.class));
                    }
                });

        final AlertDialog adDel = deleteBuilder.create();

        delAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adDel.show();
            }
        });
    }

    public void dataOut() {
        Cursor cursor = dbHelper.getAccountData(currentId);

        userNameTV = findViewById(R.id.accTextViewUsername);
        nameTV = findViewById(R.id.accTextViewName);
        surnameTV = findViewById(R.id.accTextViewSurname);

        cursor.moveToNext();
        userNameTV.setText("@" + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_USERNAME)));
        nameTV.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)));
        surnameTV.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SURNAME)));
    }



}