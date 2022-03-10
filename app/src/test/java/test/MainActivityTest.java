package test;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project.FakedMessageListener;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.SortByRecencyComparator;
import com.example.project.SortBySizeComparator;
import com.example.project.UsersViewAdapter;
import com.example.project.model.Course;
import com.example.project.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@RunWith(AndroidJUnit4.class)

//Implement AdapterView to get the spinner, the object for dropdown menu
public class MainActivityTest implements AdapterView.OnItemSelectedListener{

    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter userViewAdapter;

    List<User> fellowUsers = new ArrayList<User>();


    @Before
    public void setUp(){



        Course demo1 = new Course(2020, "spring", "CSE110", "tiny");
        Course demo2 = new Course(2077, "winter", "CSE101", "small");
        Course demo3 = new Course(2020, "fall", "CSE2", "medium");


        User user1 = new User("Luffy","", User.coursesToString(new ArrayList<Course>()), 179876, true,false);
        User user2 = new User("Zoro","", User.coursesToString(new ArrayList<Course>()), 200879, false, false);
        User user3 = new User("Nami","", User.coursesToString(new ArrayList<Course>()), 226542, false, false);



        user1.getCourses().add(demo1);
        user2.getCourses().add(demo2);
        user3.getCourses().add(demo3);

        fellowUsers.add(user1);
        fellowUsers.add(user2);
        fellowUsers.add(user3);


    }


    @Test
    public void sortRencencyCorrectly(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            activity.usersRecyclerView = activity.findViewById(R.id.users_view);
            activity.usersLayoutManager = new LinearLayoutManager(activity);
            activity.usersRecyclerView.setLayoutManager(activity.usersLayoutManager);
            activity.userViewAdapter = new UsersViewAdapter(ApplicationProvider.getApplicationContext(),activity.fellowUsers);
            activity.usersRecyclerView.setAdapter(activity.userViewAdapter);

            int count = activity.userViewAdapter.getItemCount();

            Spinner spinner = activity.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity.getApplicationContext(),
                    R.array.font_sizes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

            spinner.setSelection(adapter.getPosition("recent"));

            SortByRecencyComparator recencyComparator = new SortByRecencyComparator();
            for (int i = 0; i < count-1; i++) {
                User user1 = activity.userViewAdapter.getUserAtIndex(i);
                User user2 = activity.userViewAdapter.getUserAtIndex(i+1);

                //assertTrue(recencyComparator.compare(user1, user2) == -1 || recencyComparator.compare(user1, user2) == 0);
                assertEquals(recencyComparator.compare(user1, user2), -1);
            }
        });
    }

    /*
    @Test
    public void sortSizeCorrectly(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            activity.usersRecyclerView = activity.findViewById(R.id.users_view);
            activity.usersLayoutManager = new LinearLayoutManager(activity);
            activity.usersRecyclerView.setLayoutManager(activity.usersLayoutManager);
            activity.userViewAdapter = new UsersViewAdapter(activity.fellowUsers);
            activity.usersRecyclerView.setAdapter(activity.userViewAdapter);

            int count = activity.userViewAdapter.getItemCount();

            Spinner spinner = activity.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity.getApplicationContext(),
                    R.array.font_sizes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

            spinner.setSelection(adapter.getPosition("small classes"));

            SortBySizeComparator sizeComparator = new SortBySizeComparator();

            User user1 = activity.userViewAdapter.getUserAtIndex(0);
            User user2 = activity.userViewAdapter.getUserAtIndex(1);

            assertEquals(sizeComparator.compare(user1, user2), -1);
            user1 = activity.userViewAdapter.getUserAtIndex(1);
            user2 = activity.userViewAdapter.getUserAtIndex(2);


            assertEquals(sizeComparator.compare(user1, user2), 1);



        });
    }
    */

    /*
    @Test
    public void nearbyTest(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            Course testCourse1 = new Course(2020, "spring", "CSE110", "tiny");
            Course testCourse2 = new Course(2021, "winter", "CSE101", "medium");
            List<Course> testCourses = new ArrayList<Course>();
            testCourses.add(testCourse1);
            testCourses.add(testCourse2);

            List<Course> myCourses = new ArrayList<Course>();
            myCourses.add(testCourse1);

            String FakedMessageString = "B3%&J" + "Bjarki," + "https://photos.app.goo.gl/PizS3MAD4QCqGRNs5,";
            activity.fellowUsers = null;
            activity.myCourses = myCourses;
            for (Course c2 : testCourses) {
                FakedMessageString += c2.courseToString();
            }
            activity.messageListener = new FakedMessageListener(activity.messageListener, 1, FakedMessageString);
            System.out.println(activity.fellowUsers.size()+ "jibbi");
            List<Course> mutualCourses = activity.fellowUsers.get(0).getCourses();
            assertEquals(mutualCourses.get(0), myCourses.get(0));
        });
    }

     */


    // implementation of AdapterView
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        // Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        switch (text)
        {
            case "recent":
                Collections.sort(fellowUsers, new SortByRecencyComparator());
                break;
            case "small classes":
                Collections.sort(fellowUsers, new SortBySizeComparator());
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}


