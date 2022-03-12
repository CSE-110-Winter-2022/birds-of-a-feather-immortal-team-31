package com.example.project;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.nearby.Nearby.getMessagesClient;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.example.project.model.User;
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

import java.nio.charset.StandardCharsets;
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
    private int STORAGE_PERMISSION_CODE =1;

    public int currentYear;
    public int currentQuarter;


    public RecyclerView usersRecyclerView;
    public RecyclerView.LayoutManager usersLayoutManager;
    public UsersViewAdapter userViewAdapter;


    protected List<Course> courses = new ArrayList<Course>();
    protected Course demo1 = new Course(2020, "spring", "cse110", "tiny");
    protected Course demo2 = new Course(2021, "winter", "CSE101", "medium");
    protected Course demo3 = new Course(2021, "fall", "CSE2", "small");


    protected User user1 = new User("Luffy","",User.coursesToString( new ArrayList<Course>()), 179876, false, false, 6);
    protected User user2 = new User("Zoro","",User.coursesToString( new ArrayList<Course>()), 200879, false, false, 4);
    protected User user3 = new User("Nami","guess", User.coursesToString( new ArrayList<Course>()), 226542, false, false, 2);


    public User jon = new User("Jon", "k", User.coursesToString(new ArrayList<Course>()), 824536, false, false, 3);


    public List<User> fellowUsers = new ArrayList<User>();
    private TextView txtHelloWorld;
    private Spinner spinnerTextSize;

    public List<Course> myCourses;
    public List<Course> demoCourse;

    public String myId;
    public String fakedMessageString;
    public String messageString;

    public Message databaseMessage;

    public Spinner spinner;

    public boolean mockMode = true;  //to mock nearby functionality
    public Button mockWaveButton;

    private static final int TTL_IN_SECONDS = 600; // Three minutes.

    private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(TTL_IN_SECONDS).build();


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



        mockWaveButton = (Button) findViewById(R.id.buttonMockWave);
        mockWaveButton.setVisibility(View.INVISIBLE);
        
       

        jon.addCourse(demo1);
        fellowUsers.add(jon);

         


        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String username = preferences.getString("NAME", null);
        String photourl = preferences.getString("URL", null);

        if (username == null && photourl == null) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }

        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/




        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        myCourses = db.coursesDao().getAll();

        String usernameFinal = preferences.getString("NAME", null);
        Log.d("usernameFinal", usernameFinal + " ");
        String photourlFinal = preferences.getString("URL", null);
        myId = preferences.getString("ID", null);
        Log.e("My ID", " " + myId);

        String wavedHandsAll = preferences.getString("WavedUsers", null);
        if(wavedHandsAll == null) wavedHandsAll = "";

        messageString = "B3%&J";
        messageString += usernameFinal + "," + photourlFinal + "," + myId + ",";


        for (Course c : myCourses) {
            messageString += c.courseToString();
        }
        messageString += ":" + wavedHandsAll;

        Log.d("messageString", messageString);

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

        fakedMessageString += ":";


        updateUserView();

        //usersRecyclerView.setVisibility(View.INVISIBLE);

        // Dropdown menu using spinner object.
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_sizes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        Button buttonRequest = findViewById(R.id.permission_button);
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestStoragePermission();
                }
            }
        });
    }



    public void onButtonOpenHistoryClicked(View view) {
        Intent intent = new Intent(this, ClassHistory.class);
        startActivity(intent);
        Log.d("back from activity", " noo");
        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        myCourses = db.coursesDao().getAll();

    }

    private boolean isUserSignedIn;

    public void onSearchForClassmatesClicked(View view) {

        TextView textView = findViewById(R.id.Search_for_classmates);
        String text = textView.getText().toString();

        if (text.equals("Start")) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                // Device does not support Bluetooth
            } else if (!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "Please turn on Bluetooth",
                        Toast.LENGTH_LONG).show();
            } else {


                Nearby.getMessagesClient(this).subscribe(messageListener);
                spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

                publish();
                subscribe();
                if (mockMode) mockFunctions();
                textView.setText("Stop");
            }
        } else if (text.equals("Stop")) {
            unpublish();
            unsubscribe();
            textView.setText("Start");
        }
    }



    //Onclick event of viewing favorite students
    public void onStarClicked(View view) {
        Intent intent = new Intent(MainActivity.this, FavoriteStudentActivity.class);
        startActivity(intent);
    }

    

    private void updateUserView() {

        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(getApplicationContext(),fellowUsers);

        usersRecyclerView.setAdapter(userViewAdapter);
        usersRecyclerView.setVisibility(View.VISIBLE);
    }


    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    }*/

  /*  private void gotoProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO: add default option that sort students by their number of mutual courses


        String text = parent.getItemAtPosition(position).toString();
        // Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        Log.d("position" , text);
        spinSwitch(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    //Set method for BDD scenario test
    public void setFellowUsers(List<User> fellowUsers) {
        this.fellowUsers = fellowUsers;
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



    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
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

    public List<User> getFellowUsers() {
        return fellowUsers;
    }

    public MessageListener messageListener = new MessageListener() {
        @Override
        public void onFound(@NonNull Message message) {

            SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
            myId = preferences.getString("ID", null);
            Log.d("My id", myId);

            boolean waved = false;
            boolean stared = false;
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

                int fellowStudentNoCourses = 0;

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
                    fellowStudentNoCourses = 0;

                    Log.e("smessage", sMessage);
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
                                    Log.d("add course", fellowStudentThisCourse.courseToString());
                                    fellowStudentNoCourses++;
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

                if (!fellowStudentMutualCourse.isEmpty()) {
                    Log.d("in first if", "v");

                    User noWaveUser = new User(fellowStudentName, fellowStudentPhotoURL, User.coursesToString(fellowStudentMutualCourse), Integer.parseInt(fellowStudentID), false, stared, fellowStudentNoCourses);
                    if (waved) fellowStudentName += new String(Character.toChars(0x1F44B));
                    Log.d("fellowStudentName", fellowStudentName);
                    User user = new User(fellowStudentName, fellowStudentPhotoURL, User.coursesToString(fellowStudentMutualCourse), Integer.parseInt(fellowStudentID), waved, stared, fellowStudentNoCourses);
                    boolean alreadyAdded = false;
                    Log.d("size of fellowusers", String.valueOf(fellowUsers.size()));
                    for (User u : fellowUsers) {
                        if (u.equals(noWaveUser)) {
                            Log.d("equls user", u.name);
                            alreadyAdded = true;
                            boolean oldWaveStatus = u.isWaved();
                            if (!oldWaveStatus & waved) {
                                u.setWaved(true);
                                u.setName(fellowStudentName);
                                updateFellowUsersBySort();
                                userViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    if (!alreadyAdded) {
                        fellowUsers.add(user);
                        Log.e("Waved", String.valueOf(user.isWaved()));
                        Log.e("add user", "s");
                        updateFellowUsersBySort();
                        userViewAdapter.notifyItemInserted(fellowUsers.size()-1);

                    }
                }
            }

        }

    };

    private void updateFellowUsersBySort(){
        Spinner spin = (Spinner)findViewById(R.id.spinner);
        String text = spin.getSelectedItem().toString();
        spinSwitch(text);
    }

    private void spinSwitch(String text){
        switch (text) {
            case "most mutual classes (default)":
                Collections.sort(fellowUsers, new SortByNoCoursesComparator());
                userViewAdapter.notifyDataSetChanged();
            case "recent":
                Collections.sort(fellowUsers, new SortByRecencyComparator());
                userViewAdapter.notifyDataSetChanged();
                break;
            case "small classes":
                Collections.sort(fellowUsers, new SortBySizeComparator());
                userViewAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void mockFunctions(){
        fakedMessageString = "B3%&J";
        fakedMessageString += jon.name + "," + jon.photoURL + "," + jon.getId() + ",";

        List<Course> demoCourse = new ArrayList<Course>();

        demoCourse.add(demo1);
        //demoCourse.add(demo2);
        //demoCourse.add(demo3);

        for (Course c2 : demoCourse) {
            fakedMessageString += c2.courseToString();
        }
        fakedMessageString += ":908654,";
        messageListener.onFound(new Message(fakedMessageString.getBytes(StandardCharsets.UTF_8)));
        mockWaveButton.setVisibility(View.VISIBLE);
    }

    public void onMockWaveButtonClicked(View view){
        fakedMessageString += myId + ",";
        messageListener.onFound(new Message(fakedMessageString.getBytes(StandardCharsets.UTF_8)));
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }

        }
    }

}