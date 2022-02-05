package com.example.project.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Transaction;

@Dao
public interface CoursesDao {
    @Transaction
    @Insert
    void insert(Course course);


}
