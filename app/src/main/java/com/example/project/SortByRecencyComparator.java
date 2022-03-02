package com.example.project;


import com.example.project.model.Course;
import com.example.project.model.User;

import java.util.Comparator;
import java.util.List;

//A class that implements the function of sorting in recency
public class SortByRecencyComparator implements Comparator<User> {


    @Override
    public int compare(User user, User t1) {
        // compare year first
        int year = RecencyYearHelper(user.getCourses()) < RecencyYearHelper(t1.getCourses()) ? 1 :
                RecencyYearHelper(user.getCourses()) > RecencyYearHelper(t1.getCourses()) ? -1 : 0;

        // if there is a recency between year, return directly
        if (year != 0)
        {
            return year;
        }

        // if they have the same max year, compare quarter
        return RecencyQuarterHelper(user.getCourses()) < RecencyQuarterHelper(t1.getCourses()) ? 1 :
                RecencyQuarterHelper(user.getCourses()) > RecencyQuarterHelper(t1.getCourses()) ? -1 : 0;
    }

    public int RecencyYearHelper(List<Course> list)
    {
        int max = list.get(0).getYear();
        for(int i = 1; i < list.size(); i++)
        {
            if (list.get(i).getYear() > max)
            {
                max = list.get(i).getYear();
            }
        }
        return max;
    }
    // return the max year in a given list of courses


    // return the int value of each quarter base on recency:
    // fall -> 6; sss -> 5; ... spring -> 2; winter -> 1
    public int RecencyQuarterHelper(List<Course> list)
    {
        // temp var that stores the output
        int count = 0;
        int maxYear = RecencyYearHelper(list);

        for(int i = 0; i < list.size(); i++)
        {
            // only compare the course with max year, neglect the rest
            if (list.get(i).getYear() == maxYear)
            {
                String quarter = list.get(i).getQuarter();
                if (quarter.equals("fall"))
                {
                    // if there is fall quarter, return directly
                    return 6;
                }
                else if (quarter.equals("special summer session"))
                {
                    // if sss, assign count to 5
                    count = 5;
                }
                else if (quarter.equals("summer session ii"))
                {
                    // only change value when quarter of lower recency is visited
                    if (count < 4)
                    {
                        count = 4;
                    }
                }
                else if (quarter.equals("summer session i"))
                {
                    if (count < 3)
                    {
                        count = 3;
                    }
                }
                else if (quarter.equals("spring"))
                {
                    if (count < 2)
                    {
                        count = 2;
                    }
                }
                else if (quarter.equals("winter"))
                {
                    if (count < 1)
                    {
                        count = 1;
                    }
                }
            }
        }
        return count;
    }

}
