package com.bibhuti.todoapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import com.bibhuti.todoapp.model.Priority;
import com.bibhuti.todoapp.model.SharedViewModel;
import com.bibhuti.todoapp.model.Task;
import com.bibhuti.todoapp.model.TaskViewModel;
import com.bibhuti.todoapp.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText entertask;
    private ImageButton calenderButton;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate;
    private SharedViewModel sharedViewModel;
    private boolean isUpdate;
    private Priority priority;
    private boolean task_status;
    private ImageButton isCompleted;

    private TextView dueDateDisplay;
    Calendar calendar;
    Date date;

    public BottomSheetFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calenderButton = view.findViewById(R.id.today_calendar_button);
        calendar = Utils.getNewCalendarInstance();
        entertask = view.findViewById(R.id.enter_todo_et);
        date = calendar.getTime();
        dueDateDisplay = view.findViewById(R.id.dueDateFinal);
        isCompleted = view.findViewById(R.id.complete_status);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    private void setDueDateView(Date date){
        SimpleDateFormat format = new SimpleDateFormat("E, MMM dd");
        String dateString = format.format(date);
        dueDateDisplay.setText(dateString);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        isUpdate = sharedViewModel.getIsEdit();

        calenderButton.setOnClickListener(v -> {
            dueDateDisplay.setVisibility(dueDateDisplay.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            Utils.hideSoftKeyboard(v);
            if(isUpdate){
                Task selectedTask = sharedViewModel.getSelectedItem().getValue();
                calendar.setTime(selectedTask.dueDate);
                date = selectedTask.dueDate;
                setDueDateView(selectedTask.dueDate);
            }
        });

        calendarView.setOnDateChangeListener((view12, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
            calendar.clear();
            calendar.setTime(date);
            setDueDateView(dueDate);
        });

        priorityButton.setOnClickListener(view2 -> {
            Utils.hideSoftKeyboard(view2);
            priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            if (isUpdate) {
                priorityRadioGroup.clearCheck();
                Task selectedTask = sharedViewModel.getSelectedItem().getValue();
                priority = selectedTask.getPriority();
                if (priority == Priority.HIGH) {
                    priorityRadioGroup.check(R.id.radioButton_high);
                } else if (priority == Priority.MEDIUM) {
                    priorityRadioGroup.check(R.id.radioButton_med);
                } else if (priority == Priority.LOW) {
                    priorityRadioGroup.check(R.id.radioButton_low);
                }

            }
            priorityRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
                if (priorityRadioGroup.getVisibility() == View.VISIBLE) {
                    selectedButtonId = checkedId;
                    selectedRadioButton = view.findViewById(selectedButtonId);
                    if (selectedRadioButton.getId() == R.id.radioButton_high) {
                        priority = Priority.HIGH;
                    } else if (selectedRadioButton.getId() == R.id.radioButton_med) {
                        priority = Priority.MEDIUM;
                    } else if (selectedRadioButton.getId() == R.id.radioButton_low) {
                        priority = Priority.LOW;
                    } else {
                        priority = Priority.LOW;
                    }
                } else {
                    priority = Priority.LOW;
                }
            });
        });

        isCompleted.setOnClickListener(v -> {
                if(task_status) {
                    task_status = false;
                    isCompleted.setColorFilter(Color.RED);
                }
            else{
                task_status = true;
                isCompleted.setColorFilter(getResources().getColor(R.color.buttons));
            }
        });

        saveButton.setOnClickListener(view1 -> {
            String task = entertask.getText().toString().trim();
            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null) {
                if (isUpdate) {
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);
                    updateTask.setDone(task_status);
                    TaskViewModel.update(updateTask);
                    sharedViewModel.setIsEdit(false);
                } else {
                    Task myTask = new Task(task, priority, dueDate, Calendar.getInstance().getTime(), false);
                    TaskViewModel.insert(myTask);
                }
                if (this.isVisible()) {
                    this.dismiss();
                }
            } else {
                if (TextUtils.isEmpty(task)) {
                    entertask.requestFocus();
                    entertask.setError("Empty Field");
                } else if (dueDate == null) {
                    calenderButton.requestFocus();
                    calenderButton.setBackgroundColor(getResources().getColor(R.color.primary));
                    if (calendarView.getVisibility() == View.GONE) {
                        calenderButton.performClick();
                    }
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            calenderButton.setBackgroundColor(Color.WHITE);
                        }
                    }, 3000);
                } else if (priority == null) {
                    priorityButton.requestFocus();
                    if (priorityRadioGroup.getVisibility() == View.GONE) {
                        priorityButton.performClick();
                    }
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            priorityButton.setBackgroundColor(Color.WHITE);
                        }
                    }, 3000);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        calendar.setTime(date);
        int id = v.getId();
        if (id == R.id.today_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            dueDate = calendar.getTime();
        } else if (id == R.id.tomorrow_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();
        } else if (id == R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
        }
        setDueDateView(dueDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue() != null) {
            isUpdate = sharedViewModel.getIsEdit();
            if (isUpdate) {
                Task task = sharedViewModel.getSelectedItem().getValue();
                entertask.setText(task.getTask());
                dueDate = task.getDueDate();
                priority = task.getPriority();
                task_status = task.isDone();
                isCompleted.setVisibility(View.VISIBLE);
                if (task_status) {
                    isCompleted.setColorFilter(getResources().getColor(R.color.buttons));
                } else {
                    isCompleted.setColorFilter(Color.RED);
                }
            } else {
                priorityRadioGroup.clearCheck();
                entertask.setText("");
            }
        }
    }
}
