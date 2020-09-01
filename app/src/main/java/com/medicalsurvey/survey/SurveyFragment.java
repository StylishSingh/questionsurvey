package com.medicalsurvey.survey;


import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.basecontroller.BaseActivity;
import com.library.basecontroller.BaseFragment;
import com.library.basecontroller.BaseViewModel;
import com.library.utils.Prefs;
import com.medicalsurvey.R;
import com.medicalsurvey.databinding.FragmentSurveyBinding;
import com.medicalsurvey.interfaces.OnItemClickListener;
import com.medicalsurvey.submit.SubmitFragment;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SurveyFragment extends BaseFragment<FragmentSurveyBinding, SurveyViewModel>
        implements OnItemClickListener, View.OnClickListener {
    private int index, currentIndex = 0;
    private List<QuestionAnswerModel> questions;
    private List<QuestionAnswerModel> updatedQuestions;
    private String answer = "";
    private QuestionAnswerModel.DataBean.AnswersBean answersBean;
    private DatabaseReference mDatabase;
    private String parentJSON;

    public SurveyFragment() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected int fragmentId() {
        return R.layout.fragment_survey;
    }

    @Override
    protected Class<SurveyViewModel> viewModelClass() {
        return SurveyViewModel.class;
    }

    @Override
    protected BaseViewModel.Factory factory() {
        return new BaseViewModel.Factory() {
            @Override
            public BaseViewModel getClassInstance() {
                return new SurveyViewModel();
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBinding().setViewmodel(getViewModel());

        getBinding().rvOptions.setItemAnimator(null);

        getBinding().ivBack.setOnClickListener(this);
        getBinding().btnNext.setOnClickListener(this);
        getBinding().btnNextQuestion.setOnClickListener(this);
        getViewModel().adapter.setOnItemClickListener(this);

        if (getActivity() != null) {
            try {
                InputStream inputStream = getActivity().getAssets().open("QuestionAnswers.json");
                if (inputStream != null) {
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    inputStream.close();
                    parentJSON = new String(buffer, "UTF-8");

                    questions = new ArrayList<>();

                    if (Prefs.getBoolean(getString(R.string.is_survey_started), false)
                            && Prefs.getInt(getString(R.string.resumed_index), -1) != -1) {
                        getBinding().nsInstruction.setVisibility(View.GONE);
                        getBinding().clSurvey.setVisibility(View.VISIBLE);
                        index = Prefs.getInt(getString(R.string.resumed_index), -1);
                        questions = getPrefQuestions();
                    } else {
                        getBinding().nsInstruction.setVisibility(View.VISIBLE);
                        getBinding().clSurvey.setVisibility(View.GONE);

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

                        index = 0;
//                        getBinding().ivBack.setVisibility(View.INVISIBLE);
                        questions.add(getQuestions(parentJSON));

                    }


                    if (Prefs.getBoolean(getString(R.string.test_pre), false)) {
                        if (TextUtils.isEmpty(questions.get(0).getData().getPre().get(index).getInstructions())) {
                            getBinding().tvInstruction.setVisibility(View.GONE);
                        } else {
                            getBinding().tvInstruction.setVisibility(View.VISIBLE);
                            getBinding().tvInstruction.setText(Html.fromHtml(questions.get(0).getData().getPre().get(index).getInstructions()));

                        }

                        if (!TextUtils.isEmpty(questions.get(0).getData().getPre().get(index).getImage())) {
                            getBinding().rvOptions.setVisibility(View.GONE);
                            getBinding().tvQuestion.setVisibility(View.GONE);
                            getBinding().tvInstruction.setVisibility(View.VISIBLE);
                            getBinding().imageView.setVisibility(View.VISIBLE);

                            if (getActivity() != null) {
                                Resources resources = getActivity().getResources();
                                final int resourceId = resources.getIdentifier(questions.get(0).getData().getPre().get(index).getImage(), "drawable",
                                        getActivity().getPackageName());
                                getBinding().imageView.setImageDrawable(resources.getDrawable(resourceId));
                            }
                            answer = "it's image";
                            currentIndex = index;
                            return;
                        } else {
                            getBinding().rvOptions.setVisibility(View.VISIBLE);
                            getBinding().tvQuestion.setVisibility(View.VISIBLE);
                            getBinding().tvInstruction.setVisibility(View.GONE);
                            getBinding().imageView.setVisibility(View.GONE);
                        }


                        getBinding().tvQuestion.setText(Html.fromHtml(questions.get(0).getData().getPre().get(index).getQuestion()));
                        getViewModel().setData(questions.get(0).getData().getPre().get(index).getAnswers(),
                                questions.get(0).getData().getPre().get(index).getQuesNo());

                        answer = Prefs.getString(questions.get(0).getData().getPre().get(index).getQuesNo(), "");

                        QuestionAnswerModel.DataBean.PreBean updateModel = questions.get(0).getData().getPre().get(index);
                        updateModel.setNextIndex(index + 1);
                        updateModel.setPreviousIndex(updateModel.getPreviousIndex() == 0 ? index - 1 : updateModel.getPreviousIndex());
                        questions.get(0).getData().getPre().set(index, updateModel);
                        saveList(questions);


                    } else if (Prefs.getBoolean(getString(R.string.test_post), false)) {
                        if (TextUtils.isEmpty(questions.get(0).getData().getPost().get(index).getInstructions())) {
                            getBinding().tvInstruction.setVisibility(View.GONE);
                        } else {
                            getBinding().tvInstruction.setVisibility(View.VISIBLE);
                            getBinding().tvInstruction.setText(Html.fromHtml(questions.get(0).getData().getPost().get(index).getInstructions()));

                        }

                        if (!TextUtils.isEmpty(questions.get(0).getData().getPost().get(index).getImage())) {
                            getBinding().rvOptions.setVisibility(View.GONE);
                            getBinding().tvQuestion.setVisibility(View.GONE);
                            getBinding().tvInstruction.setVisibility(View.VISIBLE);
                            getBinding().imageView.setVisibility(View.VISIBLE);

                            if (getActivity() != null) {
                                Resources resources = getActivity().getResources();
                                final int resourceId = resources.getIdentifier(questions.get(0).getData().getPost().get(index).getImage(), "drawable",
                                        getActivity().getPackageName());
                                getBinding().imageView.setImageDrawable(resources.getDrawable(resourceId));
                            }
                            answer = "it's image";
                            currentIndex = index;
                            return;
                        } else {
                            getBinding().rvOptions.setVisibility(View.VISIBLE);
                            getBinding().tvQuestion.setVisibility(View.VISIBLE);
                            getBinding().tvInstruction.setVisibility(View.GONE);
                            getBinding().imageView.setVisibility(View.GONE);
                        }


                        getBinding().tvQuestion.setText(Html.fromHtml(questions.get(0).getData().getPost().get(index).getQuestion()));
                        getViewModel().setData(questions.get(0).getData().getPost().get(index).getAnswers(),
                                questions.get(0).getData().getPost().get(index).getQuesNo());

                        answer = Prefs.getString(questions.get(0).getData().getPost().get(index).getQuesNo(), "");

                        QuestionAnswerModel.DataBean.PostBean updateModel = questions.get(0).getData().getPost().get(index);
                        updateModel.setNextIndex(index + 1);
                        updateModel.setPreviousIndex(updateModel.getPreviousIndex() == 0 ? index - 1 : updateModel.getPreviousIndex());
                        questions.get(0).getData().getPost().set(index, updateModel);
                        saveList(questions);
                    }
                    currentIndex = index;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public QuestionAnswerModel getQuestions(String json) {
        if (!TextUtils.isEmpty(json)) {
            Gson gson = new Gson();

            Type type = new TypeToken<QuestionAnswerModel>() {
            }.getType();
            return gson.fromJson(json, type);
        }
        return new QuestionAnswerModel();
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
    public void onItemClick(int position, Object object, boolean refreshed) {


        answersBean = (QuestionAnswerModel.DataBean.AnswersBean) object;
        answer = answersBean.getOption();

        if (Prefs.getBoolean(getString(R.string.test_pre), false)) {

            QuestionAnswerModel.DataBean.PreBean updateModel = questions.get(0).getData().getPre().get(index);
            updateModel.setAnswer(answer);
            questions.get(0).getData().getPre().set(index, updateModel);

            Prefs.putString(questions.get(0).getData().getPre().get(index).getQuesNo(),
                    answersBean.getOption());

        } else if (Prefs.getBoolean(getString(R.string.test_post), false)) {
            QuestionAnswerModel.DataBean.PostBean updateModel = questions.get(0).getData().getPost().get(index);
            updateModel.setAnswer(answer);
            questions.get(0).getData().getPost().set(index, updateModel);

            Prefs.putString(questions.get(0).getData().getPost().get(index).getQuesNo(),
                    answersBean.getOption());
        }
        saveList(questions);


        System.out.println("answersBean.getOption() = " + answersBean.getOption());


        if (refreshed)
            getViewModel().adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        if (getActivity() != null) {
            Prefs.putInt(getString(R.string.resumed_index), index);


            switch (v.getId()) {
                case R.id.btn_next_question:

                    if (Prefs.getBoolean(getString(R.string.test_pre), false)) {
                        if (!TextUtils.isEmpty(questions.get(0).getData().getPre().get(index).getImage())) {
                            preNextSurvey();
                        } else {
                            if (!TextUtils.isEmpty(questions.get(0).getData().getPre().get(index).getAnswer()))
                                preNextSurvey();
                            else
                                Snackbar.make(getBinding().getRoot(), "Please select option", Snackbar.LENGTH_SHORT).show();
                        }
                    } else if (Prefs.getBoolean(getString(R.string.test_post), false)) {

                        if (!TextUtils.isEmpty(questions.get(0).getData().getPost().get(index).getImage())) {
                            postNextSurvey();
                        } else {
                            if (!TextUtils.isEmpty(questions.get(0).getData().getPost().get(index).getAnswer()))
                                postNextSurvey();
                            else
                                Snackbar.make(getBinding().getRoot(), "Please select option", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    break;

                case R.id.iv_back:
                    if (index > 0) {
                        if (Prefs.getBoolean(getString(R.string.test_pre), false)) {
                            prePreviousSurvey();
                        } else if (Prefs.getBoolean(getString(R.string.test_post), false)) {
                            postPreviousSurvey();
                        }
                    } else {
                        getBinding().clSurvey.setVisibility(View.GONE);
                        getBinding().nsInstruction.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.btn_next:
                    getBinding().nsInstruction.setVisibility(View.GONE);
                    getBinding().clSurvey.setVisibility(View.VISIBLE);

                    Prefs.putBoolean(getString(R.string.is_survey_started), true);
                    Prefs.putInt(getString(R.string.resumed_index), Prefs.getInt(getString(R.string.resumed_index), -1));


                    break;
            }
        }
    }

    private void prePreviousSurvey() {


        index = questions.get(0).getData().getPre().get(currentIndex).getPreviousIndex();


        if (index < questions.get(0).getData().getPre().size() - 1)
            getBinding().btnNextQuestion.setText(R.string.string_next);


        if (index > -1 && index < questions.get(0).getData().getPre().size()) {
            if (TextUtils.isEmpty(questions.get(0).getData().getPre().get(index).getInstructions())) {
                getBinding().tvInstruction.setVisibility(View.GONE);
            } else {
                getBinding().tvInstruction.setVisibility(View.VISIBLE);
                getBinding().tvInstruction.setText(Html.fromHtml(questions.get(0).getData().getPre().get(index).getInstructions()));
            }

            if (!TextUtils.isEmpty(questions.get(0).getData().getPre().get(index).getImage())) {
                getBinding().rvOptions.setVisibility(View.GONE);
                getBinding().tvQuestion.setVisibility(View.GONE);
                getBinding().tvInstruction.setVisibility(View.VISIBLE);
                getBinding().imageView.setVisibility(View.VISIBLE);

                if (getActivity() != null) {
                    Resources resources = getActivity().getResources();
                    final int resourceId = resources.getIdentifier(questions.get(0).getData().getPre().get(index).getImage(), "drawable",
                            getActivity().getPackageName());
                    getBinding().imageView.setImageDrawable(resources.getDrawable(resourceId));
                }


                currentIndex = index;
                return;
            } else {
                getBinding().rvOptions.setVisibility(View.VISIBLE);
                getBinding().tvQuestion.setVisibility(View.VISIBLE);
                getBinding().tvInstruction.setVisibility(View.GONE);
                getBinding().imageView.setVisibility(View.GONE);
            }


//            getBinding().tvInstruction.setText(questions.get(0).getData().getPre().get(index).getInstructions());
            getBinding().tvQuestion.setText(Html.fromHtml(questions.get(0).getData().getPre().get(index).getQuestion()));
            getViewModel().setData(questions.get(0).getData().getPre().get(index).getAnswers(),
                    questions.get(0).getData().getPre().get(index).getQuesNo());

            answer = Prefs.getString(questions.get(0).getData().getPre().get(index).getQuesNo(), "");
            currentIndex = index;

        }

        /*if (index < 1) {
            getBinding().ivBack.setVisibility(View.INVISIBLE);
        } else {
            getBinding().ivBack.setVisibility(View.VISIBLE);
        }*/
    }

    private void postPreviousSurvey() {
        index = questions.get(0).getData().getPost().get(currentIndex).getPreviousIndex();

        if (index < questions.get(0).getData().getPost().size() - 1)
            getBinding().btnNextQuestion.setText(R.string.string_next);


        if (index > -1 && index < questions.get(0).getData().getPost().size()) {
            if (TextUtils.isEmpty(questions.get(0).getData().getPost().get(index).getInstructions())) {
                getBinding().tvInstruction.setVisibility(View.GONE);
            } else {
                getBinding().tvInstruction.setVisibility(View.VISIBLE);
                getBinding().tvInstruction.setText(Html.fromHtml(questions.get(0).getData().getPost().get(index).getInstructions()));
            }

            if (!TextUtils.isEmpty(questions.get(0).getData().getPost().get(index).getImage())) {
                getBinding().rvOptions.setVisibility(View.GONE);
                getBinding().tvQuestion.setVisibility(View.GONE);
                getBinding().tvInstruction.setVisibility(View.VISIBLE);
                getBinding().imageView.setVisibility(View.VISIBLE);

                if (getActivity() != null) {
                    Resources resources = getActivity().getResources();
                    final int resourceId = resources.getIdentifier(questions.get(0).getData().getPost().get(index).getImage(), "drawable",
                            getActivity().getPackageName());
                    getBinding().imageView.setImageDrawable(resources.getDrawable(resourceId));
                }


                currentIndex = index;
                return;
            } else {
                getBinding().rvOptions.setVisibility(View.VISIBLE);
                getBinding().tvQuestion.setVisibility(View.VISIBLE);
                getBinding().tvInstruction.setVisibility(View.GONE);
                getBinding().imageView.setVisibility(View.GONE);
            }


//            getBinding().tvInstruction.setText(Html.fromHtml(questions.get(0).getData().getPost().get(index).getInstructions()));
            getBinding().tvQuestion.setText(Html.fromHtml(questions.get(0).getData().getPost().get(index).getQuestion()));
            getViewModel().setData(questions.get(0).getData().getPost().get(index).getAnswers(),
                    questions.get(0).getData().getPost().get(index).getQuesNo());

            answer = Prefs.getString(questions.get(0).getData().getPost().get(index).getQuesNo(), "");
            currentIndex = index;

            /*if (index < 1) {
                getBinding().ivBack.setVisibility(View.INVISIBLE);
            } else {
                getBinding().ivBack.setVisibility(View.VISIBLE);
            }*/

        }
    }


    private void preNextSurvey() {

         /*On survey completed show submit screen*/
        if (currentIndex < questions.get(0).getData().getPre().size()) {
            if (index == questions.get(0).getData().getPre().size() - 1) {
                if (getActivity() != null) {
                    ((BaseActivity) getActivity()).replaceFragment(SubmitFragment.class, null, true, null, null);
                }
                return;
            }
        }


        if (answersBean != null) {
            if (answersBean.getIndex() > 1) {
                index = currentIndex + answersBean.getIndex();
            } else {
                index++;
            }
        } else {
            index++;
        }

        if (index < questions.get(0).getData().getPre().size()) {

            if (TextUtils.isEmpty(questions.get(0).getData().getPre().get(index).getInstructions())) {
                getBinding().tvInstruction.setVisibility(View.GONE);
            } else {
                getBinding().tvInstruction.setVisibility(View.VISIBLE);
                getBinding().tvInstruction.setText(Html.fromHtml(questions.get(0).getData().getPre().get(index).getInstructions()));
            }

            if (!TextUtils.isEmpty(questions.get(0).getData().getPre().get(index).getImage())) {
                getBinding().rvOptions.setVisibility(View.GONE);
                getBinding().tvQuestion.setVisibility(View.GONE);
                getBinding().imageView.setVisibility(View.VISIBLE);

                QuestionAnswerModel.DataBean.PreBean updateModel = questions.get(0).getData().getPre().get(index);
                updateModel.setNextIndex(index + 1);

                if (answersBean != null) {
                    if (answersBean.getIndex() > 1) {
                        updateModel.setPreviousIndex(currentIndex);
                        for (int i = currentIndex + 1; i < index; i++) {
                            QuestionAnswerModel.DataBean.PreBean answerModel = questions.get(0).getData().getPre().get(i);
                            answerModel.setAnswer("");
                            questions.get(0).getData().getPre().set(i, answerModel);
                        }


                    } else {
                        updateModel.setPreviousIndex(index - 1);
                    }
                } else {
                    updateModel.setPreviousIndex(updateModel.getPreviousIndex() == 0 ? index - 1 : updateModel.getPreviousIndex());
                }
                questions.get(0).getData().getPre().set(index, updateModel);
                saveList(questions);
                answersBean = null;

                if (getActivity() != null) {
                    Resources resources = getActivity().getResources();
                    final int resourceId = resources.getIdentifier(questions.get(0).getData().getPre().get(index).getImage(), "drawable",
                            getActivity().getPackageName());
                    getBinding().imageView.setImageDrawable(resources.getDrawable(resourceId));
                }
                currentIndex = index;
                return;
            } else {
                getBinding().rvOptions.setVisibility(View.VISIBLE);
                getBinding().tvQuestion.setVisibility(View.VISIBLE);
                getBinding().imageView.setVisibility(View.GONE);
            }

            getBinding().tvQuestion.setText(Html.fromHtml(questions.get(0).getData().getPre().get(index).getQuestion()));
            getViewModel().setData(questions.get(0).getData().getPre().get(index).getAnswers(),
                    questions.get(0).getData().getPre().get(index).getQuesNo());

            QuestionAnswerModel.DataBean.PreBean updateModel = questions.get(0).getData().getPre().get(index);

            if (updateModel.getQuesNo().equalsIgnoreCase("5")) {
                QuestionAnswerModel.DataBean.AnswersBean answersModel;
                for (int i = 0; i < updateModel.getAnswers().size(); i++) {

                    answersModel = updateModel.getAnswers().get(i);
                    answersModel.setIndex(4);

                    updateModel.getAnswers().set(i, answersModel);
                }


            } else {
                updateModel.setNextIndex(index + 1);
            }

            if (answersBean != null) {
                if (answersBean.getIndex() > 1) {
                    updateModel.setPreviousIndex(currentIndex);
                } else {
                    updateModel.setPreviousIndex(index - 1);
                }
            } else {
                updateModel.setPreviousIndex(index - 1);
            }
            questions.get(0).getData().getPre().set(index, updateModel);
            saveList(questions);
            answersBean = null;

            currentIndex = index;
        } else {

            System.out.println("Pre Size--->" + questions.get(0).getData().getPre().size());

            for (int i = 0; i < questions.get(0).getData().getPre().size(); i++) {
                System.out.println("answer = " + questions.get(0).getData().getPre().get(i).getAnswer()
                        + " previous = " + questions.get(0).getData().getPre().get(i).getPreviousIndex()
                        + " next = " + questions.get(0).getData().getPre().get(i).getNextIndex());
            }
        }

    }

    private void postNextSurvey() {
        if (currentIndex < questions.get(0).getData().getPost().size()) {
            if (index == questions.get(0).getData().getPost().size() - 1) {
                if (getActivity() != null) {
                    ((BaseActivity) getActivity()).replaceFragment(SubmitFragment.class, null, true, null, null);
                }
                return;
            }
        }
        if (answersBean != null) {
            if (answersBean.getIndex() > 1) {
                index = currentIndex + answersBean.getIndex();
            } else {
                index++;
            }
        } else {
            index++;
        }

        if (index < questions.get(0).getData().getPost().size()) {

            if (TextUtils.isEmpty(questions.get(0).getData().getPost().get(index).getInstructions())) {
                getBinding().tvInstruction.setVisibility(View.GONE);
            } else {
                getBinding().tvInstruction.setVisibility(View.VISIBLE);
                getBinding().tvInstruction.setText(Html.fromHtml(questions.get(0).getData().getPost().get(index).getInstructions()));
            }

            if (!TextUtils.isEmpty(questions.get(0).getData().getPost().get(index).getImage())) {
                getBinding().rvOptions.setVisibility(View.GONE);
                getBinding().tvQuestion.setVisibility(View.GONE);
                getBinding().imageView.setVisibility(View.VISIBLE);

                QuestionAnswerModel.DataBean.PostBean updateModel = questions.get(0).getData().getPost().get(index);
                updateModel.setNextIndex(index + 1);

                if (answersBean != null) {
                    if (answersBean.getIndex() > 1) {
                        updateModel.setPreviousIndex(currentIndex);
                        for (int i = currentIndex + 1; i < index; i++) {
                            QuestionAnswerModel.DataBean.PostBean answerModel = questions.get(0).getData().getPost().get(i);
                            answerModel.setAnswer("");
                            questions.get(0).getData().getPost().set(i, answerModel);
                        }


                    } else {
                        updateModel.setPreviousIndex(index - 1);
                    }
                } else {
                    updateModel.setPreviousIndex(updateModel.getPreviousIndex() == 0 ? index - 1 : updateModel.getPreviousIndex());
                }
                questions.get(0).getData().getPost().set(index, updateModel);
                saveList(questions);
                answersBean = null;

                if (getActivity() != null) {
                    Resources resources = getActivity().getResources();
                    final int resourceId = resources.getIdentifier(questions.get(0).getData().getPost().get(index).getImage(), "drawable",
                            getActivity().getPackageName());
                    getBinding().imageView.setImageDrawable(resources.getDrawable(resourceId));
                }
                currentIndex = index;
                return;
            } else {
                getBinding().rvOptions.setVisibility(View.VISIBLE);
                getBinding().tvQuestion.setVisibility(View.VISIBLE);
                getBinding().imageView.setVisibility(View.GONE);
            }

            getBinding().tvQuestion.setText(Html.fromHtml(questions.get(0).getData().getPost().get(index).getQuestion()));
            getViewModel().setData(questions.get(0).getData().getPost().get(index).getAnswers(),
                    questions.get(0).getData().getPost().get(index).getQuesNo());

            QuestionAnswerModel.DataBean.PostBean updateModel = questions.get(0).getData().getPost().get(index);

            if (updateModel.getQuesNo().equalsIgnoreCase("5")) {
                QuestionAnswerModel.DataBean.AnswersBean answersModel;
                for (int i = 0; i < updateModel.getAnswers().size(); i++) {

                    answersModel = updateModel.getAnswers().get(i);
                    answersModel.setIndex(4);

                    updateModel.getAnswers().set(i, answersModel);
                }
            } else {
                updateModel.setNextIndex(index + 1);
            }


            if (answersBean != null) {
                if (answersBean.getIndex() > 1) {
                    updateModel.setPreviousIndex(currentIndex);
                } else {
                    updateModel.setPreviousIndex(index - 1);
                }
            } else {
                updateModel.setPreviousIndex(index - 1);
            }
            questions.get(0).getData().getPost().set(index, updateModel);
            saveList(questions);
            answersBean = null;

            currentIndex = index;

        } else {

            System.out.println("Post Size--->" + questions.get(0).getData().getPost().size());

            for (int i = 0; i < questions.get(0).getData().getPost().size(); i++) {
                System.out.println("answer = " + questions.get(0).getData().getPost().get(i).getAnswer()
                        + " previous = " + questions.get(0).getData().getPost().get(i).getPreviousIndex()
                        + " next = " + questions.get(0).getData().getPost().get(i).getNextIndex());
            }
        }
    }

    public void saveList(List<QuestionAnswerModel> questions) {
        Gson gson = new Gson();
        String json = gson.toJson(questions);
        Prefs.putString(getString(R.string.get_pref_questions), json);
    }

}
