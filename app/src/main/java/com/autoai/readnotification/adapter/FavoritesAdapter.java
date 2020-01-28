package com.autoai.readnotification.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autoai.readnotification.R;
import com.autoai.readnotification.models.Contacts;

import java.util.ArrayList;


public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>
{
    Context context;
    ArrayList<Contacts> arrayList;

    public FavoritesAdapter(ArrayList<Contacts> arrayList, Context context) {
        this.context = context;
        this.arrayList= arrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fav,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Contacts contacts=arrayList.get(position);
        holder.name.setText(contacts.getName());
        holder.number.setText(contacts.getNumber());
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //context.startActivity(new Intent(context, RideActivity.class).putExtra("key","Alpha One Mall"));


    }
});

    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

TextView name, number;
        public MyViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            number=itemView.findViewById(R.id.number);

        }
    }
}
