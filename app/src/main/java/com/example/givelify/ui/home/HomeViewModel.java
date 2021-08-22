package com.example.givelify.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> username;
    private final FirebaseDatabase db=FirebaseDatabase.getInstance();
    private final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser= mAuth.getCurrentUser();

    public HomeViewModel() {
        if(currentUser!=null){
            String displayName =currentUser.getDisplayName();
            username = new MutableLiveData<>();
            username.setValue(displayName);
        }

    }

    public LiveData<String> getUsername() {
        return username;
    }
}