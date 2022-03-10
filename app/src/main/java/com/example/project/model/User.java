package com.example.project.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    private int id;

    private boolean waved;


    @NonNull
    @ColumnInfo(name = "name")
    public String name;


    @NonNull
    @ColumnInfo(name = "photoURL")
    public String photoURL;

    @NonNull
    @ColumnInfo(name = "courses")
    public List<Course> courses;


    public User (String name, String photoURL, List<Course> courses, int id, boolean waved){
        this.name = name;

        this.photoURL = photoURL;

        this.courses = courses;

        this.id = id;

        this.waved = waved;
    }

    public String getName(){return this.name;}

    public String getPhotoURL(){return this.photoURL;}

    public List<Course> getCourses(){return this.courses;}

    public int getId() {
        return this.id;
    }

    public boolean isWaved() {
        return waved;
    }

    public void setWaved(boolean waved) {
        this.waved = waved;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id  && name.equals(user.name) && photoURL.equals(user.photoURL) && courses.equals(user.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, photoURL, courses);
    }
}
