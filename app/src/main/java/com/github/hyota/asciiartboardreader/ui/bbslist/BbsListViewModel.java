package com.github.hyota.asciiartboardreader.ui.bbslist;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.usecase.DeleteBbsUseCase;
import com.github.hyota.asciiartboardreader.model.usecase.LoadBbsUseCase;
import com.github.hyota.asciiartboardreader.model.value.ErrorDisplayTypeValue;
import com.github.hyota.asciiartboardreader.model.value.LoadingStateValue;
import com.github.hyota.asciiartboardreader.ui.base.BaseViewModel;
import com.github.hyota.asciiartboardreader.ui.common.ErrorMessageModel;

import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import timber.log.Timber;

public class BbsListViewModel extends BaseViewModel {

    @NonNull
    private LoadBbsUseCase loadBbsUseCase;
    @NonNull
    private DeleteBbsUseCase deleteBbsUseCase;

    private MutableLiveData<List<Bbs>> bbsList;
    @Getter
    @NonNull
    private MutableLiveData<Integer> loadingState = new MutableLiveData<>(LoadingStateValue.NONE);

    @Inject
    public BbsListViewModel(@NonNull LoadBbsUseCase loadBbsUseCase, @NonNull DeleteBbsUseCase deleteBbsUseCase) {
        this.loadBbsUseCase = loadBbsUseCase;
        this.deleteBbsUseCase = deleteBbsUseCase;
    }

    @NonNull
    public LiveData<List<Bbs>> getBbsList() {
        if (bbsList == null) {
            bbsList = new MutableLiveData<>();
            load();
        }
        return bbsList;
    }

    public void load() {
        if (loadingState.getValue() == null || loadingState.getValue() == LoadingStateValue.LOADING) {
            Timber.d("now loading.");
            return;
        }
        loadingState.postValue(LoadingStateValue.LOADING);
        loadBbsUseCase.execute(new LoadBbsUseCase.Callback() {
            @Override
            public void onSuccess(@NonNull List<Bbs> result) {
                loadingState.postValue(LoadingStateValue.SUCCESS);
                bbsList.postValue(result);
            }

            @Override
            public void onFail() {
                loadingState.postValue(LoadingStateValue.FAIL);
                errorMessage.postValue(new ErrorMessageModel(R.string.bbs_list_load_error, ErrorDisplayTypeValue.TOAST));
            }
        });
    }

    public void delete(@NonNull Bbs bbs) {
        deleteBbsUseCase.execute(bbs, new DeleteBbsUseCase.Callback() {
            @Override
            public void onSuccess() {
                load();
            }

            @Override
            public void onFail() {
                errorMessage.postValue(new ErrorMessageModel(R.string.bbs_list_delete_error, ErrorDisplayTypeValue.TOAST));
                load();
            }
        });
    }

}
