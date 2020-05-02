package com.github.hyota.asciiartboardreader.ui.bbsthredlist;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.usecase.LoadSubjectUseCase;
import com.github.hyota.asciiartboardreader.model.value.ErrorDisplayTypeValue;
import com.github.hyota.asciiartboardreader.model.value.LoadingStateValue;
import com.github.hyota.asciiartboardreader.ui.base.BaseViewModel;
import com.github.hyota.asciiartboardreader.ui.common.ErrorMessageModel;

import java.util.concurrent.Future;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

public class BbsThreadListViewModel extends BaseViewModel {

    @Nonnull
    private LoadSubjectUseCase loadSubjectUseCase;

    private MutableLiveData<Subject> subject;
    @Getter
    @NonNull
    private MutableLiveData<Integer> loadingState = new MutableLiveData<>(LoadingStateValue.NONE);

    @Getter
    @Setter
    private Bbs bbs = null;
    private Future<?> loadFuture;

    @Inject
    public BbsThreadListViewModel(@Nonnull LoadSubjectUseCase loadSubjectUseCase) {
        this.loadSubjectUseCase = loadSubjectUseCase;
    }

    public LiveData<Subject> getSubject() {
        if (subject == null) {
            subject = new MutableLiveData<>();
            load(false);
        }
        return subject;
    }

    public void load() {
        load(true);
    }

    private void load(boolean force) {
        if (loadFuture != null && !loadFuture.isDone()) {
            return;
        }
        loadingState.postValue(LoadingStateValue.LOADING);
        loadFuture = loadSubjectUseCase.execute(bbs, force, new LoadSubjectUseCase.Callback() {
            @Override
            public void onSuccess(@Nonnull Subject subject) {
                Timber.d("load success.");
                loadingState.postValue(LoadingStateValue.SUCCESS);
                BbsThreadListViewModel.this.subject.postValue(subject);
            }

            @Override
            public void onFail() {
                Timber.d("load fail.");
                loadingState.postValue(LoadingStateValue.FAIL);
                errorMessage.postValue(new ErrorMessageModel(R.string.bbs_thread_list_load_error, ErrorDisplayTypeValue.TOAST));
            }
        });
    }

}
