package com.udacity.bakingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.udacity.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientListRecyclerViewAdapter
        extends RecyclerView.Adapter<IngredientListRecyclerViewAdapter.ViewHolder> {

    private final View mParentView;
    private final List<Ingredient> mIngredientValues;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    IngredientListRecyclerViewAdapter(View parent, List<Ingredient> ingredients) {
        mIngredientValues = ingredients;
        mParentView = parent;
    }

    @Override
    public IngredientListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_ingredients_list_content, parent, false);
        return new IngredientListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IngredientListRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mIngredientNameTextView.setText(String.valueOf(mIngredientValues.get(position).getIngredient()));
        holder.mIngredientQuantityTextView.setText(String.valueOf(mIngredientValues.get(position).getQuantity()));
        holder.mIngredientMeasureTextView.setText(mIngredientValues.get(position).getMeasure());
        //holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mIngredientValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tv_ingredient_name)
        TextView mIngredientNameTextView;
        @BindView(R2.id.tv_ingredient_quantity) TextView mIngredientQuantityTextView;
        @BindView(R2.id.tv_ingredient_measure) TextView mIngredientMeasureTextView;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}