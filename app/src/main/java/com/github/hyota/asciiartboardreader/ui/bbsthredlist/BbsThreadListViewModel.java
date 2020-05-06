package com.github.hyota.asciiartboardreader.ui.bbsthredlist;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.di.annotation.Local;
import com.github.hyota.asciiartboardreader.di.annotation.Remote;
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
    private LoadSubjectUseCase loadSubjectLocalUseCase;
    @Nonnull
    private LoadSubjectUseCase loadSubjectRemoteUseCase;

    private MutableLiveData<Subject> subject;
    @Getter
    @NonNull
    private MutableLiveData<Integer> loadingState = new MutableLiveData<>(LoadingStateValue.NONE);
    @Getter
    @Nonnull
    private MutableLiveData<Integer> progressState = new MutableLiveData<>(0);

    @Getter
    @Setter
    private Bbs bbs = null;
    private Future<?> loadFuture;

    @Inject
    public BbsThreadListViewModel(@Local @Nonnull LoadSubjectUseCase loadSubjectLocalUseCase, @Nonnull @Remote LoadSubjectUseCase loadSubjectRemoteUseCase) {
        this.loadSubjectLocalUseCase = loadSubjectLocalUseCase;
        this.loadSubjectRemoteUseCase = loadSubjectRemoteUseCase;
    }

    @Nonnull
    public LiveData<Subject> getSubject() {
        if (subject == null) {
            subject = new MutableLiveData<>();
            load(loadSubjectLocalUseCase);
        }
        return subject;
    }

    public void loadFromRemote() {
        load(loadSubjectRemoteUseCase);
    }

    private void load(@Nonnull LoadSubjectUseCase useCase) {
        if (loadFuture != null && !loadFuture.isDone()) {
            return;
        }
        loadingState.postValue(LoadingStateValue.LOADING);
        loadFuture = useCase.execute(bbs, new LoadSubjectUseCase.Callback() {
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
        }, (bytesRead, contentLength, done) -> {
            if (done) {
                progressState.postValue(100);
            } else {
                int progress = (int) (100 * bytesRead / contentLength);
                progressState.postValue(progress);
            }
        });
    }

}
