package com.medicalsurvey.submit;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.basecontroller.BaseFragment;
import com.library.basecontroller.BaseViewModel;
import com.library.utils.Prefs;
import com.medicalsurvey.R;
import com.medicalsurvey.SplashActivity;
import com.medicalsurvey.databinding.FragmentSubmitBinding;
import com.medicalsurvey.databinding.LayoutDialogBinding;
import com.medicalsurvey.interfaces.CustomDialogClickListener;
import com.medicalsurvey.survey.QuestionAnswerModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmitFragment extends BaseFragment<FragmentSubmitBinding, SubmitViewModel> implements View.OnClickListener, CustomDialogClickListener {
    private DatabaseReference mDatabase;
    private List<QuestionAnswerModel> questions;
    private AlertDialog mAppDialog;

    public SubmitFragment() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    protected int fragmentId() {
        return R.layout.fragment_submit;
    }

    @Override
    protected Class<SubmitViewModel> viewModelClass() {
        return SubmitViewModel.class;
    }

    @Override
    protected BaseViewModel.Factory factory() {
        return new BaseViewModel.Factory() {
            @Override
            public BaseViewModel getClassInstance() {
                return new SubmitViewModel();
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBinding().ivBack.setOnClickListener(this);
        getBinding().btnNext.setOnClickListener(this);


    }


    public List<QuestionAnswerModel> getPrefQuestions() {
        if (Prefs.contains(getString(R.string.get_pref_questions))) {
            Gson gson = new Gson();
            String json = Prefs.getString(getString(R.string.get_pref_questions), "");

            Type type = new TypeToken<List<QuestionAnswerModel>>() {
            }.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {
                case R.id.iv_back:
                    Prefs.putInt(getString(R.string.resumed_index), 0);
                    getActivity().onBackPressed();
                    break;
                case R.id.btn_next:
                    showAppDialog(getActivity(), true, R.mipmap.ic_launcher, R.color.colorPrimary, android.R.color.white, R.color.colorPrimary,
                            "Survey Completed", "Do you want to save data?", "yes", "no", this, "");
                    break;
            }
        }
    }


    private void showAppDialog(Activity activity, boolean isLogo,
                               int logoImage, int primaryColor,
                               int positiveButtonColor, int titleTextColor,
                               String title, String message,
                               String positiveButtonText, String negativeButtonText,
                               CustomDialogClickListener customDialogClickListener, String tag) {
        if (activity != null) {
            LayoutDialogBinding binding = DataBindingUtil.inflate(activity.getLayoutInflater(),
                    R.layout.layout_dialog, null, false);
            android.app.AlertDialog.Builder builder = null;
            builder = new android.app.AlertDialog.Builder(activity);
            builder.setView(binding.getRoot());
            /*----Is Logo----*/
            binding.setIsLogo(isLogo);
            /*----Positive Button Color----*/
            binding.setPositiveButtonColor(
                    !TextUtils.isEmpty(String.valueOf(positiveButtonColor))
                            ? ContextCompat.getColor(activity, positiveButtonColor)
                            : isLogo
                            ? ContextCompat.getColor(activity, android.R.color.white)
                            : ContextCompat.getColor(activity, android.R.color.black));
            /*----Logo Image----*/
            binding.setLogoImage(
                    !TextUtils.isEmpty(String.valueOf(logoImage))
                            ? ContextCompat.getDrawable(activity, logoImage)
                            : ContextCompat.getDrawable(activity, R.mipmap.ic_launcher));
            /*----Primary Color----*/
            binding.setPrimaryColor(
                    !TextUtils.isEmpty(String.valueOf(primaryColor))
                            ? ContextCompat.getColor(activity, primaryColor)
                            : ContextCompat.getColor(activity, android.R.color.white));
            /*----Title Text Color----*/
            binding.setTitleTextColor(
                    !TextUtils.isEmpty(String.valueOf(titleTextColor))
                            ? ContextCompat.getColor(activity, titleTextColor)
                            : ContextCompat.getColor(activity, android.R.color.black));
            /*----Message Text Color----*/
            binding.setMessageTextColor(lighten(
                    !TextUtils.isEmpty(String.valueOf(titleTextColor))
                            ? ContextCompat.getColor(activity, titleTextColor)
                            : ContextCompat.getColor(activity, android.R.color.black), .9));
            /*----Set Title----*/
            binding.tvTitle.setText(
                    !TextUtils.isEmpty(title)
                            ? title
                            : "Action");
            /*----Set Message----*/
            binding.tvMessage.setText(
                    !TextUtils.isEmpty(message)
                            ? message
                            : "Do you want to perform this action");
            /*----Negative Button Text----*/
            binding.btnNegative.setText(
                    !TextUtils.isEmpty(negativeButtonText)
                            ? negativeButtonText
                            : "No");
            /*----Positive Button Text----*/
            binding.btnPositive.setText(
                    !TextUtils.isEmpty(positiveButtonText)
                            ? positiveButtonText
                            : "Yes");
            /*----Positive Button Click----*/
            binding.btnPositive.setOnClickListener(v -> {
                customDialogClickListener.onPositiveClicked();
                mAppDialog.dismiss();

            });
            /*----Negative Button Click----*/
            binding.btnNegative.setOnClickListener(v -> {
                customDialogClickListener.onNegativeClicked();
                mAppDialog.dismiss();

            });

            builder.setCancelable(true);
            mAppDialog = builder.create();
            if (mAppDialog.getWindow() != null) {
                mAppDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mAppDialog.show();
            }
        }
    }

    public static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    public static int darken(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction) {
        return (int) Math.max(color - (color * fraction), 0);
    }

    @Override
    public void onPositiveClicked() {
        questions = new ArrayList<>();
        questions = getPrefQuestions();

        if (getActivity() != null) {
            if (Prefs.getBoolean(getString(R.string.test_pre), false)) {
                Map<String, String> answers = new HashMap<>();
                for (int i = 0; i < questions.get(0).getData().getPre().size(); i++) {
                    if (!questions.get(0).getData().getPre().get(i).getQuesNo().isEmpty()) {
                        System.out.println("i = " + i + "<------->" + questions.get(0).getData().getPre().get(i).getQuesNo());
                        answers.put(questions.get(0).getData().getPre().get(i).getQuesNo(),
                                questions.get(0).getData().getPre().get(i).getAnswer());
                    }
                }

                if (!answers.isEmpty()) {
//                    Toast.makeText(getActivity(), "Answers PRE Submitted", Toast.LENGTH_SHORT).show();

                    mDatabase.child(Prefs.getString(getString(R.string.patient_id), "")).child(getString(R.string.test_pre)).setValue(answers);
                    Prefs.clear();

                    startActivity(new Intent(getActivity(), SplashActivity.class));
                    getActivity().finish();
                }


            } else if (Prefs.getBoolean(getString(R.string.test_post), false)) {
                Map<String, String> answers = new HashMap<>();
                for (int i = 0; i < questions.get(0).getData().getPost().size(); i++) {
                    if (!questions.get(0).getData().getPost().get(i).getQuesNo().isEmpty()) {
                        System.out.println("i = " + i + "<------->" + questions.get(0).getData().getPost().get(i).getQuesNo());
                        answers.put(questions.get(0).getData().getPost().get(i).getQuesNo(),
                                questions.get(0).getData().getPost().get(i).getAnswer());
                    }
                }

                if (!answers.isEmpty()) {
//                    Toast.makeText(getActivity(), "Answers POST Submitted", Toast.LENGTH_SHORT).show();

                    mDatabase.child(Prefs.getString(getString(R.string.patient_id), "")).child(getString(R.string.test_post)).setValue(answers);
                    Prefs.clear();

                    startActivity(new Intent(getActivity(), SplashActivity.class));
                    getActivity().finish();
                }
            }
        }
    }

    @Override
    public void onNegativeClicked() {

    }
}
