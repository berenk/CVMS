package com.example.emergencyalert;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String username = "";
           // final String savedCategory = PersonelLogInActivity.sharedPref.getString("category", "");
            username = intent.getStringExtra("usernameText");

            final DocumentReference docRef = FirebaseFirestore.getInstance().collection("Users").document(username);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    System.out.println();
                    if (e != null) {
                        Log.w("", "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        String category = snapshot.getData().get("category").toString();
                        Log.d("", "Current data: " + snapshot.getData());
                        String savedCategory = PersonelLogInActivity.sharedPref.getString("category", "");
                        if (!savedCategory.equals(category)) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(savedCategory);
                            PersonelLogInActivity.sharedPref.edit().putString("category", category).commit();
                            FirebaseMessaging.getInstance().subscribeToTopic(category);
                            return;
                        }
                    }else {
                        String a = PersonelLogInActivity.sharedPref.getString("category", "");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(a);
                        return;
                    }
                }
            });

        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        // STOP YOUR TASKS
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
