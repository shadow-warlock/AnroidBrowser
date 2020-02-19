package com.anroidbrowser.browser.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.myTypes.Person;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.dataBase.DBHelper;

/**
 * Created by root on 12.09.17.
 */
public class InputActivity extends Activity implements View.OnClickListener {

    private Button addButton, regButton;
    private EditText etEmail, etPass;

    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        addButton = findViewById(R.id.btnInput);
        addButton.setOnClickListener(this);
        regButton = findViewById(R.id.btnRegInput);
        regButton.setOnClickListener(this);
        etPass = findViewById(R.id.passInput);
        etEmail = findViewById(R.id.etEmailInput);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInput:
                String email = etEmail.getText().toString();
                String pass1 = etPass.getText().toString();
                Person p = dbHelper.checkInput(email, pass1);
                if(p != null){
                    Toast.makeText(this, R.string.success, Toast.LENGTH_LONG).show();
                    MainApplication.getInstance().authorization(p);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, R.string.incorrect_login_or_pass, Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btnRegInput:
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }


}
