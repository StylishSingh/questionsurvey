package com.medicalsurvey;

import android.os.Bundle;

import com.library.basecontroller.BaseActivity;
import com.library.utils.Prefs;
import com.medicalsurvey.intro.IntroFragment;
import com.medicalsurvey.survey.SurveyFragment;

public class IntroSurveyActivity extends BaseActivity {

    @Override
    protected int containerId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_survey);

        Prefs.clear();

        if (Prefs.getBoolean(getString(R.string.is_survey_started), false)) {
            replaceFragment(SurveyFragment.class, "", true, null, null);
        } else {
            replaceFragment(IntroFragment.class, "", false, null, null);
        }


    }
}
