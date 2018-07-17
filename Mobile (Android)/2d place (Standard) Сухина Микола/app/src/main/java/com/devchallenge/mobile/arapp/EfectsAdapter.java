package com.devchallenge.mobile.arapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;



public class EfectsAdapter extends RecyclerView.Adapter<EfectsAdapter.ViewHolder> {
    private EffectCallback mEffectCallback;
    private List<Integer> efects;
    private int type;

    public EfectsAdapter(EffectCallback mEffectCallback, List<Integer> mEfects, int type) {
        this.mEffectCallback = mEffectCallback;
        efects = mEfects;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tool_item, null);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Integer resId  = efects.get(position);
        holder.efectPreview.setImageResource(resId);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEffectCallback.select(resId,type);
            }
        });

    }


    @Override
    public int getItemCount() {
        return efects.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView efectPreview;
        public View view;


        public ViewHolder(View view) {
            super(view);
            this.view = view;
            efectPreview =  view.findViewById(R.id.efect_preview);

        }

    }

    public interface EffectCallback {
        void select(int res, int type);
    }


}
