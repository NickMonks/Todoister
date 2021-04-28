package com.nickmonks.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
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

        // In order to define the color of the Chip in the Recycler View (To define the priority
        // visually ) we need to change the STATE of the color of that widget. To do so, we need to
        // define a colorstatelist object.

        // in fact, all object have this colorstatelist defined.

        ColorStateList colorStateList = new ColorStateList(new int[][]{

                // This is where we define the color state. For example when we press a button
                // the color will slightly change. this attribute is defined in the android attribute
                new int[]{-android.R.attr.state_enabled},
                new int[] {android.R.attr.state_enabled}

        }, new int[] {

                Color.LTGRAY, // disabled state
                Utils.priorityColor(task) // the actual color of the chip depending of the state
        });

        // this is the actual data, and we bind it to the ViewHolder views!
        // We can set, inside our inflated row:

        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);

        // change color state of the chip
        holder.todayChip.setTextColor(Utils.priorityColor(task));

        // to change tint, we need to pass the colorstatelist
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);


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
