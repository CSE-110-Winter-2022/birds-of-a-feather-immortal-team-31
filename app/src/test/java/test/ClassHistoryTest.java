package test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project.ClassHistory;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.model.AppDatabase;
import com.example.project.model.Course;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Text;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ClassHistoryTest extends AppCompatActivity {
    /*@Test
    public void dummyTest()
    {
        assertEquals(1+1,2);
    }*/

    @Rule
    public ActivityScenarioRule<ClassHistory> activityScenarioRule
            = new ActivityScenarioRule<>(ClassHistory.class);

    @Test
    public void MainActivityTest() {
        ActivityScenario<ClassHistory> scenario = ActivityScenario.launch(ClassHistory.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        //assertEquals(1,1);
        scenario.onActivity(activity->{

                    //assert(activity.findViewById(R.id.editTextYear)== View.INVISIBLE);
                    //assertEquals(1,1);
                    //assert(activity.findViewById(R.id.textView1).getVisibility()==View.INVISIBLE);
                    //assert("Y"=="Y");

                    //TextView textView1 = (TextView)activity.findViewById(R.id.editTextYear);
                    //String name1 = (String) textView1.getText();
            TextView textView1 = (TextView)activity.findViewById(R.id.editTextYear);
            //String name1 = (String) textView1.getText();
                    TestCase.assertEquals("Year","Year:");
                }
        );


    }
    /*
    @Test
    public void classTest(){
        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<? extends Course>  Courses = db.coursesDao().getAll();
        TextView textView1 = (TextView)Courses.findViewById(R.id.editTextYear);
    }
*/
    @Test
    public void ClassHistoryTest1() {


                    //assert(activity.findViewById(R.id.editTextYear)== View.INVISIBLE);
                    //assertEquals(1,1);
                    //assert(activity.findViewById(R.id.textView1).getVisibility()==View.INVISIBLE);
                    //assert("Y"=="Y");

                    Course course = new Course(2022, "winter", "CSE110");
                    AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
                    db.coursesDao().insert(course);
                    List<Course>  courses = db.coursesDao().getAll();
                    System.out.println(courses.get(0).getYear());
                    System.out.println(course.getYear());
                    assertEquals(course.getYear(),courses.get(0).getYear());
                    assertEquals(course.getQuarter(),courses.get(0).getQuarter());
                    assertEquals(course.getSubjectAndNumber(),courses.get(0).getSubjectAndNumber());
                    /*EditText textView = activity.findViewById(R.id.editTextSubject);
                    String name = textView.getText().toString();
                    assertEquals(name,dbText);*/




    }
    @Test
    public void ClassHistoryTest2() {
        ActivityScenario<ClassHistory> scenario = ActivityScenario.launch(ClassHistory.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        //assertEquals(1,1);
        scenario.onActivity(activity->{

                    //assert(activity.findViewById(R.id.editTextYear)== View.INVISIBLE);
                    //assertEquals(1,1);
                    //assert(activity.findViewById(R.id.textView1).getVisibility()==View.INVISIBLE);
                    //assert("Y"=="Y");


                    int year = 2021;
                    String quarter = "Summer";
                    String subject = "CSE";
                    String number = "105";


                    AppDatabase db = AppDatabase.singleton(ApplicationProvider.getApplicationContext());
                    TextView yearText = (TextView) activity.findViewById(R.id.editTextYear);
                    yearText.setText(String.valueOf(year));
                    TextView quarterText = (TextView) activity.findViewById(R.id.editTextQuarter);
                    quarterText.setText(quarter);

                    TextView subjectText = (TextView) activity.findViewById(R.id.editTextSubject);
                    subjectText.setText(subject);
                    TextView numberText = (TextView) activity.findViewById(R.id.editTextNumber);
                    numberText.setText(number);

                    activity.findViewById(R.id.buttonSaveHistory).performClick();

                    Course course = new Course(year, quarter, subject+number);

                    List<Course>  courses = db.coursesDao().getAll();
                    System.out.println(courses.get(0).getYear());
                    System.out.println(course.getYear());
                    assertEquals(course.getYear(),courses.get(0).getYear());
                    assertEquals(course.getQuarter(),courses.get(0).getQuarter());
                    assertEquals(course.getSubjectAndNumber(),courses.get(0).getSubjectAndNumber());
                    /*EditText textView = activity.findViewById(R.id.editTextSubject);
                    String name = textView.getText().toString();*/
                    assertEquals(1+1,2);
                }
        );


    }
}
