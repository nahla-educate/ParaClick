package com.mine.app.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mine.app.ListUsers;
import com.mine.app.Models.User;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mine.app.Products.Recycler_View;
import com.mine.app.R;
import com.mine.app.Utility.NetworkChangeListener;

public class Register extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private static final String TAG = "signupTag";
    User user;
    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference myRootRef;
    String userName, userEmail, userPass, userConfirmPass;
    private EditText name, email, pass, confirmPass;
    private Button SignUPBtn;
    private TextView GoToLoginBtn;
    //private ProgressBar progressBar;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
   // private int selectedGender=1;
    //RadioGroup radioGroupGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initView();
        initEvent();
    }

    private void initEvent() {


        GoToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        SignUPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = name.getText().toString().trim();
                userEmail = email.getText().toString().trim();
                userPass = pass.getText().toString().trim();
                userConfirmPass = confirmPass.getText().toString().trim();


                if (TextUtils.isEmpty(userName)) {
                    name.setError("Enter full name");

                } else if (TextUtils.isEmpty(userEmail)) {
                    email.setError("Enter email");
                }
                else if (!userEmail.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address, enter valid email id", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(userPass)) {
                    pass.setError("Enter password");

                }
                else if (userPass.length()<5) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(userConfirmPass)) {
                    confirmPass.setError("Enter confirm password");
                }
                else if (!userPass.equals(userConfirmPass)) {
                    Toast.makeText(getApplicationContext(), "Password would not matched", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Matched", Toast.LENGTH_SHORT).show();
                    //signup code goes here
                    RegisterNewAccount();
                }
            }
        });
    }

    private void RegisterNewAccount() {
        //creating new account on firebase for user
       // progressBar.setVisibility(View.VISIBLE);
        SignUPBtn.setVisibility(View.GONE);

        user.setName(userName);
        user.setEmail(userEmail);
        user.setPass(userPass);
        //user.setPhotoUrl("");

        //creating account
        mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            user.setUserId(currentUserId);
                            myRootRef.child("Users").child(currentUserId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //show message
                                    Toast.makeText(Register.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                   // Paper.book().write("user", user);
                                   // Paper.book().write("active", "user");

                                    Intent intent=new Intent(Register.this, Recycler_View.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, e.toString());
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Failed to Create Account..!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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

    private void initView() {
        //casting
        name = findViewById(R.id.Name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pwd);
        confirmPass = findViewById(R.id.Cpwd);
        //progressBar = findViewById(R.id.signup_progressbar);
        SignUPBtn = findViewById(R.id.btn_register);
        GoToLoginBtn = findViewById(R.id.loginNow);
        //initialize mauth
        mAuth = FirebaseAuth.getInstance();
        //getting path
        myRootRef = FirebaseDatabase.getInstance().getReference();
        //initialize function
        user = new User();

        //Utils.statusBarColor(SignupUserActivity.this);
    }
}