package com.mine.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mine.app.Utility.NetworkChangeListener;

import java.util.Map;

public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 123;
    AppCompatButton add_btn; //add btn
    ImageView user_image; // image

    EditText emailUserr, nameUser;
    TextView nom, emailUser1;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private Map<String, String> userMap;
    private String email;
    private String userid;
    private static final String USERS = "Users";

    //listener to wifi status
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        showUserData();

    }
    private void initView(){
        //init views
        add_btn = findViewById(R.id.add_btn);
        user_image = findViewById(R.id.product_image);

        emailUserr= findViewById(R.id.button3);
        emailUser1 = findViewById(R.id.textView2);
        nom= findViewById(R.id.textView);
        nameUser=findViewById(R.id.name_edit);

    }
    public void showUserData(){
        Intent intent = getIntent();
        Log.d("TAG1", "Message to log");

        String email = getIntent().getStringExtra("email");
        String name = getIntent().getStringExtra("name");
        Log.d("TAG", "Email from login: " + email);
        Log.d("TAG", "Name from login: " + name);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USERS);
        Log.v("USERID", userRef.getKey());

        //read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              /*  DataSnapshot keyId= (DataSnapshot) snapshot.child("1VAyVGh7PNYKYtFeOUlxTdUCqkw1");
                String emailFromDB = keyId.child("email").getValue(String.class);
                emailUserr.setText(emailFromDB);*/
                for(DataSnapshot keyId: snapshot.getChildren()){
                    String emailFromDB = keyId.child("email").getValue(String.class);
                    Log.d("TAG", "Email from database Profile: " + emailFromDB);
                    String nameFromDB = keyId.child("name").getValue(String.class);
                    Log.d("TAG", "Email from database Profile: " + emailFromDB);
                    if(emailFromDB != null && emailFromDB.equals(email)){
                        emailUserr.setText(emailFromDB);
                        nom.setText(nameFromDB);
                        nameUser.setText(nameFromDB);
                        emailUser1.setText(emailFromDB);
                        Toast.makeText(Profile.this, "yes"+emailUserr, Toast.LENGTH_SHORT).show();


                        break;
                    }
                    // emailUserr.setText(emailFromDB);

                }


                Toast.makeText(Profile.this, "no"+emailUserr, Toast.LENGTH_SHORT).show();
               /* for(DataSnapshot keyId: snapshot.getChildren()){
                    String emailFromDB = keyId.child("email").getValue(String.class);
                    if(emailFromDB != null && emailFromDB.equals(email)){
                        emailUserr.setText(emailFromDB);

                        Toast.makeText(Profile.this, "fail"+emailUserr, Toast.LENGTH_SHORT).show();
                        break;
                    }

                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "fail"+email, Toast.LENGTH_SHORT).show();


            }
        });


        Log.d("TAG4", "Message to log");





    }



    @Override
    protected void onStart(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }
    @Override
    protected void onStop(){
        unregisterReceiver(networkChangeListener);
        super.onStop();

    }
}