package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.provider.Category;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder>{

    ArrayList<Category> data = new ArrayList<Category>();

    public void setData(ArrayList<Category> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        CustomViewHolder viewHolder=new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (position == 0) {
            // If position is header position, initialize the card header
            holder.itemView.setBackgroundColor(Color.GREEN);
            holder.tvCategoryId.setText("Id");
            holder.tvCategoryName.setText("Name");
            holder.tvEvent.setText("Event Count");
            holder.tvIsActive.setText("Active?");
            holder.tvCategoryId.setTextColor(Color.BLACK);
            holder.tvCategoryName.setTextColor(Color.BLACK);
            holder.tvEvent.setTextColor(Color.BLACK);
            holder.tvIsActive.setTextColor(Color.BLACK);
        } else {
            // Populate with data
            // Adjust position to skip the header
//            Category category = data.get(position - 1); // Subtract 1 to skip the header
            holder.tvCategoryId.setText(data.get(position).getCategoryId());
            holder.tvCategoryName.setText(data.get(position).getName());
            holder.tvEvent.setText(String.valueOf(data.get(position).getEvent()));
            if (data.get(position).isActive()) {
                holder.tvIsActive.setText("Active");
            } else {
                holder.tvIsActive.setText("Inactive");
            }
//            holder.cardView = holder.itemView.findViewById(R.id.card_view); // Initialize cardView
            holder.cardView.setOnClickListener(v -> {
                String categoryLocation = data.get(position).getLocation();
                Context context =holder.cardView.getContext();
                Intent intent = new Intent(context,GoogleMapActivity.class);
                intent.putExtra("categoryLocation",categoryLocation);
                context.startActivity(intent);

                // TODO: Launch new MapsActivity with Country Name in extras
            });
        }


    }

    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size(); // then return the size of ArrayList
        }

        // else return zero if data is null
        return 0;

    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategoryName;
        public TextView tvCategoryId;
        public TextView tvEvent;
        public TextView tvIsActive;
        public View cardView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            tvCategoryId = itemView.findViewById(R.id.tv_id);
            tvCategoryName = itemView.findViewById(R.id.tv_name);
            tvEvent = itemView.findViewById(R.id.tv_event);
            tvIsActive = itemView.findViewById(R.id.tv_active);
        }
    }
}
