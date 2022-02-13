package com.example.project.model;

import androidx.room.Entity;

@Entity(ta)
public class User {

    String gerName();

    String photoURL();

    Course[] getCourses();
}
