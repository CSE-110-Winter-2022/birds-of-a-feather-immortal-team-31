package test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.SortByRecencyComparator;
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
public class Sort_by_recencyTest implements AdapterView.OnItemSelectedListener{

    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter userViewAdapter;

    protected List<User> fellowUsers = new ArrayList<User>();

    protected User currUser1 = null;
    protected User currUser2 = null;


    @Before
    public void setUp(){



        Course demo1 = new Course(2020, "spring", "CSE110", "tiny");
        Course demo2 = new Course(2021, "winter", "CSE101", "medium");
        Course demo3 = new Course(2020, "fall", "CSE2", "small");

        User user1 = new User("Luffy","",new ArrayList<Course>(), 17);
        User user2 = new User("Zoro","",new ArrayList<Course>(), 20);
        User user3 = new User("Nami","", new ArrayList<Course>(), 22);


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
    public void sortedCorrectly(){



        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {

            //TODO get users from recyclerView
            activity.setFellowUsers(this.fellowUsers);

            usersRecyclerView = activity.findViewById(R.id.users_view);

            usersLayoutManager = new LinearLayoutManager(activity);
            usersRecyclerView.setLayoutManager(usersLayoutManager);

            userViewAdapter = new UsersViewAdapter(activity.getFellowUsers());
            usersRecyclerView.setAdapter(userViewAdapter);


            Spinner spinner = activity.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity.getApplicationContext(),
                    R.array.font_sizes, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

            //spinner.performClick();

            spinner.setSelection(adapter.getPosition("recent"));
            int count = usersRecyclerView.getChildCount();

            /*
            onView(withId(R.id.spinner)).perform(click());
            onData(allOf(is(instanceOf(String.class)), is("Recency"))).perform(click());
            onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Recency"))));
             */

            for (int i = 0; i < count; i++) {
                String user1Name = usersRecyclerView.getChildAt(i).toString();
                String user2Name = usersRecyclerView.getChildAt(i+1).toString();



                for(User u : activity.getFellowUsers()){
                    if(u.getName() == user1Name) this.currUser1 = u;
                    if(u.getName() == user2Name) this.currUser2 = u;
                }

               // assertEquals(1,2);
                //assertEquals(recencyComparator.compare(currUser1, currUser2), 1);
               // assertEquals(recencyComparator.compare(currUser1, currUser2), 0);

                //assertEquals(recencyComparator.compare(currUser1, currUser2), 1);

                SortByRecencyComparator recencyComparator = new SortByRecencyComparator();

                assertEquals(recencyComparator.compare(this.currUser1, this.currUser2), 1);
                assertThat(1, equalTo(2));

            }
        });

        //assertEquals(fellowUsers.get(0).getName(), "Luffy");
    }

    // implementation of AdapterView
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        // Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        switch (text)
        {
            case "recent":
                Collections.sort(fellowUsers, new SortByRecencyComparator());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Test
    public void test(){
        SortByRecencyComparator k = new SortByRecencyComparator();
        int a=k.RecencyHelper(fellowUsers.get(0).getCourses());
        assertEquals(a,4);
    }

}


