package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project.CourseViewAdapter;
import com.example.project.FakedMessageListener;
import com.example.project.MainActivity;
import com.example.project.PersonDetailActivity;
import com.example.project.R;
import com.example.project.SortByRecencyComparator;
import com.example.project.SortBySizeComparator;
import com.example.project.UsersViewAdapter;
import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.example.project.model.User;
import com.google.android.gms.nearby.messages.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@RunWith(AndroidJUnit4.class)

//Implement AdapterView to get the spinner, the object for dropdown menu
public class PersonDetailActivityTest {
    Course demo1 = new Course(2020, "spring", "cse110", "tiny");
    List <Course> courses = new ArrayList<>();
    @Test
    public void clickButtonSendsWaveTest(){
        PersonDetailActivity personDetailActivity = new PersonDetailActivity();
        ActivityScenario<PersonDetailActivity> scenario = ActivityScenario.launch(PersonDetailActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            activity.name = "Jon";
            activity.url = "";
            activity.id = "908674";
            activity.alreadyWaved = false;
            activity.courses.add(demo1);

            Button waveButton = (Button) activity.findViewById(R.id.buttonWave);
            waveButton.performClick();

            assertEquals(activity.id + ",", activity.wavedUsers);
        });
    }
}

