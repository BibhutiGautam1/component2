package com.bibhuti.todoapp.adapter;

import com.bibhuti.todoapp.model.Task;

public interface OnTaskClickListener {
    void onTaskClick(Task task);
    void onTodoRadioButtonClick(Task task);
    void onButtonClick(Task task);
}
