package com.medicalsurvey.survey;

import android.support.annotation.NonNull;

import com.library.basecontroller.BaseRecyclerViewAdapter;
import com.medicalsurvey.R;
import com.medicalsurvey.databinding.ListItemSurveyBinding;
import com.medicalsurvey.interfaces.OnItemClickListener;

public class SurveyAdapter extends BaseRecyclerViewAdapter<ListItemSurveyBinding, QuestionAnswerModel.DataBean.AnswersBean> {

    private OnItemClickListener listener;
    public int mSelectedItem = -1;
    private String answer = "";
    private boolean isRefreshed;

    public SurveyAdapter() {
        super(null, R.layout.list_item_survey);
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewAdapter.BaseRecyclerViewHolder holder, int position) {

//        ((ListItemSurveyBinding)holder.getBinding()).rbSelection.setChecked(false);

        ((ListItemSurveyBinding) holder.getBinding()).setData(getItem(holder.getAdapterPosition()));

        ((ListItemSurveyBinding) holder.getBinding()).rbSelection.setChecked(position == mSelectedItem);

        ((ListItemSurveyBinding) holder.getBinding()).rbSelection.setOnClickListener(v ->
        {
            mSelectedItem = holder.getAdapterPosition();
            listener.onItemClick(holder.getAdapterPosition(), getItem(holder.getAdapterPosition()), true);
        });

        if (position == mSelectedItem)
            listener.onItemClick(holder.getAdapterPosition(), getItem(holder.getAdapterPosition()), false);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedItem(int selectedItem, boolean isRefreshed) {
        mSelectedItem = selectedItem;
        this.isRefreshed = isRefreshed;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }
}
