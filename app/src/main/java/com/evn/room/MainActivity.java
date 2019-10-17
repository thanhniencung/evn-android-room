package com.evn.room;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String DB_NAME = "evn.db";
    AppDatabase db;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DB_NAME).build();

        RecyclerView recyclerView = findViewById(R.id.listView);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        userAdapter = new UserAdapter();
        userAdapter.setUserActionListener(new UserAdapter.UserActionListener() {
            @Override
            public void onRequestDelete(final User user) {
                deleteUser(user);
            }
        });

        recyclerView.setAdapter(userAdapter);

        db.userDao().getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userAdapter.addUsers(users);
            }
        });
    }

    void deleteUser(final User user) {
        new AsyncTask<User, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(User... users) {
                db.userDao().delete(users[0]);
                return  null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                userAdapter.removeRowByUser(user);
            }
        }.execute(user);
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
