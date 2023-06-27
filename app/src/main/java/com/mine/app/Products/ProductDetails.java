package com.mine.app.Products;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mine.app.Auth.Login;
import com.mine.app.MapLocation;
import com.mine.app.Models.Order;
import com.mine.app.Models.ProductModel;
import com.mine.app.Models.Utils;
import com.mine.app.Page_accueil;
import com.mine.app.Profile;
import com.mine.app.R;
import com.mine.app.Settings;
import com.mine.app.Utility.NetworkChangeListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ProductDetails extends AppCompatActivity {
    //listener to wifi status
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private ImageView productImg;
    private TextView productName,productDescription,price;
    ProductModel product;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        product= (ProductModel) getIntent().getSerializableExtra("product");
        Log.d("TAG7", "Message to log"+product);


            if(product.getProductIcon()!=null){
                if(!product.getProductIcon().equals("")){
                    Picasso.get().load(product.getProductIcon()).placeholder(R.drawable.ic_menu_camera).into(productImg);
                }
            }
            productName.setText(product.getProductTitle());
            productDescription.setText(product.getProductDescription());
            price.setText("dt"+product.getOriginalPrice());

        }



        private void initView() {
            productImg=findViewById(R.id.product_img);
            productName=findViewById(R.id.product_name);
            price=findViewById(R.id.product_price);
            productDescription=findViewById(R.id.product_description);

            product=new ProductModel();
            Utils.statusBarColor(ProductDetails.this);
        }

        public void goBack(View view) {
            finish();
        }

        @Override
        protected void onResume() {
            super.onResume();

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