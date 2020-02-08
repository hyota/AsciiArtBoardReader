package com.github.hyota.asciiartboardreader.ui.bbslist;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.hyota.asciiartboardreader.R;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.usecase.LoadBbsUseCase;
import com.github.hyota.asciiartboardreader.model.value.ErrorDisplayTypeValue;
import com.github.hyota.asciiartboardreader.ui.base.BaseViewModel;
import com.github.hyota.asciiartboardreader.ui.common.ErrorMessageModel;

import java.util.List;

import javax.inject.Inject;

public class BbsListViewModel extends BaseViewModel {

    private LoadBbsUseCase loadBbsUseCase;

    private MutableLiveData<List<Bbs>> bbsList;

    @Inject
    public BbsListViewModel(LoadBbsUseCase loadBbsUseCase) {
        this.loadBbsUseCase = loadBbsUseCase;
    }

    public LiveData<List<Bbs>> getBbsList() {
        if (bbsList == null) {
            bbsList = new MutableLiveData<>();
            loadBbsList();
        }
        return bbsList;
    }

    private void loadBbsList() {
        loadBbsUseCase.execute(new LoadBbsUseCase.Callback() {
            @Override
            public void onSuccess(@NonNull List<Bbs> result) {
                bbsList.postValue(result);
            }

            @Override
            public void onFail() {
                errorMessage.postValue(new ErrorMessageModel(R.string.bbs_list_load_error, ErrorDisplayTypeValue.TOAST));
            }
        });
    }
}
