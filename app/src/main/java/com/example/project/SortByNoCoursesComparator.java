package com.example.project;

import android.util.Log;

import com.example.project.model.User;

import java.util.Comparator;

public class SortByNoCoursesComparator implements Comparator<User> {
    @Override
    public int compare(User user, User t1) {
        if(user.isWaved() && !t1.isWaved()) return -1;
        else if(t1.isWaved() && !user.isWaved()) return 1;
        return Integer.compare(t1.getNoCourses(), user.getNoCourses());
    }
}
