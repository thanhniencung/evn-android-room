package com.evn.room;

import androidx.appcompat.app.AppCompatActivity;
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

        getAllUsers();
    }

    void displayUsers() {
        new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... voids) {
                return db.userDao().getAll();
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
            }
        }.execute();
    }

    void getAllUsers() {
        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... indexs) {
                return loadUsersInBackground();
            }

            @Override
            protected void onPostExecute(Integer aVoid) {
                super.onPostExecute(aVoid);
                displayUsers();
            }
        }.execute();
    }

    int loadUsersInBackground() {
        for (int i=0; i<10; i++) {
            Random rand = new Random();

            User user = new User();
            user.uid = rand.nextInt(100000000);
            user.firstName = "Hung " + i;
            user.lastName = "Nguyen";

            db.userDao().insertAll(user);
        }
        return 1;
    }
}
