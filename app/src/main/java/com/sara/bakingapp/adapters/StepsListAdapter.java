package com.sara.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sara.bakingapp.R;
import com.sara.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * Created by sara on 4/10/2018.
 */

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsViewHolder>{

    ArrayList<Step> steps;
    Context context;

    public StepsListAdapter(ArrayList<Step> steps, Context context) {
        this.steps = steps;
        this.context = context;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_step, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.stepTitle.setText(steps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return (steps != null) ? steps.size(): 0;
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder{

        TextView stepTitle;

       public StepsViewHolder(View itemView) {
           super(itemView);

           stepTitle = itemView.findViewById(R.id.step_title);
       }
   }
}