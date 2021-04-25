package com.nickmonks.adapter;

import android.text.format.DateUtils;
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

    public RecyclerViewAdapter(List<Task> taskList) {
        this.taskList = taskList;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        // this class will HOLD the views from the layout defined on CreateViewHolder

        public AppCompatRadioButton radioButton;
        public AppCompatTextView task;
        public Chip todayChip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
        }
    }
}
