package com.evn.room;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_UPDATE_USER = 1;

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
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xoá người dùng")
                        .setMessage("Bạn có chắc muốn xoá người dùng " + user.uid)
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser(user);
                            }

                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            }

            @Override
            public void onRequestUpdate(User user) {
                Intent intent = new Intent(MainActivity.this, UpdateUserActivity.class);
                intent.putExtra("USER_INFO", user);
                startActivityForResult(intent, REQUEST_UPDATE_USER);
                //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up );
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
                Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UPDATE_USER &&
                resultCode == Activity.RESULT_OK) {
            if (data != null) {
                boolean isUpdated = data.getBooleanExtra("IS_UPDATED", false);
                if (isUpdated) {
                    User user = ((User) data.getSerializableExtra("USER_INFO"));
                    updateUser(user);
                }
            }
        }
    }

    void updateUser(final User user) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                return db.userDao()
                        .updateUser(user.firstName, user.lastName, user.uid) > 0;
            }

            @Override
            protected void onPostExecute(Boolean isUpdateSuccess) {
                super.onPostExecute(isUpdateSuccess);

                if (isUpdateSuccess) {
                   userAdapter.updateUser(user);
                }
            }
        }.execute();
    }
}
