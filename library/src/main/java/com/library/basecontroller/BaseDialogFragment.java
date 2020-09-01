package com.library.basecontroller;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseDialogFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends DialogFragment {

    private DB binding;
    private VM viewModel;
    private Bundle bundle;

    public DB getBinding() {
        return binding;
    }

    public VM getViewModel() {
        return viewModel;
    }

    public void setViewModel(VM viewModel) {
        this.viewModel = viewModel;
    }

    protected abstract int fragmentId();

    protected abstract Class<VM> viewModelClass();

    protected abstract BaseViewModel.Factory factory();

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, fragmentId(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory()).get(viewModelClass());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getToast().observe(this, message -> {
            if (message != null) {
                ((BaseActivity) getActivity()).showToast(message);
                viewModel.getToast().setValue(null);
            }
        });
    }

    protected void hideKeyboard() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).hideKeyboard();
        }
    }

    protected void clearBackStack() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).clearBackStack();
        }
    }

}
