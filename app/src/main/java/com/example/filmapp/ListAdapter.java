package com.example.filmapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context context;
    ArrayList<Item> items;
    ArrayList<String> titles;
    ArrayList<String> descriptions;
    ArrayList<String> dates;

    public ListAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.title.setText(items.get(position).title);
        holder.desc.setText(items.get(position).description);
        holder.date.setText(items.get(position).date);
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView desc;
        TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.item_title);
            desc= (TextView) itemView.findViewById(R.id.item_description);
            date= (TextView) itemView.findViewById(R.id.item_date);
        }
    }
}
