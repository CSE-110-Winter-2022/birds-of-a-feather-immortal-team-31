package com.example.project;

import static android.content.ContentValues.TAG;

import static com.google.android.gms.nearby.Nearby.getMessagesClient;

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
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.gms.tasks.Task;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

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
    protected Course demo1 = new Course(2020, "spring", "cse110", "tiny");
    protected Course demo2 = new Course(2021, "winter", "CSE101", "medium");
    protected Course demo3 = new Course(2021, "fall", "CSE2", "small");

    public User user1 = new User("Luffy","",new ArrayList<Course>(), 179876, true);
    //protected User user2 = new User("Zoro","data:image/jpeg;base64,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",new ArrayList<Course>(), 20);
    //protected User user3 = new User("Nami","https://www.google.com/imgres?imgurl=https%3A%2F%2Fi.insider.com%2F6224ebce9908630019989e45%3Fwidth%3D700&imgrefurl=https%3A%2F%2Fwww.businessinsider.com%2Fukraine-plans-for-continuity-of-government-if-zelensky-dies-blinken-2022-3&tbnid=fHoGDb-jQQGF1M&vet=12ahUKEwiOurbq4bf2AhXoADQIHbJDBawQMygAegUIARCOAQ..i&docid=NvAs0rs0p0sHXM&w=700&h=525&q=volodymyr%20zelensky&ved=2ahUKEwiOurbq4bf2AhXoADQIHbJDBawQMygAegUIARCOAQ", new ArrayList<Course>(), 22);
    public User jon = new User("Jon", "", new ArrayList<Course>(), 824586, false);

    public List<User> fellowUsers = new ArrayList<User>();
    private TextView txtHelloWorld;
    private Spinner spinnerTextSize;

    public List<Course> myCourses;
    public List<Course> demoCourse;

    public String myId;
    public String fakedMessageString;
    public String messageString;

    public Message databaseMessage;

    private static final int TTL_IN_SECONDS = 600; // Three minutes.

    private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(TTL_IN_SECONDS).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*
        this.user1.getCourses().add(demo1);
        this.user2.getCourses().add(demo2);
        this.user3.getCourses().add(demo3);

     */

        /*
        fellowUsers.add(user1);
        fellowUsers.add(user2);
        fellowUsers.add(user3);
         */

        user1.getCourses().add(demo1);
        fellowUsers.add(user1);

        jon.getCourses().add(demo1);
        fellowUsers.add(jon);

        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String username = preferences.getString("NAME", null);
        String photourl = preferences.getString("URL", null);

        if (username == null && photourl == null) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });


        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        myCourses = db.coursesDao().getAll();

        String usernameFinal = preferences.getString("NAME", null);
        String photourlFinal = preferences.getString("URL", null);
        myId = preferences.getString("ID", null);
        myId = "848985";
        Log.e("My ID", " " + myId);

        String wavedHandsAll = preferences.getString("WavedUsers", null);

        messageString = "B3%&J";
        messageString += usernameFinal + "," + photourlFinal + "," + myId + ",";

        for (Course c : myCourses) {
            messageString += c.courseToString();
        }
        messageString += ":" + wavedHandsAll;

        databaseMessage = new Message(messageString.getBytes());


        fakedMessageString = "B3%&J";
        fakedMessageString += jon.name + "," + jon.photoURL + "," + jon.getId() + ",";

        List<Course> demoCourse = new ArrayList<Course>();

        demoCourse.add(demo1);
        //demoCourse.add(demo2);
        //demoCourse.add(demo3);

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

    public void onButtonOpenHistoryClicked(View view) {
        Intent intent = new Intent(this, ClassHistory.class);
        startActivity(intent);
    }

    private boolean isUserSignedIn;

    public void onSearchForClassmatesClicked(View view) {

        TextView textView = findViewById(R.id.Search_for_classmates);
        String text = textView.getText().toString();

        if (text.equals("Start")) {
            usersRecyclerView.setVisibility(View.VISIBLE);
            Nearby.getMessagesClient(this).subscribe(messageListener);

            publish();
            subscribe();
            //Update userViewAdapter
            this.messageListener = new FakedMessageListener(messageListener, 15, fakedMessageString);

            textView.setText("Stop");
        } else if (text.equals("Stop")) {
            unpublish();
            unsubscribe();

            textView.setText("Start");
        }
    }

    private void updateUserView() {
        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(fellowUsers);

        usersRecyclerView.setAdapter(userViewAdapter);
        usersRecyclerView.setVisibility(View.VISIBLE);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            gotoProfile();
        } else {
            Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        // Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        switch (text) {
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

    //Set method for BDD scenario test
    public void setFellowUsers(List<User> fellowUsers) {
        this.fellowUsers = fellowUsers;
    }

    //Get method for BDD scenario test
    public List<User> getFellowUsers() {
        return this.fellowUsers;
    }

    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        getMessagesClient(this).unsubscribe(messageListener);
    }

    private void subscribe() {
        Log.i(TAG, "Subscribing");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new SubscribeCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer subscribing");
                    }
                }).build();

        Nearby.getMessagesClient(this).subscribe(messageListener, options);
    }

    private void publish() {
        Log.i(TAG, "Publishing");
        PublishOptions options = new PublishOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new PublishCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer publishing");
                    }
                }).build();


        getMessagesClient(this).publish(databaseMessage, options)
                .addOnFailureListener(e -> {
                    Log.e("Fail", ":(");
                });
        Log.d("database message from Main Activity" ,new String(databaseMessage.getContent()));
    }

    private void unpublish() {
        Log.i(TAG, "Unpublishing.");
        Nearby.getMessagesClient(this).unpublish(databaseMessage);
    }

    public MessageListener messageListener = new MessageListener() {
        @Override
        public void onFound(@NonNull Message message) {
            boolean waved = false;
            String sMessage = new String(message.getContent());
            Log.d("Message found", sMessage.substring(0, 5));
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
                String fellowStudentPhotoURL = sMessage.substring(k1 + 1, k2);
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
                                }
                            }
                            break;
                        }
                        if (sMessage.charAt(i) == ':') break;
                        else i++;
                    }
                    if (sMessage.charAt(i) == ':') {
                        Log.d("Checking ids", "jibbi");
                        String fellowUsersWavedHands = sMessage.substring(i + 1, sMessage.length());
                        int j = i + 1;
                        while (j < sMessage.length()) {
                            Log.d("ID", sMessage.substring(j, j + 6));
                            if (sMessage.substring(j, j + 6).equals(myId)) {
                                waved = true;
                                break;
                            }
                            j += 7;
                        }
                        break;
                    }
                }

                if (fellowStudentMutualCourse.isEmpty() == false) {
                    Log.d("in first if", "v");
                    User noWaveUser = new User(fellowStudentName, fellowStudentPhotoURL, fellowStudentMutualCourse, Integer.parseInt(fellowStudentID), false);
                    if (waved) fellowStudentName += new String(Character.toChars(0x1F44B));
                    User user = new User(fellowStudentName, fellowStudentPhotoURL, fellowStudentMutualCourse, Integer.parseInt(fellowStudentID), waved);
                    boolean alreadyAdded = false;
                    Log.d("size of fellowusers", String.valueOf(fellowUsers.size()));
                    for (User u : fellowUsers) {
                        Log.d("name", u.name);
                        Log.d("id", String.valueOf(u.getId()));
                        Log.d("url", u.getPhotoURL());
                        Log.d("myname", fellowStudentName);
                        Log.d("myid", fellowStudentID);
                        Log.d("myurl", fellowStudentPhotoURL);
                        if (u.equals(noWaveUser)) {
                            Log.d("equls user", u.name);
                            alreadyAdded = true;
                            boolean oldWaveStatues = u.isWaved();
                            if (oldWaveStatues == false & waved == true) {
                                u.setWaved(true);
                                u.setName(fellowStudentName);
                                Log.d("changedname", u.name);
                                userViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    if (!alreadyAdded) {
                        fellowUsers.add(user);
                        Log.e("Waved", String.valueOf(user.isWaved()));
                        Log.e("add user", "s");
                        userViewAdapter.notifyItemInserted(fellowUsers.size()-1);

                    }

                }
            }

        }

    };
}