package com.example.project.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity (tableName = "courses")
public class Course implements Serializable {

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

    @NonNull
    @ColumnInfo(name = "size")
    public String size;

    public Course(int year, String quarter, String subjectAndNumber, String size){
        this.year = year;
        this.quarter = quarter;
        this.subjectAndNumber = subjectAndNumber;
        this.size = size;
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

    public String getSize() {return size;}

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return year == course.year && quarter.equals(course.quarter) && subjectAndNumber.equals(course.subjectAndNumber) && size.equals(course.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, quarter, subjectAndNumber, size);
    }

    public String courseToString(){
        return subjectAndNumber + "%" + quarter + "^" + String.valueOf(year) + "~" + size + "$";
    }

    public int quarterToNum(){
        switch (quarter){
            case "fall":
                return 3;
            case "summer":
                return 2;
            case "spring":
                return 1;
            case "winter":
                return 0;
        }
        return -1;
    }
}
