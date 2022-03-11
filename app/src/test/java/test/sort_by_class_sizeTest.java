package test;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project.SortBySizeComparator;
import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.example.project.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class sort_by_class_sizeTest {
    List<User> fellowUsers = new ArrayList<User>();


    @Before
    public void setUp(){



        Course demo1 = new Course(2020, "spring", "CSE110", "tiny");
        Course demo2 = new Course(2021, "winter", "CSE101", "medium");
        Course demo3 = new Course(2020, "fall", "CSE2", "small");

        User user1 = new User("Luffy","",new ArrayList<Course>(), 179876, true, 6);
        User user2 = new User("Zoro","",new ArrayList<Course>(), 200879, false, 7);
        User user3 = new User("Nami","", new ArrayList<Course>(), 226542, false,8);


        user1.getCourses().add(demo1);
        user1.getCourses().add(demo2);
        user1.getCourses().add(demo3);
        user2.getCourses().add(demo2);
        user3.getCourses().add(demo3);

        fellowUsers.add(user1);
        fellowUsers.add(user2);
        fellowUsers.add(user3);


    }

    @Test
    public void test(){
        SortBySizeComparator k = new SortBySizeComparator();
        double a=k.SizeHelper(fellowUsers.get(0).getCourses());
        assertEquals(a,1.51,0.01);
        AppDatabase.setSingletonInstance(null);

    }
}
