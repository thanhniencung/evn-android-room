package com.evn.room;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @Override
    public boolean equals(@Nullable Object newUser) {
        if (uid == ((User)newUser).uid) {
            return true;
        }
        return super.equals(newUser);
    }
}