package com.nickmonks.todoister;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nickmonks.adapter.OnTodoClickListener;
import com.nickmonks.adapter.RecyclerViewAdapter;
import com.nickmonks.model.Task;
import com.nickmonks.model.TaskViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {

    public static final String TAG = "Database";
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create bottomSheetFragment:
        bottomSheetFragment = new BottomSheetFragment();

        //To manipulate the fragment we need to instantiate the ConstraintLayout that contains it
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        // define recycler view adapter:
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // layout manager will define the disposition of the viewHolder


        // create viewmodel for Task
        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskViewModel.class);

        // create ShareViewModel:
        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        // thanks to livedata, if there is any change we will see it automatically on the screen
        // WITHOUT REFRESHING THE SCREEN.
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                // since we get all our task here, we can define our recyclerview adapter here:
                recyclerViewAdapter = new RecyclerViewAdapter(tasks, MainActivity.this);
                recyclerView.setAdapter(recyclerViewAdapter);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            showBottomSheetDialog();
        });
    }

    private void showBottomSheetDialog() {
        // support manager will let us know if we have multiple fragments
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(int adapterPosition, Task task) {

        /*Edit the Task inside the bottom fragment; to do so, MainActivity passes the data
        to the corresponding fragment. The recommended way to do this is by using the ViewModel

        ViewModel will act as a middleware between Fragment - Activity, in a lifecycle conscious-way
        i.e., implements the observer pattern */

        // Set the mutable live data...
        Log.d("Click", "onTodoClick: share view model");
        sharedViewModel.setSelectedItem(task);
        sharedViewModel.setIsEdit(true);

        // SHOW the bottom sheet dialog
        showBottomSheetDialog();

        // ... pass it to the bottom fragment:


    }

    @Override
    public void onTodoRadioButtonClick(Task task) {
        Log.d("Click", "onTodoClick: " + task.getTask());
        TaskViewModel.delete(task);

        // Once deleted, we notify the entire recycler view that something as changed
        // So render again the View
        recyclerViewAdapter.notifyDataSetChanged();

    }
}