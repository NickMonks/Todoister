package com.nickmonks.adapter;

import com.nickmonks.model.Task;

public interface OnTodoClickListener {
    // the method will pass the task and the position inside the recycler view
    void onTodoClick(int adapterPosition, Task task);
    void onTodoRadioButtonClick(Task task);
}


