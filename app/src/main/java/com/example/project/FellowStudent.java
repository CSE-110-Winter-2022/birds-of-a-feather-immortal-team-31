package com.example.project;

import com.example.project.model.Course;

import java.util.List;

public class FellowStudent {
    private final String name;
    private final String photoURL;
    private final List<Course> courses;

    public FellowStudent(String name, String photoURL, List<Course> courses){
        this.name = name;
        this.photoURL = photoURL;
        this.courses = courses;
    }
}
