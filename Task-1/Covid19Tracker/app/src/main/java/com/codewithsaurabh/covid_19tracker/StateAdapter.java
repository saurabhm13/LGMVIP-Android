package com.codewithsaurabh.covid_19tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView stateName;
        public TextView confirmedCases;
        public TextView deaths;
        public TextView recovered;

        public ViewHolder(View itemView)
        {
            super(itemView);
            stateName=itemView.findViewById(R.id.textViewStateName);
            confirmedCases=itemView.findViewById(R.id.textViewConfirmedCases);
            deaths=itemView.findViewById(R.id.textViewDeaths);
            recovered=itemView.findViewById(R.id.textViewRecovered);
        }
    }

    private ArrayList<Model> statesArrayList=new ArrayList<>();

    public StateAdapter(ArrayList<Model> statesArrayList)
    {
        this.statesArrayList=statesArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_state,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model state=statesArrayList.get(position);

        TextView textViewStateName=holder.stateName;
        TextView textViewDeaths=holder.deaths;
        TextView textViewRecovered=holder.recovered;
        TextView textViewConfirmedCases=holder.confirmedCases;

        textViewStateName.setText(state.state);
        textViewRecovered.setText(state.recovered);
        textViewDeaths.setText(state.deaths);
        textViewConfirmedCases.setText(state.confirmedCases);
    }

    @Override
    public int getItemCount() {
        return statesArrayList.size();
    }
}
