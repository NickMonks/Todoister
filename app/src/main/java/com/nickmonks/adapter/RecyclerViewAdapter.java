package com.nickmonks.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.nickmonks.Util.Utils;
import com.nickmonks.model.Task;
import com.nickmonks.todoister.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<Task> taskList;
    private final OnTodoClickListener todoClickListener;

    public RecyclerViewAdapter(List<Task> taskList, OnTodoClickListener todoClickListener) {
        this.taskList = taskList;
        this.todoClickListener = todoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate our row layout that will be attached to our recycler view

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the views and the data from the repository

        Task task = taskList.get(position);
        String formatted = Utils.formatDate(task.getDueDate());

        // this is the actual data, and we bind it to the ViewHolder views!
        // We can set, inside our inflated row:
        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // this class will HOLD the views from the layout defined on CreateViewHolder

        public AppCompatRadioButton radioButton;
        public AppCompatTextView task;
        public Chip todayChip;

        OnTodoClickListener onTodoClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
            this.onTodoClickListener = todoClickListener;

            // we attached to our itemView a click listener, so when we click it we invoke the interface method
            // which will be implemented on the activity level
            itemView.setOnClickListener(this);

            // Radio Button: Once selected, it will delete the task
            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // id of the view pressed:
            int id=v.getId();
            Task currTask = taskList.get(getAdapterPosition());
            // If we press on the todo_layout (the inflated layout of our row) then:
            if (id == R.id.todo_row_layout) {
                Log.d("Click", "onClick: " + getAdapterPosition());
                onTodoClickListener.onTodoClick(getAdapterPosition(), currTask);
            } else if (id == R.id.todo_radio_button) {
                onTodoClickListener.onTodoRadioButtonClick(currTask);
            }

        }
    }


}
