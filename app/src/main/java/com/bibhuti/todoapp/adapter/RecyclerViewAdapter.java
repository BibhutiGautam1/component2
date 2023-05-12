package com.bibhuti.todoapp.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bibhuti.todoapp.R;
import com.bibhuti.todoapp.model.Task;
import com.bibhuti.todoapp.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<Task> taskList;
    private final OnTaskClickListener taskClickListener;

    public RecyclerViewAdapter(List<Task> taskList, OnTaskClickListener taskClickListener) {
        this.taskClickListener = taskClickListener;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        String formatted = Utils.formatDate(task.getDueDate());

        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        },
                new int[]{
                        Color.LTGRAY, //disabled
                        Utils.priorityColor(task)
                });

        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);
        holder.isCompleted.setText(task.isDone ? "Completed" : "Pending" );
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.radioButton.setButtonTintList(colorStateList);
        }
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public Task getTodoAt(int position){
        return taskList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RadioButton radioButton;
        public TextView task;
        public Chip todayChip;
        public TextView isCompleted;
        public Button shareButton;

        OnTaskClickListener onTaskClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            isCompleted = itemView.findViewById(R.id.status);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
            shareButton = itemView.findViewById(R.id.share_task_details);
            this.onTaskClickListener =  taskClickListener;
            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
            shareButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Task currentTask = taskList.get(getAdapterPosition());
            int id = v.getId();
            if(id == R.id.todo_row_layout){
                onTaskClickListener.onTaskClick(currentTask);
            }
            else if (id == R.id.todo_radio_button){
                onTaskClickListener.onTodoRadioButtonClick(currentTask);
            }
            else if(id == R.id.share_task_details){
                onTaskClickListener.onButtonClick(currentTask);
            }
        }

    }

}
