package com.nickmonks.todoister;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nickmonks.model.Task;

public class SharedViewModel extends ViewModel {
    /*
    * Holds data to pass between Activity and Bottom Fragment
    * ViewModel is an abstract class that is used to pass data from activity to fragment,
    * Whereas the ViewModelAndroid contains more contextual android capabilities.
    * */

    // Mutable data - To be able to change it
    private final MutableLiveData<Task> selectedItem = new MutableLiveData<Task>();
    private boolean isEdit;

    public void setSelectedItem(Task task){
        selectedItem.setValue(task);
    }

    public LiveData<Task> getSelectedItem(){
        return selectedItem;
    }

    public void setIsEdit (boolean isEdit){
        this.isEdit = isEdit;
    }

    public boolean getIsEdit() {
        return isEdit;
    }
}
