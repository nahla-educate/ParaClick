package com.mine.app.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mine.app.Auth.Login;
import com.mine.app.Models.User;
import com.mine.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    List<User> myJokesList;
    Activity context;
    boolean isAdmin;
    public UserAdapter(List<User> usersList, Activity context, boolean isAdmin) {
        this.myJokesList = usersList;
        this.context = context;
        this.isAdmin=isAdmin;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_user, parent, false);

        return new UserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {

        User user=myJokesList.get(position);

        if(user.getPhotoUrl()!=null){
            if(!user.getPhotoUrl().equals("")){
                holder.userImg.setVisibility(View.VISIBLE);
                Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.baseline_arrow_back_ios_new_24).into(holder.userImg);
            }
        }
        holder.name.setText(user.getName());

        holder.email.setText(user.getEmail());

        holder.age.setText(user.getAge());


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAdmin){
                    Intent intent=new Intent(context, Login.class);
                    intent.putExtra("user", String.valueOf(user));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myJokesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        ImageView userImg;
        TextView name,email,age;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.parent);
            userImg = itemView.findViewById(R.id.p_img);
            name = itemView.findViewById(R.id.order_status);

            email = itemView.findViewById(R.id.order_total_price);

            age = itemView.findViewById(R.id.date_of_order);

        }
    }
}

