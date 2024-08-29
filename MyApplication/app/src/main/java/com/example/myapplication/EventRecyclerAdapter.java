package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.myapplication.provider.Event;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.CustomViewHolder>{
    ArrayList<Event> data = new ArrayList<Event>();
    public void setData(ArrayList<Event> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout,parent,false);
        EventRecyclerAdapter.CustomViewHolder viewHolder=new EventRecyclerAdapter.CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvEventId.setText(data.get(position).getEventId());
        holder.tvEventName.setText(data.get(position).getEventName());
        holder.tvCategoryId.setText(data.get(position).getCategoryId());
        holder.tvTickets.setText(String.valueOf(data.get(position).getTicketsAvailable()));
        if (data.get(position).isEventActive()){
            holder.tvIsActive.setText("Active");
        } else{
            holder.tvIsActive.setText("Inactive");
        }
        holder.cardView.setOnClickListener(v -> {
            String categoryLocation = data.get(position).getEventName();
            Context context =holder.cardView.getContext();
            Intent intent = new Intent(context,EventGoogleResult.class);
            intent.putExtra("eventName",categoryLocation);
            context.startActivity(intent);


            // TODO: Launch new MapsActivity with Country Name in extras
        });
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

        public TextView tvEventId;
        public TextView tvEventName;
        public TextView tvCategoryId;
        public TextView tvTickets;
        public TextView tvIsActive;
        public View cardView;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            tvEventId = itemView.findViewById(R.id.textViewEventId);
            tvEventName = itemView.findViewById(R.id.textViewEventName);
            tvCategoryId = itemView.findViewById(R.id.textViewCategoryId);
            tvTickets = itemView.findViewById(R.id.textViewTickets);
            tvIsActive = itemView.findViewById(R.id.textViewEventActive);
        }
    }
}
