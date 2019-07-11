package com.example.emergencyalert;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    ImageView settings;
    List<Notifications> list =  new ArrayList<>();
    RecyclerView recyclerView;


    private boolean girisYaptiMi = false;

    RecyclerView.LayoutManager layoutManager;
    Adapter adapter;
     FirebaseFirestore db;

    final int REQUEST_CODE_SETTINGS = 200;

    static SharedPreferences sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        settings = findViewById(R.id.imageView2);

        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PersonelLogInActivity.class);
                startActivityForResult(i,REQUEST_CODE_SETTINGS);
            }
        });
        recyclerView = findViewById(R.id.notificationlist);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        db= FirebaseFirestore.getInstance();




        readData();
        fill_list();
        FirebaseMessaging.getInstance().subscribeToTopic("Ogrenciler");


    }


    public void  fill_list()  {
        adapter = new Adapter(this, list);
        recyclerView.setAdapter(adapter);

    }

    public void readData(){
        db.collection("Notifications")
                 .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        list.clear();
                        if (e != null) {
                            Log.w("", "Listen failed.", e);
                            return;
                        }
                        if(value.isEmpty()){
                            fill_list();
                            return;
                        }
                         for (QueryDocumentSnapshot doc : value) {
                             if (doc.getData().get("title") != null || doc.getData().get("body") != null) {
                                 if (girisYaptiMi){
                                     //eger giris yaptıysa personelin kategorisini al, db deki category ile kıyasla, eşleşiyorsa listeye ekle
                                     list.add(0,new Notifications(doc.getData().get("title").toString(),doc.getData().get("body").toString()));
                                 }else{
                                     //eğer giriş yapmadıysa sadece öğrenci kategorisindeki duyuruları göster.
                                     if (doc.getData().get("category").toString().equalsIgnoreCase("Ogrenci")){
                                         list.add(0,new Notifications(doc.getData().get("title").toString(),doc.getData().get("body").toString()));
                                     }
                                 }


                             }

                        }

                        fill_list();
                     }
                });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        System.out.println(lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");
        setLocale(language);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_SETTINGS) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
