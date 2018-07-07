package com.sara.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sara.bakingapp.R;
import com.sara.bakingapp.model.Step;

import java.util.List;

/**
 * Created by sara on 4/9/2018.
 */

public class StepsDetailsAdapter extends RecyclerView.Adapter<StepsDetailsAdapter.StepsViewHolder> {
    List<Step> steps;
    Context context;

    public StepsDetailsAdapter(List<Step> steps, Context context) {
        this.steps = steps;
        this.context = context;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_step_detail, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.stepDescription.setText(steps.get(position).getDescription());
        String video = steps.get(position).getVideoURL();
        if (TextUtils.isEmpty(video) || video.isEmpty()){
            holder.stepVideo.setText("No Video");
        } else {
            holder.stepVideo.setText(video);
        }
    }

    @Override
    public int getItemCount() {
        return (steps != null) ? steps.size(): 0;
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder {

        TextView stepVideo, stepDescription;

        public StepsViewHolder(View itemView) {
            super(itemView);

            stepVideo = itemView.findViewById(R.id.step_video);
            stepDescription = itemView.findViewById(R.id.step_description);
        }
    }
}
