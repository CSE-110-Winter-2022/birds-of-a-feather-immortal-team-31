package com.example.project;


import com.example.project.model.Course;
import com.example.project.model.User;

import java.util.Comparator;
import java.util.List;

//A class that implements the function of sorting in recency
public class SortByRecencyComparator implements Comparator<User> {


    @Override
    public int compare(User user, User t1) {
        if(user.isWaved() && !t1.isWaved()) return -1;
        else if(t1.isWaved() && !user.isWaved()) return 1;
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
}
