package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.value.LoadingStateValue;

import javax.inject.Inject;

import lombok.Getter;
import timber.log.Timber;

public class BbsAddEditViewModel extends ViewModel {

    @Getter
    @NonNull
    private MutableLiveData<String> title = new MutableLiveData<>();
    @Getter
    @NonNull
    private MediatorLiveData<String> titleError = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MutableLiveData<String> url = new MutableLiveData<>();
    @Getter
    @NonNull
    private MediatorLiveData<String> urlError = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MediatorLiveData<Boolean> canSubmit = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MediatorLiveData<Boolean> canLoadBbsTitle = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MutableLiveData<Integer> loadBbsTitleButtonState = new MutableLiveData<>(LoadingStateValue.NONE);

    private Bbs bbs;

    @Inject
    public BbsAddEditViewModel() {
        titleError.addSource(title, s -> titleError.postValue(null));
        urlError.addSource(url, s -> urlError.postValue(null));
        canSubmit.addSource(title, s -> canSubmit.postValue(isInputted()));
        canSubmit.addSource(url, s -> canSubmit.postValue(isInputted()));
        canLoadBbsTitle.addSource(url, s -> {
            Boolean enabled = !TextUtils.isEmpty(s);
            if (canLoadBbsTitle.getValue() != enabled) {
                canLoadBbsTitle.postValue(!TextUtils.isEmpty(s));
            }
        });
    }

    public void setInitialValue(@NonNull Bbs initialValue) {
        if (this.bbs == null) {
            this.bbs = initialValue;
            title.postValue(bbs.getName());
            url.postValue(bbs.getUrl());
        }
    }

    public void loadTitle() {
        Timber.d("loadTitle start");
        loadBbsTitleButtonState.postValue(LoadingStateValue.LOADING);
    }

    public void create() {
    }

    public void update() {
    }

    private boolean isInputted() {
        return !TextUtils.isEmpty(title.getValue()) && !TextUtils.isEmpty(url.getValue());
    }

}
