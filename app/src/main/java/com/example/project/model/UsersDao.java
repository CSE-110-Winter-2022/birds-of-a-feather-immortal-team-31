package com.example.project.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UsersDao {
    @Transaction
    @Insert
    void insert(User user);

    @Transaction
    @Delete
    void delete(User user);

    @Transaction
    @Query("SELECT * FROM users")
    List<User> getAll();
}
