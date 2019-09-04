package com.udacity.bakingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.udacity.bakingapp.model.Step;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListRecyclerViewAdapter
        extends RecyclerView.Adapter<StepListRecyclerViewAdapter.ViewHolder> {

    private final StepListActivity mParentView;
    private final List<Step> mStepValues;


    StepListRecyclerViewAdapter(StepListActivity parent, List<Step> step) {
        mStepValues = step;
        mParentView = parent;

    }

    @Override
    public StepListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_steps_list_content, parent, false);
        return new StepListRecyclerViewAdapter.ViewHolder(view, mParentView);
    }

    @Override
    public void onBindViewHolder(final StepListRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mStepShortDescriptionTextView.setText(mStepValues.get(position).getShortDescription());
        if(mStepValues.get(position).getId()!=0)
        holder.mStepStepIdTextView.setText("Step "+String.valueOf(mStepValues.get(position).getId())+":");
    }

    @Override
    public int getItemCount() {
        return mStepValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R2.id.tv_step_id) TextView mStepStepIdTextView;
        @BindView(R2.id.tv_step_short_description) TextView mStepShortDescriptionTextView;
        OnRecipeStepListener onRecipeStepListener;

        ViewHolder(View view, OnRecipeStepListener onRecipeStepListener) {
            super(view);
            ButterKnife.bind(this, view);
            this.onRecipeStepListener = onRecipeStepListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecipeStepListener.onRecipeStepClick(getAdapterPosition());
        }


        public interface OnRecipeStepListener {
            void onRecipeStepClick(int stepId);
        }

    }


}
