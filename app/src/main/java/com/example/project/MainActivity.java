package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.example.project.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    //TextView textView;
    private static final int RC_SIGN_IN = 1;


    private MessageListener messageListener;

    public int currentYear;
    public int currentQuarter;

    public RecyclerView usersRecyclerView;
    public RecyclerView.LayoutManager usersLayoutManager;
    public UsersViewAdapter userViewAdapter;

    protected List<Course> courses = new ArrayList<Course>();
    protected Course demo1 = new Course(2020, "spring", "CSE110", "tiny");
    protected Course demo2 = new Course(2021, "winter", "CSE101", "medium");
    protected Course demo3 = new Course(2021, "fall", "CSE2", "small");

    protected User user1 = new User("Luffy","",User.coursesToString( new ArrayList<Course>()), 17, false);
    protected User user2 = new User("Zoro","",User.coursesToString( new ArrayList<Course>()), 20, false);
    protected User user3 = new User("Nami","guess", User.coursesToString( new ArrayList<Course>()), 22, false);

    public List<User> fellowUsers = new ArrayList<User>();
    private TextView txtHelloWorld;
    private Spinner spinnerTextSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dummy users for test purpose
        this.user1.addCourse(demo1);
        this.user2.addCourse(demo2);
        this.user3.addCourse(demo3);

        fellowUsers.add(user1);
        fellowUsers.add(user2);
        fellowUsers.add(user3);

        //TODO: implement a filter that only add users whp share the same classes with me

        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String username = preferences.getString("NAME", null);
        String photourl = preferences.getString("URL", null);

        if(username == null && photourl == null){
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
        /*
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        if(account == null){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            Intent switchActivites = new Intent(this, SignIn.class);

        }

         */


        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<Course> myCourses = db.coursesDao().getAll();

        String usernameFinal = preferences.getString("NAME", null);
        String photourlFinal = preferences.getString("URL", null);

        currentYear = db.coursesDao().getMaxYear();

        List<Course> currentYearCourses = db.coursesDao().getCoursesFromMaxYear();
        currentQuarter = 0;

        for(Course c1 : currentYearCourses) {
            int quarterToInt = c1.quarterToNum();
            if (quarterToInt > currentQuarter){ currentQuarter = quarterToInt;}
        }
        Log.d("Max year:", String.valueOf(currentYear) + " " + String.valueOf((currentQuarter)));


        String messageString = "B3%&J";
        messageString += usernameFinal + "," + photourlFinal + ",";
        for (Course c : myCourses) {
            messageString += c.courseToString();
        }

        String FakedMessageString = "B3%&J" + "Bjarki," + "demoURL,";

        List<Course> demoCourse = new ArrayList<Course>();

        demoCourse.add(demo1);
        demoCourse.add(demo2);
        demoCourse.add(demo3);


        for (Course c2 : demoCourse) {
            FakedMessageString += c2.courseToString();
        }

        Message databaseMessage = new Message(messageString.getBytes());
        Nearby.getMessagesClient(this).publish(databaseMessage);

        MessageListener messageListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                String sMessage = new String(message.getContent());
                Log.d("Message found", sMessage.substring(0,5));
                if (sMessage.substring(0, 5).equals("B3%&J")) { //authentication key of message

                    int k1 = 5;
                    while (true) {
                        k1++;
                        if (sMessage.charAt(k1) == ',') break;

                    }
                    String fellowStudentName = sMessage.substring(5, k1);

                    int k2 = k1;
                    while (true) {
                        k2++;
                        if (sMessage.charAt(k2) == ',') break;
                    }
                    String fellowStudentPhotoURL = sMessage.substring(k1, k2);

                    List<Course> fellowStudentMutualCourse = new ArrayList<Course>();

                    for (int i = k2 + 1; i < sMessage.length(); i++) {
                        int oldI = i;
                        String fellowStudentSubjectAndNumber = "";
                        String fellowStudentQuarter = "";
                        String fellowStudentYear = "";
                        String fellowStudentSize = "";
                        Course fellowStudentThisCourse;

                        while (true)
                        {
                            if (sMessage.charAt(i) == '%')
                            {   //subject and course divider
                                fellowStudentSubjectAndNumber = sMessage.substring(oldI, i);
                                oldI = i + 1;
                                i++;
                                continue;
                            }
                            else if (sMessage.charAt(i) == '^')
                            {   //Quarter divider
                                fellowStudentQuarter = sMessage.substring(oldI, i);
                                oldI = i + 1;
                                i++;
                                continue;
                            }
                            else if (sMessage.charAt(i) == '~')
                            {   //year and course divider
                                fellowStudentYear = sMessage.substring(oldI, i);
                                oldI = i + 1;
                                i++;
                                continue;
                            }
                            else if (sMessage.charAt(i) == '$')
                            {   // size divider
                                fellowStudentSize = sMessage.substring(oldI, i);
                                fellowStudentThisCourse = new Course(Integer.parseInt(fellowStudentYear),
                                        fellowStudentQuarter, fellowStudentSubjectAndNumber, fellowStudentSize);
                                for (Course myCourse : myCourses) {
                                    if (myCourse.equals(fellowStudentThisCourse)) {
                                        fellowStudentMutualCourse.add(fellowStudentThisCourse);
                                    }
                                }
                                break;
                            }
                            else i++;
                        }
                    }
                    if (!fellowStudentMutualCourse.isEmpty()) {
                        int age = 0;
                        for (Course c : fellowStudentMutualCourse){
                            Log.d("Course", c.courseToString());
                            int cQuarter = c.quarterToNum();
                            int cYear = c.getYear();
                            Log.d("in loop", "d");

                            age += currentQuarter - cQuarter + (currentYear - cYear)*4;
                            Log.d("Age", String.valueOf(age));
                        }
                        User user = new User(fellowStudentName, fellowStudentPhotoURL, User.coursesToString(fellowStudentMutualCourse), age, false);
                        fellowUsers.add(user);
                    }
                }
            }
        };

        Nearby.getMessagesClient(this).subscribe(messageListener);

        this.messageListener = new FakedMessageListener(messageListener, 60, FakedMessageString);


        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        /*
        Collections.sort(fellowUsers, new Comparator<User>() {
            @Override
            public int compare(User user, User t1) {
                return user.getAge() > t1.getAge() ? 1 : user.getAge() < t1.getAge() ? -1 : 0;
            }
        });

         */
        userViewAdapter = new UsersViewAdapter(getApplicationContext(),fellowUsers);
        usersRecyclerView.setAdapter(userViewAdapter);

        usersRecyclerView.setVisibility(View.INVISIBLE);

        // Dropdown menu using spinner object.
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    public void onButtonOpenHistoryClicked(View view){
        Intent intent = new Intent(this, ClassHistory.class);
        startActivity(intent);
    }

    private boolean isUserSignedIn;

    public void onSearchForClassmatesClicked(View view){

        TextView textView = findViewById(R.id.Search_for_classmates);
        String text = textView.getText().toString();

        if(text.equals("Start")){
            usersRecyclerView.setVisibility(View.VISIBLE);
            Nearby.getMessagesClient(this).subscribe(this.messageListener);
            //Update userViewAdapter
            userViewAdapter.setFellowUsers(fellowUsers);

            usersRecyclerView.setAdapter(userViewAdapter);

            textView.setText("Stop");
        }
        else if (text.equals("Stop")){
            Nearby.getMessagesClient(this).unsubscribe(this.messageListener);
            textView.setText("Start");
        }
    }

    //Onclick event of viewing favorite students
    public void onStarClicked(View view) {
        Intent intent= new Intent(MainActivity.this, FavoriteStudentActivity.class);
        startActivity(intent);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO: add default option that sort students by their number of mutual courses


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


    //Set method for BDD scenario test
    public void setFellowUsers(List<User> fellowUsers){
        this.fellowUsers = fellowUsers;
    }

    //Get method for BDD scenario test
    public List<User> getFellowUsers(){
        return this.fellowUsers;
    }
}