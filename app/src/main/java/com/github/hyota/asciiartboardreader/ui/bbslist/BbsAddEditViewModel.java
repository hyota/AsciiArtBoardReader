package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.usecase.LoadBbsTitleUseCase;
import com.github.hyota.asciiartboardreader.model.value.ErrorDisplayTypeValue;
import com.github.hyota.asciiartboardreader.model.value.LoadingStateValue;
import com.github.hyota.asciiartboardreader.ui.base.BaseViewModel;
import com.github.hyota.asciiartboardreader.ui.common.ErrorMessageModel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.Getter;
import timber.log.Timber;

public class BbsAddEditViewModel extends BaseViewModel {

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

    @NonNull
    private LoadBbsTitleUseCase loadBbsTitleUseCase;

    private Bbs bbs;

    @Inject
    public BbsAddEditViewModel(@NonNull LoadBbsTitleUseCase loadBbsTitleUseCase) {
        this.loadBbsTitleUseCase = loadBbsTitleUseCase;
        titleError.addSource(title, s -> titleError.postValue(null));
        urlError.addSource(url, s -> urlError.postValue(null));
        canSubmit.addSource(title, s -> canSubmit.postValue(canSubmit()));
        canSubmit.addSource(url, s -> canSubmit.postValue(canSubmit()));
        canSubmit.addSource(loadBbsTitleButtonState, state -> canSubmit.postValue(canSubmit()));
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
        String urlStr = url.getValue();
        if (urlStr != null) {
            loadBbsTitleUseCase.execute(urlStr, new LoadBbsTitleUseCase.Callback() {
                @Override
                public void onSuccess(@Nonnull String validateUrl, @Nonnull String loadedTitle) {
                    url.postValue(validateUrl);
                    title.postValue(loadedTitle);
                    loadBbsTitleButtonState.postValue(LoadingStateValue.SUCCESS);
                }

                @Override
                public void onInvalidUrl() {
                    errorMessage.postValue(new ErrorMessageModel(R.string.bbs_add_edit_error_invalid_url, ErrorDisplayTypeValue.TOAST));
                    loadBbsTitleButtonState.postValue(LoadingStateValue.FAIL);
                }

                @Override
                public void onFail() {
                    errorMessage.postValue(new ErrorMessageModel(R.string.bbs_add_edit_error_failed_load_title, ErrorDisplayTypeValue.TOAST));
                    loadBbsTitleButtonState.postValue(LoadingStateValue.FAIL);
                }
            });
        }
    }

    public void create() {
    }

    public void update() {
    }

    private boolean canSubmit() {
        return !TextUtils.isEmpty(title.getValue()) && !TextUtils.isEmpty(url.getValue())
                && loadBbsTitleButtonState.getValue() != null && loadBbsTitleButtonState.getValue() != LoadingStateValue.LOADING;
    }

}
