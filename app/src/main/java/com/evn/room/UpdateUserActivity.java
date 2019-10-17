package com.evn.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateUserActivity extends AppCompatActivity {
    TextView tvUserId;
    EditText editFirstNamel;
    EditText editLastName;
    Toolbar toolbar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        tvUserId = findViewById(R.id.userId);
        editFirstNamel = findViewById(R.id.firstName);
        editLastName = findViewById(R.id.lastName);
        toolbar = findViewById(R.id.toolbar);

        user = (User) getIntent().getSerializableExtra("USER_INFO");

        toolbar.setTitle("#" + user.uid);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setTitle(user.uid + "");

        if (user != null) {
            tvUserId.setText("#" + user.uid);
            editFirstNamel.setText(user.firstName);
            editLastName.setText(user.lastName);
        }

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editFirstNamel.getText().toString();
                String lastName = editLastName.getText().toString();

                if (firstName.isEmpty() || lastName.isEmpty()) {
                    return;
                }

                User newUser = new User();
                newUser.uid = user.uid;
                newUser.firstName = firstName;
                newUser.lastName = lastName;

                Intent intent = new Intent();
                intent.putExtra("USER_INFO", newUser);
                intent.putExtra("IS_UPDATED", isUserUpdated(firstName, lastName));

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    boolean isUserUpdated(String currentFistName, String currentLastName) {
        if (!user.firstName.equals(currentFistName) ||
            !user.lastName.equals(currentLastName)) {
            return true;
        }

        return false;
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_out_up,R.anim.slide_in_up);
    }
}
