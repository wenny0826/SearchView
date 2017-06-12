package com.wenny.searchview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.wenny.searchview.searchview.OnSearchListener;
import com.wenny.searchview.searchview.SearchView;
import com.wenny.searchview.util.PermissionUtils;
import com.wenny.searchview.util.SharedUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSearchListener {
    private  SearchView searchView;

    List<String> recommendDate;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        context = this;
        searchView = (SearchView) findViewById(R.id.searchView);
        initRecommendDate();
        searchView.setOnSearchListener(this);
    }
    private void initRecommendDate(){
        recommendDate = new ArrayList<>();
        recommendDate.add("夏装女");
        recommendDate.add("儿童图书");
        recommendDate.add("钱包");
        recommendDate.add("VC");
        recommendDate.add("android开发之旅");
        recommendDate.add("苹果笔记本");
        recommendDate.add("游戏显卡");
        recommendDate.add("小米6");
        recommendDate.add("魔术头巾");
        recommendDate.add("奥尔滨健康水");
        searchView.setRecommendDate(recommendDate);
    }
    @Override
    public void onSearch(String serach) {
        Toast.makeText(context,serach,Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    // onResume 中进行调用
    private void checkPermission() {
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }
}
