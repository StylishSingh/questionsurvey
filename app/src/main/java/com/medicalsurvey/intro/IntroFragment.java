package com.medicalsurvey.intro;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.library.basecontroller.BaseActivity;
import com.library.basecontroller.BaseFragment;
import com.library.basecontroller.BaseViewModel;
import com.medicalsurvey.R;
import com.medicalsurvey.databinding.FragmentIntroBinding;
import com.medicalsurvey.formselection.FormSelectionFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends BaseFragment<FragmentIntroBinding, IntroViewModel> implements View.OnClickListener {


    public IntroFragment() {
        // Required empty public constructor
    }


    @Override
    protected int fragmentId() {
        return R.layout.fragment_intro;
    }

    @Override
    protected Class<IntroViewModel> viewModelClass() {
        return IntroViewModel.class;
    }

    @Override
    protected BaseViewModel.Factory factory() {
        return new BaseViewModel.Factory() {
            @Override
            public BaseViewModel getClassInstance() {
                return new IntroViewModel();
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBinding().setViewmodel(getViewModel());

        getBinding().btnStarted.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.btn_started:
                    ((BaseActivity) getActivity()).replaceFragment(FormSelectionFragment.class, null, true, null, null);
                    break;
            }
        }
    }
}
