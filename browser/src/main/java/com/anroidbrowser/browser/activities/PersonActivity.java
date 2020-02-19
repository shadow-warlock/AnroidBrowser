package com.anroidbrowser.browser.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.support.NotificationAppearanceSettings;
import com.anroidbrowser.browser.myTypes.Person;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.dataBase.DBHelper;

import java.util.Locale;

/**
 * Created by root on 12.09.17.
 */
public class PersonActivity extends Activity implements View.OnClickListener {

    private Button editButton, exitButton, settingButton, bestButton, historyButton, downloadButton, avatarButton;
    private EditText etName, etFamily, etAge, etEmail, etPass2, etCountry, etCity;
    private ImageView imageView;
    private String strAvatar = "";
    private TextView etPass;
    private RadioGroup radioGroup;

    Person p;

    private static int RESULT_LOAD_IMAGE = 1;

    DBHelper dbHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        p = MainApplication.getInstance().getPerson();
        editButton = findViewById(R.id.btnAdd);
        editButton.setOnClickListener(this);

        imageView = findViewById(R.id.img);
        avatarButton = findViewById(R.id.btnAvatar);
        avatarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        exitButton = findViewById(R.id.btnExit);
        exitButton.setOnClickListener(this);
        settingButton = findViewById(R.id.btnSettings);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsMenu();
            }
        });

        bestButton = findViewById(R.id.btnBest);
        bestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, BestActivity.class);
                startActivity(intent);
            }
        });

        historyButton = findViewById(R.id.btnHistory);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        downloadButton = findViewById(R.id.btnLoadingHistory);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonActivity.this, DownloadActivity.class);
                startActivity(intent);
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

        etEmail.setText(p.getMail());
        etPass.setText(p.getPass());
        etName.setText(p.getName());
        etCountry.setText(p.getCountry());
        etCity.setText(p.getCity());
        etFamily.setText(p.getFamily());
        etAge.setText(p.getAge());
        strAvatar = p.getAvatar();
        imageView.setImageBitmap(BitmapFactory.decodeFile(strAvatar));
        if (p.getSex().equals("M"))
            radioGroup.check(R.id.male);
        else
            radioGroup.check(R.id.female);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_find) {
            showDialog(0);
            return true;
        }
        if (id == R.id.action_lang) {
            showDialog(1);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (id == 0) {
            builder.setTitle(R.string.select_default_search_engine)
                    .setItems(new String[]{"google", "yandex", "mail"}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationAppearanceSettings.GetCurrent().setFindEngine(which);
                        }
                    });
        }
        if (id == 1) {
            builder.setTitle(R.string.select_language)
                    .setItems(new String[]{"en", "ru"}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationAppearanceSettings.GetCurrent().setLanguage(which);
                            String locale = which == 0 ? "en" : "ru";
                            Resources res = getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            android.content.res.Configuration conf = res.getConfiguration();
                            conf.locale = new Locale(locale.toLowerCase());
                            res.updateConfiguration(conf, dm);
                            recreate();
                        }
                    });
        }

        return builder.create();
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnExit) {
            MainApplication.getInstance().exit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return;
        }

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

        // подключаемся к БД


        switch (v.getId()) {
            case R.id.btnAdd:
                // подготовим данные для вставки в виде пар: наименование столбца - значение
                long rowID = dbHelper.update(name, email, pass1, country, city, age, family, sex, strAvatar, p.getId());
                Toast.makeText(this, R.string.success, Toast.LENGTH_LONG).show();
                MainApplication.getInstance().authorization(new Person(new String[]{name, family, age, sex, email, pass1, country, city, strAvatar}, rowID, p.getHistoryElementList(), p.getBestElementList(), p.getDownloadsList()));
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;
        }
        // закрываем подключение к БД
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
            imageView.setImageBitmap(BitmapFactory.decodeFile(strAvatar));
            cursor.close();


        }


    }


}
