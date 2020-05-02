package com.github.hyota.asciiartboardreader.ui.bbslist;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.usecase.InsertUpdateBbsUseCase;
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
    private MediatorLiveData<ErrorMessageModel> titleError = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MutableLiveData<String> url = new MutableLiveData<>();
    @Getter
    @NonNull
    private MediatorLiveData<ErrorMessageModel> urlError = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MediatorLiveData<Boolean> canInput = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MediatorLiveData<Boolean> canSubmit = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MediatorLiveData<Boolean> canLoadBbsTitle = new MediatorLiveData<>();
    @Getter
    @NonNull
    private MutableLiveData<Integer> loadBbsTitleButtonState = new MutableLiveData<>(LoadingStateValue.NONE);
    @Getter
    @Nonnull
    private MutableLiveData<Integer> submitState = new MutableLiveData<>(LoadingStateValue.NONE);

    @NonNull
    private LoadBbsTitleUseCase loadBbsTitleUseCase;
    @NonNull
    private InsertUpdateBbsUseCase.InsertBbsUseCase insertBbsUseCase;
    @NonNull
    private InsertUpdateBbsUseCase.UpdateBbsUseCase updateBbsUseCase;

    private Bbs bbs;

    @Inject
    public BbsAddEditViewModel(@NonNull LoadBbsTitleUseCase loadBbsTitleUseCase, @NonNull InsertUpdateBbsUseCase.InsertBbsUseCase saveBbsUseCase, @NonNull InsertUpdateBbsUseCase.UpdateBbsUseCase updateBbsUseCase) {
        this.loadBbsTitleUseCase = loadBbsTitleUseCase;
        this.insertBbsUseCase = saveBbsUseCase;
        this.updateBbsUseCase = updateBbsUseCase;
        titleError.addSource(title, s -> titleError.postValue(null));
        urlError.addSource(url, s -> urlError.postValue(null));

        canInput.addSource(loadBbsTitleButtonState, state -> canInput.postValue(canInput()));
        canInput.addSource(submitState, state -> canInput.postValue(canInput()));

        canSubmit.addSource(title, s -> canSubmit.postValue(canSubmit()));
        canSubmit.addSource(url, s -> canSubmit.postValue(canSubmit()));
        canSubmit.addSource(loadBbsTitleButtonState, state -> canSubmit.postValue(canSubmit()));
        canSubmit.addSource(submitState, state -> canSubmit.postValue(canSubmit()));

        canLoadBbsTitle.addSource(url, s -> {
            if (canLoadBbsTitle.getValue() == null || canLoadBbsTitle.getValue() != canLoadTitle()) {
                canLoadBbsTitle.postValue(canLoadTitle());
            }
        });
        canLoadBbsTitle.addSource(submitState, state -> {
            if (canLoadBbsTitle.getValue() == null || canLoadBbsTitle.getValue() != canLoadTitle()) {
                canLoadBbsTitle.postValue(canLoadTitle());
            }
        });
    }

    public void setInitialValue(@NonNull Bbs initialValue) {
        if (this.bbs == null) {
            this.bbs = initialValue;
            title.postValue(bbs.getTitle());
            url.postValue(bbs.toUrlString());
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
                    urlError.postValue(new ErrorMessageModel(R.string.bbs_add_edit_error_invalid_url));
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

    public void insert() {
        insertOrUpdate(insertBbsUseCase);
    }

    public void update() {
        insertOrUpdate(updateBbsUseCase);
    }

    private boolean canInput() {
        return loadBbsTitleButtonState.getValue() != null && loadBbsTitleButtonState.getValue() != LoadingStateValue.LOADING && submitState.getValue() != null && submitState.getValue() != LoadingStateValue.LOADING;
    }

    private boolean canSubmit() {
        return !TextUtils.isEmpty(title.getValue()) && !TextUtils.isEmpty(url.getValue())
                && loadBbsTitleButtonState.getValue() != null && loadBbsTitleButtonState.getValue() != LoadingStateValue.LOADING && submitState.getValue() != null && submitState.getValue() != LoadingStateValue.LOADING;
    }

    private boolean canLoadTitle() {
        return !TextUtils.isEmpty(url.getValue()) && submitState.getValue() != null && submitState.getValue() != LoadingStateValue.LOADING;
    }

    private void insertOrUpdate(InsertUpdateBbsUseCase useCase) {
        submitState.postValue(LoadingStateValue.LOADING);
        String titleStr = this.title.getValue();
        String urlStr = this.url.getValue();
        if (titleStr == null || titleStr.isEmpty() || urlStr == null || urlStr.isEmpty()) {
            return;
        }
        useCase.execute(titleStr, urlStr, bbs, new InsertUpdateBbsUseCase.Callback() {
            @Override
            public void onSuccess() {
                submitState.postValue(LoadingStateValue.SUCCESS);
            }

            @Override
            public void onInvalidUrl() {
                urlError.postValue(new ErrorMessageModel(R.string.bbs_add_edit_error_invalid_url));
                submitState.postValue(LoadingStateValue.FAIL);
            }

            @Override
            public void onDuplicatedTitle() {
                titleError.postValue(new ErrorMessageModel(R.string.bbs_add_edit_error_duplicated_title));
                submitState.postValue(LoadingStateValue.FAIL);
            }

            @Override
            public void onDuplicatedUrl(@Nonnull String validatedUrl) {
                url.postValue(validatedUrl);
                urlError.postValue(new ErrorMessageModel(R.string.bbs_add_edit_error_duplicated_url));
                submitState.postValue(LoadingStateValue.FAIL);
            }

            @Override
            public void onFail() {
                errorMessage.postValue(new ErrorMessageModel(R.string.bbs_add_edit_error_fail, ErrorDisplayTypeValue.TOAST));
                submitState.postValue(LoadingStateValue.FAIL);
            }
        });
    }

}
