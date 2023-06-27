package com.mine.app.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.mine.app.MainActivity;
import com.mine.app.Models.Admin;
import com.mine.app.R;
import com.mine.app.Utility.NetworkChangeListener;

public class RegisterAdmin extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private static final String TAG = "signupTag";
    Admin admin;
    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference myRootRef;
    String adminName, adminEmail, adminPass;
    private EditText name, email, pass;
    private Button SignUPBtn;
    private TextView GoToLoginBtn;
    //private ProgressBar progressBar;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    // private int selectedGender=1;
    //RadioGroup radioGroupGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        initView();
        initEvent();
    }

    private void initEvent() {


        GoToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterAdmin.this, LoginAdmin.class);
                startActivity(intent);
            }
        });
        SignUPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminName = name.getText().toString().trim();
                adminEmail = email.getText().toString().trim();
                adminPass = pass.getText().toString().trim();


                if (TextUtils.isEmpty(adminName)) {
                    name.setError("Enter Full name");
                } else if (TextUtils.isEmpty(adminEmail)) {
                    email.setError("Enter email");
                }
                else if (!adminEmail.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address, enter valid email id", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(adminPass)) {
                    pass.setError("Enter pass");
                }
                else {
                    //signup code goes here
                    RegisterNewAccount();
                }
            }
        });
    }

    private void RegisterNewAccount() {
        //creating new account on firebase for admin
        // progressBar.setVisibility(View.VISIBLE);
        SignUPBtn.setVisibility(View.GONE);

        admin.setName(adminName);
        admin.setEmail(adminEmail);
        admin.setPass(adminPass);
        admin.setUserType("admin");
        //admin.setPhotoUrl("");

        //creating account
        mAuth.createUserWithEmailAndPassword(adminEmail, adminPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in admin's information
                            Log.d(TAG, "createUserWithEmail:success");
                            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            admin.setAdminId(currentUserId);
                            myRootRef.child("Admin").child(currentUserId).setValue(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //show message
                                    Toast.makeText(RegisterAdmin.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                    // Paper.book().write("user", user);
                                    // Paper.book().write("active", "user");

                                    Intent intent=new Intent(RegisterAdmin.this, MainActivity.class);
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
                            Toast.makeText(RegisterAdmin.this, "Failed to Create Account..!", Toast.LENGTH_SHORT).show();
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
        //progressBar = findViewById(R.id.signup_progressbar);
        SignUPBtn = findViewById(R.id.btn_register);
        GoToLoginBtn = findViewById(R.id.loginNow);
        //initialize mauth
        mAuth = FirebaseAuth.getInstance();
        //getting path
        myRootRef = FirebaseDatabase.getInstance().getReference();
        //initialize function
        admin = new Admin();

        //Utils.statusBarColor(SignupUserActivity.this);
    }
}