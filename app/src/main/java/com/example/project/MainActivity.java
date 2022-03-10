package com.example.project;

import static android.content.ContentValues.TAG;

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
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    //An instance for other class to access this activity
    private static MainActivity mainActivity;
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    //TextView textView;
    private static final int RC_SIGN_IN = 1;


    public int currentYear;
    public int currentQuarter;

    public RecyclerView usersRecyclerView;
    public RecyclerView.LayoutManager usersLayoutManager;
    public UsersViewAdapter userViewAdapter;


    protected List<Course> courses = new ArrayList<Course>();
    protected Course demo1 = new Course(2020, "spring", "CSE110", "tiny");
    protected Course demo2 = new Course(2021, "winter", "CSE101", "medium");
    protected Course demo3 = new Course(2021, "fall", "CSE2", "small");


    protected User user1 = new User("Luffy","",User.coursesToString( new ArrayList<Course>()), 179876, true, false);
    protected User user2 = new User("Zoro","",User.coursesToString( new ArrayList<Course>()), 200879, false, false);
    protected User user3 = new User("Nami","guess", User.coursesToString( new ArrayList<Course>()), 226542, false, false);


    public List<User> fellowUsers = new ArrayList<User>();
    private TextView txtHelloWorld;
    private Spinner spinnerTextSize;

    public List<Course> myCourses;
    public List<Course> demoCourse;

    public String myId;
    public String fakedMessageString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

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


        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        myCourses = db.coursesDao().getAll();

        String usernameFinal = preferences.getString("NAME", null);
        String photourlFinal = preferences.getString("URL", null);
        myId = preferences.getString("ID", null);
        myId = "848985";
        Log.e("My ID", " "+ myId);

        currentYear = db.coursesDao().getMaxYear();

        List<Course> currentYearCourses = db.coursesDao().getCoursesFromMaxYear();
        currentQuarter = 0;

        for(Course c1 : currentYearCourses) {
            int quarterToInt = c1.quarterToNum();
            if (quarterToInt > currentQuarter){ currentQuarter = quarterToInt;}
        }
        Log.d("Max year:", String.valueOf(currentYear) + " " + String.valueOf((currentQuarter)));


        fakedMessageString = "B3%&J" + "Jon," + "https://photos.app.goo.gl/PizS3MAD4QCqGRNs5," + "825103,";

        List<Course> demoCourse = new ArrayList<Course>();

        demoCourse.add(demo1);
        demoCourse.add(demo2);
        demoCourse.add(demo3);


        for (Course c2 : demoCourse) {
            fakedMessageString += c2.courseToString();
        }
        fakedMessageString += ":908654,848985,";


        //Nearby.getMessagesClient(this).subscribe(messageListener);
        //Nearby.getMessagesClient(this).unsubscribe(messageListener);


        updateUserView();

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
            Nearby.getMessagesClient(this).subscribe(messageListener);
            Intent intent = new Intent(this, PublishMessageService.class);
            startService(intent);


            this.messageListener = new FakedMessageListener(messageListener, 15, fakedMessageString);

            //Update userViewAdapter
            updateUserView();

            textView.setText("Stop");
        }
        else if (text.equals("Stop")){
            unsubscribe();

            textView.setText("Start");
        }
    }


    //Onclick event of viewing favorite students
    public void onStarClicked(View view) {
        Intent intent = new Intent(MainActivity.this, FavoriteStudentActivity.class);
        startActivity(intent);
    }

    private void updateUserView(){
        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(getApplicationContext(),fellowUsers);

        usersRecyclerView.setAdapter(userViewAdapter);
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
                updateUserView();
                break;
            case "small classes":
                Collections.sort(fellowUsers, new SortBySizeComparator());
                updateUserView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    //Set method for BDD scenario test
    public void setFellowUsers(List<User> fellowUsers){
        this.fellowUsers = fellowUsers;
    }

    //Get method for BDD scenario test
    public List<User> getFellowUsers(){
        return this.fellowUsers;
    }


    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    public MessageListener messageListener = new MessageListener() {
        @Override
        public void onFound(@NonNull Message message) {
            boolean waved = false;
            boolean stared = false;
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
                String fellowStudentPhotoURL = sMessage.substring(k1+1, k2);
                //Log.d("fellowstudenturl", fellowStudentPhotoURL);

                int k3 = k2 + 1;
                int k4 = k3 + 6;

                String fellowStudentID = sMessage.substring(k3, k4);
                List<Course> fellowStudentMutualCourse = new ArrayList<Course>();
                Log.d("FellowStudentID", fellowStudentID);

                for (int i = k4 + 1; i < sMessage.length(); i++) {
                    int oldI = i;
                    String fellowStudentSubjectAndNumber = "";
                    String fellowStudentQuarter = "";
                    String fellowStudentYear = "";
                    String fellowStudentSize = "";
                    Course fellowStudentThisCourse;

                    while (true) {
                        if (sMessage.charAt(i) == '%') {   //subject and course divider
                            fellowStudentSubjectAndNumber = sMessage.substring(oldI, i);
                            oldI = i + 1;
                            i++;
                            continue;
                        } else if (sMessage.charAt(i) == '^') {   //Quarter divider
                            fellowStudentQuarter = sMessage.substring(oldI, i);
                            oldI = i + 1;
                            i++;
                            continue;
                        } else if (sMessage.charAt(i) == '~') {   //year and course divider
                            fellowStudentYear = sMessage.substring(oldI, i);
                            oldI = i + 1;
                            i++;
                            continue;
                        } else if (sMessage.charAt(i) == '$') {   // size divider
                            fellowStudentSize = sMessage.substring(oldI, i);
                            fellowStudentThisCourse = new Course(Integer.parseInt(fellowStudentYear),
                                    fellowStudentQuarter.toLowerCase(Locale.ROOT), fellowStudentSubjectAndNumber.toLowerCase(Locale.ROOT), fellowStudentSize);
                            for (Course myCourse : myCourses) {
                                if (myCourse.equals(fellowStudentThisCourse)) {
                                    fellowStudentMutualCourse.add(fellowStudentThisCourse);
                                    Log.e("Add course", fellowStudentThisCourse.courseToString());
                                }
                            }
                            break;
                        }
                        if(sMessage.charAt(i) == ':') break;
                        else i++;
                    }
                    if (sMessage.charAt(i) == ':'){
                        Log.d("Checking ids", "jibbi");
                        String fellowUsersWavedHands = sMessage.substring(i+1, sMessage.length());
                        int j = i + 1;
                        while(j < sMessage.length()){
                            Log.d("ID", sMessage.substring(j, j+6));
                            if(sMessage.substring(j, j+6).equals(myId)){
                                waved = true;
                                break;
                            }
                            j += 7;
                        }
                        break;
                    }
                }

                if (fellowStudentMutualCourse.isEmpty()==false) {
                    Log.d("in first if", "v");
                    int age = 0;
                    for (Course c : fellowStudentMutualCourse){
                        int cQuarter = c.quarterToNum();
                        int cYear = c.getYear();

                        age += currentQuarter - cQuarter + (currentYear - cYear)*6;

                    }
                    if(waved) fellowStudentName += new String(Character.toChars(0x1F44B));
                    User user = new User(fellowStudentName, fellowStudentPhotoURL, User.coursesToString(fellowStudentMutualCourse), Integer.parseInt(fellowStudentID), waved, stared);
                    boolean alreadyAdded = false;
                    Log.d("size of fellowusers", String.valueOf(fellowUsers.size()));
                    for (User u : fellowUsers){
                        if(u.equals(user)) {
                            alreadyAdded = true;
                            boolean oldWaveStatues = u.isWaved();
                            if(oldWaveStatues == false & waved == true){
                                user.setWaved(true);
                                user.setName(fellowStudentName);
                                updateUserView();
                            }
                        }
                    }
                    if(!alreadyAdded) {
                        fellowUsers.add(user);
                        Log.e("Waved", String.valueOf(user.isWaved()));
                        updateUserView();
                        Log.e("add user", "s");
                        updateUserView();
                    }

                }
            }

        }

    };
}