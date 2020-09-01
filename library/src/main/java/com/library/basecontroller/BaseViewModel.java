package com.library.basecontroller;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class BaseViewModel extends ViewModel {

    private MutableLiveData<String> toast = new MutableLiveData<>();

    MutableLiveData<String> getToast() {
        return toast;
    }


    public void showToast(String message) {
        toast.setValue(message);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public abstract static class Factory extends ViewModelProvider.NewInstanceFactory {
        public abstract BaseViewModel getClassInstance();

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) getClassInstance();
        }
    }

}
