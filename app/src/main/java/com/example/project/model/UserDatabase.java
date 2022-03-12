package com.example.project.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Store all the students that I marked as favorite
@Database(entities = {User.class}, version = 4, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase singletonInstance;

    public static UserDatabase singleton(Context context){
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, UserDatabase.class, "users.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return singletonInstance;
    }

    public abstract UsersDao usersDao();
}

