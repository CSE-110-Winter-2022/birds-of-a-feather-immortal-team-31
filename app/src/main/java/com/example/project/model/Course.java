package com.example.project.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "courses")
public class Course {

    @NonNull
    @ColumnInfo(name = "year")
    public int year;

    @NonNull
    @ColumnInfo(name = "quarter")
    public String quarter;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "course")
    public String subjectAndNumber;

    public Course(int year, String quarter, String subjectAndNumber){
        this.year = year;
        this.quarter = quarter;
        this.subjectAndNumber = subjectAndNumber;
    }

    public int getYear() {
        return year;
    }

    public String getQuarter() {
        return quarter;
    }

    public String getSubjectAndNumber(){
        return subjectAndNumber;
    }
}
