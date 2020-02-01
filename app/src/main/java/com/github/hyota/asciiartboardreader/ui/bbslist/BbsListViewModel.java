package com.github.hyota.asciiartboardreader.ui.bbslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.usecase.BbsLoadUseCase;
import com.github.hyota.asciiartboardreader.ui.common.DataWithErrorState;

import java.util.List;

import javax.inject.Inject;

public class BbsListViewModel extends ViewModel {

    private BbsLoadUseCase bbsLoadUseCase;

    private MutableLiveData<DataWithErrorState<List<Bbs>>> bbsList;

    @Inject
    public BbsListViewModel(BbsLoadUseCase bbsLoadUseCase) {
        this.bbsLoadUseCase = bbsLoadUseCase;
    }

    public LiveData<DataWithErrorState<List<Bbs>>> getBbsList() {
        if (bbsList == null) {
            bbsList = new MutableLiveData<>();
            loadBbsList();
        }
        return bbsList;
    }

    private void loadBbsList() {
        bbsLoadUseCase.execute(
                result -> bbsList.postValue(new DataWithErrorState<>(result)),
                () -> bbsList.postValue(DataWithErrorState.error()));
    }

}
