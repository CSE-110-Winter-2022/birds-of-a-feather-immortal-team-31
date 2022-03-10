package com.example.project.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "users")
public class User {

    private int age;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "star")
    public boolean star;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;


    @NonNull
    @ColumnInfo(name = "photoURL")
    public String photoURL;


    @NonNull
    @ColumnInfo(name = "courses")
    public String courses;


    //You should use a string as the parameter to input courses, the convert method is below
    public User (String name, String photoURL, String courses, int age, boolean star){
        this.name = name;

        this.photoURL = photoURL;

        this.courses = courses;

        this.age = age;

        this.star = star;
    }

    public String getName(){return this.name;}

    public String getPhotoURL(){return this.photoURL;}

    // Output type: List<Course>
    public List<Course> getCourses(){return stringToCourses(this.courses);}

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

    //Helper method to convert courses into string
    public static String coursesToString(List<Course> courses){
        StringBuilder courseAsString = new StringBuilder();
        for(Course course: courses){
            courseAsString.append(course.courseToString());
        }
        return courseAsString.toString();
    }

    //Easier way to set this.courses
    public void setCourses(List<Course> courses){
        this.courses = coursesToString(courses);
    }

    //Add a course to this.courses and convert it into string
    public void addCourse(Course course){
        this.courses += course.courseToString();
    }

    //Helper method to convert string back to courses
    public static List<Course> stringToCourses(String coursesAsString){
        List<Course> stringToCourses = new ArrayList<Course>();

        for (int i = 0; i < coursesAsString.length(); i++) {
            int oldI = i;
            String fellowStudentSubjectAndNumber = "";
            String fellowStudentQuarter = "";
            String fellowStudentYear = "";
            String fellowStudentSize = "";
            Course fellowStudentThisCourse;

            while (true)
            {
                if (coursesAsString.charAt(i) == '%')
                {   //subject and course divider
                    fellowStudentSubjectAndNumber = coursesAsString.substring(oldI, i);
                    oldI = i + 1;
                    i++;
                    continue;
                }
                else if (coursesAsString.charAt(i) == '^')
                {   //Quarter divider
                    fellowStudentQuarter = coursesAsString.substring(oldI, i);
                    oldI = i + 1;
                    i++;
                    continue;
                }
                else if (coursesAsString.charAt(i) == '~')
                {   //year and course divider
                    fellowStudentYear = coursesAsString.substring(oldI, i);
                    oldI = i + 1;
                    i++;
                    continue;
                }
                else if (coursesAsString.charAt(i) == '$')
                {   // size divider
                    fellowStudentSize = coursesAsString.substring(oldI, i);
                    fellowStudentThisCourse = new Course(Integer.parseInt(fellowStudentYear),
                            fellowStudentQuarter, fellowStudentSubjectAndNumber, fellowStudentSize);
                    stringToCourses.add(fellowStudentThisCourse);
                    break;
                }
                else i++;
            }
        }

        return stringToCourses;
    }
}
