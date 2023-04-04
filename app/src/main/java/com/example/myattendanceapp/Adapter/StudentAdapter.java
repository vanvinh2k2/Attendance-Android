package com.example.myattendanceapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myattendanceapp.Interface.ItemClickListen;
import com.example.myattendanceapp.Model.Student;
import com.example.myattendanceapp.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.HolderStudentAdapter> {
    Context context;
    List<Student> studentItemList;

    public StudentAdapter(Context context, List<Student> studentItemList) {
        this.context = context;
        this.studentItemList = studentItemList;
    }

    @NonNull
    @Override
    public HolderStudentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student,parent,false);
        return new HolderStudentAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderStudentAdapter holder, int position) {
        Student studentItem = studentItemList.get(position);
        holder.status.setText(studentItem.getStatus());
        holder.roll.setText(studentItem.getRoll());
        holder.name.setText(studentItem.getName());
        holder.setItemClickListen(new ItemClickListen() {
            @Override
            public void setOnItemClick(View view, int pos, boolean isClick) {
                if(!isClick){
                    if(studentItem.getStatus()=="A") {
                        studentItemList.get(pos).setStatus("P");
                        String color = "#DAF884";
                        holder.cardView.setBackgroundColor(Color.parseColor(color));
                    }
                    else {
                        studentItemList.get(pos).setStatus("A");
                        String color = "#0D000000";
                        holder.cardView.setBackgroundColor(Color.parseColor(color));
                    }
                    holder.status.setText(studentItem.getStatus());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentItemList.size();
    }

    class HolderStudentAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView roll,name,status;
        ItemClickListen itemClickListen;
        LinearLayout cardView;
        public HolderStudentAdapter(@NonNull View itemView) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll);
            name = itemView.findViewById(R.id.namecode);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListen.setOnItemClick(v,getAdapterPosition(),false);
        }

        public void setItemClickListen(ItemClickListen itemClickListen) {
            this.itemClickListen = itemClickListen;
        }
    }
}
