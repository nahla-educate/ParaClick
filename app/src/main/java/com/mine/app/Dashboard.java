package com.mine.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.mine.app.Auth.LoginAdmin;
import com.mine.app.Products.AddProduct;
import com.mine.app.Products.Recycler_View;
import com.mine.app.Utility.NetworkChangeListener;

public class Dashboard extends AppCompatActivity {

    //listener to wifi status
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    RelativeLayout addProduct,viewProducts,
            viewUsers,logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initView();
        initEvent();
    }
    private void initView() {
        addProduct = findViewById(R.id.new_product_layout);
        viewProducts = findViewById(R.id.view_all_products);
        viewUsers = findViewById(R.id.view_user);
        logoutBtn = findViewById(R.id.logout_btn);
    }

    private void initEvent(){
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, AddProduct.class);
                startActivity(intent);
            }});

        viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Recycler_View.class);
                startActivity(intent);
            }});

        viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ListUsers.class);
                startActivity(intent);
            }});

        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginAdmin.class);
                startActivity(intent);
                finish();
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

}