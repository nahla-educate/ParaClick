package com.mine.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    Button button;
    FirebaseAuth auth;
    FirebaseUser user;
    TextView txtView;
    ImageView imageMenu;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initEvent();

        navigationView.setItemIconTintList(null);

        //NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
       // NavigationUI.setupWithNavController(navigationView,navController);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


    }
    /**
     * Récupération des widget
     */
    private void initView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        txtView = findViewById(R.id.textTitle);
        imageMenu = findViewById(R.id.imageMenu);
        navigationView = findViewById(R.id.navigationView);
    }
    /**
     * Déclaration des events
     */
    private void initEvent() {

        imageMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);}
        });


    }

}