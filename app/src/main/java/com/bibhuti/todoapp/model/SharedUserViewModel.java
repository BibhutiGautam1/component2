package com.bibhuti.todoapp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedUserViewModel extends ViewModel {
    private final MutableLiveData<User> selectedUser = new MutableLiveData<>();

    public void selectItem(User user){
        selectedUser.setValue(user);
    }

    public LiveData<User> getSelectedItem(){
        return selectedUser;
    }
}
