package com.example.project.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//Store all the students that I marked as favorite
@Database(entities = {User.class}, version = 4, exportSchema = false)
public abstract class FavoriteUserDatabase extends RoomDatabase {
    private static FavoriteUserDatabase singletonInstance;

    public static FavoriteUserDatabase singleton(Context context){
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, FavoriteUserDatabase.class, "favorite_users.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return singletonInstance;
    }



    public abstract UsersDao usersDao();
}
