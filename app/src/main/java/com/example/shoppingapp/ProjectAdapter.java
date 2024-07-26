package com.example.shoppingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.Viewholder> {
    ArrayList<ProjectModel> list;
    Context context;
    DatabaseReference databaseReference;

    public ProjectAdapter(ArrayList<ProjectModel> list, Context context) {
        this.list = list;
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("product");
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_product_design, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ProjectModel model = list.get(position);

        Picasso.get().load(model.getIvProductImage()).placeholder(R.drawable.redshirt)
                .into(holder.newProductImage);
        holder.newHeadline.setText(model.getTvHeadline());
        holder.newDescription.setText(model.getTvDescription());
        holder.newPrice.setText(model.getTvPrice());
        holder.newBrand.setText(model.getTvBrand());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView newHeadline, newPrice, newDescription, newBrand;
        ImageView newProductImage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            newHeadline = itemView.findViewById(R.id.newHeadline);
            newDescription = itemView.findViewById(R.id.newDescription);
            newPrice = itemView.findViewById(R.id.newPrice);
            newBrand = itemView.findViewById(R.id.newBrand);
            newProductImage = itemView.findViewById(R.id.newProductImage);
        }
    }
}