package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

public class BbsAddEditViewModel extends ViewModel {

    @Getter
    @NonNull
    private MutableLiveData<String> name = new MutableLiveData<>("");
    @Getter
    @NonNull
    private MutableLiveData<String> url = new MutableLiveData<>("");
    @Getter
    @NonNull
    private MediatorLiveData<Boolean> canSubmit = new MediatorLiveData<>();

    private Bbs bbs;

    @Inject
    public BbsAddEditViewModel() {
        canSubmit.addSource(name, s -> canSubmit.postValue(isInputted()));
        canSubmit.addSource(url, s -> canSubmit.postValue(isInputted()));
    }

    public void setInitialValue(@NonNull Bbs initialValue) {
        if (this.bbs == null) {
            this.bbs = initialValue;
            name.postValue(bbs.getName());
            url.postValue(bbs.getUrl());
        }
    }

    public void create() {
    }

    public void update() {
    }

    private boolean isInputted() {
        return !TextUtils.isEmpty(name.getValue()) && !TextUtils.isEmpty(url.getValue());
    }

}
