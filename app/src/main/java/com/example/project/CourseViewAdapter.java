package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.Course;

import java.util.List;



public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {
    private final List<Course> courses;

    public CourseViewAdapter(List<Course> courses) {
        super();
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_person_detail_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewAdapter.ViewHolder holder, int position) {
        String courseName = courses.get(position).getSubjectAndNumber();
        int year = courses.get(position).getYear();
        String quarter = courses.get(position).getQuarter();
        String text = courseName + ", " + quarter + " " + String.valueOf(year);
        holder.courseView.setText(text);
    }

    @Override
    public int getItemCount() {
        return this.courses.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
             {
        private final TextView courseView;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseView = itemView.findViewById(R.id.course_row);
        }
    }
}
