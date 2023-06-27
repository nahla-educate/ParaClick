package com.mine.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mine.app.Auth.Login;
import com.mine.app.Products.ListOrders;
import com.mine.app.Products.ProductDetails;
import com.mine.app.Products.Recycler_View;

public class Page_accueil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
        private DrawerLayout drawer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_page_accueil);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            if (savedInstanceState == null) {
                /***afficher fragment
                 getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_page_accueil, new HomeFragment())
                        .commit();*/
               /* Intent intentHome = new Intent(Page_accueil.this, Recycler_View.class);
                startActivity(intentHome);
                finish();
                navigationView.setCheckedItem(R.id.nav_home); // set the initial checked item*/
            }
        }

        // disable the back button while the drawer is open
        @Override
        public void onBackPressed() {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuProfilee:
                    Intent intent = new Intent(Page_accueil.this, Profile.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.homeMenu:
                    Intent intentHome = new Intent(Page_accueil.this, Recycler_View.class);
                    startActivity(intentHome);
                    finish();
                    break;
                case R.id.settingsMenu:
                    Intent intentSettings = new Intent(Page_accueil.this, Settings.class);
                    startActivity(intentSettings);
                    finish();

                    break;



                case R.id.locationMenu:
                    Intent intentLocation = new Intent(Page_accueil.this, MapLocation.class);
                    startActivity(intentLocation);
                    finish();

                    break;
                case R.id.nav_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent intentLog = new Intent(getApplicationContext(), Login.class);
                    startActivity(intentLog);
                    finish();

                    break;
                // add more cases for other menu items


            }Log.d("TAG", "nooooooooooooooo one " );

            // close the drawer after an item is selected
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }
