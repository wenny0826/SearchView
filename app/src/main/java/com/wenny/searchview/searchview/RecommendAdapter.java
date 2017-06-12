package com.wenny.searchview.searchview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wenny.searchview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wenny} on 2017/6/12.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private List<String> stringList;

    private OnSearchListener onSearchListener;

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
        this.notifyDataSetChanged();
    }

    public RecommendAdapter(Context context) {
        this.context = context;
        stringList = new ArrayList<>();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_recommend, parent,false);
        return new HistoryViewHoder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryViewHoder historyViewHoder = (HistoryViewHoder) holder;
        historyViewHoder.textView.setText(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    private class HistoryViewHoder extends RecyclerView.ViewHolder{

        TextView textView;

        public HistoryViewHoder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onSearchListener != null){
                        onSearchListener.onSearch(stringList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
