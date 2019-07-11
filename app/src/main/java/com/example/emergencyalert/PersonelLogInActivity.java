package com.example.emergencyalert;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;


public class PersonelLogInActivity extends AppCompatActivity {

    TextView usernameText, passwordText;
    Button login;
    FirebaseFirestore db;
    static SharedPreferences sharedPref;
    ImageView changeLang ;



    CheckBox rememberMeCheckbox ;

    SharedPreferences prefs ;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_log_in);



        rememberMeCheckbox = findViewById(R.id.remember);
        usernameText = findViewById(R.id.txt_username);
        passwordText = findViewById(R.id.txt_password);
        login = findViewById(R.id.btn_login);
        db = FirebaseFirestore.getInstance();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        prefs = getSharedPreferences("RememberMe", Activity.MODE_PRIVATE);
        editor = prefs.edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin(usernameText.getText().toString(), passwordText.getText().toString());
            }
        });

        changeLang = findViewById(R.id.changeMyLang);
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

        checkSharedPreferences();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rememberMeCheckbox.isChecked()){
                    rememberMeSave("True",usernameText.getText().toString(),passwordText.getText().toString());
                }else{
                    rememberMeSave("False","","");
                }
                //Login işlemi

                //Login işlemi sonu




            }
        });

    }

    private void rememberMeSave(String ischecked, String name, String password){
        editor.putString("checkbox",ischecked);
        editor.commit();

        editor.putString("name",name);
        editor.commit();

        editor.putString("password",password);
        editor.commit();

    }

    private void showChangeLanguageDialog() {
        final String[] listItems= {"English","Türkçe"};
        AlertDialog.Builder mBuilder= new AlertDialog.Builder(PersonelLogInActivity.this);
        mBuilder.setTitle("Choose Language / Dil seçiniz");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i==0){
                    setLocale("en");
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }
                else if(i==1){
                    setLocale("tr");
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();


                }
                dialog.dismiss();

            }
        });
        AlertDialog mDialog=mBuilder.create();
        mDialog.show();

    }

    private void checkSharedPreferences(){

        String checkBox = prefs.getString("checkbox","False");
        String name = prefs.getString("name","");
        String password = prefs.getString("password","");

        usernameText.setText(name);
        passwordText.setText(password);

        if (checkBox.equals("True")){
            rememberMeCheckbox.setChecked(true);
        }
        else{
            rememberMeCheckbox.setChecked(false);
        }




    }


    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");
        setLocale(language);
    }

    public void onLogin(final String username, final String password){
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String category= document.get("category").toString();
                        if(sharedPref.getString("category","") == ""){
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("category", category);
                            editor.commit();
                        }
                        String savedCategory = sharedPref.getString("category", "");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("Ogrenciler");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(savedCategory);
                        FirebaseMessaging.getInstance().subscribeToTopic(category);
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        Intent j = new Intent(getApplicationContext(),MyService.class);
                        j.putExtra("usernameText",username);
                        startService(j);
                        startActivity(i);
                    }
                } else {
                    Log.d("", "get failed with ", task.getException());
                }
            }
        });
    }


}
