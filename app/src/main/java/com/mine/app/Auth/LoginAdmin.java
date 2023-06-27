package com.mine.app.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mine.app.Dashboard;
import com.mine.app.R;
import com.mine.app.Utility.NetworkChangeListener;

public class LoginAdmin extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private static final String TAG = "signupTag";
    String adminEmail, adminPass;
    private EditText email, pass;
    private TextView forgetPass;
    private AppCompatButton loginBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference myRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        initView();
        initEvent();

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

    private void initEvent() {

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminEmail = email.getText().toString().trim();
                adminPass = pass.getText().toString().trim();
                if (TextUtils.isEmpty(adminEmail)) {
                    email.setError("enter email");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(adminPass)) {
                    pass.setError("enter pass");
                    pass.requestFocus();
                } else {
                    //call the signin funtion here
                    // progressBar.setVisibility(View.VISIBLE);
                    loginBtn.setVisibility(View.GONE);

                    mAuth.signInWithEmailAndPassword(adminEmail, adminPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //loginResultTv.setText("pass");
                                        //sign in Success
                                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        myRootRef.child("Admin").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //place data in datasnapshot that we can show
                                                if (dataSnapshot.exists()) {
                                                    //progressBar.setVisibility(View.GONE);
                                                    loginBtn.setEnabled(true);
                                                    loginBtn.setVisibility(View.VISIBLE);

                                                    //  Paper.book().write("active", "user");
                                                    Toast.makeText(LoginAdmin.this, "Welcome..!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginAdmin.this, Dashboard.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    mAuth.signOut();
                                                    Toast.makeText(LoginAdmin.this, "This is not admin login details", Toast.LENGTH_SHORT).show();
                                                    //progressBar.setVisibility(View.GONE);
                                                    loginBtn.setEnabled(true);
                                                    loginBtn.setVisibility(View.VISIBLE);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        //loginResultTv.setText("fail");
                                        //progressBar.setVisibility(View.GONE);
                                        loginBtn.setEnabled(true);
                                        loginBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginAdmin.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });


        //forget Password
        forgetPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String emailEdit= String.valueOf( ((EditText) findViewById(R.id.email)).getText().toString());
                if (TextUtils.isEmpty(emailEdit)) {
                    email.setError("Enter your email");
                    email.requestFocus();
                } else {

                    // Get a reference to Firebase database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersRef = database.getReference("Users");

                    // Create a Firebase Query to search for a matching email address in the "users" node
                    Query query = usersRef.orderByChild("email").equalTo(emailEdit);
                    Log.d("query:", String.valueOf(query));
                    // Add a ValueEventListener to the Query to handle the search results
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // If a match is found, the dataSnapshot will contain the user object(s)
                            boolean isMatchFound = dataSnapshot.exists();
                            Log.d("match:", String.valueOf(isMatchFound));
                            if(isMatchFound == true){
                                FirebaseAuth.getInstance().sendPasswordResetEmail(emailEdit).addOnSuccessListener(new OnSuccessListener<Void>(){
                                            @Override
                                            public void onSuccess(Void unused){
                                                Toast.makeText(LoginAdmin.this, "Email sent.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(LoginAdmin.this, e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }else{
                                Toast.makeText(LoginAdmin.this, "Your email doesn't exist.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // TODO: do something with the boolean value, e.g. validate the login credentials
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors, if any
                            Toast.makeText(LoginAdmin.this, databaseError.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private void initView() {
        forgetPass = findViewById(R.id.pwdF);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pwd);
        loginBtn = findViewById(R.id.btn_login_admin);

        mAuth = FirebaseAuth.getInstance();
        myRootRef = FirebaseDatabase.getInstance().getReference();

        //Utils.statusBarColor(Login.this);

    }
}