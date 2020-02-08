package com.github.hyota.asciiartboardreader.ui.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.hyota.asciiartboardreader.ui.common.ErrorMessageModel;

import lombok.Getter;
import lombok.NonNull;

public class BaseViewModel extends ViewModel {

    @Getter
    @NonNull
    protected MutableLiveData<ErrorMessageModel> errorMessage = new MutableLiveData<>();

    public void clearErrorMessage() {
        errorMessage.postValue(null);
    }

}
