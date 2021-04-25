package com.nickmonks.todoister;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;

// A fragment allows to create the sensation of having one single page for the user
// and adapts much better to different devices. Needs to be attached to another fragment
// or an activity

// In this case, when we click the fab button, it will pop up under the main_layout

public class BottomSheetFragment extends BottomSheetDialogFragment {

    // An important requirement is to have an empty constructor!
    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}