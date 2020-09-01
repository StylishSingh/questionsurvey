package com.medicalsurvey.survey;

import android.text.TextUtils;

import com.library.basecontroller.BaseViewModel;
import com.library.utils.Prefs;

import java.util.List;

public class SurveyViewModel extends BaseViewModel {
    public SurveyAdapter adapter;

    public SurveyViewModel() {
        this.adapter = new SurveyAdapter();
    }

    public void setData(List<QuestionAnswerModel.DataBean.AnswersBean> answersBeanList, String quesNo) {
        adapter.mSelectedItem = -1;

        if (!TextUtils.isEmpty(Prefs.getString(quesNo, ""))) {

            for (int i = 0; i < answersBeanList.size(); i++) {

                if (Prefs.getString(quesNo, "").equalsIgnoreCase(answersBeanList.get(i).getOption())) {
                    adapter.setSelectedItem(i,false);
                    break;
                }
            }

        } else {
            adapter.setSelectedItem(-1,false);
        }


        adapter.addAllItems(answersBeanList);


    }
}
