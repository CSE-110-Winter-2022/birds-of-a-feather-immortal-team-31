package com.example.project.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity (tableName = "courses")
public class Course {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "year")
    public int year;

    @NonNull
    @ColumnInfo(name = "quarter")
    public String quarter;


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

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id && year == course.year && quarter.equals(course.quarter) && subjectAndNumber.equals(course.subjectAndNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, quarter, subjectAndNumber);
    }
}
