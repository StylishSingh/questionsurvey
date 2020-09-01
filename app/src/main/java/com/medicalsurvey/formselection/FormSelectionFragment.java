package com.medicalsurvey.formselection;


import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.library.basecontroller.BaseActivity;
import com.library.basecontroller.BaseFragment;
import com.library.basecontroller.BaseViewModel;
import com.library.utils.Prefs;
import com.medicalsurvey.R;
import com.medicalsurvey.databinding.FragmentFormSelectionBinding;
import com.medicalsurvey.survey.SurveyFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FormSelectionFragment extends BaseFragment<FragmentFormSelectionBinding, FormSelectionViewModel> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    public FormSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    protected int fragmentId() {
        return R.layout.fragment_form_selection;
    }

    @Override
    protected Class<FormSelectionViewModel> viewModelClass() {
        return FormSelectionViewModel.class;
    }

    @Override
    protected BaseViewModel.Factory factory() {
        return new BaseViewModel.Factory() {
            @Override
            public BaseViewModel getClassInstance() {
                return new FormSelectionViewModel();
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Prefs.putBoolean(getString(R.string.test_pre), false);
        Prefs.putBoolean(getString(R.string.test_post), false);

        getBinding().setViewmodel(getViewModel());

        getBinding().btnNext.setOnClickListener(this);

        getBinding().rbPre.setOnCheckedChangeListener(this);
        getBinding().rbPost.setOnCheckedChangeListener(this);

        String[] dateTimeArray = getDate().split(" ");

        getBinding().etDate.setEnabled(false);
        getBinding().etTime.setEnabled(false);

        getBinding().etDate.setText(getDate());
        getBinding().etTime.setText(getTime());

        getBinding().etStudyNumber.setText("123456789");
        getBinding().etSiteNumber.setText("123456789");
        getBinding().etSiteStaffNumber.setText("123456789");
        getBinding().etVisitSelection.setText("123456789");
        getBinding().etPatientId.setText("123456789");

    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.btn_next:
                    if (TextUtils.isEmpty(getBinding().etStudyNumber.getText().toString())) {
                        Snackbar.make(getBinding().getRoot(), "Enter Study Number", Snackbar.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(getBinding().etSiteNumber.getText().toString())) {
                        Snackbar.make(getBinding().getRoot(), "Enter Site Number", Snackbar.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(getBinding().etSiteNumber.getText().toString())) {
                        Snackbar.make(getBinding().getRoot(), "Enter Site Staff Number", Snackbar.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(getBinding().etSiteStaffNumber.getText().toString())) {
                        Snackbar.make(getBinding().getRoot(), "Enter Site Staff Number", Snackbar.LENGTH_SHORT).show();
                    } else if (!getBinding().rbPre.isChecked() && !getBinding().rbPost.isChecked()) {
                        Snackbar.make(getBinding().getRoot(), "Select Questionnaire either PRE or POST", Snackbar.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(getBinding().etVisitSelection.getText().toString())) {
                        Snackbar.make(getBinding().getRoot(), "Enter Visit Selection", Snackbar.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(getBinding().etPatientId.getText().toString())) {
                        Snackbar.make(getBinding().getRoot(), "Enter Patient ID", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Prefs.putString(getString(R.string.patient_id), getBinding().etPatientId.getText().toString());

                        ((BaseActivity) getActivity()).replaceFragment(SurveyFragment.class, null, true, null, null);
                    }

                    break;
            }
        }
    }

    public String getDate() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
            return dateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getTime() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            return dateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).hideKeyboard();
        if (getBinding().rbPre.isChecked()) {
            Prefs.putBoolean(getString(R.string.test_pre), true);
            Prefs.putBoolean(getString(R.string.test_post), false);
        } else if (getBinding().rbPost.isChecked()) {
            Prefs.putBoolean(getString(R.string.test_post), true);
            Prefs.putBoolean(getString(R.string.test_pre), false);
        } else {
            Prefs.putBoolean(getString(R.string.test_post), false);
            Prefs.putBoolean(getString(R.string.test_pre), false);
        }
    }


}
