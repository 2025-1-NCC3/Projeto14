package com.ubercab.securityvoice.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mTextExtra;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home ");

        mTextExtra = new MutableLiveData<>();
        mTextExtra.setValue("Fuck");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getTextExtra(){return mTextExtra;}
}