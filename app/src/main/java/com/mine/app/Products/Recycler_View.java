package com.mine.app.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mine.app.Adapters.ProductAdapter;
import com.mine.app.Auth.Login;
import com.mine.app.MainActivity;
import com.mine.app.MapLocation;
import com.mine.app.Models.ProductModel;
import com.mine.app.Models.Utils;
import com.mine.app.Profile;
import com.mine.app.R;
import com.mine.app.Settings;
import com.mine.app.Utility.NetworkChangeListener;

import java.util.ArrayList;

public class Recycler_View extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //listener to wifi status
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private AppCompatButton mButtonEdit,mButtonDelete;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private ProductAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<ProductModel> productArrayList;

    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noJokeText, nameUser, emailUser;
    private EditText nameInput;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recycler_view);
            initView();





            productArrayList =new ArrayList<ProductModel>();

            setSupportActionBar(toolbar);
            navigationView.setNavigationItemSelectedListener(this);

            //open menu
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            myRootRef = FirebaseDatabase.getInstance().getReference();
            Utils.statusBarColor(Recycler_View.this);

            mAdapter = new ProductAdapter(productArrayList, Recycler_View.this,true);
            mAdapter.notifyDataSetChanged();
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            getDataFromFirebase();

            // In your Home activity:
         /*   String email = getIntent().getStringExtra("email");
            String name = getIntent().getStringExtra("name");
            Log.d("TAG", "Email from database to list: " + email);
            Log.d("TAG", "Email from database to list: " + name);*/

           /* Intent intent = new Intent(Recycler_View.this, Profile.class);
            intent.putExtra("email", email);
            intent.putExtra("name", name);
            startActivity(intent);*/



            searchFunc();


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
                // In your Home activity:
                String email = getIntent().getStringExtra("email");
                String name = getIntent().getStringExtra("name");
                Log.d("TAG", "Email from database to list: " + email);
                Log.d("TAG", "Email from database to list: " + name);

                Intent intent = new Intent(Recycler_View.this, Profile.class);

                intent.putExtra("email", email);
                intent.putExtra("name", name);
                startActivity(intent);
                finish();
                break;
            case R.id.homeMenu:
                Intent intentHome = new Intent(Recycler_View.this, Recycler_View.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.settingsMenu:
                Intent intentSettings = new Intent(Recycler_View.this, Settings.class);
                startActivity(intentSettings);
                finish();

                break;

            case R.id.locationMenu:
                Intent intentLocation = new Intent(Recycler_View.this, MapLocation.class);
                startActivity(intentLocation);
                finish();
                    /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_page_accueil, new LocationFragment())
                        .commit();*/

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
        private void initView(){

            recyclerView =findViewById(R.id.product_list);
            progressBar = findViewById(R.id.spin_progress_bar);
            noJokeText = findViewById(R.id.no_product);
            nameInput = findViewById(R.id.name_input);

            mButtonEdit = findViewById(R.id.btn_update);
            mButtonDelete = findViewById(R.id.btn_delete);



            nameUser =findViewById(R.id.name_user);

            toolbar = findViewById(R.id.toolbar);

            drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
        }
    private void setData() {
        if(productArrayList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            noJokeText.setVisibility(View.GONE);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            noJokeText.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }


        public void getDataFromFirebase() {
            progressBar.setVisibility(View.VISIBLE);
            final int[] counter = {0};
            myRootRef.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            ProductModel product = child.getValue(ProductModel.class);
                            productArrayList.add(product);
                            counter[0]++;
                            if (counter[0] == dataSnapshot.getChildrenCount()) {
                                setData();
                                progressBar.setVisibility(View.GONE);
                            }
                            Log.d("ShowEventInfo:", product.toString());
                        }
                    }
                    else{
                        noJokeText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    private void searchFunc() {
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    if(productArrayList.size()!=0){
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new ProductAdapter(productArrayList,Recycler_View.this,true);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<ProductModel> clone = new ArrayList<>();
                    for (ProductModel element : productArrayList) {
                        if (element.getProductTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                            clone.add(element);
                        }
                    }
                    if(clone.size()!=0){
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new ProductAdapter(clone,Recycler_View.this,true);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




        public void goBack(View view) {
            finish();
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