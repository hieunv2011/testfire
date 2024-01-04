package com.example.testing;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>  {
    Context context;
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
    final Date now = new Date();
    final String fileName = formatter.format(now);
    ArrayList<Student> list;


    public StudentAdapter(Context context, ArrayList<Student> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_student_item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Student student = list.get(position);
        holder.firstName.setText(student.getStudentName());
        holder.lastName.setText(student.getStudentId());
        holder.age.setText(student.getAge());
        holder.email.setText(student.getEmail());
//        holder.status.setText(student.getStatus());
        if (student.getStatus() != null && student.getStatus().equalsIgnoreCase(fileName)) {
            holder.status.setText("True");
            holder.checkBox.setChecked(true);
        } else {
            holder.status.setText("False");
            holder.checkBox.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView firstName, lastName, age,email,status;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            email=itemView.findViewById(R.id.email);
            firstName = itemView.findViewById(R.id.tvfirstName);
            lastName = itemView.findViewById(R.id.tvlastName);
            age = itemView.findViewById(R.id.tvage);
            status=itemView.findViewById(R.id.tvStatus);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}
