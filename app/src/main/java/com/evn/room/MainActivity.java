package com.evn.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String DB_NAME = "evn.db";
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DB_NAME).build();

        db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                String a = "";
            }
        });
    }

    void demoInsert() {
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... indexs) {
                for (int i=0; i<10; i++) {
                    Random rand = new Random();

                    User user = new User();
                    user.uid = rand.nextInt(1000000);
                    user.firstName = "Hoa " + i;
                    user.lastName = "Nguyen";

                    db.userDao().insertAll(user);
                }
                return 1;
            }

            @Override
            protected void onPostExecute(Integer aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

}
