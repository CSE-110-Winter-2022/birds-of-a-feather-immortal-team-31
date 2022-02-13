package com.example.project.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface PersonDao {
    @Transaction
    @Insert
    void insert(Person person);

    @Transaction
    @Query("SELECT * FROM courses")
    Person get();
}
