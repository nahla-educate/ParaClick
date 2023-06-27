package com.mine.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mine.app.Adapters.ProductAdapter;
import com.mine.app.Adapters.UserAdapter;
import com.mine.app.Models.ProductModel;
import com.mine.app.Models.User;
import com.mine.app.Models.Utils;
import com.mine.app.Utility.NetworkChangeListener;

import java.util.ArrayList;

public class ListUsers extends AppCompatActivity {

    //listener to wifi status
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private UserAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<User> userArrayList;

    DatabaseReference myRootRef;
    private ProgressBar progressBar;
    private TextView noJokeText;
    private EditText nameInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        userArrayList =new ArrayList<User>();
        recyclerView =findViewById(R.id.product_list);
        progressBar = findViewById(R.id.spin_progress_bar);
        noJokeText = findViewById(R.id.no_product);
        nameInput = findViewById(R.id.name_input);
        myRootRef = FirebaseDatabase.getInstance().getReference();
        Utils.statusBarColor(ListUsers.this);

        mAdapter = new UserAdapter(userArrayList, ListUsers.this,true);
        mAdapter.notifyDataSetChanged();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        getDataFromFirebase();

        searchFunc();

    }
    private void searchFunc() {
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    if(userArrayList.size()!=0){
                        recyclerView.setVisibility(View.VISIBLE);
                        noJokeText.setVisibility(View.GONE);
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        noJokeText.setVisibility(View.VISIBLE);
                    }

                    mAdapter = new UserAdapter(userArrayList,ListUsers.this,true);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<User> clone = new ArrayList<>();
                    for (User element : userArrayList) {
                        if (element.getName().toLowerCase().contains(s.toString().toLowerCase())) {
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

                    mAdapter = new UserAdapter(clone,ListUsers.this,true);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getDataFromFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        final int[] counter = {0};
        myRootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        userArrayList.add(user);
                        counter[0]++;
                        if (counter[0] == dataSnapshot.getChildrenCount()) {
                            setData();
                            progressBar.setVisibility(View.GONE);
                        }
                        Log.d("ShowEventInfo:", user.toString());
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

    private void setData() {
        if(userArrayList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            noJokeText.setVisibility(View.GONE);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            noJokeText.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
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