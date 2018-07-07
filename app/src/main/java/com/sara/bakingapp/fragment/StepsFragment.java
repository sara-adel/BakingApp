package com.sara.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chootdev.recycleclick.RecycleClick;
import com.sara.bakingapp.Constants;
import com.sara.bakingapp.R;
import com.sara.bakingapp.adapters.StepsListAdapter;
import com.sara.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * Created by sara on 4/10/2018.
 */

public class StepsFragment extends Fragment{

    RecyclerView stepsList;
    private ArrayList<Step> steps;
    private StepsListAdapter adapter;
    private Parcelable layoutManagerSavedState;
    OnStepClickListener onStepClickListener;

    View rootView;

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        initViews();

        if (savedInstanceState != null) {
            layoutManagerSavedState = savedInstanceState.getParcelable(Constants.LIST_STATE);
        }

        return rootView;
    }

    public void initViews(){
        stepsList = rootView.findViewById(R.id.steps_list);

        stepsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new StepsListAdapter(steps, getActivity());
        stepsList.setAdapter(adapter);
        if (layoutManagerSavedState != null){
            stepsList.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
        onRestoreScrolling();
        RecycleClick.addTo(stepsList).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                onStepClickListener.onStepClicked(steps.get(position).getDescription(), steps.get(position).getVideoURL(), steps.get(position).getThumbnailURL());
            }
        });
    }

    public interface OnStepClickListener {
        void onStepClicked(String description, String video, String thumbnail);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onStepClickListener = (OnStepClickListener) context;
        } catch (Exception e) {
            Log.e("StepsFragment", e.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.LIST_STATE, stepsList.getLayoutManager().onSaveInstanceState());
    }

    private void onRestoreScrolling() {
        if (layoutManagerSavedState != null) {
            stepsList.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

}