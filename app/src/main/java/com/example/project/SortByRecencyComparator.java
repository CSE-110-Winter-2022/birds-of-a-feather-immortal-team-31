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
        return RecencyHelper(user.getCourses()) < RecencyHelper(t1.getCourses()) ? 1 :
                RecencyHelper(user.getCourses()) > RecencyHelper(t1.getCourses()) ? -1 : 0;
    }

    public int RecencyHelper(List<Course> list)
    {
        int weight = 0;
        for(int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getYear() < 2021)
            {
                weight++;
            }
            else
            {
                String quarter = list.get(i).getQuarter();
                switch(quarter)
                {
                    case "fall":
                        weight += 5;
                        break;
                    case "summer":
                        weight += 4;
                        break;
                    case "spring":
                        weight += 3;
                        break;
                    case "winter":
                        weight += 2;
                        break;
                }
            }
        }
        return weight;
    }
    // return the max year in a given list of courses


    // return the int value of each quarter base on recency:
    // fall -> 6; sss -> 5; ... spring -> 2; winter -> 1
 /*   public int RecencyQuarterHelper(List<Course> list)
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
*/
}
