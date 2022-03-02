package test;

import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project.ClassHistory;
import com.example.project.CourseViewAdapter;
import com.example.project.PersonDetailActivity;
import com.example.project.R;
import com.example.project.model.Course;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PersonDetailActivityTest {

    protected RecyclerView coursesRecyclerView;
    protected RecyclerView.LayoutManager coursesLayoutManager;
    protected CourseViewAdapter courseViewAdapter;

    @Rule
    public ActivityScenarioRule<ClassHistory> activityScenarioRule
            = new ActivityScenarioRule<>(ClassHistory.class);

    @Test
    public void testDisplayedStringCorrect(){
        ActivityScenario<PersonDetailActivity> scenario = ActivityScenario.launch(PersonDetailActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity->{
            List<Course> data = new ArrayList<Course>();
            Course demo1 = new Course(2018, "Fall", "CSE110");
            data.add(demo1);

            coursesRecyclerView = activity.findViewById(R.id.courses_view);

            coursesLayoutManager = new LinearLayoutManager(activity);
            coursesRecyclerView.setLayoutManager(coursesLayoutManager);

            courseViewAdapter = new CourseViewAdapter(data);
            coursesRecyclerView.setAdapter(courseViewAdapter);

            //String courses_view = ((TextView) coursesRecyclerView.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.title)).getText().toString();
            View view=coursesRecyclerView;
            TextView textView= (TextView) view.findViewById(R.id.courses_view);
            String text=textView.getText().toString();

            TestCase.assertEquals("CSE110, Fall 2018", text);
                }
        );
    }

}
