package com.wenny.searchview.searchview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wenny.searchview.R;
import com.wenny.searchview.util.SharedUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wenny} on 2017/6/12.
 */

public class SearchView extends FrameLayout implements android.support.v7.widget.SearchView.OnQueryTextListener, OnSearchListener {

    private Context mContext;

    private android.support.v7.widget.SearchView searchView;

    private RecyclerView recycleView_recommend;

    private RecyclerView recycleView_history;

    private LinearLayout ll_clean_history;

    private TextView tv_history;

    private HistoryAdapter historyAdapter;

    private RecommendAdapter recommendAdapter;

    private List<String> historyList;

    private String oldHistory;

    private String SEARCH_HISTORY = "history";

    private OnSearchListener onSearchListener;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        init();
    }

    private void init() {

        SharedUtil.init(mContext);
        // Inflate view
        LayoutInflater.from(mContext).inflate(R.layout.search_view, this, true);

        searchView = (android.support.v7.widget.SearchView) findViewById(R.id.search_view);
        recycleView_recommend = (RecyclerView) findViewById(R.id.recycleView_recommend);
        recycleView_history = (RecyclerView) findViewById(R.id.recycleView_history);
        ll_clean_history = (LinearLayout) findViewById(R.id.ll_clean_history);
        tv_history = (TextView) findViewById(R.id.tv_history);


        historyAdapter = new HistoryAdapter(mContext);
        recommendAdapter = new RecommendAdapter(mContext);

        recycleView_history.setLayoutManager(new LinearLayoutManager(mContext));
        recycleView_recommend.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));

        recycleView_history.setAdapter(historyAdapter);
        recycleView_recommend.setAdapter(recommendAdapter);

        searchView.setOnQueryTextListener(this);
        historyAdapter.setOnSearchListener(this);
        recommendAdapter.setOnSearchListener(this);
        historyList = new ArrayList<>();


        bindListener();
        getLocalityHistory();
    }

    private void bindListener() {
        ll_clean_history.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("print", "cleanHistory: ");
                cleanHistory();
            }
        });
    }

    /**
     * 读取本地存储的搜索历史
     */
    private void getLocalityHistory() {
        oldHistory = SharedUtil.getString(SEARCH_HISTORY);
        Log.d("print", "getLocalityHistory: " + oldHistory);
        if (!TextUtils.isEmpty(oldHistory)) {
            List<String> list = new ArrayList<>();
            for (Object o : oldHistory.split(",")) {
                list.add((String) o);
            }
            historyList = list;
            if (historyList.size() > 0) {
                ll_clean_history.setVisibility(VISIBLE);
                tv_history.setText("搜索历史");
                historyAdapter.setStringList(historyList);
            } else {
                ll_clean_history.setVisibility(GONE);
                tv_history.setText("暂无搜索历史");
            }
        } else {
            ll_clean_history.setVisibility(GONE);
            tv_history.setText("暂无搜索历史");
        }
    }

    /**
     * 保存搜索内容
     *
     * @param search
     */
    private void saveHistory(String search) {
        //将搜索历史写入本地数据库
        if (!TextUtils.isEmpty(search)) {

            if (TextUtils.isEmpty(oldHistory)) {
                SharedUtil.putString(SEARCH_HISTORY, search);
            } else {
                //去除掉重复的搜索内容
                for (int i = 0; i < historyList.size(); i++) {
                    if(historyList.get(i).equals(search)){
                        historyList.remove(i);
                    }
                }
                oldHistory = "";
                for (int i = 0; i < historyList.size(); i++) {
                    if(i == historyList.size() - 1){
                        oldHistory += historyList.get(i);
                    } else {
                        oldHistory += historyList.get(i) + ",";
                    }
                }
                SharedUtil.putString(SEARCH_HISTORY, search + "," + oldHistory);
            }
        }
    }

    /**
     * 清除历史纪录
     */

    private void cleanHistory() {
        SharedUtil.remove(SEARCH_HISTORY);
        historyList.clear();
        historyAdapter.clear();
        ll_clean_history.setVisibility(GONE);
        tv_history.setText("暂无搜索历史");
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public void setRecommendDate(List<String> strings) {
        recommendAdapter.setStringList(strings);
    }

    public android.support.v7.widget.SearchView getSearchView() {
        return searchView;
    }

    public RecyclerView getRecycleView_recommend() {
        return recycleView_recommend;
    }

    public RecyclerView getRecycleView_history() {
        return recycleView_history;
    }

    /**
     * 当点击搜索按钮时触发该方法
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (onSearchListener != null) {
            onSearchListener.onSearch(query);
        }
        saveHistory(query);
        //刷新下数据
        getLocalityHistory();
        return false;
    }

    /**
     * 当搜索内容改变时触发该方法
     *
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onSearch(String serach) {
        if (onSearchListener != null) {
            onSearchListener.onSearch(serach);
        }
        saveHistory(serach);
        getLocalityHistory();
    }
}
