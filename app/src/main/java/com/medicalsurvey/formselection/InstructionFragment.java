package com.medicalsurvey.formselection;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.library.basecontroller.BaseActivity;
import com.library.basecontroller.BaseFragment;
import com.library.basecontroller.BaseViewModel;
import com.library.utils.Prefs;
import com.medicalsurvey.R;
import com.medicalsurvey.databinding.FragmentInstructionBinding;
import com.medicalsurvey.survey.SurveyFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionFragment extends BaseFragment<FragmentInstructionBinding, InstructionViewModel> implements View.OnClickListener {


    public InstructionFragment() {
    }


    @Override
    protected int fragmentId() {
        return R.layout.fragment_instruction;
    }

    @Override
    protected Class<InstructionViewModel> viewModelClass() {
        return InstructionViewModel.class;
    }

    @Override
    protected BaseViewModel.Factory factory() {
        return new BaseViewModel.Factory() {
            @Override
            public BaseViewModel getClassInstance() {
                return new InstructionViewModel();
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SpannableString text;
        if (Prefs.getBoolean(getString(R.string.test_pre), false)) {
            text = new SpannableString(getString(R.string.text_pre_instruction));
            text.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new UnderlineSpan(), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else {
            text = new SpannableString(getString(R.string.text_post_instruction));
            text.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new UnderlineSpan(), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        getBinding().tvIntro1.setText(text);


        getBinding().btnNext.setOnClickListener(this);
        getBinding().ivBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.btn_next:
                    Prefs.putBoolean(getString(R.string.is_survey_started), true);
                    Prefs.putInt(getString(R.string.resumed_index), Prefs.getInt(getString(R.string.resumed_index), -1));
                    ((BaseActivity) getActivity()).replaceFragment(SurveyFragment.class, null, true, null, null);
                    break;

                case R.id.iv_back:
                    getActivity().onBackPressed();
                    break;
            }
        }
    }
}
