package com.example.project;

import com.example.project.model.Course;
import com.example.project.model.User;

import java.util.Comparator;
import java.util.List;

public class SortBySizeComparator implements Comparator<User> {

    //@Override
    public int compare(User user, User t1) {
        if(user.isWaved() && !t1.isWaved()) return -1;
        else if(t1.isWaved() && !user.isWaved()) return 1;
        // compare total weight
        if (SizeHelper(user.getCourses()) < SizeHelper(t1.getCourses())){
            return 1;
        }
        else if (SizeHelper(user.getCourses()) > SizeHelper(t1.getCourses())){
            return -1;
        }

        return 0;
      // return SizeHelper(user.getCourses()) < SizeHelper(t1.getCourses()) ? 0 :
              //  SizeHelper(user.getCourses()) > SizeHelper(t1.getCourses()) ? 0 : 0;

    }

    // return the double value of total weight of a given course list
    public double SizeHelper(List<Course> list)
    {
        // temp var that stores the output
        double count = 0;
        for(int i = 0; i < list.size(); i++) {
            String size = list.get(i).getSize();
            switch (size) {
                case "tiny":
                    count++;
                    break;
                case "small":
                    count += 0.33;
                    break;
                case "medium":
                    count += 0.18;
                    break;
                case "large":
                    count += 0.1;
                    break;
                case "huge":
                    count += 0.06;
                    break;
                case "gigantic":
                    count += 0.03;
                    break;
            }
        }
        return count;
    }

}
