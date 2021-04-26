package com.nickmonks.todoister;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.nickmonks.Util.Utils;
import com.nickmonks.model.Priority;
import com.nickmonks.model.Task;
import com.nickmonks.model.TaskViewModel;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

// A fragment allows to create the sensation of having one single page for the user
// and adapts much better to different devices. Needs to be attached to another fragment
// or an activity

// In this case, when we click the fab button, it will pop up under the main_layout

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    private EditText enterTodo;
    private ImageButton calendarButton;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;    // Group objects basically GROUP Views to manipulate alltogether
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private SharedViewModel sharedViewModel;
    private boolean isEdit;

    private Date dueDate;
    Calendar calendar = Calendar.getInstance();

    // An important requirement is to have an empty constructor!
    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        // Inside this method we also bind the data here!

        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarButton = view.findViewById(R.id.today_calendar_button);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);

        // THis fragment implements onclickListener to be able to pass 'this' instance directly for click events
        Chip todayChip = view.findViewById(R.id.today_chip);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);

        todayChip.setOnClickListener(this);
        tomorrowChip.setOnClickListener(this);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    // this is called right after all view widgets are created
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // poll the data from the VewModel LiveData:
        // PD. requireActivity will retrieve the activity that hosts this fragment
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);


        calendarButton.setOnClickListener(v -> {
            // toggle functionality: show when click once; hide click twice
            calendarGroup.setVisibility(
                    calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );

            // hide keyboards:
            Utils.hideSoftKeyboard(v);
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Create a date object - Use the calendar object and adapt it to a Date format
                // using getTime method
                calendar.clear();
                calendar.set(year, month, dayOfMonth);
                dueDate = calendar.getTime();
            }
        });

        saveButton.setOnClickListener(v -> {
            String task = enterTodo.getText().toString().trim();
            Log.d("TAG", "onViewCreated: "+task);
            if (!TextUtils.isEmpty(task) && dueDate != null){
                Log.d("TAG", "onViewCreated: is not empty");
                Task myTask = new Task(task, Priority.HIGH,
                        dueDate,Calendar.getInstance().getTime(),
                        false);

                if (isEdit){

                    // If true, create new task and set the fields:
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(Priority.HIGH);
                    updateTask.setDueDate(dueDate);

                    TaskViewModel.update(updateTask);

                    sharedViewModel.setIsEdit(false);

                } else {
                    // Else, create new task:
                    TaskViewModel.insert(myTask);
                }

                enterTodo.setText("");
                if (this.isVisible()){
                    this.dismiss();
                }

            } else {
                Snackbar.make(saveButton, R.string.empty_field, Snackbar.LENGTH_LONG);
            }
        });

    }

    @Override
    public void onResume() {
        // onResume will be called when the fragment appears on the foreground AND can be interactible
        // This is the best place to put the logic when it pops up

        // If we put the logic on OnViewCreated, it is only created ONCE the view is created
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue() != null ){

            // The field is set to be edited:
            isEdit = sharedViewModel.getIsEdit();

            // set fragment view
            enterTodo.setText("");
            Task task = sharedViewModel.getSelectedItem().getValue();
            enterTodo.setText(task.getTask());

        }
    }

    @Override
    public void onClick(View v) {
        // onClick method for the Chip vew
        int id = calendarView.getId();
        switch (id){
            case R.id.today_chip:
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                dueDate = calendar.getTime();
                break;
            case R.id.tomorrow_chip:
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                dueDate = calendar.getTime();
                break;
            case R.id.next_week_chip:
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                dueDate = calendar.getTime();
                break;
        }

    }
}