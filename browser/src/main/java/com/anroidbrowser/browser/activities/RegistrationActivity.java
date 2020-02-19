package com.anroidbrowser.browser.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.myTypes.Person;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.dataBase.DBHelper;

/**
 * Created by root on 12.09.17.
 */
public class RegistrationActivity extends Activity implements View.OnClickListener {

    private Button addButton, avatarButton;
    private EditText etName, etFamily, etAge, etEmail, etPass, etPass2, etCountry, etCity;
    private RadioGroup radioGroup;
    private ImageView imageView;
    private String strAvatar = "";
    private DBHelper dbHelper;

    private static int RESULT_LOAD_IMAGE = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        addButton = findViewById(R.id.btnAdd);
        addButton.setOnClickListener(this);
        imageView = findViewById(R.id.imageView);
        avatarButton = findViewById(R.id.btnAvatarReg);
        avatarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });

        etName = findViewById(R.id.etName);
        etPass = findViewById(R.id.pass1);
        etPass2 = findViewById(R.id.pass2);
        etEmail = findViewById(R.id.etEmail);
        etCountry = findViewById(R.id.country);
        etCity = findViewById(R.id.city);
        etFamily = findViewById(R.id.family);
        etAge = findViewById(R.id.age);
        radioGroup = findViewById(R.id.sexGroup);


        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            strAvatar = cursor.getString(columnIndex);
            Bitmap bitmap = BitmapFactory.decodeFile(strAvatar);
            imageView.setImageBitmap(bitmap);
            cursor.close();


        }


    }


    @Override
    public void onClick(View v) {

        // создаем объект для данных


        // получаем данные из полей ввода
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String pass1 = etPass.getText().toString();
        String pass2 = etPass2.getText().toString();
        String country = etCountry.getText().toString();
        String city = etCity.getText().toString();
        String family = etFamily.getText().toString();
        String age = etAge.getText().toString();
        String sex = (radioGroup.getCheckedRadioButtonId() == R.id.male) ? "M" : "F";

        if (email.equals("") || pass1.equals("") || pass2.equals("") || name.equals("") || country.equals("") || city.equals("") || family.equals("") || age.equals("") || strAvatar.equals("")) {
            Toast.makeText(this, R.string.emptyErr, Toast.LENGTH_LONG).show();
            return;
        }

        if (!pass1.equals(pass2)) {
            Toast.makeText(this, R.string.passErr, Toast.LENGTH_LONG).show();
            return;
        }

        if (dbHelper.findMail(email)) {
            Toast.makeText(this, R.string.mailErr, Toast.LENGTH_LONG).show();
            return;
        }


        // подключаемся к БД


        switch (v.getId()) {
            case R.id.btnAdd:
                // подготовим данные для вставки в виде пар: наименование столбца - значение
                long rowId = dbHelper.registration(name, email, pass1, country, city, age, family, sex, strAvatar);
                Toast.makeText(this, R.string.success, Toast.LENGTH_LONG).show();
                MainApplication.getInstance().authorization(new Person(new String[]{name, family, age, sex, email, pass1, country, city, strAvatar}, rowId, null, null, null));
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);


                break;
        }
        // закрываем подключение к БД
        dbHelper.close();
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (this.shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("To download a file it is necessary to allow required permission");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            RegistrationActivity.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    RegistrationActivity.this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


}
