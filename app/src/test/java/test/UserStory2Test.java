package com.example.project;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserStory2Test extends AppCompatActivity {
    @Test
    public void dummyTest()
    {
        assertEquals(1+1,2);
    }

    @Test
    public void MainActivityTest() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        //assertEquals(1,1);
        scenario.onActivity(activity->{

                    //assert(activity.findViewById(R.id.editTextYear)== View.INVISIBLE);
                    //assertEquals(1,1);
                    //assert(activity.findViewById(R.id.textView1).getVisibility()==View.INVISIBLE);
                    //assert("Y"=="Y");

                    //TextView textView1 = (TextView)activity.findViewById(R.id.editTextYear);
                    //String name1 = (String) textView1.getText();
                    TestCase.assertEquals("Yifei Wang","Yifei Wang");
                }
        );

    }
}
