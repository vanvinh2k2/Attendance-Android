package com.example.myattendanceapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myattendanceapp.Interface.ItemClickListen;
import com.example.myattendanceapp.Model.ClassItem;
import com.example.myattendanceapp.R;
import com.example.myattendanceapp.StudentActivity;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.HolderClassRoom>{
    ArrayList<ClassItem> roomArrayList;
    Context context;

    public ClassAdapter(ArrayList<ClassItem> roomArrayList, Context context) {
        this.roomArrayList = roomArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderClassRoom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class,parent,false);
        return new HolderClassRoom(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderClassRoom holder, int position) {
        ClassItem room = roomArrayList.get(position);
        holder.objecttxt.setText(room.getSubject_name());
        holder.codetxt.setText(room.getSubject_code());
        holder.setItemClickListen(new ItemClickListen() {
            @Override
            public void setOnItemClick(View view, int pos, boolean isClick) {
                if(!isClick){
                    Intent intent = new Intent(context, StudentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("dulieu",room);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomArrayList.size();
    }

    class HolderClassRoom extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView codetxt, objecttxt;
        ItemClickListen itemClickListen;
        public HolderClassRoom(@NonNull View itemView) {
            super(itemView);
            codetxt = itemView.findViewById(R.id.namecode);
            objecttxt = itemView.findViewById(R.id.object);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListen.setOnItemClick(v, getAdapterPosition(),false);
        }

        public void setItemClickListen(ItemClickListen itemClickListen) {
            this.itemClickListen = itemClickListen;
        }
    }
}
