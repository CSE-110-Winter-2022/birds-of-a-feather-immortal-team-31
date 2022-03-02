package test;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import com.example.project.MainActivity;

import org.junit.Test;

public class Sort_by_recencyTest {

    @Test
    public void sortedCorrectly(){
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {





        });

    }
}
