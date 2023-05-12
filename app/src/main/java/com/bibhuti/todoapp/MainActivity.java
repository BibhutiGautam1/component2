package com.bibhuti.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bibhuti.todoapp.adapter.OnTaskClickListener;
import com.bibhuti.todoapp.adapter.RecyclerViewAdapter;
import com.bibhuti.todoapp.model.SharedViewModel;
import com.bibhuti.todoapp.model.Task;
import com.bibhuti.todoapp.model.TaskViewModel;
import com.bibhuti.todoapp.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements OnTaskClickListener {

    private static final String TAG = "Task";
    BottomSheetFragment bottomSheetFragment;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(TaskViewModel.class);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        taskViewModel.getallTasks().observe(this, tasks -> {
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Task task = recyclerViewAdapter.getTodoAt(viewHolder.getAdapterPosition());
                        if(direction == ItemTouchHelper.LEFT){
                            onTodoRadioButtonClick(task);
                        }
                        else if(direction == ItemTouchHelper.RIGHT){
                            onTaskClick(task);
                        }
                    }
                }).attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            sharedViewModel.setIsEdit(false);
            showBottomSheetDialog();
        });
    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if(id == R.id.action_deletCompleted){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete Completed task?")
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TaskViewModel.deleteCompleted();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        if(id == R.id.action_deleteAll){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete all tasks?")
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TaskViewModel.deleteAll();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskClick(Task task) {
    sharedViewModel.selectItem(task);
    sharedViewModel.setIsEdit(true);
    showBottomSheetDialog();
    recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTodoRadioButtonClick(Task task) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this task?")
                .setPositiveButton("Cancel", (dialog, id) -> {
                })
                .setNegativeButton("Delete", (dialog, id) -> TaskViewModel.delete(task));
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onButtonClick(Task task) {
        String text = task.getTask() + " : " + Utils.formatDate(task.getDueDate()) + " : " + task.getPriority();
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share this task with:")
                .setText(text)
                .startChooser();
    }

}