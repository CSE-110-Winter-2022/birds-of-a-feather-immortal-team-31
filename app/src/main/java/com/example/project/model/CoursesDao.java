package com.example.project.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CoursesDao {
    @Transaction
    @Insert
    void insert(Course course);

    @Transaction
    @Query("SELECT * FROM courses")
    List<Course> getAll();
}
