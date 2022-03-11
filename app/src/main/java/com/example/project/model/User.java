package com.example.project.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "users")
public class User {

    private int age;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;


    @NonNull
    @ColumnInfo(name = "photoURL")
    public String photoURL;

    @NonNull
    @ColumnInfo(name = "courses")
    public List<Course> courses;


    public User (String name, String photoURL, List<Course> courses, int age){
        this.name = name;

        this.photoURL = photoURL;

        this.courses = courses;

        this.age = age;
    }

    public String getName(){return this.name;}

    public String getPhotoURL(){return this.photoURL;}

    public List<Course> getCourses(){return this.courses;}

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && id == user.id && name.equals(user.name) && photoURL.equals(user.photoURL) && courses.equals(user.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, id, name, photoURL, courses);
    }
}
