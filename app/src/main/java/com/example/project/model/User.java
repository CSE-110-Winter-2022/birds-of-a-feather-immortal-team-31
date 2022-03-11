package com.example.project.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "users")
public class User implements Serializable {

    private int age;

    public boolean star;
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

    @NonNull
    @ColumnInfo(name = "session")
    public String session;

    public User (String name, String photoURL, List<Course> courses, int age, String session){
        this.name = name;

        this.photoURL = photoURL;

        this.courses = courses;

        this.age = age;

        this.star = false;

        this.session = session;
    }

    public String getName(){return this.name;}

    public String getPhotoURL(){return this.photoURL;}

    public List<Course> getCourses(){return this.courses;}

    //Get the state of favorite of this user
    public boolean getStar(){
        return this.star;
    }

    public int getAge() {
        return age;
    }

    //Add or remove users from my "favorite"
    public void changeStar(){
        this.star = !this.star;
    }

    // get the session of this User
    public String getSession() {return session;}

    public User getUsingId(int id)
    {
        if (id == this.id)
        {
            return this;
        }
        return null;
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
        return Objects.hash(age, id, name, photoURL, courses, session);
    }
}
